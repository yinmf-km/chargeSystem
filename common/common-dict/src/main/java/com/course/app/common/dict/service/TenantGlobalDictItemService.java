package com.course.app.common.dict.service;

import com.course.app.common.core.base.service.IBaseService;
import com.course.app.common.dict.model.TenantGlobalDict;
import com.course.app.common.dict.model.TenantGlobalDictItem;

import java.io.Serializable;
import java.util.List;

/**
 * 租户全局字典项目数据操作服务接口。
 *
 * @author 云翼
 * @date 2023-02-21
 */
public interface TenantGlobalDictItemService extends IBaseService<TenantGlobalDictItem, Long> {

    /**
     * 保存新增的租户字典项目。
     *
     * @param tenantGlobalDict     字典对象。
     * @param tenantGlobalDictItem 新字典项目对象。
     * @return 保存后的对象。
     */
    TenantGlobalDictItem saveNew(TenantGlobalDict tenantGlobalDict, TenantGlobalDictItem tenantGlobalDictItem);

    /**
     * 更新租户字典项目对象。
     *
     * @param tenantGlobalDict             字典对象。
     * @param tenantGlobalDictItem         更新的全局字典项目对象。
     * @param originalTenantGlobalDictItem 原有的全局字典项目对象。
     * @return 更新成功返回true，否则false。
     */
    boolean update(
            TenantGlobalDict tenantGlobalDict,
            TenantGlobalDictItem tenantGlobalDictItem,
            TenantGlobalDictItem originalTenantGlobalDictItem);

    /**
     * 更新字典条目的编码。
     *
     * @param oldCode 原有编码。
     * @param newCode 新编码。
     */
    void updateNewCode(String oldCode, String newCode);

    /**
     * 更新字典条目的状态。
     *
     * @param tenantGlobalDict     字典对象。
     * @param tenantGlobalDictItem 字典项目对象。
     * @param status               状态值。
     */
    void updateStatus(TenantGlobalDict tenantGlobalDict, TenantGlobalDictItem tenantGlobalDictItem, Integer status);

    /**
     * 删除指定租户字典项目。
     *
     * @param tenantGlobalDict     字典对象。
     * @param tenantGlobalDictItem 待删除字典项目。
     * @return 成功返回true，否则false。
     */
    boolean remove(TenantGlobalDict tenantGlobalDict, TenantGlobalDictItem tenantGlobalDictItem);

    /**
     * 判断指定字典的项目Id是否存在。如果是租户非公用字典，会基于租户Id进行过滤。
     *
     * @param tenantGlobalDict 字典对象。
     * @param itemId           项目Id。
     * @return true存在，否则false。
     */
    boolean existDictCodeAndItemId(TenantGlobalDict tenantGlobalDict, Serializable itemId);

    /**
     * 判断指定租户的编码是否已经存在字典数据。
     *
     * @param dictCode 字典编码。
     * @return true存在，否则false。
     */
    boolean existDictCode(String dictCode);

    /**
     * 根据租户字典编码和项目Id获取指定字段项目对象。
     *
     * @param dictCode 字典编码。
     * @param itemId   项目Id。
     * @return 字典项目对象。
     */
    TenantGlobalDictItem getGlobalDictItemByDictCodeAndItemId(String dictCode, Serializable itemId);

    /**
     * 查询租户数据字典项目列表。
     *
     * @param filter  过滤对象。
     * @param orderBy 排序字符串，如果为空，则按照showOrder升序排序。
     * @return 查询结果列表。
     */
    List<TenantGlobalDictItem> getGlobalDictItemList(TenantGlobalDictItem filter, String orderBy);

    /**
     * 查询指定字典的租户数据字典项目列表。如果是租户非公用字典，会仅仅返回该租户的字典数据列表。按照showOrder升序排序。
     *
     * @param tenantGlobalDict 编码字典对象。
     * @return 查询结果列表。
     */
    List<TenantGlobalDictItem> getGlobalDictItemList(TenantGlobalDict tenantGlobalDict);
}
