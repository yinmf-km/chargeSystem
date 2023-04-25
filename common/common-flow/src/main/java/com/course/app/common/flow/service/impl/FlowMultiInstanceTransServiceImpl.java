package com.course.app.common.flow.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.course.app.common.core.base.dao.BaseDaoMapper;
import com.course.app.common.core.base.service.BaseService;
import com.course.app.common.core.object.TokenData;
import com.course.app.common.flow.dao.FlowMultiInstanceTransMapper;
import com.course.app.common.flow.model.FlowMultiInstanceTrans;
import com.course.app.common.flow.service.FlowMultiInstanceTransService;
import com.course.app.common.sequence.wrapper.IdGeneratorWrapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

/**
 * 会签任务操作流水数据操作服务接口。
 *
 * @author 云翼
 * @date 2023-02-21
 */
@Slf4j
@Service("flowMultiInstanceTransService")
public class FlowMultiInstanceTransServiceImpl
        extends BaseService<FlowMultiInstanceTrans, Long> implements FlowMultiInstanceTransService {

    @Autowired
    private FlowMultiInstanceTransMapper flowMultiInstanceTransMapper;
    @Autowired
    private IdGeneratorWrapper idGenerator;

    /**
     * 返回当前Service的主表Mapper对象。
     *
     * @return 主表Mapper对象。
     */
    @Override
    protected BaseDaoMapper<FlowMultiInstanceTrans> mapper() {
        return flowMultiInstanceTransMapper;
    }

    /**
     * 保存新增对象。
     *
     * @param flowMultiInstanceTrans 新增对象。
     * @return 返回新增对象。
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public FlowMultiInstanceTrans saveNew(FlowMultiInstanceTrans flowMultiInstanceTrans) {
        flowMultiInstanceTrans.setId(idGenerator.nextLongId());
        TokenData tokenData = TokenData.takeFromRequest();
        flowMultiInstanceTrans.setCreateUserId(tokenData.getUserId());
        flowMultiInstanceTrans.setCreateLoginName(tokenData.getLoginName());
        flowMultiInstanceTrans.setCreateUsername(tokenData.getShowName());
        flowMultiInstanceTrans.setCreateTime(new Date());
        flowMultiInstanceTransMapper.insert(flowMultiInstanceTrans);
        return flowMultiInstanceTrans;
    }

    @Override
    public FlowMultiInstanceTrans getByExecutionId(String executionId, String taskId) {
        LambdaQueryWrapper<FlowMultiInstanceTrans> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(FlowMultiInstanceTrans::getExecutionId, executionId);
        queryWrapper.eq(FlowMultiInstanceTrans::getTaskId, taskId);
        return flowMultiInstanceTransMapper.selectOne(queryWrapper);
    }

    @Override
    public FlowMultiInstanceTrans getWithAssigneeListByMultiInstanceExecId(String multiInstanceExecId) {
        if (multiInstanceExecId == null) {
            return null;
        }
        LambdaQueryWrapper<FlowMultiInstanceTrans> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(FlowMultiInstanceTrans::getMultiInstanceExecId, multiInstanceExecId);
        queryWrapper.isNotNull(FlowMultiInstanceTrans::getAssigneeList);
        return flowMultiInstanceTransMapper.selectOne(queryWrapper);
    }
}
