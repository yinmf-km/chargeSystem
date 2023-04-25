package com.course.app.common.flow.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.course.app.common.core.annotation.DisableDataFilter;
import com.course.app.common.core.base.dao.BaseDaoMapper;
import com.course.app.common.core.constant.GlobalDeletedFlag;
import com.course.app.common.core.object.*;
import com.course.app.common.core.base.service.BaseService;
import com.course.app.common.flow.constant.FlowTaskStatus;
import com.course.app.common.flow.constant.FlowConstant;
import com.course.app.common.flow.dao.FlowWorkOrderExtMapper;
import com.course.app.common.flow.dao.FlowWorkOrderMapper;
import com.course.app.common.flow.model.FlowEntry;
import com.course.app.common.flow.model.FlowWorkOrder;
import com.course.app.common.flow.model.FlowWorkOrderExt;
import com.course.app.common.flow.vo.FlowWorkOrderVo;
import com.course.app.common.flow.service.FlowApiService;
import com.course.app.common.flow.service.FlowEntryService;
import com.course.app.common.flow.service.FlowWorkOrderService;
import com.course.app.common.flow.util.BaseFlowIdentityExtHelper;
import com.course.app.common.flow.util.FlowCustomExtFactory;
import com.course.app.common.redis.util.CommonRedisUtil;
import com.course.app.common.sequence.wrapper.IdGeneratorWrapper;
import lombok.extern.slf4j.Slf4j;
import org.flowable.engine.runtime.ProcessInstance;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 工作流工单表数据操作服务类。
 *
 * @author 云翼
 * @date 2023-02-21
 */
@Slf4j
@Service("flowWorkOrderService")
public class FlowWorkOrderServiceImpl extends BaseService<FlowWorkOrder, Long> implements FlowWorkOrderService {

    @Autowired
    private FlowWorkOrderMapper flowWorkOrderMapper;
    @Autowired
    private FlowWorkOrderExtMapper flowWorkOrderExtMapper;
    @Autowired
    private IdGeneratorWrapper idGenerator;
    @Autowired
    private FlowCustomExtFactory flowCustomExtFactory;
    @Autowired
    private FlowApiService flowApiService;
    @Autowired
    private FlowEntryService flowEntryService;
    @Autowired
    private CommonRedisUtil commonRedisUtil;

    /**
     * 返回当前Service的主表Mapper对象。
     *
     * @return 主表Mapper对象。
     */
    @Override
    protected BaseDaoMapper<FlowWorkOrder> mapper() {
        return flowWorkOrderMapper;
    }

    /**
     * 保存新增对象。
     *
     * @param instance      流程实例对象。
     * @param dataId        流程实例的BusinessKey。
     * @param onlineTableId 在线数据表的主键Id。
     * @param tableName     面向静态表单所使用的表名。
     * @return 新增的工作流工单对象。
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public FlowWorkOrder saveNew(ProcessInstance instance, Object dataId, Long onlineTableId, String tableName) {
        FlowWorkOrder flowWorkOrder = this.createWith(instance);
        flowWorkOrder.setWorkOrderCode(this.generateWorkOrderCode(instance.getProcessDefinitionKey()));
        flowWorkOrder.setBusinessKey(dataId.toString());
        flowWorkOrder.setOnlineTableId(onlineTableId);
        flowWorkOrder.setTableName(tableName);
        flowWorkOrder.setFlowStatus(FlowTaskStatus.SUBMITTED);
        flowWorkOrderMapper.insert(flowWorkOrder);
        return flowWorkOrder;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public FlowWorkOrder saveNewWithDraft(
            ProcessInstance instance, Long onlineTableId, String tableName, String masterData, String slaveData) {
        FlowWorkOrder flowWorkOrder = this.createWith(instance);
        flowWorkOrder.setWorkOrderCode(this.generateWorkOrderCode(instance.getProcessDefinitionKey()));
        flowWorkOrder.setOnlineTableId(onlineTableId);
        flowWorkOrder.setTableName(tableName);
        flowWorkOrder.setFlowStatus(FlowTaskStatus.DRAFT);
        JSONObject draftData = new JSONObject();
        if (masterData != null) {
            draftData.put(FlowConstant.MASTER_DATA_KEY, masterData);
        }
        if (slaveData != null) {
            draftData.put(FlowConstant.SLAVE_DATA_KEY, slaveData);
        }
        FlowWorkOrderExt flowWorkOrderExt =
                BeanUtil.copyProperties(flowWorkOrder, FlowWorkOrderExt.class);
        flowWorkOrderExt.setId(idGenerator.nextLongId());
        flowWorkOrderExt.setDraftData(JSON.toJSONString(draftData));
        flowWorkOrderExtMapper.insert(flowWorkOrderExt);
        flowWorkOrderMapper.insert(flowWorkOrder);
        return flowWorkOrder;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void updateDraft(Long workOrderId, String masterData, String slaveData) {
        JSONObject draftData = new JSONObject();
        if (masterData != null) {
            draftData.put(FlowConstant.MASTER_DATA_KEY, masterData);
        }
        if (slaveData != null) {
            draftData.put(FlowConstant.SLAVE_DATA_KEY, slaveData);
        }
        FlowWorkOrderExt flowWorkOrderExt = new FlowWorkOrderExt();
        flowWorkOrderExt.setDraftData(JSON.toJSONString(draftData));
        flowWorkOrderExt.setUpdateTime(new Date());
        flowWorkOrderExtMapper.update(flowWorkOrderExt,
                new LambdaQueryWrapper<FlowWorkOrderExt>().eq(FlowWorkOrderExt::getWorkOrderId, workOrderId));
    }

    /**
     * 删除指定数据。
     *
     * @param workOrderId 主键Id。
     * @return 成功返回true，否则false。
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean remove(Long workOrderId) {
        return flowWorkOrderMapper.deleteById(workOrderId) == 1;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void removeByProcessInstanceId(String processInstanceId) {
        FlowWorkOrder filter = new FlowWorkOrder();
        filter.setProcessInstanceId(processInstanceId);
        super.removeBy(filter);
    }

    @Override
    public List<FlowWorkOrder> getFlowWorkOrderList(FlowWorkOrder filter, String orderBy) {
        return flowWorkOrderMapper.getFlowWorkOrderList(filter, orderBy);
    }

    @Override
    public List<FlowWorkOrder> getFlowWorkOrderListWithRelation(FlowWorkOrder filter, String orderBy) {
        List<FlowWorkOrder> resultList = flowWorkOrderMapper.getFlowWorkOrderList(filter, orderBy);
        this.buildRelationForDataList(resultList, MyRelationParam.dictOnly());
        return resultList;
    }

    @Override
    public FlowWorkOrder getFlowWorkOrderByProcessInstanceId(String processInstanceId) {
        FlowWorkOrder filter = new FlowWorkOrder();
        filter.setProcessInstanceId(processInstanceId);
        return flowWorkOrderMapper.selectOne(new QueryWrapper<>(filter));
    }

    @Override
    public boolean existByBusinessKey(String tableName, Object businessKey, boolean unfinished) {
        LambdaQueryWrapper<FlowWorkOrder> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(FlowWorkOrder::getBusinessKey, businessKey.toString());
        queryWrapper.eq(FlowWorkOrder::getTableName, tableName);
        if (unfinished) {
            queryWrapper.notIn(FlowWorkOrder::getFlowStatus,
                    FlowTaskStatus.FINISHED, FlowTaskStatus.CANCELLED, FlowTaskStatus.STOPPED);
        }
        return flowWorkOrderMapper.selectCount(queryWrapper) > 0;
    }

    @DisableDataFilter
    @Transactional(rollbackFor = Exception.class)
    @Override
    public void updateFlowStatusByProcessInstanceId(String processInstanceId, Integer flowStatus) {
        if (flowStatus == null) {
            return;
        }
        FlowWorkOrder flowWorkOrder = new FlowWorkOrder();
        flowWorkOrder.setFlowStatus(flowStatus);
        if (FlowTaskStatus.FINISHED != flowStatus) {
            flowWorkOrder.setUpdateTime(new Date());
            flowWorkOrder.setUpdateUserId(TokenData.takeFromRequest().getUserId());
        }
        LambdaQueryWrapper<FlowWorkOrder> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(FlowWorkOrder::getProcessInstanceId, processInstanceId);
        flowWorkOrderMapper.update(flowWorkOrder, queryWrapper);
    }

    @DisableDataFilter
    @Transactional(rollbackFor = Exception.class)
    @Override
    public void updateLatestApprovalStatusByProcessInstanceId(String processInstanceId, Integer approvalStatus) {
        if (approvalStatus == null) {
            return;
        }
        FlowWorkOrder flowWorkOrder = new FlowWorkOrder();
        flowWorkOrder.setLatestApprovalStatus(approvalStatus);
        LambdaQueryWrapper<FlowWorkOrder> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(FlowWorkOrder::getProcessInstanceId, processInstanceId);
        flowWorkOrderMapper.update(flowWorkOrder, queryWrapper);
    }

    @Override
    public boolean hasDataPermOnFlowWorkOrder(String processInstanceId) {
        // 开启数据权限，并进行验证。
        boolean originalFlag = GlobalThreadLocal.setDataFilter(true);
        long count;
        try {
            FlowWorkOrder filter = new FlowWorkOrder();
            filter.setProcessInstanceId(processInstanceId);
            count = flowWorkOrderMapper.selectCount(new QueryWrapper<>(filter));
        } finally {
            // 恢复之前的数据权限标记
            GlobalThreadLocal.setDataFilter(originalFlag);
        }
        return count > 0;
    }

    @Override
    public void fillUserShowNameByLoginName(List<FlowWorkOrderVo> dataList) {
        BaseFlowIdentityExtHelper identityExtHelper = flowCustomExtFactory.getFlowIdentityExtHelper();
        Set<String> loginNameSet = dataList.stream()
                .map(FlowWorkOrderVo::getSubmitUsername).collect(Collectors.toSet());
        if (CollUtil.isEmpty(loginNameSet)) {
            return;
        }
        Map<String, String> userNameMap = identityExtHelper.mapUserShowNameByLoginName(loginNameSet);
        dataList.forEach(workOrder -> {
            if (StrUtil.isNotBlank(workOrder.getSubmitUsername())) {
                workOrder.setUserShowName(userNameMap.get(workOrder.getSubmitUsername()));
            }
        });
    }

    @Override
    public FlowWorkOrderExt getFlowWorkOrderExtByWorkFlowId(Long workOrderId) {
        return flowWorkOrderExtMapper.selectOne(
                new LambdaQueryWrapper<FlowWorkOrderExt>().eq(FlowWorkOrderExt::getWorkOrderId, workOrderId));
    }

    @Override
    public List<FlowWorkOrderExt> getFlowWorkOrderExtByWorkFlowIds(Set<Long> workOrderIdSet) {
        return flowWorkOrderExtMapper.selectList(
                new LambdaQueryWrapper<FlowWorkOrderExt>().in(FlowWorkOrderExt::getWorkOrderId, workOrderIdSet));
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public CallResult removeDraft(FlowWorkOrder flowWorkOrder) {
        flowWorkOrderMapper.deleteById(flowWorkOrder.getWorkOrderId());
        return flowApiService.stopProcessInstance(
                flowWorkOrder.getProcessInstanceId(), "撤销草稿", true);
    }

    private FlowWorkOrder createWith(ProcessInstance instance) {
        TokenData tokenData = TokenData.takeFromRequest();
        Date now = new Date();
        FlowWorkOrder flowWorkOrder = new FlowWorkOrder();
        flowWorkOrder.setWorkOrderId(idGenerator.nextLongId());
        flowWorkOrder.setProcessDefinitionKey(instance.getProcessDefinitionKey());
        flowWorkOrder.setProcessDefinitionName(instance.getProcessDefinitionName());
        flowWorkOrder.setProcessDefinitionId(instance.getProcessDefinitionId());
        flowWorkOrder.setProcessInstanceId(instance.getId());
        flowWorkOrder.setSubmitUsername(tokenData.getLoginName());
        flowWorkOrder.setDeptId(tokenData.getDeptId());
        flowWorkOrder.setCreateUserId(tokenData.getUserId());
        flowWorkOrder.setUpdateUserId(tokenData.getUserId());
        flowWorkOrder.setCreateTime(now);
        flowWorkOrder.setUpdateTime(now);
        flowWorkOrder.setDeletedFlag(GlobalDeletedFlag.NORMAL);
        return flowWorkOrder;
    }

    private String generateWorkOrderCode(String processDefinitionKey) {
        FlowEntry flowEntry = flowEntryService.getFlowEntryFromCache(processDefinitionKey);
        if (StrUtil.isBlank(flowEntry.getEncodedRule())) {
            return null;
        }
        ColumnEncodedRule rule = JSON.parseObject(flowEntry.getEncodedRule(), ColumnEncodedRule.class);
        if (rule.getIdWidth() == null) {
            rule.setIdWidth(10);
        }
        return commonRedisUtil.generateTransId(
                rule.getPrefix(), rule.getPrecisionTo(), rule.getMiddle(), rule.getIdWidth());
    }
}
