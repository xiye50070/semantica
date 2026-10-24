package org.openmetadata.service.ontology;

import static org.openmetadata.common.utils.CommonUtil.listOrEmpty;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import jakarta.ws.rs.BadRequestException;
import jakarta.ws.rs.core.UriInfo;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.time.Clock;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HexFormat;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.UUID;
import java.util.function.Consumer;
import org.openmetadata.schema.EntityInterface;
import org.openmetadata.schema.entity.data.Glossary;
import org.openmetadata.schema.entity.data.GlossaryTerm;
import org.openmetadata.schema.entity.data.OntologyChangeSet;
import org.openmetadata.schema.entity.data.RelationshipType;
import org.openmetadata.schema.entity.teams.Team;
import org.openmetadata.schema.entity.teams.User;
import org.openmetadata.schema.type.EntityReference;
import org.openmetadata.schema.type.OntologyChangeApplicationResult;
import org.openmetadata.schema.type.OntologyChangeOperation;
import org.openmetadata.schema.type.OntologyChangeOperationResult;
import org.openmetadata.schema.type.OntologyChangeOperationResultStatus;
import org.openmetadata.schema.type.OntologyChangeOperationType;
import org.openmetadata.schema.type.OntologyChangeSetState;
import org.openmetadata.schema.type.OntologyEditLeaseToken;
import org.openmetadata.schema.type.OntologyRelationship;
import org.openmetadata.schema.type.Relationship;
import org.openmetadata.schema.type.TermRelation;
import org.openmetadata.schema.type.TermRelationMetadata;
import org.openmetadata.schema.utils.JsonUtils;
import org.openmetadata.service.Entity;
import org.openmetadata.service.exception.PreconditionFailedException;
import org.openmetadata.service.jdbi3.GlossaryTermRepository;
import org.openmetadata.service.jdbi3.OntologyChangeSetRepository;
import org.openmetadata.service.jdbi3.OntologyConditionalStateDAO;
import org.openmetadata.service.jdbi3.OntologyConditionalStateDAO.RelationRow;
import org.openmetadata.service.jdbi3.OntologyConditionalStateDAO.Scope;
import org.openmetadata.service.jdbi3.RdfInfraDAOs.OntologyEditLockRow;
import org.openmetadata.service.rdf.RdfUpdater;
import org.openmetadata.service.util.RestUtil.PutResponse;

/** Explicit strong path. Never delegates to the legacy preflight/apply/GET hydration. */
public final class OntologyConditionalApplyService {
  private final OntologyChangeSetRepository changes;
  private final GlossaryTermRepository terms;
  private final OntologyConditionalStateDAO dao;
  private final Clock clock;

  public record TargetToken(String entityType, UUID id, String token) {}

  public record ExpectedState(
      Integer schemaVersion, UUID changeSetId, String stateToken, List<TargetToken> readSet) {}

  public record StateResponse(
      ExpectedState expectedState,
      OntologyChangeSet changeSet,
      List<GlossaryTerm> terms,
      List<RelationshipType> relationshipTypes) {}

  public record ApplyRequest(OntologyEditLeaseToken lease, ExpectedState expectedState) {}

  public OntologyConditionalApplyService(
      OntologyChangeSetRepository changes, GlossaryTermRepository terms, Clock clock) {
    this.changes = changes;
    this.terms = terms;
    this.clock = clock;
    this.dao = terms.getDaoCollection().ontologyConditionalStateDAO();
  }

  public StateResponse snapshot(UUID id, Consumer<EntityInterface> authorizeView) {
    long end = System.nanoTime() + 22_000_000_000L;
    return terms.executeInTransaction(
        () -> {
          deadline(end);
          try (Scope scope = dao.scope(end)) {
            Locked state = lock(id, scope);
            state.entities.values().stream()
                .filter(e -> !(e instanceof User) && !(e instanceof Team))
                .forEach(authorizeView);
            scope.check();
            deadline(end);
            return state.response();
          }
        });
  }

  public PutResponse<OntologyChangeSet> apply(
      UriInfo uriInfo,
      UUID id,
      String user,
      UUID holderId,
      ApplyRequest request,
      Consumer<OntologyChangeSet> authorizeApply) {
    validateExpected(id, request);
    // This immutable expected state remains unchanged through whole-transaction deadlock retries.
    ExpectedState expected =
        new ExpectedState(
            request.expectedState().schemaVersion(),
            id,
            request.expectedState().stateToken(),
            List.copyOf(request.expectedState().readSet()));
    long end = System.nanoTime() + 22_000_000_000L;
    PutResponse<OntologyChangeSet> response =
        terms.executeInTransaction(
            () -> {
              deadline(end);
              try (Scope scope = dao.scope(end)) {
                Locked state = lock(id, scope);
                authorizeApply.accept(state.changeSet);
                OntologyEditLockRow lease = state.lease;
                requireLease(lease, holderId, request.lease());
                ExpectedState current = state.expected();
                if (!current.readSet().stream()
                    .map(t -> key(t.entityType(), t.id()))
                    .toList()
                    .equals(
                        expected.readSet().stream().map(t -> key(t.entityType(), t.id())).toList()))
                  throw new BadRequestException(
                      "Malformed conditional expectedState: incomplete or changed readSet");
                if (!current.equals(expected))
                  throw new PreconditionFailedException("CONDITIONAL_STATE_MISMATCH");
                new OntologyChangePreflight((type, key) -> state.require(type, key))
                    .validate(state.changeSet, state.operations);
                validatePayloads(state);
                List<OntologyChangeOperationResult> results = new ArrayList<>();
                Map<String, RelationshipType> types = new LinkedHashMap<>();
                state.entities.values().stream()
                    .filter(e -> e instanceof RelationshipType)
                    .map(e -> (RelationshipType) e)
                    .forEach(t -> types.put(t.getName(), t));
                RelationshipTypeResolver.withLockedTypes(
                    types,
                    () -> {
                      for (OntologyChangeOperation operation : state.operations) {
                        scope.check();
                        deadline(end);
                        GlossaryTerm result;
                        if (operation.getOperationType()
                            == OntologyChangeOperationType.UPDATE_TERM) {
                          GlossaryTerm original =
                              (GlossaryTerm)
                                  state.require(Entity.GLOSSARY_TERM, operation.getTargetId());
                          result =
                              terms.updateConditionalDescription(
                                  uriInfo,
                                  user,
                                  original,
                                  operation.getTerm().getDescription(),
                                  state.raw.get(key(Entity.GLOSSARY_TERM, original.getId())));
                        } else {
                          OntologyRelationship relationship = operation.getRelationship();
                          GlossaryTerm original =
                              (GlossaryTerm)
                                  state.require(
                                      Entity.GLOSSARY_TERM, relationship.getFromTerm().getId());
                          GlossaryTerm target =
                              (GlossaryTerm)
                                  state.require(
                                      Entity.GLOSSARY_TERM, relationship.getToTerm().getId());
                          RelationshipType type =
                              (RelationshipType)
                                  state.require(
                                      Entity.RELATIONSHIP_TYPE,
                                      relationship.getRelationshipType().getId());
                          TermRelation relation =
                              new TermRelation()
                                  .withId(relationship.getId())
                                  .withTerm(target.getEntityReference())
                                  .withRelationType(type.getName())
                                  .withRelationshipType(type.getEntityReference())
                                  .withProvenance(relationship.getProvenance())
                                  .withStatus(relationship.getStatus())
                                  .withCreatedBy(relationship.getCreatedBy())
                                  .withCreatedAt(relationship.getCreatedAt());
                          result =
                              terms.addConditionalRelation(
                                  uriInfo,
                                  user,
                                  original,
                                  relation,
                                  state.raw.get(key(Entity.GLOSSARY_TERM, original.getId())));
                        }
                        state.entities.put(key(Entity.GLOSSARY_TERM, result.getId()), result);
                        state.raw.put(
                            key(Entity.GLOSSARY_TERM, result.getId()),
                            dao.raw(Entity.GLOSSARY_TERM, result.getId()));
                        results.add(
                            new OntologyChangeOperationResult()
                                .withOperationId(operation.getId())
                                .withStatus(OntologyChangeOperationResultStatus.APPLIED)
                                .withEntity(result.getEntityReference())
                                .withEntityVersion(result.getVersion()));
                      }
                      return null;
                    });
                requireLease(dao.lease(id), holderId, request.lease());
                scope.check();
                deadline(end);
                OntologyChangeApplicationResult result =
                    new OntologyChangeApplicationResult()
                        .withAppliedAt(clock.millis())
                        .withAppliedBy(user)
                        .withOperationsProcessed(results.size())
                        .withOperationsApplied(results.size())
                        .withOperationsFailed(0)
                        .withResults(List.copyOf(results));
                PutResponse<OntologyChangeSet> applied =
                    changes.transitionConditionalLocked(
                        uriInfo,
                        user,
                        state.changeSet,
                        result,
                        state.raw.get(key(Entity.ONTOLOGY_CHANGE_SET, id)));
                // Require a live lease through the final SQL mutation; never renew it
                // automatically.
                requireLease(dao.lease(id), holderId, request.lease());
                scope.check();
                deadline(end);
                return applied;
              }
            });
    new OntologyChangeEventPublisher()
        .publish(
            org.openmetadata.schema.type.EventType.ONTOLOGY_CHANGE_SET_APPLIED,
            response.getEntity(),
            user);
    return response;
  }

  private void requireLease(OntologyEditLockRow row, UUID holder, OntologyEditLeaseToken lease) {
    new OntologyEditLeasePolicy()
        .requireOwned(
            row,
            new OntologyEditLeasePolicy.LeaseIdentity(
                holder, lease.getSessionId(), lease.getVersion(), clock.millis()));
    if (row.expiresAt() - clock.millis() < 5_000)
      throw OntologyConditionalStateDAO.unsupported("lease margin below5s");
  }

  private Locked lock(UUID id, Scope scope) {
    if (RdfUpdater.isEnabled()) throw OntologyConditionalStateDAO.unsupported("RDF enabled");
    Locked state = new Locked();
    state.add(Entity.ONTOLOGY_CHANGE_SET, id);
    state.changeSet = (OntologyChangeSet) state.require(Entity.ONTOLOGY_CHANGE_SET, id);
    state.lease = dao.lease(id);
    if (!Set.of(
            OntologyChangeSetState.DRAFT,
            OntologyChangeSetState.SUBMITTED,
            OntologyChangeSetState.APPLY_FAILED)
        .contains(state.changeSet.getState()))
      throw new BadRequestException("Conditional change set is not applicable");
    List<OntologyChangeOperation> all = listOrEmpty(state.changeSet.getOperations());
    int cursor = state.changeSet.getUndoCursor() == null ? 0 : state.changeSet.getUndoCursor();
    if (cursor < 1 || cursor > all.size() || cursor > 2)
      throw OntologyConditionalStateDAO.unsupported("operation bound");
    state.operations = List.copyOf(all.subList(0, cursor));
    Set<UUID> termIds = new LinkedHashSet<>(), typeIds = new LinkedHashSet<>();
    for (OntologyChangeOperation operation : state.operations) {
      if (operation.getOperationType() == OntologyChangeOperationType.UPDATE_TERM) {
        if (operation.getTargetId() == null || operation.getTerm() == null)
          throw new BadRequestException("Conditional UPDATE_TERM requires target and term");
        termIds.add(operation.getTargetId());
      } else if (operation.getOperationType() == OntologyChangeOperationType.ADD_RELATIONSHIP) {
        OntologyRelationship relationship = operation.getRelationship();
        if (relationship == null
            || relationship.getFromTerm() == null
            || relationship.getToTerm() == null
            || relationship.getRelationshipType() == null
            || relationship.getFromTerm().getId() == null
            || relationship.getToTerm().getId() == null
            || relationship.getRelationshipType().getId() == null)
          throw new BadRequestException("Conditional relationship requires typed endpoints");
        termIds.add(relationship.getFromTerm().getId());
        termIds.add(relationship.getToTerm().getId());
        typeIds.add(relationship.getRelationshipType().getId());
      } else throw OntologyConditionalStateDAO.unsupported("operation type");
    }
    if (termIds.size() > 3 || typeIds.size() > 1)
      throw OntologyConditionalStateDAO.unsupported("readset bound");
    termIds.stream().sorted().forEach(term -> state.add(Entity.GLOSSARY_TERM, term));
    typeIds.stream().sorted().forEach(type -> state.add(Entity.RELATIONSHIP_TYPE, type));
    state.lockRanges(new ArrayList<>(state.entities.values()), scope);
    Set<UUID> glossaryIds = new LinkedHashSet<>();
    for (EntityReference glossary : listOrEmpty(state.changeSet.getGlossaries()))
      glossaryIds.add(glossary.getId());
    for (RelationRow row : state.relations.values()) {
      if (Entity.GLOSSARY.equals(row.fromEntity())
          && termIds.contains(row.toId())
          && (row.relation() == Relationship.CONTAINS.ordinal()
              || row.relation() == Relationship.HAS.ordinal())) glossaryIds.add(row.fromId());
    }
    if (glossaryIds.size() > 2) throw OntologyConditionalStateDAO.unsupported("glossary bound");
    glossaryIds.stream().sorted().forEach(glossary -> state.add(Entity.GLOSSARY, glossary));
    state.lockRanges(
        glossaryIds.stream().map(g -> state.require(Entity.GLOSSARY, g)).toList(), scope);
    // Only finite User/Team identity references are supported; never recurse across an arbitrary
    // graph.
    for (RelationRow row : new ArrayList<>(state.relations.values())) {
      if ((row.relation() == Relationship.OWNS.ordinal()
              || row.relation() == Relationship.REVIEWS.ordinal())
          && (Entity.USER.equals(row.fromEntity()) || Entity.TEAM.equals(row.fromEntity())))
        state.add(row.fromEntity(), row.fromId());
    }
    state.changeSet.setOwners(state.refs(state.changeSet, Relationship.OWNS));
    state.hydrate(termIds, typeIds, glossaryIds);
    scope.check();
    return state;
  }

  /** Runtime wire validation, independent of global Jackson unknown-property configuration. */
  public static ApplyRequest parseRequest(JsonNode node) {
    exactObject(node, Set.of("lease", "expectedState"));
    JsonNode lease = node.get("lease"), expected = node.get("expectedState");
    exactObject(lease, Set.of("sessionId", "version"));
    exactObject(expected, Set.of("schemaVersion", "changeSetId", "stateToken", "readSet"));
    if (!lease.get("sessionId").isTextual()
        || lease.get("sessionId").textValue().isBlank()
        || lease.get("sessionId").textValue().length() > 256
        || !lease.get("version").isIntegralNumber()
        || !lease.get("version").canConvertToInt()
        || lease.get("version").intValue() < 1
        || !expected.get("schemaVersion").isIntegralNumber()
        || !expected.get("schemaVersion").canConvertToInt()
        || expected.get("schemaVersion").intValue() != 1
        || !expected.get("stateToken").isTextual()
        || !expected.get("readSet").isArray()
        || expected.get("readSet").isEmpty()
        || expected.get("readSet").size() > 16)
      throw new BadRequestException("Malformed conditional request types");
    UUID id = strictUuid(expected.get("changeSetId"));
    List<TargetToken> tokens = new ArrayList<>();
    Set<String> seen = new LinkedHashSet<>();
    for (JsonNode entry : expected.get("readSet")) {
      exactObject(entry, Set.of("entityType", "id", "token"));
      if (!entry.get("entityType").isTextual() || !entry.get("token").isTextual())
        throw new BadRequestException("Malformed conditional readSet");
      String type = entry.get("entityType").textValue();
      UUID target = strictUuid(entry.get("id"));
      if (!OntologyConditionalStateDAO.TABLES.containsKey(type) || !seen.add(key(type, target)))
        throw new BadRequestException("Unknown or duplicate conditional readSet entry");
      tokens.add(new TargetToken(type, target, entry.get("token").textValue()));
    }
    ApplyRequest parsed =
        new ApplyRequest(
            new OntologyEditLeaseToken()
                .withSessionId(lease.get("sessionId").textValue())
                .withVersion(lease.get("version").intValue()),
            new ExpectedState(1, id, expected.get("stateToken").textValue(), List.copyOf(tokens)));
    validateExpected(id, parsed);
    return parsed;
  }

  private static void exactObject(JsonNode node, Set<String> fields) {
    if (node == null || !node.isObject() || node.size() != fields.size())
      throw new BadRequestException("Conditional request requires exact object fields");
    for (String field : fields)
      if (!node.hasNonNull(field))
        throw new BadRequestException("Missing/null conditional request field: " + field);
  }

  private static UUID strictUuid(JsonNode node) {
    try {
      if (node == null || !node.isTextual()) throw new IllegalArgumentException();
      UUID id = UUID.fromString(node.textValue());
      if (!id.toString().equals(node.textValue())) throw new IllegalArgumentException();
      return id;
    } catch (IllegalArgumentException error) {
      throw new BadRequestException("Malformed conditional UUID");
    }
  }

  private static void validateExpected(UUID id, ApplyRequest request) {
    if (request == null
        || request.lease() == null
        || request.lease().getSessionId() == null
        || request.lease().getVersion() == null
        || request.expectedState() == null)
      throw new BadRequestException("Conditional apply requires lease and expectedState");
    ExpectedState state = request.expectedState();
    if (!Integer.valueOf(1).equals(state.schemaVersion())
        || !id.equals(state.changeSetId())
        || state.stateToken() == null
        || !state.stateToken().matches("sha256:[0-9a-f]{64}")
        || state.readSet() == null
        || state.readSet().size() > 16
        || state.readSet().stream()
            .anyMatch(
                t ->
                    t == null
                        || t.id() == null
                        || t.entityType() == null
                        || t.token() == null
                        || !t.token().matches("sha256:[0-9a-f]{64}")))
      throw new BadRequestException("Malformed conditional expectedState");
  }

  /** Native default containment only; background ChangeSets never become current write scope. */
  static boolean isKnownChangeSetContainment(
      OntologyChangeSet current, Set<UUID> lockedGlossaries, RelationRow row) {
    if (current == null || row == null || row.deleted() || row.fromId() == null || row.toId() == null
        || !Entity.GLOSSARY.equals(row.fromEntity())
        || !Entity.ONTOLOGY_CHANGE_SET.equals(row.toEntity())
        || row.relation() != Relationship.CONTAINS.ordinal()
        || !"".equals(row.relationType()) || row.jsonSchema() != null || row.json() != null
        || row.fullRow() == null || !lockedGlossaries.contains(row.fromId())) return false;
    Set<String> nativeColumns = Set.of("fromId", "toId", "fromEntity", "toEntity", "relation",
        "relationType", "jsonSchema", "json", "deleted", "relationshipId", "relationshipTypeId");
    if (!nativeColumns.containsAll(row.fullRow().keySet())
        || row.fullRow().get("relationshipId") != null
        || row.fullRow().get("relationshipTypeId") != null) return false;
    // The current C edge must agree with its locked raw scope. Other C edges are readonly
    // background rows under an already locked glossary; no other C entity is loaded or modified.
    return !current.getId().equals(row.toId())
        || listOrEmpty(current.getGlossaries()).stream().anyMatch(g -> row.fromId().equals(g.getId()));
  }

  /** Exact native default Task.about edge; Task never becomes a target or write scope. */
  static boolean isKnownTaskMention(Set<UUID> lockedTerms, RelationRow row) {
    if (lockedTerms == null || row == null || row.deleted() || row.fromId() == null
        || row.toId() == null || !Entity.GLOSSARY_TERM.equals(row.fromEntity())
        || !Entity.TASK.equals(row.toEntity())
        || row.relation() != Relationship.MENTIONED_IN.ordinal()
        || !"".equals(row.relationType()) || row.jsonSchema() != null || row.json() != null
        || row.fullRow() == null || !lockedTerms.contains(row.fromId())) return false;
    Set<String> nativeColumns = Set.of("fromId", "toId", "fromEntity", "toEntity", "relation",
        "relationType", "jsonSchema", "json", "deleted", "relationshipId", "relationshipTypeId");
    return nativeColumns.equals(row.fullRow().keySet())
        && row.fullRow().get("relationshipId") == null
        && row.fullRow().get("relationshipTypeId") == null;
  }

  private static void validatePayloads(Locked state) {
    for (OntologyChangeOperation operation : state.operations) {
      if (operation.getOperationType() == OntologyChangeOperationType.ADD_RELATIONSHIP) {
        GlossaryTerm from =
            (GlossaryTerm)
                state.require(
                    Entity.GLOSSARY_TERM, operation.getRelationship().getFromTerm().getId());
        GlossaryTerm to =
            (GlossaryTerm)
                state.require(
                    Entity.GLOSSARY_TERM, operation.getRelationship().getToTerm().getId());
        if (!from.getGlossary().getId().equals(to.getGlossary().getId())) {
          throw OntologyConditionalStateDAO.unsupported("v1 requires same-glossary typed pair");
        }
        continue;
      }
      if (operation.getOperationType() != OntologyChangeOperationType.UPDATE_TERM) continue;
      GlossaryTerm locked =
          (GlossaryTerm) state.require(Entity.GLOSSARY_TERM, operation.getTargetId());
      if (!locked.getId().equals(operation.getTerm().getId())
          || !payloadComparable(locked).equals(payloadComparable(operation.getTerm())))
        throw OntologyConditionalStateDAO.unsupported(
            "UPDATE_TERM supports description-only exact payload");
    }
  }

  /**
   * Only explicit read-only REST presentation/audit wrappers and the selected description differ.
   */
  static JsonNode payloadComparable(GlossaryTerm term) {
    ObjectNode node = (ObjectNode) JsonUtils.valueToTree(term);
    node.remove(
        List.of(
            "description",
            "href",
            "changeDescription",
            "incrementalChangeDescription",
            "changeSummary"));
    normalizeEmptyDerivedReadFields(node);
    for (String field : List.of("owners", "reviewers", "glossary", "parent"))
      removeRefHrefs(node.get(field));
    JsonNode related = node.get("relatedTerms");
    if (related != null && related.isArray())
      for (JsonNode row : related) {
        removeRefHrefs(row.get("term"));
        removeRefHrefs(row.get("relationshipType"));
      }
    return canonical(node);
  }

  /** Only absent versus the exact empty read-derived representation is comparable.
   * Explicit null, nonempty children, every vote and unknown fields remain substantive differences. */
  private static void normalizeEmptyDerivedReadFields(ObjectNode value){
      JsonNode children=value.get("children");
      if(children!=null&&children.isArray()&&children.isEmpty())value.remove("children");
      JsonNode votes=value.get("votes");
      if(votes!=null&&votes.isObject()&&votes.size()==4&&votes.has("upVotes")&&votes.has("downVotes")&&votes.has("upVoters")&&votes.has("downVoters")
              &&votes.path("upVotes").isIntegralNumber()&&votes.path("upVotes").bigIntegerValue().signum()==0
              &&votes.path("downVotes").isIntegralNumber()&&votes.path("downVotes").bigIntegerValue().signum()==0
              &&votes.path("upVoters").isArray()&&votes.path("upVoters").isEmpty()
              &&votes.path("downVoters").isArray()&&votes.path("downVoters").isEmpty())value.remove("votes");
  }

  private static void removeRefHrefs(JsonNode node) {
    if (node instanceof ObjectNode object) object.remove("href");
    else if (node != null && node.isArray())
      for (JsonNode ref : node) if (ref instanceof ObjectNode object) object.remove("href");
  }

  private final class Locked {
    final Map<String, String> raw = new TreeMap<>();
    final Map<String, EntityInterface> entities = new TreeMap<>();
    final Map<String, RelationRow> relations = new TreeMap<>();
    final Map<String, List<Map<String, Object>>> tags = new TreeMap<>();
    OntologyChangeSet changeSet;
    OntologyEditLockRow lease;
    List<OntologyChangeOperation> operations;

    void add(String type, UUID id) {
      String key = key(type, id);
      if (raw.containsKey(key)) return;
      if (entities.size() >= 16) throw OntologyConditionalStateDAO.unsupported("entity bound");
      String json = dao.raw(type, id);
      Class<? extends EntityInterface> clazz =
          switch (type) {
            case Entity.ONTOLOGY_CHANGE_SET -> OntologyChangeSet.class;
            case Entity.GLOSSARY_TERM -> GlossaryTerm.class;
            case Entity.GLOSSARY -> Glossary.class;
            case Entity.RELATIONSHIP_TYPE -> RelationshipType.class;
            case Entity.USER -> User.class;
            case Entity.TEAM -> Team.class;
            default -> throw OntologyConditionalStateDAO.unsupported("entity type");
          };
      EntityInterface entity = JsonUtils.readValue(json, clazz);
      if (Boolean.TRUE.equals(entity.getDeleted()))
        throw new BadRequestException("Conditional target deleted");
      raw.put(key, json);
      entities.put(key, entity);
    }

    EntityInterface require(String type, UUID id) {
      EntityInterface entity = entities.get(key(type, id));
      if (entity == null) throw OntologyConditionalStateDAO.unsupported("unlocked dependency");
      return entity;
    }

    void lockRanges(List<? extends EntityInterface> resources, Scope scope) {
      for (EntityInterface entity :
          resources.stream().sorted(Comparator.comparing(e -> e.getId().toString())).toList()) {
        scope.check();
        for (boolean outgoing : List.of(true, false))
          for (RelationRow row : dao.relations(entity.getId(), outgoing)) {
            String key =
                row.fromId() + ":" + row.toId() + ":" + row.relation() + ":" + row.relationType();
            relations.put(key, row);
          }
        if (!(entity instanceof User) && !(entity instanceof Team)) {
          List<Map<String, Object>> values = dao.tags(entity.getFullyQualifiedName());
          tags.put(entity.getId().toString(), values);
          if (!values.isEmpty())
            throw OntologyConditionalStateDAO.unsupported("nonempty tag dependencies");
        }
        if (relations.size() > 1024)
          throw OntologyConditionalStateDAO.unsupported("dependency row bound");
      }
    }

    List<EntityReference> refs(EntityInterface entity, Relationship relationship) {
      List<EntityReference> refs = new ArrayList<>();
      for (RelationRow row : relations.values())
        if (!row.deleted()
            && row.toId().equals(entity.getId())
            && row.relation() == relationship.ordinal()) {
          if (!Set.of(Entity.USER, Entity.TEAM).contains(row.fromEntity()))
            throw OntologyConditionalStateDAO.unsupported("owner/reviewer dependency type");
          refs.add(require(row.fromEntity(), row.fromId()).getEntityReference());
        }
      refs.sort(org.openmetadata.service.util.EntityUtil.compareEntityReference);
      return refs;
    }

    void hydrate(Set<UUID> termIds, Set<UUID> typeIds, Set<UUID> glossaryIds) {
      for (UUID id : glossaryIds) {
        Glossary glossary = (Glossary) require(Entity.GLOSSARY, id);
        glossary.setOwners(refs(glossary, Relationship.OWNS));
        glossary.setReviewers(refs(glossary, Relationship.REVIEWS));
        if (glossary.getOntologyConfiguration() != null
            && Boolean.TRUE.equals(glossary.getOntologyConfiguration().getReadOnly()))
          throw OntologyConditionalStateDAO.unsupported("readonly glossary");
      }
      for (UUID id : typeIds) {
        RelationshipType type = (RelationshipType) require(Entity.RELATIONSHIP_TYPE, id);
        type.setOwners(refs(type, Relationship.OWNS));
        type.setReviewers(refs(type, Relationship.REVIEWS));
        if ((type.getInverse() != null && !type.getId().equals(type.getInverse().getId()))
            || type.getCardinality() != null
            || (type.getDomain() != null && !type.getDomain().isEmpty())
            || (type.getRange() != null && !type.getRange().isEmpty())
            || !listOrEmpty(type.getPropertyChain()).isEmpty()
            || (type.getCharacteristics() != null && !type.getCharacteristics().isEmpty()))
          throw OntologyConditionalStateDAO.unsupported("relationship type dependency/constraint");
      }
      for (UUID id : termIds) {
        GlossaryTerm term = (GlossaryTerm) require(Entity.GLOSSARY_TERM, id);
        List<RelationRow> parents =
            relations.values().stream()
                .filter(
                    r ->
                        !r.deleted()
                            && r.toId().equals(id)
                            && (r.relation() == Relationship.CONTAINS.ordinal()
                                || r.relation() == Relationship.HAS.ordinal())
                            && (Entity.GLOSSARY.equals(r.fromEntity())
                                || Entity.GLOSSARY_TERM.equals(r.fromEntity())))
                .toList();
        if (parents.size() != 1 || !Entity.GLOSSARY.equals(parents.get(0).fromEntity()))
          throw OntologyConditionalStateDAO.unsupported("nonroot/missing glossary term");
        Glossary glossary = (Glossary) require(Entity.GLOSSARY, parents.get(0).fromId());
        term.setGlossary(glossary.getEntityReference());
        term.setParent(null);
        List<EntityReference> owners = refs(term, Relationship.OWNS);
        term.setOwners(owners.isEmpty() ? inherited(glossary.getOwners()) : owners);
        List<EntityReference> reviewers = new ArrayList<>(refs(term, Relationship.REVIEWS));
        for (EntityReference inherited : inherited(glossary.getReviewers()))
          if (reviewers.stream().noneMatch(r -> r.getId().equals(inherited.getId())))
            reviewers.add(inherited);
        reviewers.sort(org.openmetadata.service.util.EntityUtil.compareEntityReferenceById);
        term.setReviewers(reviewers);
        List<TermRelation> related = new ArrayList<>();
        for (RelationRow row : relations.values())
          if (!row.deleted()
              && row.relation() == Relationship.RELATED_TO.ordinal()
              && (row.fromId().equals(id) || row.toId().equals(id))) {
            if (!Entity.GLOSSARY_TERM.equals(row.fromEntity())
                || !Entity.GLOSSARY_TERM.equals(row.toEntity()))
              throw OntologyConditionalStateDAO.unsupported("nonterm related dependency");
            TermRelationMetadata metadata =
                JsonUtils.readValue(row.json(), TermRelationMetadata.class);
            RelationshipType type =
                typeIds.stream()
                    .map(t -> (RelationshipType) require(Entity.RELATIONSHIP_TYPE, t))
                    .filter(t -> t.getName().equals(row.relationType()))
                    .findFirst()
                    .orElseThrow(
                        () -> OntologyConditionalStateDAO.unsupported("extra relationship type"));
            UUID other = row.fromId().equals(id) ? row.toId() : row.fromId();
            GlossaryTerm target = (GlossaryTerm) require(Entity.GLOSSARY_TERM, other);
            related.add(
                new TermRelation()
                    .withId(metadata.getId())
                    .withTerm(target.getEntityReference())
                    .withRelationType(type.getName())
                    .withRelationshipType(type.getEntityReference())
                    .withProvenance(metadata.getProvenance())
                    .withStatus(metadata.getStatus())
                    .withCreatedBy(metadata.getCreatedBy())
                    .withCreatedAt(metadata.getCreatedAt()));
          }
        term.setRelatedTerms(related);
      }
      for (RelationRow row : relations.values()) {
        if (row.deleted()) continue;
        boolean ownerReviewer =
            (row.relation() == Relationship.OWNS.ordinal()
                    || row.relation() == Relationship.REVIEWS.ordinal())
                && Set.of(Entity.USER, Entity.TEAM).contains(row.fromEntity())
                && entities.containsKey(key(row.toEntity(), row.toId()));
        boolean parent =
            Entity.GLOSSARY.equals(row.fromEntity())
                && Entity.GLOSSARY_TERM.equals(row.toEntity())
                && (row.relation() == Relationship.CONTAINS.ordinal()
                    || row.relation() == Relationship.HAS.ordinal());
        boolean related =
            Entity.GLOSSARY_TERM.equals(row.fromEntity())
                && Entity.GLOSSARY_TERM.equals(row.toEntity())
                && row.relation() == Relationship.RELATED_TO.ordinal();
        boolean changeSetContainment = isKnownChangeSetContainment(changeSet, glossaryIds, row);
        boolean taskMention = isKnownTaskMention(termIds, row);
        if (!ownerReviewer && !parent && !related && !changeSetContainment && !taskMention)
          throw OntologyConditionalStateDAO.unsupported("additional relationship dependency");
      }
    }

    ExpectedState expected() {
      List<TargetToken> entries = new ArrayList<>();
      for (var entry : raw.entrySet()) {
        String[] parts = entry.getKey().split(":", 2);
        Map<String, Object> envelope = new LinkedHashMap<>();
        envelope.put("raw", JsonUtils.readTree(entry.getValue()));
        UUID id = UUID.fromString(parts[1]);
        envelope.put(
            "relations",
            relations.values().stream()
                .filter(r -> r.fromId().equals(id) || r.toId().equals(id))
                .toList());
        envelope.put("tags", tags.getOrDefault(id.toString(), List.of()));
        entries.add(new TargetToken(parts[0], id, digest(envelope)));
      }
      return new ExpectedState(1, changeSet.getId(), digest(entries), List.copyOf(entries));
    }

    StateResponse response() {
      return new StateResponse(
          expected(),
          changeSet,
          entities.values().stream()
              .filter(e -> e instanceof GlossaryTerm)
              .map(e -> (GlossaryTerm) e)
              .toList(),
          entities.values().stream()
              .filter(e -> e instanceof RelationshipType)
              .map(e -> (RelationshipType) e)
              .toList());
    }
  }

  private static List<EntityReference> inherited(List<EntityReference> refs) {
    return listOrEmpty(refs).stream()
        .map(r -> JsonUtils.deepCopy(r, EntityReference.class).withInherited(true))
        .toList();
  }

  private static String key(String type, UUID id) {
    return type + ":" + id;
  }

  private static void deadline(long end) {
    if (System.nanoTime() > end)
      throw OntologyConditionalStateDAO.unsupported("request deadline exceeded");
  }

  static String digest(Object value) {
    try {
      return "sha256:"
          + HexFormat.of()
              .formatHex(
                  MessageDigest.getInstance("SHA-256")
                      .digest(
                          canonical(JsonUtils.valueToTree(value))
                              .toString()
                              .getBytes(StandardCharsets.UTF_8)));
    } catch (java.security.NoSuchAlgorithmException exception) {
      throw new IllegalStateException(exception);
    }
  }

  static JsonNode canonical(JsonNode node) {
    if (node.isObject()) {
      ObjectNode result = JsonUtils.getObjectMapper().createObjectNode();
      node.properties().stream()
          .sorted(Map.Entry.comparingByKey())
          .forEach(e -> result.set(e.getKey(), canonical(e.getValue())));
      return result;
    }
    if (node.isArray()) {
      ArrayNode result = JsonUtils.getObjectMapper().createArrayNode();
      node.forEach(e -> result.add(canonical(e)));
      return result;
    }
    return node;
  }
}
