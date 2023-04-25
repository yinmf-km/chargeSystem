package com.course.app.common.flow.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.lang.Assert;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.BooleanUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.core.convert.Convert;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;
import com.course.app.common.core.object.*;
import com.course.app.common.core.util.MyDateUtil;
import com.course.app.common.core.util.MyCommonUtil;
import com.course.app.common.flow.exception.FlowOperationException;
import com.course.app.common.flow.object.FlowEntryExtensionData;
import com.course.app.common.flow.object.FlowTaskMultiSignAssign;
import com.course.app.common.flow.object.FlowTaskOperation;
import com.course.app.common.flow.object.FlowTaskPostCandidateGroup;
import com.course.app.common.flow.constant.FlowBackType;
import com.course.app.common.flow.constant.FlowConstant;
import com.course.app.common.flow.constant.FlowApprovalType;
import com.course.app.common.flow.constant.FlowTaskStatus;
import com.course.app.common.flow.model.*;
import com.course.app.common.flow.service.*;
import com.course.app.common.flow.util.BaseFlowIdentityExtHelper;
import com.course.app.common.flow.util.FlowCustomExtFactory;
import com.course.app.common.flow.vo.FlowTaskVo;
import lombok.Cleanup;
import lombok.extern.slf4j.Slf4j;
import org.flowable.bpmn.converter.BpmnXMLConverter;
import org.flowable.bpmn.model.*;
import org.flowable.bpmn.model.Process;
import org.flowable.common.engine.impl.de.odysseus.el.ExpressionFactoryImpl;
import org.flowable.common.engine.impl.de.odysseus.el.util.SimpleContext;
import org.flowable.common.engine.impl.identity.Authentication;
import org.flowable.common.engine.impl.javax.el.ExpressionFactory;
import org.flowable.common.engine.impl.javax.el.ValueExpression;
import org.flowable.engine.*;
import org.flowable.engine.delegate.ExecutionListener;
import org.flowable.engine.delegate.TaskListener;
import org.flowable.engine.history.*;
import org.flowable.engine.impl.persistence.entity.ExecutionEntityImpl;
import org.flowable.engine.repository.ProcessDefinition;
import org.flowable.engine.runtime.ChangeActivityStateBuilder;
import org.flowable.engine.runtime.Execution;
import org.flowable.engine.runtime.ProcessInstance;
import org.flowable.task.api.Task;
import org.flowable.task.api.TaskInfo;
import org.flowable.task.api.TaskQuery;
import org.flowable.task.api.history.HistoricTaskInstance;
import org.flowable.task.api.history.HistoricTaskInstanceQuery;
import org.flowable.variable.api.history.HistoricVariableInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

@Slf4j
@Service("flowApiService")
public class FlowApiServiceImpl implements FlowApiService {

    @Autowired
    private RepositoryService repositoryService;
    @Autowired
    private RuntimeService runtimeService;
    @Autowired
    private TaskService taskService;
    @Autowired
    private HistoryService historyService;
    @Autowired
    private FlowEntryService flowEntryService;
    @Autowired
    private FlowTaskCommentService flowTaskCommentService;
    @Autowired
    private FlowTaskExtService flowTaskExtService;
    @Autowired
    private FlowWorkOrderService flowWorkOrderService;
    @Autowired
    private FlowMessageService flowMessageService;
    @Autowired
    private FlowCustomExtFactory flowCustomExtFactory;
    @Autowired
    private FlowMultiInstanceTransService flowMultiInstanceTransService;

    @Transactional(rollbackFor = Exception.class)
    @Override
    public ProcessInstance start(String processDefinitionId, Object dataId) {
        String loginName = TokenData.takeFromRequest().getLoginName();
        Map<String, Object> variableMap = this.initAndGetProcessInstanceVariables(processDefinitionId);
        Authentication.setAuthenticatedUserId(loginName);
        String businessKey = dataId == null ? null : dataId.toString();
        return runtimeService.startProcessInstanceById(processDefinitionId, businessKey, variableMap);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public Task takeFirstTask(String processInstanceId, FlowTaskComment flowTaskComment, JSONObject taskVariableData) {
        String loginName = TokenData.takeFromRequest().getLoginName();
        // 获取流程启动后的第一个任务。
        Task task = taskService.createTaskQuery().processInstanceId(processInstanceId).active().singleResult();
        if (StrUtil.equalsAny(task.getAssignee(), loginName, FlowConstant.START_USER_NAME_VAR)) {
            // 按照规则，调用该方法的用户，就是第一个任务的assignee，因此默认会自动执行complete。
            flowTaskComment.fillWith(task);
            this.completeTask(task, flowTaskComment, taskVariableData);
        }
        return task;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public ProcessInstance startAndTakeFirst(
            String processDefinitionId, Object dataId, FlowTaskComment flowTaskComment, JSONObject taskVariableData) {
        String loginName = TokenData.takeFromRequest().getLoginName();
        Authentication.setAuthenticatedUserId(loginName);
        // 设置流程变量。
        Map<String, Object> variableMap = this.initAndGetProcessInstanceVariables(processDefinitionId);
        // 根据当前流程的主版本，启动一个流程实例，同时将businessKey参数设置为主表主键值。
        ProcessInstance instance = runtimeService.startProcessInstanceById(
                processDefinitionId, dataId.toString(), variableMap);
        // 获取流程启动后的第一个任务。
        Task task = taskService.createTaskQuery().processInstanceId(instance.getId()).active().singleResult();
        if (StrUtil.equalsAny(task.getAssignee(), loginName, FlowConstant.START_USER_NAME_VAR)) {
            // 按照规则，调用该方法的用户，就是第一个任务的assignee，因此默认会自动执行complete。
            flowTaskComment.fillWith(task);
            this.completeTask(task, flowTaskComment, taskVariableData);
        }
        return instance;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void submitConsign(
            HistoricTaskInstance startTaskInstance, Task multiInstanceActiveTask, String newAssignees, boolean isAdd) {
        JSONArray assigneeArray = JSON.parseArray(newAssignees);
        String multiInstanceExecId = this.getExecutionVariableStringWithSafe(
                multiInstanceActiveTask.getExecutionId(), FlowConstant.MULTI_SIGN_TASK_EXECUTION_ID_VAR);
        FlowMultiInstanceTrans trans =
                flowMultiInstanceTransService.getWithAssigneeListByMultiInstanceExecId(multiInstanceExecId);
        Set<String> assigneeSet = new HashSet<>(StrUtil.split(trans.getAssigneeList(), ","));
        Task runtimeTask = null;
        for (int i = 0; i < assigneeArray.size(); i++) {
            String assignee = assigneeArray.getString(i);
            if (isAdd) {
                assigneeSet.add(assignee);
            } else {
                assigneeSet.remove(assignee);
            }
            if (isAdd) {
                Map<String, Object> variables = new HashMap<>(2);
                variables.put("assignee", assigneeArray.getString(i));
                variables.put(FlowConstant.MULTI_SIGN_START_TASK_VAR, startTaskInstance.getId());
                runtimeService.addMultiInstanceExecution(
                        multiInstanceActiveTask.getTaskDefinitionKey(), multiInstanceActiveTask.getProcessInstanceId(), variables);
            } else {
                TaskQuery query = taskService.createTaskQuery().active();
                query.processInstanceId(multiInstanceActiveTask.getProcessInstanceId());
                query.taskDefinitionKey(multiInstanceActiveTask.getTaskDefinitionKey());
                query.taskAssignee(assignee);
                runtimeTask = query.singleResult();
                if (runtimeTask == null) {
                    throw new FlowOperationException("审批人 [" + assignee + "] 已经提交审批，不能执行减签操作！");
                }
                runtimeService.deleteMultiInstanceExecution(runtimeTask.getExecutionId(), false);
            }
        }
        if (!isAdd && runtimeTask != null) {
            this.doChangeTask(runtimeTask);
        }
        trans.setAssigneeList(StrUtil.join(",", assigneeSet));
        flowMultiInstanceTransService.updateById(trans);
        FlowTaskComment flowTaskComment = new FlowTaskComment();
        flowTaskComment.fillWith(startTaskInstance);
        flowTaskComment.setApprovalType(isAdd ? FlowApprovalType.MULTI_CONSIGN : FlowApprovalType.MULTI_MINUS_SIGN);
        String showName = TokenData.takeFromRequest().getLoginName();
        String comment = String.format("用户 [%s] [%s] [%s]。", isAdd ? "加签" : "减签", showName, newAssignees);
        flowTaskComment.setTaskComment(comment);
        flowTaskCommentService.saveNew(flowTaskComment);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void completeTask(Task task, FlowTaskComment flowTaskComment, JSONObject taskVariableData) {
        JSONObject passCopyData = null;
        if (taskVariableData != null) {
            passCopyData = (JSONObject) taskVariableData.remove(FlowConstant.COPY_DATA_KEY);
        }
        if (flowTaskComment != null) {
            // 这里处理多实例会签逻辑。
            if (flowTaskComment.getApprovalType().equals(FlowApprovalType.MULTI_SIGN)) {
                String loginName = TokenData.takeFromRequest().getLoginName();
                if (taskVariableData == null) {
                    taskVariableData = new JSONObject();
                }
                String assigneeList = this.getMultiInstanceAssigneeList(task, taskVariableData);
                Assert.isTrue(StrUtil.isNotBlank(assigneeList));
                taskVariableData.put(FlowConstant.MULTI_AGREE_COUNT_VAR, 0);
                taskVariableData.put(FlowConstant.MULTI_REFUSE_COUNT_VAR, 0);
                taskVariableData.put(FlowConstant.MULTI_ABSTAIN_COUNT_VAR, 0);
                taskVariableData.put(FlowConstant.MULTI_SIGN_NUM_OF_INSTANCES_VAR, 0);
                taskVariableData.put(FlowConstant.MULTI_SIGN_START_TASK_VAR, task.getId());
                String multiInstanceExecId = MyCommonUtil.generateUuid();
                taskVariableData.put(FlowConstant.MULTI_SIGN_TASK_EXECUTION_ID_VAR, multiInstanceExecId);
                String comment = String.format("用户 [%s] 会签 [%s]。", loginName, assigneeList);
                FlowMultiInstanceTrans multiInstanceTrans = new FlowMultiInstanceTrans(task);
                multiInstanceTrans.setMultiInstanceExecId(multiInstanceExecId);
                multiInstanceTrans.setAssigneeList(assigneeList);
                flowMultiInstanceTransService.saveNew(multiInstanceTrans);
                flowTaskComment.setTaskComment(comment);
            }
            // 处理转办。
            if (FlowApprovalType.TRANSFER.equals(flowTaskComment.getApprovalType())) {
                this.transferTo(task, flowTaskComment);
                return;
            }
            if (taskVariableData == null) {
                taskVariableData = new JSONObject();
            }
            this.handleMultiInstanceApprovalType(
                    task.getExecutionId(), flowTaskComment.getApprovalType(), taskVariableData);
            taskVariableData.put(FlowConstant.OPERATION_TYPE_VAR, flowTaskComment.getApprovalType());
            flowTaskComment.fillWith(task);
            if (this.isMultiInstanceTask(task.getProcessDefinitionId(), task.getTaskDefinitionKey())) {
                String multiInstanceExecId = getExecutionVariableStringWithSafe(
                        task.getExecutionId(), FlowConstant.MULTI_SIGN_TASK_EXECUTION_ID_VAR);
                FlowMultiInstanceTrans multiInstanceTrans = new FlowMultiInstanceTrans(task);
                multiInstanceTrans.setMultiInstanceExecId(multiInstanceExecId);
                flowMultiInstanceTransService.saveNew(multiInstanceTrans);
                flowTaskComment.setMultiInstanceExecId(multiInstanceExecId);
            }
            flowTaskCommentService.saveNew(flowTaskComment);
        }
        // 判断当前完成执行的任务，是否存在抄送设置。
        Object copyData = runtimeService.getVariable(
                task.getProcessInstanceId(), FlowConstant.COPY_DATA_MAP_PREFIX + task.getTaskDefinitionKey());
        if (copyData != null || passCopyData != null) {
            JSONObject copyDataJson = this.mergeCopyData(copyData, passCopyData);
            flowMessageService.saveNewCopyMessage(task, copyDataJson);
        }
        Integer approvalStatus = MapUtil.getInt(taskVariableData, FlowConstant.LATEST_APPROVAL_STATUS_KEY);
        flowWorkOrderService.updateLatestApprovalStatusByProcessInstanceId(task.getProcessInstanceId(), approvalStatus);
        taskService.complete(task.getId(), taskVariableData);
        flowMessageService.updateFinishedStatusByTaskId(task.getId());
    }

    private String getMultiInstanceAssigneeList(Task task, JSONObject taskVariableData) {
        JSONArray assigneeArray = taskVariableData.getJSONArray(FlowConstant.MULTI_ASSIGNEE_LIST_VAR);
        String assigneeList;
        if (CollUtil.isEmpty(assigneeArray)) {
            FlowTaskExt flowTaskExt = flowTaskExtService.getByProcessDefinitionIdAndTaskId(
                    task.getProcessDefinitionId(), task.getTaskDefinitionKey());
            assigneeList = this.buildMutiSignAssigneeList(flowTaskExt.getOperationListJson());
            if (assigneeList != null) {
                taskVariableData.put(FlowConstant.MULTI_ASSIGNEE_LIST_VAR, StrUtil.split(assigneeList, ','));
            }
        } else {
            assigneeList = CollUtil.join(assigneeArray, ",");
        }
        return assigneeList;
    }

    private JSONObject mergeCopyData(Object copyData, JSONObject passCopyData) {
        // passCopyData是传阅数据，copyData是抄送数据。
        JSONObject resultCopyDataJson = passCopyData;
        if (resultCopyDataJson == null) {
            resultCopyDataJson = JSON.parseObject(copyData.toString());
        } else if (copyData != null) {
            JSONObject copyDataJson = JSON.parseObject(copyData.toString());
            for (Map.Entry<String, Object> entry : copyDataJson.entrySet()) {
                String value = resultCopyDataJson.getString(entry.getKey());
                if (value == null) {
                    resultCopyDataJson.put(entry.getKey(), entry.getValue());
                } else {
                    List<String> list1 = StrUtil.split(value, ",");
                    List<String> list2 = StrUtil.split(entry.getValue().toString(), ",");
                    Set<String> valueSet = new HashSet<>(list1);
                    valueSet.addAll(list2);
                    resultCopyDataJson.put(entry.getKey(), StrUtil.join(",", valueSet));
                }
            }
        }
        this.processMergeCopyData(resultCopyDataJson);
        return resultCopyDataJson;
    }

    private void processMergeCopyData(JSONObject resultCopyDataJson) {
        TokenData tokenData = TokenData.takeFromRequest();
        BaseFlowIdentityExtHelper flowIdentityExtHelper = flowCustomExtFactory.getFlowIdentityExtHelper();
        for (Map.Entry<String, Object> entry : resultCopyDataJson.entrySet()) {
            String type = entry.getKey();
            switch (type) {
                case FlowConstant.GROUP_TYPE_UP_DEPT_POST_LEADER_VAR:
                    Object upLeaderDeptPostId =
                            flowIdentityExtHelper.getUpLeaderDeptPostId(tokenData.getDeptId());
                    entry.setValue(upLeaderDeptPostId);
                    break;
                case FlowConstant.GROUP_TYPE_DEPT_POST_LEADER_VAR:
                    Object leaderDeptPostId =
                            flowIdentityExtHelper.getLeaderDeptPostId(tokenData.getDeptId());
                    entry.setValue(leaderDeptPostId);
                    break;
                case FlowConstant.GROUP_TYPE_SELF_DEPT_POST_VAR:
                    Set<String> selfPostIdSet = new HashSet<>(StrUtil.split(entry.getValue().toString(), ","));
                    Map<String, String> deptPostIdMap =
                            flowIdentityExtHelper.getDeptPostIdMap(tokenData.getDeptId(), selfPostIdSet);
                    String deptPostIdValues = "";
                    if (deptPostIdMap != null) {
                        deptPostIdValues = StrUtil.join(",", deptPostIdMap.values());
                    }
                    entry.setValue(deptPostIdValues);
                    break;
                case FlowConstant.GROUP_TYPE_SIBLING_DEPT_POST_VAR:
                    Set<String> siblingPostIdSet = new HashSet<>(StrUtil.split(entry.getValue().toString(), ","));
                    Map<String, String> siblingDeptPostIdMap =
                            flowIdentityExtHelper.getSiblingDeptPostIdMap(tokenData.getDeptId(), siblingPostIdSet);
                    String siblingDeptPostIdValues = "";
                    if (siblingDeptPostIdMap != null) {
                        siblingDeptPostIdValues = StrUtil.join(",", siblingDeptPostIdMap.values());
                    }
                    entry.setValue(siblingDeptPostIdValues);
                    break;
                case FlowConstant.GROUP_TYPE_UP_DEPT_POST_VAR:
                    Set<String> upPostIdSet = new HashSet<>(StrUtil.split(entry.getValue().toString(), ","));
                    Map<String, String> upDeptPostIdMap =
                            flowIdentityExtHelper.getUpDeptPostIdMap(tokenData.getDeptId(), upPostIdSet);
                    String upDeptPostIdValues = "";
                    if (upDeptPostIdMap != null) {
                        upDeptPostIdValues = StrUtil.join(",", upDeptPostIdMap.values());
                    }
                    entry.setValue(upDeptPostIdValues);
                    break;
                default:
                    break;
            }
        }
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public CallResult verifyAssigneeOrCandidateAndClaim(Task task) {
        String errorMessage;
        String loginName = TokenData.takeFromRequest().getLoginName();
        // 这里必须先执行拾取操作，如果当前用户是候选人，特别是对于分布式场景，更是要先完成候选人的拾取。
        if (task.getAssignee() == null) {
            // 没有指派人
            if (!this.isAssigneeOrCandidate(task)) {
                errorMessage = "数据验证失败，当前用户不是该待办任务的候选人，请刷新后重试！";
                return CallResult.error(errorMessage);
            }
            // 作为候选人主动拾取任务。
            taskService.claim(task.getId(), loginName);
        } else {
            if (!task.getAssignee().equals(loginName)) {
                errorMessage = "数据验证失败，当前用户不是该待办任务的指派人，请刷新后重试！";
                return CallResult.error(errorMessage);
            }
        }
        return CallResult.ok();
    }

    @Override
    public Map<String, Object> initAndGetProcessInstanceVariables(String processDefinitionId) {
        TokenData tokenData = TokenData.takeFromRequest();
        String loginName = tokenData.getLoginName();
        // 设置流程变量。
        Map<String, Object> variableMap = new HashMap<>(4);
        variableMap.put(FlowConstant.PROC_INSTANCE_INITIATOR_VAR, loginName);
        variableMap.put(FlowConstant.PROC_INSTANCE_START_USER_NAME_VAR, loginName);
        List<FlowTaskExt> flowTaskExtList = flowTaskExtService.getByProcessDefinitionId(processDefinitionId);
        boolean hasDeptPostLeader = false;
        boolean hasUpDeptPostLeader = false;
        boolean hasPostCandidateGroup = false;
        for (FlowTaskExt flowTaskExt : flowTaskExtList) {
            if (StrUtil.equals(flowTaskExt.getGroupType(), FlowConstant.GROUP_TYPE_UP_DEPT_POST_LEADER)) {
                hasUpDeptPostLeader = true;
            } else if (StrUtil.equals(flowTaskExt.getGroupType(), FlowConstant.GROUP_TYPE_DEPT_POST_LEADER)) {
                hasDeptPostLeader = true;
            } else if (StrUtil.equals(flowTaskExt.getGroupType(), FlowConstant.GROUP_TYPE_POST)) {
                hasPostCandidateGroup = true;
            }
        }
        // 如果流程图的配置中包含用户身份相关的变量(如：部门领导和上级领导审批)，flowIdentityExtHelper就不能为null。
        // 这个需要子类去实现 BaseFlowIdentityExtHelper 接口，并注册到FlowCustomExtFactory的工厂中。
        BaseFlowIdentityExtHelper flowIdentityExtHelper = flowCustomExtFactory.getFlowIdentityExtHelper();
        if (hasUpDeptPostLeader) {
            Assert.notNull(flowIdentityExtHelper);
            Object upLeaderDeptPostId = flowIdentityExtHelper.getUpLeaderDeptPostId(tokenData.getDeptId());
            if (upLeaderDeptPostId == null) {
                variableMap.put(FlowConstant.GROUP_TYPE_UP_DEPT_POST_LEADER_VAR, null);
            } else {
                variableMap.put(FlowConstant.GROUP_TYPE_UP_DEPT_POST_LEADER_VAR, upLeaderDeptPostId.toString());
            }
        }
        if (hasDeptPostLeader) {
            Assert.notNull(flowIdentityExtHelper);
            Object leaderDeptPostId = flowIdentityExtHelper.getLeaderDeptPostId(tokenData.getDeptId());
            if (leaderDeptPostId == null) {
                variableMap.put(FlowConstant.GROUP_TYPE_DEPT_POST_LEADER_VAR, null);
            } else {
                variableMap.put(FlowConstant.GROUP_TYPE_DEPT_POST_LEADER_VAR, leaderDeptPostId.toString());
            }
        }
        if (hasPostCandidateGroup) {
            Assert.notNull(flowIdentityExtHelper);
            Map<String, Object> postGroupDataMap =
                    this.buildPostCandidateGroupData(flowIdentityExtHelper, flowTaskExtList);
            variableMap.putAll(postGroupDataMap);
        }
        this.buildCopyData(flowTaskExtList, variableMap);
        return variableMap;
    }

    private void buildCopyData(List<FlowTaskExt> flowTaskExtList, Map<String, Object> variableMap) {
        for (FlowTaskExt flowTaskExt : flowTaskExtList) {
            if (StrUtil.isBlank(flowTaskExt.getCopyListJson())) {
                continue;
            }
            List<JSONObject> copyDataList = JSON.parseArray(flowTaskExt.getCopyListJson(), JSONObject.class);
            Map<String, Object> copyDataMap = new HashMap<>(copyDataList.size());
            for (JSONObject copyData : copyDataList) {
                String type = copyData.getString("type");
                String id = copyData.getString("id");
                copyDataMap.put(type, id == null ? "" : id);
            }
            variableMap.put(FlowConstant.COPY_DATA_MAP_PREFIX + flowTaskExt.getTaskId(), JSON.toJSONString(copyDataMap));
        }
    }

    private Map<String, Object> buildPostCandidateGroupData(
            BaseFlowIdentityExtHelper flowIdentityExtHelper, List<FlowTaskExt> flowTaskExtList) {
        Map<String, Object> postVariableMap = new HashMap<>();
        Set<String> selfPostIdSet = new HashSet<>();
        Set<String> siblingPostIdSet = new HashSet<>();
        Set<String> upPostIdSet = new HashSet<>();
        for (FlowTaskExt flowTaskExt : flowTaskExtList) {
            if (flowTaskExt.getGroupType().equals(FlowConstant.GROUP_TYPE_POST)) {
                Assert.notNull(flowTaskExt.getDeptPostListJson());
                List<FlowTaskPostCandidateGroup> groupDataList =
                        JSONArray.parseArray(flowTaskExt.getDeptPostListJson(), FlowTaskPostCandidateGroup.class);
                for (FlowTaskPostCandidateGroup groupData : groupDataList) {
                    switch (groupData.getType()) {
                        case FlowConstant.GROUP_TYPE_SELF_DEPT_POST_VAR:
                            selfPostIdSet.add(groupData.getPostId());
                            break;
                        case FlowConstant.GROUP_TYPE_SIBLING_DEPT_POST_VAR:
                            siblingPostIdSet.add(groupData.getPostId());
                            break;
                        case FlowConstant.GROUP_TYPE_UP_DEPT_POST_VAR:
                            upPostIdSet.add(groupData.getPostId());
                            break;
                        default:
                            break;
                    }
                }
            }
        }
        postVariableMap.putAll(this.buildSelfPostCandidateGroupData(flowIdentityExtHelper, selfPostIdSet));
        postVariableMap.putAll(this.buildSiblingPostCandidateGroupData(flowIdentityExtHelper, siblingPostIdSet));
        postVariableMap.putAll(this.buildUpPostCandidateGroupData(flowIdentityExtHelper, upPostIdSet));
        return postVariableMap;
    }

    private Map<String, Object> buildSelfPostCandidateGroupData(
            BaseFlowIdentityExtHelper flowIdentityExtHelper, Set<String> selfPostIdSet) {
        Map<String, Object> postVariableMap = new HashMap<>();
        if (CollUtil.isNotEmpty(selfPostIdSet)) {
            Map<String, String> deptPostIdMap =
                    flowIdentityExtHelper.getDeptPostIdMap(TokenData.takeFromRequest().getDeptId(), selfPostIdSet);
            for (String postId : selfPostIdSet) {
                if (MapUtil.isNotEmpty(deptPostIdMap) && deptPostIdMap.containsKey(postId)) {
                    String deptPostId = deptPostIdMap.get(postId);
                    postVariableMap.put(FlowConstant.SELF_DEPT_POST_PREFIX + postId, deptPostId);
                } else {
                    postVariableMap.put(FlowConstant.SELF_DEPT_POST_PREFIX + postId, "");
                }
            }
        }
        return postVariableMap;
    }

    private Map<String, Object> buildSiblingPostCandidateGroupData(
            BaseFlowIdentityExtHelper flowIdentityExtHelper, Set<String> siblingPostIdSet) {
        Map<String, Object> postVariableMap = new HashMap<>();
        if (CollUtil.isNotEmpty(siblingPostIdSet)) {
            Map<String, String> siblingDeptPostIdMap =
                    flowIdentityExtHelper.getSiblingDeptPostIdMap(TokenData.takeFromRequest().getDeptId(), siblingPostIdSet);
            for (String postId : siblingPostIdSet) {
                if (MapUtil.isNotEmpty(siblingDeptPostIdMap) && siblingDeptPostIdMap.containsKey(postId)) {
                    String siblingDeptPostId = siblingDeptPostIdMap.get(postId);
                    postVariableMap.put(FlowConstant.SIBLING_DEPT_POST_PREFIX + postId, siblingDeptPostId);
                } else {
                    postVariableMap.put(FlowConstant.SIBLING_DEPT_POST_PREFIX + postId, "");
                }
            }
        }
        return postVariableMap;
    }

    private Map<String, Object> buildUpPostCandidateGroupData(
            BaseFlowIdentityExtHelper flowIdentityExtHelper, Set<String> upPostIdSet) {
        Map<String, Object> postVariableMap = new HashMap<>();
        if (CollUtil.isNotEmpty(upPostIdSet)) {
            Map<String, String> upDeptPostIdMap =
                    flowIdentityExtHelper.getUpDeptPostIdMap(TokenData.takeFromRequest().getDeptId(), upPostIdSet);
            for (String postId : upPostIdSet) {
                if (MapUtil.isNotEmpty(upDeptPostIdMap) && upDeptPostIdMap.containsKey(postId)) {
                    String upDeptPostId = upDeptPostIdMap.get(postId);
                    postVariableMap.put(FlowConstant.UP_DEPT_POST_PREFIX + postId, upDeptPostId);
                } else {
                    postVariableMap.put(FlowConstant.UP_DEPT_POST_PREFIX + postId, "");
                }
            }
        }
        return postVariableMap;
    }

    @Override
    public boolean isAssigneeOrCandidate(TaskInfo task) {
        String loginName = TokenData.takeFromRequest().getLoginName();
        if (StrUtil.isNotBlank(task.getAssignee())) {
            return StrUtil.equals(loginName, task.getAssignee());
        }
        TaskQuery query = taskService.createTaskQuery();
        this.buildCandidateCondition(query, loginName);
        query.taskId(task.getId());
        return query.active().count() != 0;
    }

    @Override
    public Collection<FlowElement> getProcessAllElements(String processDefinitionId) {
        Process process = repositoryService.getBpmnModel(processDefinitionId).getProcesses().get(0);
        return this.getAllElements(process.getFlowElements(), null);
    }

    @Override
    public boolean isProcessInstanceStarter(String processInstanceId) {
        String loginName = TokenData.takeFromRequest().getLoginName();
        return historyService.createHistoricProcessInstanceQuery()
                .processInstanceId(processInstanceId).startedBy(loginName).count() != 0;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void setBusinessKeyForProcessInstance(String processInstanceId, Object dataId) {
        runtimeService.updateBusinessKey(processInstanceId, dataId.toString());
    }

    @Override
    public boolean existActiveProcessInstance(String processInstanceId) {
        return runtimeService.createProcessInstanceQuery()
                .processInstanceId(processInstanceId).active().count() != 0;
    }

    @Override
    public ProcessInstance getProcessInstance(String processInstanceId) {
        return runtimeService.createProcessInstanceQuery().processInstanceId(processInstanceId).singleResult();
    }

    @Override
    public Task getProcessInstanceActiveTask(String processInstanceId, String taskId) {
        TaskQuery query = taskService.createTaskQuery().processInstanceId(processInstanceId);
        if (StrUtil.isNotBlank(taskId)) {
            query.taskId(taskId);
        }
        return query.active().singleResult();
    }

    @Override
    public List<Task> getProcessInstanceActiveTaskList(String processInstanceId) {
        return taskService.createTaskQuery().processInstanceId(processInstanceId).list();
    }

    @Override
    public Task getTaskById(String taskId) {
        return taskService.createTaskQuery().taskId(taskId).singleResult();
    }

    @Override
    public MyPageData<Task> getTaskListByUserName(
            String username, String definitionKey, String definitionName, String taskName, MyPageParam pageParam) {
        TaskQuery query = taskService.createTaskQuery().active();
        if (StrUtil.isNotBlank(definitionKey)) {
            query.processDefinitionKey(definitionKey);
        }
        if (StrUtil.isNotBlank(definitionName)) {
            query.processDefinitionNameLike("%" + definitionName + "%");
        }
        if (StrUtil.isNotBlank(taskName)) {
            query.taskNameLike("%" + taskName + "%");
        }
        this.buildCandidateCondition(query, username);
        long totalCount = query.count();
        query.orderByTaskCreateTime().desc();
        int firstResult = (pageParam.getPageNum() - 1) * pageParam.getPageSize();
        List<Task> taskList = query.listPage(firstResult, pageParam.getPageSize());
        return new MyPageData<>(taskList, totalCount);
    }

    @Override
    public long getTaskCountByUserName(String username) {
        return taskService.createTaskQuery().taskCandidateOrAssigned(username).active().count();
    }

    @Override
    public List<Task> getTaskListByProcessInstanceIds(List<String> processInstanceIdSet) {
        return taskService.createTaskQuery().processInstanceIdIn(processInstanceIdSet).active().list();
    }

    @Override
    public List<ProcessInstance> getProcessInstanceList(Set<String> processInstanceIdSet) {
        return runtimeService.createProcessInstanceQuery().processInstanceIds(processInstanceIdSet).list();
    }

    @Override
    public ProcessDefinition getProcessDefinitionById(String processDefinitionId) {
        return repositoryService.createProcessDefinitionQuery().processDefinitionId(processDefinitionId).singleResult();
    }

    @Override
    public List<ProcessDefinition> getProcessDefinitionList(Set<String> processDefinitionIdSet) {
        return repositoryService.createProcessDefinitionQuery().processDefinitionIds(processDefinitionIdSet).list();
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void suspendProcessDefinition(String processDefinitionId) {
        repositoryService.suspendProcessDefinitionById(processDefinitionId);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void activateProcessDefinition(String processDefinitionId) {
        repositoryService.activateProcessDefinitionById(processDefinitionId);
    }

    @Override
    public BpmnModel getBpmnModelByDefinitionId(String processDefinitionId) {
        return repositoryService.getBpmnModel(processDefinitionId);
    }

    @Override
    public boolean isMultiInstanceTask(String processDefinitionId, String taskKey) {
        BpmnModel model = this.getBpmnModelByDefinitionId(processDefinitionId);
        FlowElement flowElement = model.getFlowElement(taskKey);
        if (!(flowElement instanceof UserTask)) {
            return false;
        }
        UserTask userTask = (UserTask) flowElement;
        return userTask.hasMultiInstanceLoopCharacteristics();
    }

    @Override
    public ProcessDefinition getProcessDefinitionByDeployId(String deployId) {
        return repositoryService.createProcessDefinitionQuery().deploymentId(deployId).singleResult();
    }

    @Override
    public void setProcessInstanceVariables(String processInstanceId, Map<String, Object> variableMap) {
        runtimeService.setVariables(processInstanceId, variableMap);
    }

    @Override
    public Object getProcessInstanceVariable(String processInstanceId, String variableName) {
        return runtimeService.getVariable(processInstanceId, variableName);
    }

    @Override
    public List<FlowTaskVo> convertToFlowTaskList(List<Task> taskList) {
        List<FlowTaskVo> flowTaskVoList = new LinkedList<>();
        if (CollUtil.isEmpty(taskList)) {
            return flowTaskVoList;
        }
        Set<String> processDefinitionIdSet = taskList.stream()
                .map(Task::getProcessDefinitionId).collect(Collectors.toSet());
        Set<String> procInstanceIdSet = taskList.stream()
                .map(Task::getProcessInstanceId).collect(Collectors.toSet());
        List<FlowEntryPublish> flowEntryPublishList =
                flowEntryService.getFlowEntryPublishList(processDefinitionIdSet);
        Map<String, FlowEntryPublish> flowEntryPublishMap =
                flowEntryPublishList.stream().collect(Collectors.toMap(FlowEntryPublish::getProcessDefinitionId, c -> c));
        List<ProcessInstance> instanceList = this.getProcessInstanceList(procInstanceIdSet);
        Map<String, ProcessInstance> instanceMap =
                instanceList.stream().collect(Collectors.toMap(ProcessInstance::getId, c -> c));
        List<ProcessDefinition> definitionList = this.getProcessDefinitionList(processDefinitionIdSet);
        Map<String, ProcessDefinition> definitionMap =
                definitionList.stream().collect(Collectors.toMap(ProcessDefinition::getId, c -> c));
        List<FlowWorkOrder> workOrderList =
                flowWorkOrderService.getInList("processInstanceId", procInstanceIdSet);
        Map<String, FlowWorkOrder> workOrderMap =
                workOrderList.stream().collect(Collectors.toMap(FlowWorkOrder::getProcessInstanceId, c -> c));
        for (Task task : taskList) {
            FlowTaskVo flowTaskVo = new FlowTaskVo();
            flowTaskVo.setTaskId(task.getId());
            flowTaskVo.setTaskName(task.getName());
            flowTaskVo.setTaskKey(task.getTaskDefinitionKey());
            flowTaskVo.setTaskFormKey(task.getFormKey());
            flowTaskVo.setEntryId(flowEntryPublishMap.get(task.getProcessDefinitionId()).getEntryId());
            ProcessDefinition processDefinition = definitionMap.get(task.getProcessDefinitionId());
            flowTaskVo.setProcessDefinitionId(processDefinition.getId());
            flowTaskVo.setProcessDefinitionName(processDefinition.getName());
            flowTaskVo.setProcessDefinitionKey(processDefinition.getKey());
            flowTaskVo.setProcessDefinitionVersion(processDefinition.getVersion());
            ProcessInstance processInstance = instanceMap.get(task.getProcessInstanceId());
            flowTaskVo.setProcessInstanceId(processInstance.getId());
            Object initiator = this.getProcessInstanceVariable(
                    processInstance.getId(), FlowConstant.PROC_INSTANCE_INITIATOR_VAR);
            flowTaskVo.setProcessInstanceInitiator(initiator.toString());
            flowTaskVo.setProcessInstanceStartTime(processInstance.getStartTime());
            flowTaskVo.setBusinessKey(processInstance.getBusinessKey());
            FlowWorkOrder flowWorkOrder = workOrderMap.get(task.getProcessInstanceId());
            if (flowWorkOrder != null) {
                flowTaskVo.setIsDraft(flowWorkOrder.getFlowStatus().equals(FlowTaskStatus.DRAFT));
            }
            flowTaskVoList.add(flowTaskVo);
        }
        Set<String> loginNameSet = flowTaskVoList.stream()
                .map(FlowTaskVo::getProcessInstanceInitiator).collect(Collectors.toSet());
        Map<String, String> userInfoMap = flowCustomExtFactory
                .getFlowIdentityExtHelper().mapUserShowNameByLoginName(loginNameSet);
        for (FlowTaskVo flowTaskVo : flowTaskVoList) {
            flowTaskVo.setShowName(userInfoMap.get(flowTaskVo.getProcessInstanceInitiator()));
        }
        return flowTaskVoList;
    }

    @Override
    public void addProcessInstanceEndListener(BpmnModel bpmnModel, Class<? extends ExecutionListener> listenerClazz) {
        Assert.notNull(listenerClazz);
        Process process = bpmnModel.getMainProcess();
        FlowableListener listener = this.createListener("end", listenerClazz.getName());
        process.getExecutionListeners().add(listener);
    }

    @Override
    public void addExecutionListener(
            FlowElement flowElement,
            Class<? extends ExecutionListener> listenerClazz,
            String event,
            List<FieldExtension> fieldExtensions) {
        Assert.notNull(listenerClazz);
        FlowableListener listener = this.createListener(event, listenerClazz.getName());
        if (fieldExtensions != null) {
            listener.setFieldExtensions(fieldExtensions);
        }
        flowElement.getExecutionListeners().add(listener);
    }

    @Override
    public void addTaskCreateListener(UserTask userTask, Class<? extends TaskListener> listenerClazz) {
        Assert.notNull(listenerClazz);
        FlowableListener listener = this.createListener("create", listenerClazz.getName());
        userTask.getTaskListeners().add(listener);
    }

    @Override
    public HistoricProcessInstance getHistoricProcessInstance(String processInstanceId) {
        return historyService.createHistoricProcessInstanceQuery().processInstanceId(processInstanceId).singleResult();
    }

    @Override
    public List<HistoricProcessInstance> getHistoricProcessInstanceList(Set<String> processInstanceIdSet) {
        return historyService.createHistoricProcessInstanceQuery().processInstanceIds(processInstanceIdSet).list();
    }

    @Override
    public MyPageData<HistoricProcessInstance> getHistoricProcessInstanceList(
            String processDefinitionKey,
            String processDefinitionName,
            String startUser,
            String beginDate,
            String endDate,
            MyPageParam pageParam,
            boolean finishedOnly) throws ParseException {
        HistoricProcessInstanceQuery query = historyService.createHistoricProcessInstanceQuery();
        if (StrUtil.isNotBlank(processDefinitionKey)) {
            query.processDefinitionKey(processDefinitionKey);
        }
        if (StrUtil.isNotBlank(processDefinitionName)) {
            query.processDefinitionName(processDefinitionName);
        }
        if (StrUtil.isNotBlank(startUser)) {
            query.startedBy(startUser);
        }
        if (StrUtil.isNotBlank(beginDate)) {
            SimpleDateFormat sdf = new SimpleDateFormat(MyDateUtil.COMMON_SHORT_DATETIME_FORMAT);
            query.startedAfter(sdf.parse(beginDate));
        }
        if (StrUtil.isNotBlank(endDate)) {
            SimpleDateFormat sdf = new SimpleDateFormat(MyDateUtil.COMMON_SHORT_DATETIME_FORMAT);
            query.startedBefore(sdf.parse(endDate));
        }
        if (finishedOnly) {
            query.finished();
        }
        query.orderByProcessInstanceStartTime().desc();
        long totalCount = query.count();
        int firstResult = (pageParam.getPageNum() - 1) * pageParam.getPageSize();
        List<HistoricProcessInstance> instanceList = query.listPage(firstResult, pageParam.getPageSize());
        return new MyPageData<>(instanceList, totalCount);
    }

    @Override
    public MyPageData<HistoricTaskInstance> getHistoricTaskInstanceFinishedList(
            String processDefinitionName,
            String beginDate,
            String endDate,
            MyPageParam pageParam) throws ParseException {
        String loginName = TokenData.takeFromRequest().getLoginName();
        HistoricTaskInstanceQuery query = historyService.createHistoricTaskInstanceQuery()
                .taskAssignee(loginName)
                .finished();
        if (StrUtil.isNotBlank(processDefinitionName)) {
            query.processDefinitionName(processDefinitionName);
        }
        if (StrUtil.isNotBlank(beginDate)) {
            SimpleDateFormat sdf = new SimpleDateFormat(MyDateUtil.COMMON_SHORT_DATETIME_FORMAT);
            query.taskCompletedAfter(sdf.parse(beginDate));
        }
        if (StrUtil.isNotBlank(endDate)) {
            SimpleDateFormat sdf = new SimpleDateFormat(MyDateUtil.COMMON_SHORT_DATETIME_FORMAT);
            query.taskCompletedBefore(sdf.parse(endDate));
        }
        query.orderByHistoricTaskInstanceEndTime().desc();
        long totalCount = query.count();
        int firstResult = (pageParam.getPageNum() - 1) * pageParam.getPageSize();
        List<HistoricTaskInstance> instanceList = query.listPage(firstResult, pageParam.getPageSize());
        return new MyPageData<>(instanceList, totalCount);
    }

    @Override
    public List<HistoricActivityInstance> getHistoricActivityInstanceList(String processInstanceId) {
        return historyService.createHistoricActivityInstanceQuery().processInstanceId(processInstanceId).list();
    }

    @Override
    public List<HistoricActivityInstance> getHistoricActivityInstanceListOrderByStartTime(String processInstanceId) {
        return historyService.createHistoricActivityInstanceQuery()
                .processInstanceId(processInstanceId).orderByHistoricActivityInstanceStartTime().asc().list();
    }

    @Override
    public HistoricTaskInstance getHistoricTaskInstance(String processInstanceId, String taskId) {
        return historyService.createHistoricTaskInstanceQuery()
                .processInstanceId(processInstanceId).taskId(taskId).singleResult();
    }

    @Override
    public List<HistoricActivityInstance> getHistoricUnfinishedInstanceList(String processInstanceId) {
        return historyService.createHistoricActivityInstanceQuery()
                .processInstanceId(processInstanceId).unfinished().list();
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public CallResult stopProcessInstance(String processInstanceId, String stopReason, boolean forCancel) {
        //需要先更新状态，以便FlowFinishedListener监听器可以正常的判断流程结束的状态。
        int status = FlowTaskStatus.STOPPED;
        if (forCancel) {
            status = FlowTaskStatus.CANCELLED;
        }
        return this.stopProcessInstance(processInstanceId, stopReason, status);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public CallResult stopProcessInstance(String processInstanceId, String stopReason, int status) {
        List<Task> taskList = taskService.createTaskQuery().processInstanceId(processInstanceId).active().list();
        if (CollUtil.isEmpty(taskList)) {
            return CallResult.error("数据验证失败，当前流程尚未开始或已经结束！");
        }
        flowWorkOrderService.updateFlowStatusByProcessInstanceId(processInstanceId, status);
        for (Task task : taskList) {
            String currActivityId = task.getTaskDefinitionKey();
            BpmnModel bpmnModel = repositoryService.getBpmnModel(task.getProcessDefinitionId());
            FlowNode currFlow = (FlowNode) bpmnModel.getMainProcess().getFlowElement(currActivityId);
            if (currFlow == null) {
                List<SubProcess> subProcessList =
                        bpmnModel.getMainProcess().findFlowElementsOfType(SubProcess.class);
                for (SubProcess subProcess : subProcessList) {
                    FlowElement flowElement = subProcess.getFlowElement(currActivityId);
                    if (flowElement != null) {
                        currFlow = (FlowNode) flowElement;
                        break;
                    }
                }
            }
            EndEvent endEvent = bpmnModel.getMainProcess()
                    .findFlowElementsOfType(EndEvent.class, false).get(0);
            org.springframework.util.Assert.notNull(currFlow, "currFlow can't be NULL");
            if (!(currFlow.getParentContainer().equals(endEvent.getParentContainer()))) {
                throw new FlowOperationException("数据验证失败，不能从子流程直接中止！");
            }
            // 保存原有的输出方向。
            List<SequenceFlow> oriSequenceFlows = Lists.newArrayList();
            oriSequenceFlows.addAll(currFlow.getOutgoingFlows());
            // 清空原有方向。
            currFlow.getOutgoingFlows().clear();
            // 建立新方向。
            SequenceFlow newSequenceFlow = new SequenceFlow();
            String uuid = UUID.randomUUID().toString().replace("-", "");
            newSequenceFlow.setId(uuid);
            newSequenceFlow.setSourceFlowElement(currFlow);
            newSequenceFlow.setTargetFlowElement(endEvent);
            currFlow.setOutgoingFlows(CollUtil.newArrayList(newSequenceFlow));
            // 完成任务并跳转到新方向。
            taskService.complete(task.getId());
            FlowTaskComment taskComment = new FlowTaskComment(task);
            taskComment.setApprovalType(FlowApprovalType.STOP);
            taskComment.setTaskComment(stopReason);
            flowTaskCommentService.saveNew(taskComment);
            // 回复原有输出方向。
            currFlow.setOutgoingFlows(oriSequenceFlows);
        }
        flowMessageService.updateFinishedStatusByProcessInstanceId(processInstanceId);
        return CallResult.ok();
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void deleteProcessInstance(String processInstanceId) {
        historyService.deleteHistoricProcessInstance(processInstanceId);
        flowMessageService.removeByProcessInstanceId(processInstanceId);
        FlowWorkOrder workOrder = flowWorkOrderService.getFlowWorkOrderByProcessInstanceId(processInstanceId);
        if (workOrder == null) {
            return;
        }
        FlowEntry flowEntry = flowEntryService.getFlowEntryFromCache(workOrder.getProcessDefinitionKey());
        if (StrUtil.isNotBlank(flowEntry.getExtensionData())) {
            FlowEntryExtensionData extData = JSON.parseObject(flowEntry.getExtensionData(), FlowEntryExtensionData.class);
            if (BooleanUtil.isTrue(extData.getCascadeDeleteBusinessData())) {
                if (workOrder.getOnlineTableId() != null) {
                    // 级联删除在线表单工作流的业务数据。
                    flowCustomExtFactory.getOnlineBusinessDataExtHelper().deleteBusinessData(workOrder);
                } else {
                    // 级联删除路由表单工作流的业务数据。
                    flowCustomExtFactory.getBusinessDataExtHelper().deleteBusinessData(workOrder);
                }
            }
        }
        flowWorkOrderService.removeByProcessInstanceId(processInstanceId);
    }

    @Override
    public Object getTaskVariable(String taskId, String variableName) {
        return taskService.getVariable(taskId, variableName);
    }

    @Override
    public String getTaskVariableStringWithSafe(String taskId, String variableName) {
        try {
            Object v = taskService.getVariable(taskId, variableName);
            return v.toString();
        } catch (Exception e) {
            String errorMessage =
                    String.format("Failed to getTaskVariable taskId [%s], variableName [%s]", taskId, variableName);
            log.error(errorMessage, e);
            return null;
        }
    }

    @Override
    public Object getExecutionVariable(String executionId, String variableName) {
        return runtimeService.getVariable(executionId, variableName);
    }

    @Override
    public String getExecutionVariableStringWithSafe(String executionId, String variableName) {
        try {
            Object v = runtimeService.getVariable(executionId, variableName);
            return v.toString();
        } catch (Exception e) {
            String errorMessage = String.format(
                    "Failed to getExecutionVariableStringWithSafe executionId [%s], variableName [%s]", executionId, variableName);
            log.error(errorMessage, e);
            return null;
        }
    }

    @Override
    public Object getHistoricProcessInstanceVariable(String processInstanceId, String variableName) {
        HistoricVariableInstance hv = historyService.createHistoricVariableInstanceQuery()
                .processInstanceId(processInstanceId)
                .variableName(FlowConstant.GROUP_TYPE_DEPT_POST_LEADER_VAR).singleResult();
        return hv == null ? null : hv.getValue();
    }

    @Override
    public BpmnModel convertToBpmnModel(String bpmnXml) throws XMLStreamException {
        BpmnXMLConverter converter = new BpmnXMLConverter();
        InputStream in = new ByteArrayInputStream(bpmnXml.getBytes(StandardCharsets.UTF_8));
        @Cleanup XMLStreamReader reader = XMLInputFactory.newInstance().createXMLStreamReader(in);
        return converter.convertToBpmnModel(reader);
    }

    @Transactional
    @Override
    public CallResult backToRuntimeTask(
            Task task, String targetKey, Integer backType, String reason, JSONObject taskVariableData) {
        String errorMessage;
        ProcessDefinition processDefinition = this.getProcessDefinitionById(task.getProcessDefinitionId());
        Collection<FlowElement> allElements = this.getProcessAllElements(processDefinition.getId());
        Tuple2<FlowElement, FlowElement> sourceAndTarget =
                this.findSourceAndTargetFlement(allElements, task.getTaskDefinitionKey(), targetKey);
        FlowElement source = sourceAndTarget.getFirst();
        // 获取跳转的节点元素
        FlowElement target = sourceAndTarget.getSecond();
        if (targetKey != null && target == null) {
            errorMessage = "数据验证失败，被驳回的指定目标节点不存在！";
            return CallResult.error(errorMessage);
        }
        UserTask oneUserTask = null;
        List<String> targetIds = null;
        if (target == null) {
            List<UserTask> parentUserTaskList = this.getParentUserTaskList(source, new HashSet<>(), new ArrayList<>());
            if (CollUtil.isEmpty(parentUserTaskList)) {
                errorMessage = "数据验证失败，当前节点为初始任务节点，不能驳回！";
                return CallResult.error(errorMessage);
            }
            // 获取上一步途径过的用户任务Ids
            targetIds = this.getBackParentUserTaskIds(parentUserTaskList, task);
            // 目的获取所有需要被跳转的节点 currentIds
            // 取其中一个父级任务，因为后续要么存在公共网关，要么就是串行公共线路
            oneUserTask = parentUserTaskList.get(0);
        } else {
            if (!this.canJumpTo(source, targetKey, new HashSet<>())) {
                errorMessage = "数据验证失败，当前节点与驳回的目标节点不是串行关系，不能驳回！";
                return CallResult.error(errorMessage);
            }
        }
        // 获取所有正常进行的执行任务的活动节点ID，这些任务不能直接使用，需要找出其中需要撤回的任务
        List<Execution> runExecutionList =
                runtimeService.createExecutionQuery().processInstanceId(task.getProcessInstanceId()).list();
        List<String> runActivityIdList = runExecutionList.stream()
                .filter(c -> StrUtil.isNotBlank(c.getActivityId()))
                .map(Execution::getActivityId).collect(Collectors.toList());
        // 需驳回任务列表
        List<String> currentIds = new ArrayList<>();
        // 通过父级网关的出口连线，结合 runExecutionList 比对，获取需要撤回的任务
        List<FlowElement> currentFlowElementList = this.getChildUserTaskList(
                target != null ? target : oneUserTask, runActivityIdList, new HashSet<>(), new ArrayList<>());
        currentFlowElementList.forEach(item -> currentIds.add(item.getId()));
        if (target == null && targetIds.size() > 1 && currentIds.size() > 1) {
            // 规定：并行网关之前节点必须需存在唯一用户任务节点，如果出现多个任务节点，则并行网关节点默认为结束节点，原因为不考虑多对多情况
            errorMessage = "数据验证失败，任务出现多对多情况，无法撤回！";
            return CallResult.error(errorMessage);
        }
        this.updateChangedReason(runExecutionList, task, currentIds, targetKey, targetIds);
        try {
            this.changeActivityState(task, currentIds, targetKey, targetIds);
            FlowTaskComment comment = new FlowTaskComment();
            comment.setTaskId(task.getId());
            comment.setTaskKey(task.getTaskDefinitionKey());
            comment.setTaskName(task.getName());
            switch (backType) {
                case FlowBackType.REJECT:
                    comment.setApprovalType(FlowApprovalType.REJECT);
                    break;
                case FlowBackType.REVOKE:
                    comment.setApprovalType(FlowApprovalType.REVOKE);
                    break;
                case FlowBackType.INTERVENE:
                    comment.setApprovalType(FlowApprovalType.INTERVENE);
                    break;
                default:
                    break;
            }
            comment.setProcessInstanceId(task.getProcessInstanceId());
            comment.setTaskComment(reason);
            flowTaskCommentService.saveNew(comment);
            Integer approvalStatus = MapUtil.getInt(taskVariableData, FlowConstant.LATEST_APPROVAL_STATUS_KEY);
            flowWorkOrderService.updateLatestApprovalStatusByProcessInstanceId(task.getProcessInstanceId(), approvalStatus);
        } catch (Exception e) {
            log.error("Failed to execute moveSingleActivityIdToActivityIds", e);
            return CallResult.error(e.getMessage());
        }
        return CallResult.ok();
    }
    
    @Transactional
    @Override
    public CallResult backToRuntimeTaskAndTransfer(
            Task task, String targetKey, Integer backType, String reason, String delegateAssignee) {
        CallResult result = this.backToRuntimeTask(task, targetKey, backType, reason, null);
        if (!result.isSuccess()) {
            return result;
        }
        FlowTaskComment flowTaskComment = new FlowTaskComment();
        flowTaskComment.setDelegateAssignee(delegateAssignee);
        flowTaskComment.setApprovalType(FlowApprovalType.INTERVENE);
        Task targetTask = this.getProcessInstanceActiveTaskList(task.getProcessInstanceId()).get(0);
        this.transferTo(targetTask, flowTaskComment);
        return result;
    }

    @Override
    public List<UserTask> getRejectCandidateUserTaskList(Task task) {
        List<UserTask> userTaskList = null;
        ProcessDefinition processDefinition = this.getProcessDefinitionById(task.getProcessDefinitionId());
        Collection<FlowElement> allElements = this.getProcessAllElements(processDefinition.getId());
        if (CollUtil.isEmpty(allElements)) {
            return userTaskList;
        }
        // 获取当前任务节点元素
        UserTask source = null;
        for (FlowElement flowElement : allElements) {
            if (flowElement.getId().equals(task.getTaskDefinitionKey())) {
                source = (UserTask) flowElement;
                break;
            }
        }
        // 获取节点的所有路线
        List<List<UserTask>> roads = this.findRoad(source, null, null, null);
        // 可回退的节点列表
        userTaskList = new ArrayList<>();
        for (List<UserTask> road : roads) {
            if (CollUtil.isEmpty(userTaskList)) {
                // 还没有可回退节点直接添加
                userTaskList = road;
            } else {
                // 如果已有回退节点，则比对取交集部分
                userTaskList.retainAll(road);
            }
        }
        return userTaskList;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void transferTo(Task task, FlowTaskComment flowTaskComment) {
        List<String> transferUserList = StrUtil.split(flowTaskComment.getDelegateAssignee(), ",");
        for (String transferUser : transferUserList) {
            if (transferUser.equals(FlowConstant.START_USER_NAME_VAR)) {
                String startUser = this.getProcessInstanceVariable(
                        task.getProcessInstanceId(), FlowConstant.PROC_INSTANCE_START_USER_NAME_VAR).toString();
                String newDelegateAssignee = StrUtil.replace(
                        flowTaskComment.getDelegateAssignee(), FlowConstant.START_USER_NAME_VAR, startUser);
                flowTaskComment.setDelegateAssignee(newDelegateAssignee);
                transferUserList = StrUtil.split(flowTaskComment.getDelegateAssignee(), ",");
                break;
            }
        }
        taskService.unclaim(task.getId());
        FlowTaskExt taskExt = flowTaskExtService.getByProcessDefinitionIdAndTaskId(
                task.getProcessDefinitionId(), task.getTaskDefinitionKey());
        if (StrUtil.isNotBlank(taskExt.getCandidateUsernames())) {
            List<String> candidateUsernames = this.getCandidateUsernames(taskExt, task.getId());
            if (CollUtil.isNotEmpty(candidateUsernames)) {
                for (String username : candidateUsernames) {
                    taskService.deleteCandidateUser(task.getId(), username);
                }
            }
        } else {
            this.removeCandidateGroup(taskExt, task);
        }
        transferUserList.forEach(u -> taskService.addCandidateUser(task.getId(), u));
        flowTaskComment.fillWith(task);
        flowTaskCommentService.saveNew(flowTaskComment);
    }

    private void removeCandidateGroup(FlowTaskExt taskExt, Task task) {
        if (StrUtil.isNotBlank(taskExt.getDeptIds())) {
            for (String deptId : StrUtil.split(taskExt.getDeptIds(), ",")) {
                taskService.deleteCandidateGroup(task.getId(), deptId);
            }
        }
        if (StrUtil.isNotBlank(taskExt.getRoleIds())) {
            for (String roleId : StrUtil.split(taskExt.getRoleIds(), ",")) {
                taskService.deleteCandidateGroup(task.getId(), roleId);
            }
        }
        Tuple2<Set<String>, Set<String>> tuple2 =
                getDeptPostIdAndPostIds(taskExt, task.getProcessInstanceId(), false);
        if (CollUtil.isNotEmpty(tuple2.getFirst())) {
            for (String deptPostId : tuple2.getFirst()) {
                taskService.deleteCandidateGroup(task.getId(), deptPostId);
            }
        }
        if (CollUtil.isNotEmpty(tuple2.getSecond())) {
            for (String postId : tuple2.getSecond()) {
                taskService.deleteCandidateGroup(task.getId(), postId);
            }
        }
    }

    @Override
    public List<String> getCandidateUsernames(FlowTaskExt flowTaskExt, String taskId) {
        if (StrUtil.isBlank(flowTaskExt.getCandidateUsernames())) {
            return Collections.emptyList();
        }
        if (!StrUtil.equals(flowTaskExt.getCandidateUsernames(), "${" + FlowConstant.TASK_APPOINTED_ASSIGNEE_VAR + "}")) {
            return StrUtil.split(flowTaskExt.getCandidateUsernames(), ",");
        }
        Object candidateUsernames = getTaskVariableStringWithSafe(taskId, FlowConstant.TASK_APPOINTED_ASSIGNEE_VAR);
        return candidateUsernames == null ? null : StrUtil.split(candidateUsernames.toString(), ",");
    }

    @Override
    public Tuple2<Set<String>, Set<String>> getDeptPostIdAndPostIds(
            FlowTaskExt flowTaskExt, String processInstanceId, boolean historic) {
        Set<String> postIdSet = new LinkedHashSet<>();
        Set<String> deptPostIdSet = new LinkedHashSet<>();
        if (StrUtil.equals(flowTaskExt.getGroupType(), FlowConstant.GROUP_TYPE_UP_DEPT_POST_LEADER)) {
            Object v = this.getProcessInstanceVariable(
                    processInstanceId, FlowConstant.GROUP_TYPE_UP_DEPT_POST_LEADER_VAR, historic);
            if (ObjectUtil.isNotEmpty(v)) {
                deptPostIdSet.add(v.toString());
            }
        } else if (StrUtil.equals(flowTaskExt.getGroupType(), FlowConstant.GROUP_TYPE_DEPT_POST_LEADER)) {
            Object v = this.getProcessInstanceVariable(
                    processInstanceId, FlowConstant.GROUP_TYPE_DEPT_POST_LEADER_VAR, historic);
            if (ObjectUtil.isNotEmpty(v)) {
                deptPostIdSet.add(v.toString());
            }
        } else if (StrUtil.equals(flowTaskExt.getGroupType(), FlowConstant.GROUP_TYPE_POST)
                && StrUtil.isNotBlank(flowTaskExt.getDeptPostListJson())) {
            this.buildDeptPostIdAndPostIdsForPost(flowTaskExt, processInstanceId, historic, postIdSet, deptPostIdSet);
        }
        return new Tuple2<>(deptPostIdSet, postIdSet);
    }

    private void buildDeptPostIdAndPostIdsForPost(
            FlowTaskExt flowTaskExt,
            String processInstanceId,
            boolean historic,
            Set<String> postIdSet,
            Set<String> deptPostIdSet) {
        List<FlowTaskPostCandidateGroup> groupDataList =
                JSON.parseArray(flowTaskExt.getDeptPostListJson(), FlowTaskPostCandidateGroup.class);
        for (FlowTaskPostCandidateGroup groupData : groupDataList) {
            switch (groupData.getType()) {
                case FlowConstant.GROUP_TYPE_ALL_DEPT_POST_VAR:
                    postIdSet.add(groupData.getPostId());
                    break;
                case FlowConstant.GROUP_TYPE_DEPT_POST_VAR:
                    deptPostIdSet.add(groupData.getDeptPostId());
                    break;
                case FlowConstant.GROUP_TYPE_SELF_DEPT_POST_VAR:
                    Object v = this.getProcessInstanceVariable(
                            processInstanceId, FlowConstant.SELF_DEPT_POST_PREFIX + groupData.getPostId(), historic);
                    if (ObjectUtil.isNotEmpty(v)) {
                        deptPostIdSet.add(v.toString());
                    }
                    break;
                case FlowConstant.GROUP_TYPE_UP_DEPT_POST_VAR:
                    Object v2 = this.getProcessInstanceVariable(
                            processInstanceId, FlowConstant.UP_DEPT_POST_PREFIX + groupData.getPostId(), historic);
                    if (ObjectUtil.isNotEmpty(v2)) {
                        deptPostIdSet.add(v2.toString());
                    }
                    break;
                case FlowConstant.GROUP_TYPE_SIBLING_DEPT_POST_VAR:
                    Object v3 = this.getProcessInstanceVariable(
                            processInstanceId, FlowConstant.SIBLING_DEPT_POST_PREFIX + groupData.getPostId(), historic);
                    if (ObjectUtil.isNotEmpty(v3)) {
                        deptPostIdSet.addAll(StrUtil.split(v3.toString(), ",")
                                .stream().filter(StrUtil::isNotBlank).collect(Collectors.toList()));
                    }
                    break;
                default:
                    break;
            }
        }
    }

    private Object getProcessInstanceVariable(String processInstanceId, String variableName, boolean historic) {
        if (historic) {
            return getHistoricProcessInstanceVariable(processInstanceId, variableName);
        }
        return getProcessInstanceVariable(processInstanceId, variableName);
    }

    private List<List<UserTask>> findRoad(
            FlowElement source, List<UserTask> passRoads, Set<String> hasSequenceFlow, List<List<UserTask>> roads) {
        passRoads = passRoads == null ? new ArrayList<>() : passRoads;
        roads = roads == null ? new ArrayList<>() : roads;
        hasSequenceFlow = hasSequenceFlow == null ? new HashSet<>() : hasSequenceFlow;
        if (source instanceof StartEvent && source.getSubProcess() != null) {
            roads = findRoad(source.getSubProcess(), passRoads, hasSequenceFlow, roads);
        }
        List<SequenceFlow> sequenceFlows = getElementIncomingFlows(source);
        if (CollUtil.isEmpty(sequenceFlows)) {
            roads.add(passRoads);
            return roads;
        }
        for (SequenceFlow sequenceFlow : sequenceFlows) {
            // 如果发现连线重复，说明循环了，跳过这个循环
            if (hasSequenceFlow.contains(sequenceFlow.getId())) {
                continue;
            }
            hasSequenceFlow.add(sequenceFlow.getId());
            UserTask nowUserTask = null;
            if (sequenceFlow.getSourceFlowElement() instanceof UserTask
                    && !((UserTask) sequenceFlow.getSourceFlowElement()).hasMultiInstanceLoopCharacteristics()) {
                nowUserTask = (UserTask) sequenceFlow.getSourceFlowElement();
                passRoads.add(nowUserTask);
            }
            roads = findRoad(sequenceFlow.getSourceFlowElement(),
                    new ArrayList<>(passRoads), new HashSet<>(hasSequenceFlow), roads);
            if (nowUserTask != null) {
                passRoads.remove(nowUserTask);
            }
        }
        return roads;
    }

    private boolean canJumpTo(FlowElement source, String targetKey, Set<String> hasSequenceFlow) {
        boolean canJump = true;
        // 如果该节点为开始节点，且存在上级子节点，则顺着上级子节点继续迭代
        if (source instanceof StartEvent && source.getSubProcess() != null) {
            canJump = canJumpTo(source.getSubProcess(), targetKey, hasSequenceFlow);
        }
        List<SequenceFlow> sequenceFlows = getElementIncomingFlows(source);
        if (sequenceFlows == null) {
            return canJump;
        }
        // 循环找到目标元素
        for (SequenceFlow sequenceFlow : sequenceFlows) {
            // 如果发现连线重复，说明遍历过了，跳过这个循环
            if (!hasSequenceFlow.contains(sequenceFlow.getId())) {
                // 添加已经走过的连线
                hasSequenceFlow.add(sequenceFlow.getId());
                // 如果目标节点已被判断为并行，后面都不需要执行，直接返回
                if (!canJump || sequenceFlow.getSourceFlowElement() instanceof StartEvent) {
                    canJump = false;
                    break;
                }
                // 这条线路存在目标节点，这条线路完成，进入下个线路
                if (!targetKey.equals(sequenceFlow.getSourceFlowElement().getId())) {
                    // 注意：已经经过的节点与连线都应该用浅拷贝出来的对象
                    // 比如分支：a->b->c与a->d->c，走完a->b->c后走另一个路线是，已经经过的节点应该不包含a->b->c路线的数据
                    canJump = canJumpTo(sequenceFlow.getSourceFlowElement(), targetKey, new HashSet<>(hasSequenceFlow));
                }
            }
        }
        return canJump;
    }

    private List<UserTask> getParentUserTaskList(
            FlowElement source, Set<String> hasSequenceFlow, List<UserTask> userTaskList) {
        // 如果该节点为开始节点，且存在上级子节点，则顺着上级子节点继续迭代
        if (source instanceof StartEvent && source.getSubProcess() != null) {
            userTaskList = getParentUserTaskList(source.getSubProcess(), hasSequenceFlow, userTaskList);
        }
        List<SequenceFlow> sequenceFlows = getElementIncomingFlows(source);
        if (sequenceFlows != null) {
            // 循环找到目标元素
            for (SequenceFlow sequenceFlow : sequenceFlows) {
                // 如果发现连线重复，说明循环了，跳过这个循环
                if (!hasSequenceFlow.contains(sequenceFlow.getId())) {
                    // 添加已经走过的连线
                    hasSequenceFlow.add(sequenceFlow.getId());
                    this.findParentUserTaskListByFlow(sequenceFlow, hasSequenceFlow, userTaskList);
                }
            }
        }
        return userTaskList;
    }

    private void findParentUserTaskListByFlow(
            SequenceFlow sequenceFlow, Set<String> hasSequenceFlow, List<UserTask> userTaskList) {
        // 类型为用户节点，则新增父级节点
        if (sequenceFlow.getSourceFlowElement() instanceof UserTask) {
            userTaskList.add((UserTask) sequenceFlow.getSourceFlowElement());
            return;
        }
        // 类型为子流程，则添加子流程开始节点出口处相连的节点
        if (sequenceFlow.getSourceFlowElement() instanceof SubProcess) {
            // 获取子流程用户任务节点
            List<UserTask> childUserTaskList = findChildProcessUserTasks(
                    (StartEvent) ((SubProcess) sequenceFlow.getSourceFlowElement()).getFlowElements().toArray()[0], null, null);
            // 如果找到节点，则说明该线路找到节点，不继续向下找，反之继续
            if (CollUtil.isNotEmpty(childUserTaskList)) {
                userTaskList.addAll(childUserTaskList);
                return;
            }
        }
        // 网关场景的继续迭代
        // 注意：已经经过的节点与连线都应该用浅拷贝出来的对象
        // 比如分支：a->b->c与a->d->c，走完a->b->c后走另一个路线是，已经经过的节点应该不包含a->b->c路线的数据
        getParentUserTaskList(sequenceFlow.getSourceFlowElement(), new HashSet<>(hasSequenceFlow), userTaskList);
    }

    private List<FlowElement> getChildUserTaskList(
            FlowElement source, List<String> runActiveIdList, Set<String> hasSequenceFlow, List<FlowElement> flowElementList) {
        // 如果该节点为开始节点，且存在上级子节点，则顺着上级子节点继续迭代
        if (source instanceof EndEvent && source.getSubProcess() != null) {
            flowElementList = getChildUserTaskList(
                    source.getSubProcess(), runActiveIdList, hasSequenceFlow, flowElementList);
        }
        // 根据类型，获取出口连线
        List<SequenceFlow> sequenceFlows = getElementOutgoingFlows(source);
        if (sequenceFlows != null) {
            // 循环找到目标元素
            for (SequenceFlow sequenceFlow: sequenceFlows) {
                // 如果发现连线重复，说明循环了，跳过这个循环
                if (!hasSequenceFlow.contains(sequenceFlow.getId())) {
                    // 添加已经走过的连线
                    hasSequenceFlow.add(sequenceFlow.getId());
                    this.findChildUserTaskListByFlow(sequenceFlow, runActiveIdList, hasSequenceFlow, flowElementList);
                }
            }
        }
        return flowElementList;
    }

    private boolean isUserTaskOrGateway(FlowElement element) {
        return element instanceof UserTask || element instanceof Gateway;
    }

    private void findChildUserTaskListByFlow(
            SequenceFlow sequenceFlow, List<String> runActiveIdList, Set<String> hasSequenceFlow, List<FlowElement> flowElementList) {
        // 如果为用户任务类型，或者为网关
        // 活动节点ID 在运行的任务中存在，添加
        FlowElement targetElement = sequenceFlow.getTargetFlowElement();
        if (this.isUserTaskOrGateway(targetElement) && runActiveIdList.contains(targetElement.getId())) {
            flowElementList.add(sequenceFlow.getTargetFlowElement());
            return;
        }
        // 如果节点为子流程节点情况，则从节点中的第一个节点开始获取
        if (sequenceFlow.getTargetFlowElement() instanceof SubProcess) {
            List<FlowElement> childUserTaskList = getChildUserTaskList(
                    (FlowElement) (((SubProcess) sequenceFlow.getTargetFlowElement()).getFlowElements().toArray()[0]), runActiveIdList, hasSequenceFlow, null);
            // 如果找到节点，则说明该线路找到节点，不继续向下找，反之继续
            if (CollUtil.isNotEmpty(childUserTaskList)) {
                flowElementList.addAll(childUserTaskList);
                return;
            }
        }
        // 继续迭代
        // 注意：已经经过的节点与连线都应该用浅拷贝出来的对象
        // 比如分支：a->b->c与a->d->c，走完a->b->c后走另一个路线是，已经经过的节点应该不包含a->b->c路线的数据
        getChildUserTaskList(sequenceFlow.getTargetFlowElement(), runActiveIdList, new HashSet<>(hasSequenceFlow), flowElementList);
    }

    private void handleMultiInstanceApprovalType(String executionId, String approvalType, JSONObject taskVariableData) {
        if (StrUtil.isBlank(approvalType)) {
            return;
        }
        if (StrUtil.equalsAny(approvalType,
                FlowApprovalType.MULTI_AGREE,
                FlowApprovalType.MULTI_REFUSE,
                FlowApprovalType.MULTI_ABSTAIN)) {
            Map<String, Object> variables = runtimeService.getVariables(executionId);
            Integer agreeCount = (Integer) variables.get(FlowConstant.MULTI_AGREE_COUNT_VAR);
            Integer refuseCount = (Integer) variables.get(FlowConstant.MULTI_REFUSE_COUNT_VAR);
            Integer abstainCount = (Integer) variables.get(FlowConstant.MULTI_ABSTAIN_COUNT_VAR);
            Integer nrOfInstances = (Integer) variables.get(FlowConstant.NUMBER_OF_INSTANCES_VAR);
            taskVariableData.put(FlowConstant.MULTI_AGREE_COUNT_VAR, agreeCount);
            taskVariableData.put(FlowConstant.MULTI_REFUSE_COUNT_VAR, refuseCount);
            taskVariableData.put(FlowConstant.MULTI_ABSTAIN_COUNT_VAR, abstainCount);
            taskVariableData.put(FlowConstant.MULTI_SIGN_NUM_OF_INSTANCES_VAR, nrOfInstances);
            switch (approvalType) {
                case FlowApprovalType.MULTI_AGREE:
                    if (agreeCount == null) {
                        agreeCount = 0;
                    }
                    taskVariableData.put(FlowConstant.MULTI_AGREE_COUNT_VAR, agreeCount + 1);
                    break;
                case FlowApprovalType.MULTI_REFUSE:
                    if (refuseCount == null) {
                        refuseCount = 0;
                    }
                    taskVariableData.put(FlowConstant.MULTI_REFUSE_COUNT_VAR, refuseCount + 1);
                    break;
                case FlowApprovalType.MULTI_ABSTAIN:
                    if (abstainCount == null) {
                        abstainCount = 0;
                    }
                    taskVariableData.put(FlowConstant.MULTI_ABSTAIN_COUNT_VAR, abstainCount + 1);
                    break;
                default:
                    break;
            }
        }
    }

    private void buildCandidateCondition(TaskQuery query, String loginName) {
        Set<String> groupIdSet = new HashSet<>();
        // NOTE: 需要注意的是，部门Id、部门岗位Id，或者其他类型的分组Id，他们之间一定不能重复。
        TokenData tokenData = TokenData.takeFromRequest();
        Object deptId = tokenData.getDeptId();
        if (deptId != null) {
            groupIdSet.add(deptId.toString());
        }
        String roleIds = tokenData.getRoleIds();
        if (StrUtil.isNotBlank(tokenData.getRoleIds())) {
            groupIdSet.addAll(StrUtil.split(roleIds, ","));
        }
        String postIds = tokenData.getPostIds();
        if (StrUtil.isNotBlank(tokenData.getPostIds())) {
            groupIdSet.addAll(StrUtil.split(postIds, ","));
        }
        String deptPostIds = tokenData.getDeptPostIds();
        if (StrUtil.isNotBlank(deptPostIds)) {
            groupIdSet.addAll(StrUtil.split(deptPostIds, ","));
        }
        if (CollUtil.isNotEmpty(groupIdSet)) {
            query.or().taskCandidateGroupIn(groupIdSet).taskCandidateOrAssigned(loginName).endOr();
        } else {
            query.taskCandidateOrAssigned(loginName);
        }
    }

    private String buildMutiSignAssigneeList(String operationListJson) {
        FlowTaskMultiSignAssign multiSignAssignee = null;
        List<FlowTaskOperation> taskOperationList = JSONArray.parseArray(operationListJson, FlowTaskOperation.class);
        for (FlowTaskOperation taskOperation : taskOperationList) {
            if ("multi_sign".equals(taskOperation.getType())) {
                multiSignAssignee = taskOperation.getMultiSignAssignee();
                break;
            }
        }
        org.springframework.util.Assert.notNull(multiSignAssignee, "multiSignAssignee can't be NULL");
        if (FlowTaskMultiSignAssign.ASSIGN_TYPE_USER.equals(multiSignAssignee.getAssigneeType())) {
            return multiSignAssignee.getAssigneeList();
        }
        Set<String> usernameSet = null;
        BaseFlowIdentityExtHelper extHelper = flowCustomExtFactory.getFlowIdentityExtHelper();
        Set<String> idSet = CollUtil.newHashSet(StrUtil.split(multiSignAssignee.getAssigneeList(), ","));
        switch (multiSignAssignee.getAssigneeType()) {
            case FlowTaskMultiSignAssign.ASSIGN_TYPE_ROLE:
                usernameSet = extHelper.getUsernameListByRoleIds(idSet);
                break;
            case FlowTaskMultiSignAssign.ASSIGN_TYPE_DEPT:
                usernameSet = extHelper.getUsernameListByDeptIds(idSet);
                break;
            case FlowTaskMultiSignAssign.ASSIGN_TYPE_POST:
                usernameSet = extHelper.getUsernameListByPostIds(idSet);
                break;
            case FlowTaskMultiSignAssign.ASSIGN_TYPE_DEPT_POST:
                usernameSet = extHelper.getUsernameListByDeptPostIds(idSet);
                break;
            default:
                break;
        }
        return CollUtil.isEmpty(usernameSet) ? null : CollUtil.join(usernameSet, ",");
    }

    private Collection<FlowElement> getAllElements(Collection<FlowElement> flowElements, Collection<FlowElement> allElements) {
        allElements = allElements == null ? new ArrayList<>() : allElements;
        for (FlowElement flowElement : flowElements) {
            allElements.add(flowElement);
            if (flowElement instanceof SubProcess) {
                allElements = getAllElements(((SubProcess) flowElement).getFlowElements(), allElements);
            }
        }
        return allElements;
    }

    private void doChangeTask(Task runtimeTask) {
        Map<String, UserTask> allUserTaskMap =
                this.getAllUserTaskMap(runtimeTask.getProcessDefinitionId());
        UserTask userTaskModel = allUserTaskMap.get(runtimeTask.getTaskDefinitionKey());
        String completeCondition = userTaskModel.getLoopCharacteristics().getCompletionCondition();
        Execution parentExecution = this.getMultiInstanceRootExecution(runtimeTask);
        Object nrOfCompletedInstances = runtimeService.getVariable(
                parentExecution.getId(), FlowConstant.NUMBER_OF_COMPLETED_INSTANCES_VAR);
        Object nrOfInstances = runtimeService.getVariable(
                parentExecution.getId(), FlowConstant.NUMBER_OF_INSTANCES_VAR);
        ExpressionFactory factory = new ExpressionFactoryImpl();
        SimpleContext context = new SimpleContext();
        context.setVariable("nrOfCompletedInstances",
                factory.createValueExpression(nrOfCompletedInstances, Integer.class));
        context.setVariable("nrOfInstances",
                factory.createValueExpression(nrOfInstances, Integer.class));
        ValueExpression e = factory.createValueExpression(context, completeCondition, Boolean.class);
        Boolean ok = Convert.convert(Boolean.class, e.getValue(context));
        if (BooleanUtil.isTrue(ok)) {
            FlowElement targetKey = userTaskModel.getOutgoingFlows().get(0).getTargetFlowElement();
            ChangeActivityStateBuilder builder = runtimeService.createChangeActivityStateBuilder()
                    .processInstanceId(runtimeTask.getProcessInstanceId())
                    .moveActivityIdTo(userTaskModel.getId(), targetKey.getId());
            builder.localVariable(targetKey.getId(), FlowConstant.MULTI_SIGN_NUM_OF_INSTANCES_VAR, nrOfInstances);
            builder.changeState();
        }
    }

    private Execution getMultiInstanceRootExecution(Task runtimeTask) {
        List<Execution> executionList = runtimeService.createExecutionQuery()
                .processInstanceId(runtimeTask.getProcessInstanceId())
                .activityId(runtimeTask.getTaskDefinitionKey()).list();
        for (Execution e : executionList) {
            ExecutionEntityImpl ee = (ExecutionEntityImpl) e;
            if (ee.isMultiInstanceRoot()) {
                return e;
            }
        }
        Execution execution = executionList.get(0);
        return runtimeService.createExecutionQuery()
                .processInstanceId(runtimeTask.getProcessInstanceId())
                .executionId(execution.getParentId()).singleResult();
    }

    private Map<String, UserTask> getAllUserTaskMap(String processDefinitionId) {
        BpmnModel bpmnModel = repositoryService.getBpmnModel(processDefinitionId);
        Process process = bpmnModel.getProcesses().get(0);
        return process.findFlowElementsOfType(UserTask.class)
                .stream().collect(Collectors.toMap(UserTask::getId, a -> a, (k1, k2) -> k1));
    }

    private List<SequenceFlow> getElementIncomingFlows(FlowElement source) {
        List<SequenceFlow> sequenceFlows = null;
        if (source instanceof org.flowable.bpmn.model.Task) {
            sequenceFlows = ((org.flowable.bpmn.model.Task) source).getIncomingFlows();
        } else if (source instanceof Gateway) {
            sequenceFlows = ((Gateway) source).getIncomingFlows();
        } else if (source instanceof SubProcess) {
            sequenceFlows = ((SubProcess) source).getIncomingFlows();
        } else if (source instanceof StartEvent) {
            sequenceFlows = ((StartEvent) source).getIncomingFlows();
        } else if (source instanceof EndEvent) {
            sequenceFlows = ((EndEvent) source).getIncomingFlows();
        }
        return sequenceFlows;
    }

    private List<SequenceFlow> getElementOutgoingFlows(FlowElement source) {
        List<SequenceFlow> sequenceFlows = null;
        if (source instanceof org.flowable.bpmn.model.Task) {
            sequenceFlows = ((org.flowable.bpmn.model.Task) source).getOutgoingFlows();
        } else if (source instanceof Gateway) {
            sequenceFlows = ((Gateway) source).getOutgoingFlows();
        } else if (source instanceof SubProcess) {
            sequenceFlows = ((SubProcess) source).getOutgoingFlows();
        } else if (source instanceof StartEvent) {
            sequenceFlows = ((StartEvent) source).getOutgoingFlows();
        } else if (source instanceof EndEvent) {
            sequenceFlows = ((EndEvent) source).getOutgoingFlows();
        }
        return sequenceFlows;
    }

    private List<UserTask> findChildProcessUserTasks(FlowElement source, Set<String> hasSequenceFlow, List<UserTask> userTaskList) {
        hasSequenceFlow = hasSequenceFlow == null ? new HashSet<>() : hasSequenceFlow;
        userTaskList = userTaskList == null ? new ArrayList<>() : userTaskList;
        // 根据类型，获取出口连线
        List<SequenceFlow> sequenceFlows = getElementOutgoingFlows(source);
        if (sequenceFlows != null) {
            // 循环找到目标元素
            for (SequenceFlow sequenceFlow : sequenceFlows) {
                // 如果发现连线重复，说明循环了，跳过这个循环
                if (!hasSequenceFlow.contains(sequenceFlow.getId())) {
                    // 添加已经走过的连线
                    hasSequenceFlow.add(sequenceFlow.getId());
                    this.findChildUserTasksByFlow(sequenceFlow, hasSequenceFlow, userTaskList);
                }
            }
        }
        return userTaskList;
    }

    private void findChildUserTasksByFlow(SequenceFlow sequenceFlow, Set<String> hasSequenceFlow, List<UserTask> userTaskList) {
        // 如果为用户任务类型，且任务节点的 Key 正在运行的任务中存在，添加
        if (sequenceFlow.getTargetFlowElement() instanceof UserTask) {
            userTaskList.add((UserTask) sequenceFlow.getTargetFlowElement());
            return;
        }
        // 如果节点为子流程节点情况，则从节点中的第一个节点开始获取
        if (sequenceFlow.getTargetFlowElement() instanceof SubProcess) {
            List<UserTask> childUserTaskList = findChildProcessUserTasks((FlowElement) (((SubProcess) sequenceFlow.getTargetFlowElement()).getFlowElements().toArray()[0]), hasSequenceFlow, null);
            // 如果找到节点，则说明该线路找到节点，不继续向下找，反之继续
            if (CollUtil.isNotEmpty(childUserTaskList)) {
                userTaskList.addAll(childUserTaskList);
                return;
            }
        }
        // 继续迭代
        // 注意：已经经过的节点与连线都应该用浅拷贝出来的对象
        // 比如分支：a->b->c与a->d->c，走完a->b->c后走另一个路线是，已经经过的节点应该不包含a->b->c路线的数据
        findChildProcessUserTasks(sequenceFlow.getTargetFlowElement(), new HashSet<>(hasSequenceFlow), userTaskList);
    }

    private Set<String> findDirtyRoads(
            FlowElement source, List<String> passRoads, Set<String> hasSequenceFlow, List<String> targets, Set<String> dirtyRoads) {
        passRoads = passRoads == null ? new ArrayList<>() : passRoads;
        dirtyRoads = dirtyRoads == null ? new HashSet<>() : dirtyRoads;
        hasSequenceFlow = hasSequenceFlow == null ? new HashSet<>() : hasSequenceFlow;
        // 如果该节点为开始节点，且存在上级子节点，则顺着上级子节点继续迭代
        if (source instanceof StartEvent && source.getSubProcess() != null) {
            dirtyRoads = findDirtyRoads(source.getSubProcess(), passRoads, hasSequenceFlow, targets, dirtyRoads);
        }
        // 根据类型，获取入口连线
        List<SequenceFlow> sequenceFlows = getElementIncomingFlows(source);
        if (sequenceFlows != null) {
            // 循环找到目标元素
            for (SequenceFlow sequenceFlow: sequenceFlows) {
                // 如果发现连线重复，说明循环了，跳过这个循环
                if (!hasSequenceFlow.contains(sequenceFlow.getId())) {
                    // 添加已经走过的连线
                    hasSequenceFlow.add(sequenceFlow.getId());
                    this.findDirtyRoadsByFlow(sequenceFlow, passRoads, hasSequenceFlow, targets, dirtyRoads);
                }
            }
        }
        return dirtyRoads;
    }

    private void findDirtyRoadsByFlow(
            SequenceFlow sequenceFlow, List<String> passRoads, Set<String> hasSequenceFlow, List<String> targets, Set<String> dirtyRoads) {
        // 新增经过的路线
        passRoads.add(sequenceFlow.getSourceFlowElement().getId());
        // 如果此点为目标点，确定经过的路线为脏线路，添加点到脏线路中，然后找下个连线
        if (targets.contains(sequenceFlow.getSourceFlowElement().getId())) {
            dirtyRoads.addAll(passRoads);
            return;
        }
        // 如果该节点为开始节点，且存在上级子节点，则顺着上级子节点继续迭代
        if (sequenceFlow.getSourceFlowElement() instanceof SubProcess) {
            dirtyRoads = findChildProcessAllDirtyRoad(
                    (StartEvent) ((SubProcess) sequenceFlow.getSourceFlowElement()).getFlowElements().toArray()[0], null, dirtyRoads);
            // 是否存在子流程上，true 是，false 否
            Boolean isInChildProcess = dirtyTargetInChildProcess(
                    (StartEvent) ((SubProcess) sequenceFlow.getSourceFlowElement()).getFlowElements().toArray()[0], null, targets, null);
            if (BooleanUtil.isTrue(isInChildProcess)) {
                // 已在子流程上找到，该路线结束
                return;
            }
        }
        // 继续迭代
        // 注意：已经经过的节点与连线都应该用浅拷贝出来的对象
        // 比如分支：a->b->c与a->d->c，走完a->b->c后走另一个路线是，已经经过的节点应该不包含a->b->c路线的数据
        findDirtyRoads(sequenceFlow.getSourceFlowElement(),
                new ArrayList<>(passRoads), new HashSet<>(hasSequenceFlow), targets, dirtyRoads);
    }

    private Set<String> findChildProcessAllDirtyRoad(
            FlowElement source, Set<String> hasSequenceFlow, Set<String> dirtyRoads) {
        hasSequenceFlow = hasSequenceFlow == null ? new HashSet<>() : hasSequenceFlow;
        dirtyRoads = dirtyRoads == null ? new HashSet<>() : dirtyRoads;
        // 根据类型，获取出口连线
        List<SequenceFlow> sequenceFlows = getElementOutgoingFlows(source);
        if (sequenceFlows != null) {
            // 循环找到目标元素
            for (SequenceFlow sequenceFlow: sequenceFlows) {
                // 如果发现连线重复，说明循环了，跳过这个循环
                if (hasSequenceFlow.contains(sequenceFlow.getId())) {
                    continue;
                }
                // 添加已经走过的连线
                hasSequenceFlow.add(sequenceFlow.getId());
                // 添加脏路线
                dirtyRoads.add(sequenceFlow.getTargetFlowElement().getId());
                // 如果节点为子流程节点情况，则从节点中的第一个节点开始获取
                if (sequenceFlow.getTargetFlowElement() instanceof SubProcess) {
                    dirtyRoads = findChildProcessAllDirtyRoad(
                            (FlowElement) (((SubProcess) sequenceFlow.getTargetFlowElement()).getFlowElements().toArray()[0]), hasSequenceFlow, dirtyRoads);
                }
                // 继续迭代
                // 注意：已经经过的节点与连线都应该用浅拷贝出来的对象
                // 比如分支：a->b->c与a->d->c，走完a->b->c后走另一个路线是，已经经过的节点应该不包含a->b->c路线的数据
                dirtyRoads = findChildProcessAllDirtyRoad(
                        sequenceFlow.getTargetFlowElement(), new HashSet<>(hasSequenceFlow), dirtyRoads);
            }
        }
        return dirtyRoads;
    }

    private Boolean dirtyTargetInChildProcess(
            FlowElement source, Set<String> hasSequenceFlow, List<String> targets, Boolean inChildProcess) {
        hasSequenceFlow = hasSequenceFlow == null ? new HashSet<>() : hasSequenceFlow;
        if (inChildProcess == null) {
            inChildProcess = false;
        }
        // 根据类型，获取出口连线
        List<SequenceFlow> sequenceFlows = getElementOutgoingFlows(source);
        if (sequenceFlows == null || inChildProcess) {
            return inChildProcess;
        }
        // 循环找到目标元素
        for (SequenceFlow sequenceFlow: sequenceFlows) {
            // 如果发现连线重复，说明循环了，跳过这个循环
            if (!hasSequenceFlow.contains(sequenceFlow.getId())) {
                // 添加已经走过的连线
                hasSequenceFlow.add(sequenceFlow.getId());
                // 如果发现目标点在子流程上存在，说明只到子流程为止
                if (targets.contains(sequenceFlow.getTargetFlowElement().getId())) {
                    inChildProcess = true;
                    break;
                }
                // 如果节点为子流程节点情况，则从节点中的第一个节点开始获取
                if (sequenceFlow.getTargetFlowElement() instanceof SubProcess) {
                    inChildProcess = dirtyTargetInChildProcess((FlowElement)
                            (((SubProcess) sequenceFlow.getTargetFlowElement()).getFlowElements().toArray()[0]), hasSequenceFlow, targets, inChildProcess);
                }
                // 继续迭代
                // 注意：已经经过的节点与连线都应该用浅拷贝出来的对象
                // 比如分支：a->b->c与a->d->c，走完a->b->c后走另一个路线是，已经经过的节点应该不包含a->b->c路线的数据
                inChildProcess = dirtyTargetInChildProcess(
                        sequenceFlow.getTargetFlowElement(), new HashSet<>(hasSequenceFlow), targets, inChildProcess);
            }
        }
        return inChildProcess;
    }

    private FlowableListener createListener(String eventName, String listenerClassName) {
        FlowableListener listener = new FlowableListener();
        listener.setEvent(eventName);
        listener.setImplementationType("class");
        listener.setImplementation(listenerClassName);
        return listener;
    }

    private Tuple2<FlowElement, FlowElement> findSourceAndTargetFlement(
            Collection<FlowElement> allElements, String sourceKey, String targetKey) {
        FlowElement source = null;
        // 获取跳转的节点元素
        FlowElement target = null;
        for (FlowElement flowElement : allElements) {
            if (flowElement.getId().equals(sourceKey)) {
                source = flowElement;
                if (StrUtil.isBlank(targetKey)) {
                    break;
                }
            }
            if (StrUtil.equals(targetKey, flowElement.getId())) {
                target = flowElement;
            }
        }
        return new Tuple2<>(source, target);
    }

    private List<String> getBackParentUserTaskIds(List<UserTask> parentUserTaskList, Task task) {
        List<String> targetIds = new ArrayList<>();
        // 获取活动ID, 即节点Key
        Set<String> parentUserTaskKeySet = new HashSet<>();
        parentUserTaskList.forEach(item -> parentUserTaskKeySet.add(item.getId()));
        List<HistoricActivityInstance> historicActivityIdList =
                this.getHistoricActivityInstanceListOrderByStartTime(task.getProcessInstanceId());
        // 数据清洗，将回滚导致的脏数据清洗掉
        List<String> lastHistoricTaskInstanceList = CollUtil.reverse(historicActivityIdList)
                .stream().map(HistoricActivityInstance::getActivityId).collect(Collectors.toList());
        // 此时历史任务实例为倒序，获取最后走的节点
        // 循环结束标识，遇到当前目标节点的次数
        int number = 0;
        String parentHistoricTaskKey = null;
        for (String historicTaskInstanceKey : lastHistoricTaskInstanceList) {
            // 当会签时候会出现特殊的，连续都是同一个节点历史数据的情况，这种时候跳过
            if (!StrUtil.equals(parentHistoricTaskKey, historicTaskInstanceKey)) {
                parentHistoricTaskKey = historicTaskInstanceKey;
                if (historicTaskInstanceKey.equals(task.getTaskDefinitionKey())) {
                    number++;
                }
                if (number == 2) {
                    break;
                }
                // 如果当前历史节点，属于父级的节点，说明最后一次经过了这个点，需要退回这个点
                if (parentUserTaskKeySet.contains(historicTaskInstanceKey)) {
                    targetIds.add(historicTaskInstanceKey);
                }
            }
        }
        return targetIds;
    }

    private void updateChangedReason(
            List<Execution> runExecutionList, Task task, List<String> currentIds, String targetKey, List<String> targetIds) {
        AtomicReference<List<HistoricActivityInstance>> tmp = new AtomicReference<>();
        // 用于下面新增网关删除信息时使用
        String targetTmp = targetKey != null ? targetKey : String.join(",", targetIds);
        // currentIds 为活动ID列表
        // currentExecutionIds 为执行任务ID列表
        // 需要通过执行任务ID来设置驳回信息，活动ID不行
        currentIds.forEach(currentId -> runExecutionList.forEach(runExecution -> {
            if (StrUtil.equals(currentId, runExecution.getActivityId())) {
                // 查询当前节点的执行任务的历史数据
                tmp.set(historyService.createHistoricActivityInstanceQuery()
                        .processInstanceId(task.getProcessInstanceId())
                        .executionId(runExecution.getId())
                        .activityId(runExecution.getActivityId()).list());
                // 如果这个列表的数据只有 1 条数据
                // 网关肯定只有一条，且为包容网关或并行网关
                // 这里的操作目的是为了给网关在扭转前提前加上删除信息，结构与普通节点的删除信息一样，目的是为了知道这个网关也是有经过跳转的
                if (tmp.get() != null && tmp.get().size() == 1 && StrUtil.isNotBlank(tmp.get().get(0).getActivityType())
                        && ("parallelGateway".equals(tmp.get().get(0).getActivityType()) || "inclusiveGateway".equals(tmp.get().get(0).getActivityType()))) {
                    // singleResult 能够执行更新操作
                    // 利用 流程实例ID + 执行任务ID + 活动节点ID 来指定唯一数据，保证数据正确
                    historyService.createNativeHistoricActivityInstanceQuery().sql(
                            "UPDATE ACT_HI_ACTINST SET DELETE_REASON_ = 'Change activity to " + targetTmp + "'  WHERE PROC_INST_ID_='" + task.getProcessInstanceId() + "' AND EXECUTION_ID_='" + runExecution.getId() + "' AND ACT_ID_='" + runExecution.getActivityId() + "'").singleResult();
                }
            }
        }));
    }

    private void changeActivityState(Task task, List<String> currentIds, String targetKey, List<String> targetIds) {
        if (StrUtil.isNotBlank(targetKey)) {
            runtimeService.createChangeActivityStateBuilder()
                    .processInstanceId(task.getProcessInstanceId())
                    .moveActivityIdsToSingleActivityId(currentIds, targetKey).changeState();
            return;
        }
        // 如果父级任务多于 1 个，说明当前节点不是并行节点，原因为不考虑多对多情况
        if (targetIds.size() > 1) {
            // 1 对 多任务跳转，currentIds 当前节点(1)，targetIds 跳转到的节点(多)
            ChangeActivityStateBuilder builder = runtimeService.createChangeActivityStateBuilder()
                    .processInstanceId(task.getProcessInstanceId())
                    .moveSingleActivityIdToActivityIds(currentIds.get(0), targetIds);
            for (String targetId : targetIds) {
                FlowTaskComment taskComment =
                        flowTaskCommentService.getLatestFlowTaskComment(task.getProcessInstanceId(), targetId);
                // 如果驳回后的目标任务包含指定人，则直接通过变量回抄，如果没有则自动忽略该变量，不会给流程带来任何影响。
                String submitLoginName = taskComment.getCreateLoginName();
                if (StrUtil.isNotBlank(submitLoginName)) {
                    builder.localVariable(targetId, FlowConstant.TASK_APPOINTED_ASSIGNEE_VAR, submitLoginName);
                }
            }
            builder.changeState();
        }
        // 如果父级任务只有一个，因此当前任务可能为网关中的任务
        if (targetIds.size() == 1) {
            // 1 对 1 或 多 对 1 情况，currentIds 当前要跳转的节点列表(1或多)，targetIds.get(0) 跳转到的节点(1)
            // 如果驳回后的目标任务包含指定人，则直接通过变量回抄，如果没有则自动忽略该变量，不会给流程带来任何影响。
            ChangeActivityStateBuilder builder = runtimeService.createChangeActivityStateBuilder()
                    .processInstanceId(task.getProcessInstanceId())
                    .moveActivityIdsToSingleActivityId(currentIds, targetIds.get(0));
            FlowTaskComment taskComment =
                    flowTaskCommentService.getLatestFlowTaskComment(task.getProcessInstanceId(), targetIds.get(0));
            String submitLoginName = taskComment.getCreateLoginName();
            if (StrUtil.isNotBlank(submitLoginName)) {
                builder.localVariable(targetIds.get(0), FlowConstant.TASK_APPOINTED_ASSIGNEE_VAR, submitLoginName);
            }
            builder.changeState();
        }
    }
}
