import { cleanup, render, screen, waitFor, within } from '@testing-library/react'
import { QueryClient, QueryClientProvider } from '@tanstack/react-query'
import { createMemoryRouter, RouterProvider } from 'react-router'
import { App } from 'antd'
import userEvent from '@testing-library/user-event'
import { afterEach, beforeEach, expect, it, vi } from 'vitest'
import { setSessionTokens, writeCachedValue } from '/Users/wangmingzhang/atlas-ui-admin-react/shared/auth-session'
import { httpClient } from '/Users/wangmingzhang/atlas-ui-admin-react/apps/console-react/src/platform/http'
import { MODELING_CAPABILITIES, modelingPendingStorageKey } from '/Users/wangmingzhang/atlas-ui-admin-react/apps/console-react/src/features/ontology/modelingAccessApi'
import { profileContinuationStorageKey } from '/Users/wangmingzhang/atlas-ui-admin-react/apps/console-react/src/features/ontology/modelingProfileContinuationApi'
import { provisionContinuationStorageKey } from '/Users/wangmingzhang/atlas-ui-admin-react/apps/console-react/src/features/ontology/modelingProvisionPreflightApi'
import { profileVerificationStorageKey } from '/Users/wangmingzhang/atlas-ui-admin-react/apps/console-react/src/features/ontology/modelingProfileVerificationApi'
import { profileVerificationResumeStorageKey } from '/Users/wangmingzhang/atlas-ui-admin-react/apps/console-react/src/features/ontology/modelingProfileVerificationResumeApi'
import Page from '/Users/wangmingzhang/atlas-ui-admin-react/apps/console-react/src/features/ontology/OntologyModelingAccessPage.tsx'
vi.mock('/Users/wangmingzhang/atlas-ui-admin-react/apps/console-react/src/platform/AuthProvider.tsx', () => ({ useAuth: () => ({ sessionKey: 'isolated-session' }) }))
vi.mock('/Users/wangmingzhang/atlas-ui-admin-react/apps/console-react/src/features/ontology/OntologyProjectScope.tsx', () => ({ OntologyProjectScope: ({ children }: { children: (x: unknown) => unknown }) => children(scope) }))
const owner = { tenantId:'42',actorId:'7',spaceId:'s-1',projectId:'p-1' }
const context = { role:'ADMIN',tenantId:'42',actorId:'7',spaces:[{id:'s-1',name:'知识空间',role:'ADMIN'}],projects:[{id:'p-1',name:'原项目',spaceId:'s-1',role:'ADMIN'}],capabilities:[] }
const scope = { context,project:{id:'p-1',tenantId:'42',spaceId:'s-1',name:'原项目',version:3},sessionKey:'isolated-session',refreshError:null,retryContext:()=>{} }
const modeling = {contractVersion:1 as const,projectId:'p-1',scope:{tenantId:'42',spaceId:'s-1',projectId:'p-1'},projectVersion:3,currentAccountId:'7',canManage:true,instances:[{instanceId:'om-approved',name:'批准实例',available:true,reason:''}]}
const access = {projectId:'p-1',accountId:'9',instanceId:'om-approved',nativeUserId:'44444444-4444-4444-8444-444444444444',bindingVersion:3,credentialEpoch:1,scope:modeling.scope,mappingState:'MAPPED' as const,managedSyncState:'UNKNOWN' as const,qualifications:MODELING_CAPABILITIES.map(capability=>({capability,state:'NOT_VERIFIED' as const,verifiedAt:null,evidenceRef:null})),version:1}
const intentId='11111111-1111-4111-8111-111111111111', nativeTaskId='22222222-2222-4222-8222-222222222222'
const intent = {intentId,family:'INIT' as const,operation:'PROVISION_IDENTITY',planId:'plan-1',planHash:'sha256:'+'a'.repeat(64),operationKey:'original-operation-key',scope:modeling.scope,initiator:'7',taskId:intentId,createdAt:'2030-01-01T00:00:00Z'}
const task = {intent,state:'UNKNOWN' as const,access,taskState:'UNKNOWN',allowedActions:['READ_ORIGINAL'],reason:'原预检已停止，保留原任务'}
const bindingId='33333333-3333-4333-8333-333333333333'
const cut = {configurationVersion:1,bindingVersion:3,desiredRevision:1,attempt:2,leaseEpoch:2,projectVersion:3,originalProfileDispatchEpoch:2,desiredHash:'sha256:'+'b'.repeat(64),requestHash:'sha256:'+'c'.repeat(64),historyHash:'sha256:'+'d'.repeat(64),originalProfileDispatchHash:'sha256:'+'1'.repeat(64),certificateHash:'sha256:'+'2'.repeat(64),planHash:intent.planHash,intentOperationKey:intent.operationKey}
const profile={nativeName:'original-native-name',nativeEmail:'user@example.invalid',nativeVersion:'0.1',currentDisplayName:null,desiredDisplayName:'原建模人员'}
const eligible = {intentId,nativeTaskId,bindingId,scope:modeling.scope,accountId:'9',instanceId:'om-approved',nativeUserId:access.nativeUserId,available:true,reason:'CURRENT_ORIGINAL_PROFILE_CONTINUATION_ALLOWED',contextHash:'sha256:'+'e'.repeat(64),cut,profile,continuationState:'NONE' as const,allowedActions:['READ_ORIGINAL','CONTINUE_ORIGINAL_PROFILE_STEP']}
const requested = {...eligible,available:false,contextHash:null,cut:null,profile:null,continuationState:'REQUESTED' as const,allowedActions:['READ_ORIGINAL'],reason:'MODELING_PROFILE_CONTINUATION_REQUESTED'}
const receipt = {intentId,nativeTaskId,bindingId,continuationState:'REQUESTED' as const,replayed:false,reason:'ORIGINAL_PROFILE_STEP_CONTINUATION_RECORDED'}
const pending = {version:1,projectId:'p-1',accountId:'9',instanceId:'om-approved',change:'CONFIGURE',confirm:{planId:intent.planId,planHash:intent.planHash,operationKey:intent.operationKey,cut:{scope:modeling.scope,actorId:'7',configurationVersion:1,identityVersion:1,credentialEpoch:null}}}
const original=httpClient.defaults.adapter
let client:QueryClient
const ok=(config:any,data:unknown)=>({config,data:{code:0,data},status:200,statusText:'OK',headers:{}})
beforeEach(()=>{vi.stubGlobal('fetch',vi.fn(()=>Promise.reject(new Error('REAL_HTTP_FORBIDDEN'))));vi.stubGlobal('XMLHttpRequest',class{constructor(){throw new Error('REAL_XHR_FORBIDDEN')}});vi.stubGlobal('WebSocket',class{constructor(){throw new Error('REAL_WS_FORBIDDEN')}});localStorage.clear();sessionStorage.clear();setSessionTokens({accessToken:'test-only',refreshToken:'test-only'});writeCachedValue('tenantId',42);Object.defineProperty(navigator,'locks',{configurable:true,value:{request:async(_key:string,_options:unknown,action:()=>unknown)=>action()}});client=new QueryClient({defaultOptions:{queries:{retry:false,refetchOnWindowFocus:false,gcTime:0},mutations:{retry:false}}})})
afterEach(()=>{cleanup();client.clear();httpClient.defaults.adapter=original;vi.restoreAllMocks();vi.unstubAllGlobals();localStorage.clear();sessionStorage.clear()})

function snapshot(store:Storage){return Object.fromEntries(Array.from({length:store.length},(_,i)=>store.key(i)!).sort().map(k=>[k,store.getItem(k)]))}
function mount(){const router=createMemoryRouter([{path:'/ontology/projects/:projectId/modeling-access',element:<App><QueryClientProvider client={client}><Page/></QueryClientProvider></App>}],{initialEntries:[`/ontology/projects/p-1/modeling-access?account=9&instance=om-approved&identityTask=${intentId}`]});render(<RouterProvider router={router}/>);return router}

it.each(['取消','Escape'] as const)('current real Page: %s closes the original-profile confirm without POST or pending/marker changes',async mode=>{
 const user=userEvent.setup()
 const originalPending=JSON.stringify(pending);localStorage.setItem(modelingPendingStorageKey(owner),originalPending)
 const historicalId='55555555-5555-4555-8555-555555555555'
 const historicalMarker=JSON.stringify({version:1,intentId:historicalId,contextHash:'sha256:'+'f'.repeat(64),operationKey:'retained-historical-operation-key'})
 for(const key of [profileContinuationStorageKey(owner,historicalId),provisionContinuationStorageKey(owner,historicalId),profileVerificationStorageKey(owner,historicalId),profileVerificationResumeStorageKey(owner,historicalId)])localStorage.setItem(key,historicalMarker)
 const calls:{method:string;path:string;params:unknown}[]=[]
 httpClient.defaults.adapter=async config=>{
  calls.push({method:config.method??'',path:config.url??'',params:config.params})
  if(config.method!=='get')throw new Error('UNEXPECTED_LOCAL_FIXTURE_WRITE')
  let data:unknown
  switch(config.url){
   case '/ontology/context':data=context;break
   case '/ontology/modeling-access/context':expect(config.params).toEqual({projectId:'p-1'});data=modeling;break
   case '/ontology/modeling-access/candidates':data={items:[{accountId:'9',name:'建模人员',eligibleRoles:['USER'],managedIdentityState:'UNKNOWN'}],nextCursor:null};break
   case '/ontology/modeling-access':expect(config.params).toEqual({projectId:'p-1',accountId:'9',instanceId:'om-approved'});data=access;break
   case `/ontology/modeling-access/intents/${intentId}`:expect(config.params).toEqual({projectId:'p-1'});data=task;break
   case `/ontology/modeling-access/intents/${intentId}/profile-continuation-context`:expect(config.params).toEqual({projectId:'p-1'});data=eligible;break
   default:throw new Error('UNEXPECTED_LOCAL_FIXTURE_GET '+config.url)
  }
  return ok(config,data)
 }
 const router=mount();const read=await screen.findByRole('button',{name:'核对原资料续接资格'})
 expect(calls.some(c=>c.path.endsWith('/profile-continuation-context'))).toBe(false)
 await user.click(read)
 const trigger=await screen.findByRole('button',{name:'继续原资料步骤'})
 await waitFor(()=>expect(read).toBeEnabled())
 const beforeLocal=snapshot(localStorage),beforeSession=snapshot(sessionStorage),beforeURL=router.state.location.pathname+router.state.location.search
 expect(localStorage.getItem(profileContinuationStorageKey(owner,intentId))).toBeNull()
 await user.click(trigger)
 const dialog=await screen.findByRole('dialog',{name:'继续原任务的资料补齐？'})
 expect(within(dialog).getByText('原任务 '+intentId)).toBeInTheDocument()
 const cancel=within(dialog).getByRole('button',{name:'取消'})
 // Read the actual default focus; no .focus(), force, fake timers or dialog mocks.
 await waitFor(()=>expect(cancel).toHaveFocus())
 if(mode==='取消')await user.click(cancel)
 else await user.keyboard('{Escape}')
 await waitFor(()=>expect(screen.queryByRole('dialog')).not.toBeInTheDocument())
 // Existing Page's afterClose explicitly returns to the original qualification/read control.
 await waitFor(()=>expect(read).toHaveFocus())
 expect(screen.getByRole('heading',{name:'原身份配置任务'})).toBeInTheDocument()
 expect(screen.getByText('原预检已停止，保留原任务')).toBeInTheDocument()
 expect(router.state.location.pathname+router.state.location.search).toBe(beforeURL)
 expect(localStorage.getItem(modelingPendingStorageKey(owner))).toBe(originalPending)
 expect(localStorage.getItem(profileContinuationStorageKey(owner,intentId))).toBeNull()
 expect(snapshot(localStorage)).toEqual(beforeLocal);expect(snapshot(sessionStorage)).toEqual(beforeSession)
 expect(calls.filter(c=>c.method!=='get')).toEqual([])
 expect(calls.filter(c=>c.path.endsWith('/prepare')||c.path.endsWith('/confirm')||c.path.endsWith('/profile-continuation'))).toEqual([])
})
