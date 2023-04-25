package com.course.app.common.flow.listener;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.course.app.common.core.util.ApplicationContextHolder;
import com.course.app.common.flow.constant.FlowConstant;
import com.course.app.common.flow.model.FlowTaskExt;
import com.course.app.common.flow.object.FlowUserTaskExtData;
import com.course.app.common.flow.service.FlowApiService;
import com.course.app.common.flow.service.FlowTaskExtService;
import com.course.app.common.flow.util.BaseFlowNotifyExtHelper;
import com.course.app.common.flow.util.FlowCustomExtFactory;
import com.course.app.common.flow.vo.FlowTaskVo;
import com.course.app.common.flow.vo.FlowUserInfoVo;
import lombok.extern.slf4j.Slf4j;
import org.flowable.engine.delegate.TaskListener;
import org.flowable.engine.runtime.ProcessInstance;
import org.flowable.task.api.Task;
import org.flowable.task.service.delegate.DelegateTask;

import java.util.List;

/**
 * 任务进入待办状态时的通知监听器。
 *
 * @author 云翼
 * @date 2023-02-21
 */
@Slf4j
public class FlowTaskNotifyListener implements TaskListener {

    private final transient FlowTaskExtService flowTaskExtService =
            ApplicationContextHolder.getBean(FlowTaskExtService.class);
    private final transient FlowApiService flowApiService =
            ApplicationContextHolder.getBean(FlowApiService.class);
    private final transient FlowCustomExtFactory flowCustomExtFactory =
            ApplicationContextHolder.getBean(FlowCustomExtFactory.class);

    @Override
    public void notify(DelegateTask delegateTask) {
        String definitionId = delegateTask.getProcessDefinitionId();
        String instanceId = delegateTask.getProcessInstanceId();
        String taskId = delegateTask.getId();
        String taskKey = delegateTask.getTaskDefinitionKey();
        FlowTaskExt taskExt = flowTaskExtService.getByProcessDefinitionIdAndTaskId(definitionId, taskKey);
        if (StrUtil.isBlank(taskExt.getExtraDataJson())) {
            return;
        }
        FlowUserTaskExtData extData = JSON.parseObject(taskExt.getExtraDataJson(), FlowUserTaskExtData.class);
        if (CollUtil.isEmpty(extData.getFlowNotifyTypeList())) {
            return;
        }
        ProcessInstance instance = flowApiService.getProcessInstance(instanceId);
        Object initiator = flowApiService.getProcessInstanceVariable(instanceId, FlowConstant.PROC_INSTANCE_INITIATOR_VAR);
        boolean isMultiInstanceTask = flowApiService.isMultiInstanceTask(definitionId, taskKey);
        Task task = flowApiService.getProcessInstanceActiveTask(instanceId, taskId);
        List<FlowUserInfoVo> userInfoList =
                flowTaskExtService.getCandidateUserInfoList(instanceId, taskExt, task, isMultiInstanceTask, false);
        Assert.notEmpty(userInfoList);
        BaseFlowNotifyExtHelper helper = flowCustomExtFactory.getFlowNotifyExtHelper();
        Assert.notNull(helper);
        for (String notifyType : extData.getFlowNotifyTypeList()) {
            FlowTaskVo flowTaskVo = new FlowTaskVo();
            flowTaskVo.setProcessDefinitionId(definitionId);
            flowTaskVo.setProcessInstanceId(instanceId);
            flowTaskVo.setTaskKey(taskKey);
            flowTaskVo.setTaskName(delegateTask.getName());
            flowTaskVo.setTaskId(delegateTask.getId());
            flowTaskVo.setBusinessKey(instance.getBusinessKey());
            flowTaskVo.setProcessInstanceInitiator(initiator.toString());
            helper.doNotify(notifyType, userInfoList, flowTaskVo);
        }
    }
}
