package com.course.app.webadmin.upms.dao;

import com.course.app.common.core.base.dao.BaseDaoMapper;
import com.course.app.webadmin.upms.model.SysDeptRelation;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 部门关系树关联关系表访问接口。
 *
 * @author 云翼
 * @date 2023-02-21
 */
public interface SysDeptRelationMapper extends BaseDaoMapper<SysDeptRelation> {

    /**
     * 将myDeptId的所有子部门，与其父部门parentDeptId解除关联关系。
     *
     * @param parentDeptIds myDeptId的父部门Id列表。
     * @param myDeptId      当前部门。
     */
    void removeBetweenChildrenAndParents(
            @Param("parentDeptIds") List<Long> parentDeptIds, @Param("myDeptId") Long myDeptId);

    /**
     * 批量插入部门关联数据。
     * 由于目前版本(3.4.1)的Mybatis Plus没有提供真正的批量插入，为了保证效率需要自己实现。
     * 目前我们仅仅给出MySQL和PostgresSQL的insert list实现作为参考，其他数据库需要自行修改。
     *
     * @param deptRelationList 部门关联关系数据列表。
     */
    void insertList(List<SysDeptRelation> deptRelationList);

    /**
     * 批量插入当前部门的所有父部门列表，包括自己和自己的关系。
     *
     * @param parentDeptId myDeptId的父部门Id。
     * @param myDeptId     当前部门。
     */
    void insertParentList(@Param("parentDeptId") Long parentDeptId, @Param("myDeptId") Long myDeptId);
}
