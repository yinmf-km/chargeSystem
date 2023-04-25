package com.course.app.common.flow.dao;

import com.course.app.common.core.base.dao.BaseDaoMapper;
import com.course.app.common.flow.model.FlowEntryPublishVariable;

import java.util.List;

/**
 * 数据操作访问接口。
 *
 * @author 云翼
 * @date 2023-02-21
 */
public interface FlowEntryPublishVariableMapper extends BaseDaoMapper<FlowEntryPublishVariable> {

    /**
     * 批量插入流程发布的变量列表。
     *
     * @param entryPublishVariableList 流程发布的变量列表。
     */
    void insertList(List<FlowEntryPublishVariable> entryPublishVariableList);
}
