package com.course.app.common.log.service;

import com.course.app.common.core.base.service.IBaseService;
import com.course.app.common.log.model.SysOperationLog;

import java.util.List;

/**
 * 操作日志服务接口。
 *
 * @author 云翼
 * @date 2023-02-21
 */
public interface SysOperationLogService extends IBaseService<SysOperationLog, Long> {

    /**
     * 异步的插入一条新操作日志。
     *
     * @param operationLog 操作日志对象。
     */
    void saveNewAsync(SysOperationLog operationLog);

    /**
     * 插入一条新操作日志。
     *
     * @param operationLog 操作日志对象。
     */
    void saveNew(SysOperationLog operationLog);

    /**
     * 批量插入。
     *
     * @param sysOperationLogList 操作日志列表。
     */
    void batchSave(List<SysOperationLog> sysOperationLogList);

    /**
     * 根据过滤条件和排序规则，查询操作日志。
     *
     * @param filter  操作日志的过滤对象。
     * @param orderBy 排序规则。
     * @return 查询列表。
     */
    List<SysOperationLog> getSysOperationLogList(SysOperationLog filter, String orderBy);
}
