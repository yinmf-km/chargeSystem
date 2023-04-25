package com.course.app.common.flow.controller;

import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import io.swagger.annotations.Api;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.BooleanUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.page.PageMethod;
import com.course.app.common.core.annotation.MyRequestBody;
import com.course.app.common.core.constant.ErrorCodeEnum;
import com.course.app.common.core.object.*;
import com.course.app.common.core.util.MyCommonUtil;
import com.course.app.common.core.util.MyModelUtil;
import com.course.app.common.core.util.MyPageUtil;
import com.course.app.common.core.validator.UpdateGroup;
import com.course.app.common.log.annotation.OperationLog;
import com.course.app.common.log.model.constant.SysOperationLogType;
import com.course.app.common.flow.object.FlowUserTaskExtData;
import com.course.app.common.flow.object.FlowEntryExtensionData;
import com.course.app.common.flow.constant.FlowConstant;
import com.course.app.common.flow.constant.FlowTaskType;
import com.course.app.common.flow.dto.*;
import com.course.app.common.flow.model.*;
import com.course.app.common.flow.model.constant.FlowEntryStatus;
import com.course.app.common.flow.service.*;
import com.course.app.common.flow.vo.*;
import lombok.extern.slf4j.Slf4j;
import org.flowable.bpmn.model.*;
import org.flowable.bpmn.model.Process;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.groups.Default;
import javax.xml.stream.XMLStreamException;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 工作流操作控制器类。
 *
 * @author 云翼
 * @date 2023-02-21
 */
@Api(tags = "工作流操作接口")
@Slf4j
@RestController
@RequestMapping("${common-flow.urlPrefix}/flowEntry")
public class FlowEntryController {

    @Autowired
    private FlowEntryService flowEntryService;
    @Autowired
    private FlowCategoryService flowCategoryService;
    @Autowired
    private FlowEntryVariableService flowEntryVariableService;
    @Autowired
    private FlowApiService flowApiService;
    @Autowired
    private FlowTaskExtService flowTaskExtSerivce;

    /**
     * 新增工作流对象数据。
     *
     * @param flowEntryDto 新增对象。
     * @return 应答结果对象，包含新增对象主键Id。
     */
    @ApiOperationSupport(ignoreParameters = {"flowEntryDto.entryId"})
    @OperationLog(type = SysOperationLogType.ADD)
    @PostMapping("/add")
    public ResponseResult<Long> add(@MyRequestBody FlowEntryDto flowEntryDto) {
        String errorMessage = MyCommonUtil.getModelValidationError(flowEntryDto);
        if (errorMessage != null) {
            return ResponseResult.error(ErrorCodeEnum.DATA_VALIDATED_FAILED, errorMessage);
        }
        FlowEntry flowEntry = MyModelUtil.copyTo(flowEntryDto, FlowEntry.class);
        if (flowEntryService.existByProcessDefinitionKey(flowEntry.getProcessDefinitionKey())) {
            errorMessage = "数据验证失败，该流程定义标识已存在！";
            return ResponseResult.error(ErrorCodeEnum.DATA_VALIDATED_FAILED, errorMessage);
        }
        // 验证关联Id的数据合法性
        CallResult callResult = flowEntryService.verifyRelatedData(flowEntry, null);
        if (!callResult.isSuccess()) {
            errorMessage = callResult.getErrorMessage();
            return ResponseResult.error(ErrorCodeEnum.DATA_VALIDATED_FAILED, errorMessage);
        }
        flowEntry = flowEntryService.saveNew(flowEntry);
        return ResponseResult.success(flowEntry.getEntryId());
    }

    /**
     * 更新工作流对象数据。
     *
     * @param flowEntryDto 更新对象。
     * @return 应答结果对象。
     */
    @OperationLog(type = SysOperationLogType.UPDATE)
    @PostMapping("/update")
    public ResponseResult<Void> update(@MyRequestBody FlowEntryDto flowEntryDto) {
        String errorMessage = MyCommonUtil.getModelValidationError(flowEntryDto, Default.class, UpdateGroup.class);
        if (errorMessage != null) {
            return ResponseResult.error(ErrorCodeEnum.DATA_VALIDATED_FAILED, errorMessage);
        }
        FlowEntry flowEntry = MyModelUtil.copyTo(flowEntryDto, FlowEntry.class);
        FlowEntry originalFlowEntry = flowEntryService.getById(flowEntry.getEntryId());
        if (originalFlowEntry == null) {
            errorMessage = "数据验证失败，当前流程并不存在，请刷新后重试！";
            return ResponseResult.error(ErrorCodeEnum.DATA_NOT_EXIST, errorMessage);
        }
        if (ObjectUtil.notEqual(flowEntry.getProcessDefinitionKey(), originalFlowEntry.getProcessDefinitionKey())) {
            if (originalFlowEntry.getStatus().equals(FlowEntryStatus.PUBLISHED)) {
                errorMessage = "数据验证失败，当前流程为发布状态，流程标识不能修改！";
                return ResponseResult.error(ErrorCodeEnum.DATA_VALIDATED_FAILED, errorMessage);
            }
            if (flowEntryService.existByProcessDefinitionKey(flowEntry.getProcessDefinitionKey())) {
                errorMessage = "数据验证失败，该流程定义标识已存在！";
                return ResponseResult.error(ErrorCodeEnum.DATA_VALIDATED_FAILED, errorMessage);
            }
        }
        // 验证关联Id的数据合法性
        CallResult callResult = flowEntryService.verifyRelatedData(flowEntry, originalFlowEntry);
        if (!callResult.isSuccess()) {
            errorMessage = callResult.getErrorMessage();
            return ResponseResult.error(ErrorCodeEnum.DATA_VALIDATED_FAILED, errorMessage);
        }
        if (!flowEntryService.update(flowEntry, originalFlowEntry)) {
            return ResponseResult.error(ErrorCodeEnum.DATA_NOT_EXIST);
        }
        return ResponseResult.success();
    }

    /**
     * 删除工作流对象数据。
     *
     * @param entryId 删除对象主键Id。
     * @return 应答结果对象。
     */
    @OperationLog(type = SysOperationLogType.DELETE)
    @PostMapping("/delete")
    public ResponseResult<Void> delete(@MyRequestBody Long entryId) {
        String errorMessage;
        if (MyCommonUtil.existBlankArgument(entryId)) {
            return ResponseResult.error(ErrorCodeEnum.ARGUMENT_NULL_EXIST);
        }
        // 验证关联Id的数据合法性
        FlowEntry originalFlowEntry = flowEntryService.getById(entryId);
        if (originalFlowEntry == null) {
            errorMessage = "数据验证失败，当前流程并不存在，请刷新后重试！";
            return ResponseResult.error(ErrorCodeEnum.DATA_NOT_EXIST, errorMessage);
        }
        if (originalFlowEntry.getStatus().equals(FlowEntryStatus.PUBLISHED)) {
            errorMessage = "数据验证失败，当前流程为发布状态，不能删除！";
            return ResponseResult.error(ErrorCodeEnum.DATA_VALIDATED_FAILED, errorMessage);
        }
        if (!flowEntryService.remove(entryId)) {
            errorMessage = "数据操作失败，删除的对象不存在，请刷新后重试！";
            return ResponseResult.error(ErrorCodeEnum.DATA_NOT_EXIST, errorMessage);
        }
        return ResponseResult.success();
    }

    /**
     * 发布工作流。
     *
     * @param entryId 流程主键Id。
     * @return 应答结果对象。
     */
    @OperationLog(type = SysOperationLogType.PUBLISH)
    @PostMapping("/publish")
    public ResponseResult<Void> publish(@MyRequestBody(required = true) Long entryId) throws XMLStreamException {
        String errorMessage;
        FlowEntry flowEntry = flowEntryService.getById(entryId);
        if (flowEntry == null) {
            errorMessage = "数据验证失败，该流程并不存在，请刷新后重试！";
            return ResponseResult.error(ErrorCodeEnum.DATA_NOT_EXIST, errorMessage);
        }
        if (StrUtil.isBlank(flowEntry.getBpmnXml())) {
            errorMessage = "数据验证失败，该流程没有流程图不能被发布！";
            return ResponseResult.error(ErrorCodeEnum.DATA_NOT_EXIST, errorMessage);
        }
        ResponseResult<TaskInfoVo> taskInfoResult = this.verifyAndGetInitialTaskInfo(flowEntry);
        if (!taskInfoResult.isSuccess()) {
            return ResponseResult.errorFrom(taskInfoResult);
        }
        List<FlowTaskExt> flowTaskExtList = this.buildTaskExtList(flowEntry);
        String taskInfo = taskInfoResult.getData() == null ? null : JSON.toJSONString(taskInfoResult.getData());
        flowEntryService.publish(flowEntry, taskInfo, flowTaskExtList);
        return ResponseResult.success();
    }

    /**
     * 列出符合过滤条件的工作流列表。
     *
     * @param flowEntryDtoFilter 过滤对象。
     * @param orderParam         排序参数。
     * @param pageParam          分页参数。
     * @return 应答结果对象，包含查询结果集。
     */
    @PostMapping("/list")
    public ResponseResult<MyPageData<FlowEntryVo>> list(
            @MyRequestBody FlowEntryDto flowEntryDtoFilter,
            @MyRequestBody MyOrderParam orderParam,
            @MyRequestBody MyPageParam pageParam) {
        if (pageParam != null) {
            PageMethod.startPage(pageParam.getPageNum(), pageParam.getPageSize());
        }
        FlowEntry flowEntryFilter = MyModelUtil.copyTo(flowEntryDtoFilter, FlowEntry.class);
        String orderBy = MyOrderParam.buildOrderBy(orderParam, FlowEntry.class);
        List<FlowEntry> flowEntryList = flowEntryService.getFlowEntryListWithRelation(flowEntryFilter, orderBy);
        return ResponseResult.success(MyPageUtil.makeResponseData(flowEntryList, FlowEntry.INSTANCE));
    }

    /**
     * 查看指定工作流对象详情。
     *
     * @param entryId 指定对象主键Id。
     * @return 应答结果对象，包含对象详情。
     */
    @GetMapping("/view")
    public ResponseResult<FlowEntryVo> view(@RequestParam Long entryId) {
        if (MyCommonUtil.existBlankArgument(entryId)) {
            return ResponseResult.error(ErrorCodeEnum.ARGUMENT_NULL_EXIST);
        }
        FlowEntry flowEntry = flowEntryService.getByIdWithRelation(entryId, MyRelationParam.full());
        if (flowEntry == null) {
            return ResponseResult.error(ErrorCodeEnum.DATA_NOT_EXIST);
        }
        FlowEntryVo flowEntryVo = FlowEntry.INSTANCE.fromModel(flowEntry);
        return ResponseResult.success(flowEntryVo);
    }

    /**
     * 列出指定流程的发布版本列表。
     *
     * @param entryId 流程主键Id。
     * @return 应答结果对象，包含流程发布列表数据。
     */
    @GetMapping("/listFlowEntryPublish")
    public ResponseResult<List<FlowEntryPublishVo>> listFlowEntryPublish(@RequestParam Long entryId) {
        List<FlowEntryPublish> flowEntryPublishList = flowEntryService.getFlowEntryPublishList(entryId);
        return ResponseResult.success(MyModelUtil.copyCollectionTo(flowEntryPublishList, FlowEntryPublishVo.class));
    }

    /**
     * 以字典形式返回全部FlowEntry数据集合。字典的键值为[entryId, procDefinitionName]。
     * 白名单接口，登录用户均可访问。
     *
     * @param filter 过滤对象。
     * @return 应答结果对象，包含的数据为 List<Map<String, String>>，map中包含两条记录，key的值分别是id和name，value对应具体数据。
     */
    @GetMapping("/listDict")
    public ResponseResult<List<Map<String, Object>>> listDict(FlowEntry filter) {
        List<FlowEntry> resultList = flowEntryService.getListByFilter(filter);
        return ResponseResult.success(
                MyCommonUtil.toDictDataList(resultList, FlowEntry::getEntryId, FlowEntry::getProcessDefinitionName));
    }

    /**
     * 白名单接口，根据流程Id，获取流程引擎需要的流程标识和流程名称。
     *
     * @param entryId 流程Id。
     * @return 流程的部分数据。
     */
    @GetMapping("/viewDict")
    public ResponseResult<Map<String, Object>> viewDict(@RequestParam Long entryId) {
        FlowEntry flowEntry = flowEntryService.getById(entryId);
        if (flowEntry == null) {
            return ResponseResult.error(ErrorCodeEnum.DATA_NOT_EXIST);
        }
        Map<String, Object> resultMap = new HashMap<>(2);
        resultMap.put("processDefinitionKey", flowEntry.getProcessDefinitionKey());
        resultMap.put("processDefinitionName", flowEntry.getProcessDefinitionName());
        return ResponseResult.success(resultMap);
    }

    /**
     * 切换指定工作的发布主版本。
     *
     * @param entryId           工作流主键Id。
     * @param newEntryPublishId 新的工作流发布主版本对象的主键Id。
     * @return 应答结果对象。
     */
    @OperationLog(type = SysOperationLogType.UPDATE)
    @PostMapping("/updateMainVersion")
    public ResponseResult<Void> updateMainVersion(
            @MyRequestBody(required = true) Long entryId,
            @MyRequestBody(required = true) Long newEntryPublishId) {
        String errorMessage;
        FlowEntryPublish flowEntryPublish = flowEntryService.getFlowEntryPublishFromCache(newEntryPublishId);
        if (flowEntryPublish == null) {
            errorMessage = "数据验证失败，当前流程发布版本并不存在，请刷新后重试!";
            return ResponseResult.error(ErrorCodeEnum.DATA_NOT_EXIST, errorMessage);
        }
        if (ObjectUtil.notEqual(entryId, flowEntryPublish.getEntryId())) {
            errorMessage = "数据验证失败，当前工作流并不包含该工作流发布版本数据，请刷新后重试！";
            return ResponseResult.error(ErrorCodeEnum.DATA_VALIDATED_FAILED, errorMessage);
        }
        if (BooleanUtil.isTrue(flowEntryPublish.getMainVersion())) {
            errorMessage = "数据验证失败，该版本已经为当前工作流的发布主版本，不能重复设置！";
            return ResponseResult.error(ErrorCodeEnum.DATA_VALIDATED_FAILED, errorMessage);
        }
        flowEntryService.updateFlowEntryMainVersion(flowEntryService.getById(entryId), flowEntryPublish);
        return ResponseResult.success();
    }

    /**
     * 挂起工作流的指定发布版本。
     *
     * @param entryPublishId 工作发布Id。
     * @return 应答结果对象。
     */
    @OperationLog(type = SysOperationLogType.SUSPEND)
    @PostMapping("/suspendFlowEntryPublish")
    public ResponseResult<Void> suspendFlowEntryPublish(@MyRequestBody(required = true) Long entryPublishId) {
        String errorMessage;
        FlowEntryPublish flowEntryPublish = flowEntryService.getFlowEntryPublishFromCache(entryPublishId);
        if (flowEntryPublish == null) {
            errorMessage = "数据验证失败，当前流程发布版本并不存在，请刷新后重试！";
            return ResponseResult.error(ErrorCodeEnum.DATA_NOT_EXIST, errorMessage);
        }
        if (BooleanUtil.isFalse(flowEntryPublish.getActiveStatus())) {
            errorMessage = "数据验证失败，当前流程发布版本已处于挂起状态！";
            return ResponseResult.error(ErrorCodeEnum.DATA_VALIDATED_FAILED, errorMessage);
        }
        flowEntryService.suspendFlowEntryPublish(flowEntryPublish);
        return ResponseResult.success();
    }

    /**
     * 激活工作流的指定发布版本。
     *
     * @param entryPublishId 工作发布Id。
     * @return 应答结果对象。
     */
    @OperationLog(type = SysOperationLogType.RESUME)
    @PostMapping("/activateFlowEntryPublish")
    public ResponseResult<Void> activateFlowEntryPublish(@MyRequestBody(required = true) Long entryPublishId) {
        String errorMessage;
        FlowEntryPublish flowEntryPublish = flowEntryService.getFlowEntryPublishFromCache(entryPublishId);
        if (flowEntryPublish == null) {
            errorMessage = "数据验证失败，当前流程发布版本并不存在，请刷新后重试！";
            return ResponseResult.error(ErrorCodeEnum.DATA_NOT_EXIST, errorMessage);
        }
        if (BooleanUtil.isTrue(flowEntryPublish.getActiveStatus())) {
            errorMessage = "数据验证失败，当前流程发布版本已处于激活状态！";
            return ResponseResult.error(ErrorCodeEnum.DATA_VALIDATED_FAILED, errorMessage);
        }
        flowEntryService.activateFlowEntryPublish(flowEntryPublish);
        return ResponseResult.success();
    }

    private List<FlowTaskExt> buildTaskExtList(FlowEntry flowEntry) throws XMLStreamException {
        List<FlowTaskExt> flowTaskExtList = new LinkedList<>();
        BpmnModel bpmnModel = flowApiService.convertToBpmnModel(flowEntry.getBpmnXml());
        List<Process> processList = bpmnModel.getProcesses();
        for (Process process : processList) {
            for (FlowElement element : process.getFlowElements()) {
                if (element instanceof UserTask) {
                    FlowTaskExt flowTaskExt = flowTaskExtSerivce.buildTaskExtByUserTask((UserTask) element);
                    flowTaskExtList.add(flowTaskExt);
                }
            }
        }
        if (StrUtil.isNotBlank(flowEntry.getExtensionData())) {
            this.mergeTaskNotifyData(flowEntry, flowTaskExtList);
        }
        return flowTaskExtList;
    }

    private void mergeTaskNotifyData(FlowEntry flowEntry, List<FlowTaskExt> flowTaskExtList) {
        FlowEntryExtensionData flowEntryExtensionData =
                JSON.parseObject(flowEntry.getExtensionData(), FlowEntryExtensionData.class);
        if (CollUtil.isEmpty(flowEntryExtensionData.getNotifyTypes())) {
            return;
        }
        List<String> flowTaskNotifyTypes =
                flowEntryExtensionData.getNotifyTypes().stream().filter(StrUtil::isNotBlank).collect(Collectors.toList());
        if (CollUtil.isEmpty(flowTaskNotifyTypes)) {
            return;
        }
        for (FlowTaskExt flowTaskExt : flowTaskExtList) {
            if (flowTaskExt.getExtraDataJson() == null) {
                JSONObject o = new JSONObject();
                o.put(FlowConstant.USER_TASK_NOTIFY_TYPES_KEY, flowTaskNotifyTypes);
                flowTaskExt.setExtraDataJson(o.toJSONString());
            } else {
                FlowUserTaskExtData taskExtData =
                        JSON.parseObject(flowTaskExt.getExtraDataJson(), FlowUserTaskExtData.class);
                if (CollUtil.isEmpty(taskExtData.getFlowNotifyTypeList())) {
                    taskExtData.setFlowNotifyTypeList(flowTaskNotifyTypes);
                } else {
                    Set<String> notifyTypesSet = taskExtData.getFlowNotifyTypeList()
                            .stream().filter(StrUtil::isNotBlank).collect(Collectors.toSet());
                    notifyTypesSet.addAll(flowTaskNotifyTypes);
                    taskExtData.setFlowNotifyTypeList(new LinkedList<>(notifyTypesSet));
                }
                flowTaskExt.setExtraDataJson(JSON.toJSONString(taskExtData));
            }
        }
    }

    private ResponseResult<TaskInfoVo> verifyAndGetInitialTaskInfo(FlowEntry flowEntry) throws XMLStreamException {
        String errorMessage;
        BpmnModel bpmnModel = flowApiService.convertToBpmnModel(flowEntry.getBpmnXml());
        Process process = bpmnModel.getMainProcess();
        if (process == null) {
            errorMessage = "数据验证失败，当前流程标识 [" + flowEntry.getProcessDefinitionKey() + "] 关联的流程模型并不存在！";
            return ResponseResult.error(ErrorCodeEnum.DATA_VALIDATED_FAILED, errorMessage);
        }
        Collection<FlowElement> elementList = process.getFlowElements();
        FlowElement startEvent = null;
        // 这里我们只定位流程模型中的第二个节点。
        for (FlowElement flowElement : elementList) {
            if (flowElement instanceof StartEvent) {
                startEvent = flowElement;
                break;
            }
        }
        if (startEvent == null) {
            errorMessage = "数据验证失败，当前流程图没有包含 [开始事件] 节点，请修改流程图！";
            return ResponseResult.error(ErrorCodeEnum.DATA_VALIDATED_FAILED, errorMessage);
        }
        FlowElement firstTask = this.findFirstTask(elementList, startEvent);
        if (firstTask == null) {
            errorMessage = "数据验证失败，当前流程图没有包含 [开始事件] 节点没有任何连线，请修改流程图！";
            return ResponseResult.error(ErrorCodeEnum.DATA_VALIDATED_FAILED, errorMessage);
        }
        TaskInfoVo taskInfoVo;
        if (firstTask instanceof UserTask) {
            UserTask userTask = (UserTask) firstTask;
            String formKey = userTask.getFormKey();
            if (StrUtil.isNotBlank(formKey)) {
                taskInfoVo = JSON.parseObject(formKey, TaskInfoVo.class);
            } else {
                taskInfoVo = new TaskInfoVo();
            }
            taskInfoVo.setAssignee(userTask.getAssignee());
            taskInfoVo.setTaskKey(userTask.getId());
            taskInfoVo.setTaskType(FlowTaskType.USER_TYPE);
            Map<String, List<ExtensionElement>> extensionMap = userTask.getExtensionElements();
            if (MapUtil.isNotEmpty(extensionMap)) {
                taskInfoVo.setOperationList(flowTaskExtSerivce.buildOperationListExtensionElement(extensionMap));
                taskInfoVo.setVariableList(flowTaskExtSerivce.buildVariableListExtensionElement(extensionMap));
            }
        } else {
            taskInfoVo = new TaskInfoVo();
            taskInfoVo.setTaskType(FlowTaskType.OTHER_TYPE);
        }
        return ResponseResult.success(taskInfoVo);
    }

    private FlowElement findFirstTask(Collection<FlowElement> elementList, FlowElement startEvent) {
        for (FlowElement flowElement : elementList) {
            if (flowElement instanceof SequenceFlow) {
                SequenceFlow sequenceFlow = (SequenceFlow) flowElement;
                if (sequenceFlow.getSourceFlowElement().equals(startEvent)) {
                    return sequenceFlow.getTargetFlowElement();
                }
            }
        }
        return null;
    }
}
