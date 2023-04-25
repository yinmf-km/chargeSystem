package com.course.app.common.dict.service.impl;

import cn.hutool.core.util.StrUtil;
import cn.hutool.core.util.BooleanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.course.app.common.core.annotation.MyDataSource;
import com.course.app.common.core.base.dao.BaseDaoMapper;
import com.course.app.common.core.base.service.BaseService;
import com.course.app.common.core.constant.ApplicationConstant;
import com.course.app.common.core.constant.GlobalDeletedFlag;
import com.course.app.common.core.object.TokenData;
import com.course.app.common.dict.constant.GlobalDictItemStatus;
import com.course.app.common.dict.dao.TenantGlobalDictItemMapper;
import com.course.app.common.dict.model.TenantGlobalDict;
import com.course.app.common.dict.model.TenantGlobalDictItem;
import com.course.app.common.dict.service.TenantGlobalDictItemService;
import com.course.app.common.dict.service.TenantGlobalDictService;
import com.course.app.common.sequence.wrapper.IdGeneratorWrapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 租户全局字典项目数据操作服务类。
 *
 * @author 云翼
 * @date 2023-02-21
 */
@MyDataSource(ApplicationConstant.TENANT_GLOBAL_DICT_DATASOURCE_TYPE)
@Slf4j
@Service("tenantGlobalDictItemService")
public class TenantGlobalDictItemServiceImpl
        extends BaseService<TenantGlobalDictItem, Long> implements TenantGlobalDictItemService {

    @Autowired
    private TenantGlobalDictItemMapper tenantGlobalDictItemMapper;
    @Autowired
    private TenantGlobalDictService tenantGlobalDictService;
    @Autowired
    private IdGeneratorWrapper idGenerator;

    /**
     * 返回当前Service的主表Mapper对象。
     *
     * @return 主表Mapper对象。
     */
    @Override
    protected BaseDaoMapper<TenantGlobalDictItem> mapper() {
        return tenantGlobalDictItemMapper;
    }

    @Override
    public TenantGlobalDictItem saveNew(TenantGlobalDict dict, TenantGlobalDictItem dictItem) {
        tenantGlobalDictService.removeCache(dict);
        if (BooleanUtil.isFalse(dict.getTenantCommon())) {
            dictItem.setTenantId(TokenData.takeFromRequest().getTenantId());
        }
        dictItem.setId(idGenerator.nextLongId());
        dictItem.setDeletedFlag(GlobalDeletedFlag.NORMAL);
        dictItem.setStatus(GlobalDictItemStatus.NORMAL);
        dictItem.setCreateUserId(TokenData.takeFromRequest().getUserId());
        dictItem.setUpdateUserId(dictItem.getCreateUserId());
        dictItem.setCreateTime(new Date());
        dictItem.setUpdateTime(dictItem.getCreateTime());
        tenantGlobalDictItemMapper.insert(dictItem);
        return dictItem;
    }

    @Override
    public boolean update(TenantGlobalDict dict, TenantGlobalDictItem dictItem, TenantGlobalDictItem originalDictItem) {
        tenantGlobalDictService.removeCache(dict);
        // 该方法不能直接修改字典状态，更不会修改tenantId。
        dictItem.setStatus(originalDictItem.getStatus());
        dictItem.setTenantId(originalDictItem.getTenantId());
        dictItem.setCreateUserId(originalDictItem.getCreateUserId());
        dictItem.setCreateTime(originalDictItem.getCreateTime());
        dictItem.setUpdateUserId(TokenData.takeFromRequest().getUserId());
        dictItem.setUpdateTime(new Date());
        return tenantGlobalDictItemMapper.updateById(dictItem) == 1;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void updateNewCode(String oldCode, String newCode) {
        TenantGlobalDictItem dictItem = new TenantGlobalDictItem();
        dictItem.setDictCode(newCode);
        LambdaQueryWrapper<TenantGlobalDictItem> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(TenantGlobalDictItem::getDictCode, oldCode);
        tenantGlobalDictItemMapper.update(dictItem, queryWrapper);
    }

    @Override
    public void updateStatus(TenantGlobalDict dict, TenantGlobalDictItem dictItem, Integer status) {
        tenantGlobalDictService.removeCache(dict);
        dictItem.setStatus(status);
        dictItem.setUpdateUserId(TokenData.takeFromRequest().getUserId());
        dictItem.setUpdateTime(new Date());
        tenantGlobalDictItemMapper.updateById(dictItem);
    }

    @Override
    public boolean remove(TenantGlobalDict dict, TenantGlobalDictItem dictItem) {
        tenantGlobalDictService.removeCache(dict);
        return this.removeById(dictItem.getId());
    }

    @Override
    public boolean existDictCodeAndItemId(TenantGlobalDict dict, Serializable itemId) {
        LambdaQueryWrapper<TenantGlobalDictItem> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(TenantGlobalDictItem::getDictCode, dict.getDictCode());
        queryWrapper.eq(TenantGlobalDictItem::getItemId, itemId.toString());
        if (BooleanUtil.isFalse(dict.getTenantCommon())) {
            queryWrapper.eq(TenantGlobalDictItem::getTenantId, TokenData.takeFromRequest().getTenantId());
        }
        return tenantGlobalDictItemMapper.selectCount(queryWrapper) > 0;
    }

    @Override
    public boolean existDictCode(String dictCode) {
        LambdaQueryWrapper<TenantGlobalDictItem> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(TenantGlobalDictItem::getDictCode, dictCode);
        return tenantGlobalDictItemMapper.selectCount(queryWrapper) > 0;
    }

    @Override
    public TenantGlobalDictItem getGlobalDictItemByDictCodeAndItemId(String dictCode, Serializable itemId) {
        LambdaQueryWrapper<TenantGlobalDictItem> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(TenantGlobalDictItem::getDictCode, dictCode);
        queryWrapper.eq(TenantGlobalDictItem::getItemId, itemId.toString());
        return tenantGlobalDictItemMapper.selectOne(queryWrapper);
    }

    @Override
    public List<TenantGlobalDictItem> getGlobalDictItemList(TenantGlobalDictItem filter, String orderBy) {
        LambdaQueryWrapper<TenantGlobalDictItem> queryWrapper = new LambdaQueryWrapper<>(filter);
        if (StrUtil.isNotBlank(orderBy)) {
            queryWrapper.last(" ORDER BY " + orderBy);
        } else {
            queryWrapper.orderByAsc(TenantGlobalDictItem::getShowOrder);
        }
        return tenantGlobalDictItemMapper.selectList(queryWrapper);
    }

    @Override
    public List<TenantGlobalDictItem> getGlobalDictItemList(TenantGlobalDict dict) {
        LambdaQueryWrapper<TenantGlobalDictItem> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(TenantGlobalDictItem::getDictCode, dict.getDictCode());
        if (BooleanUtil.isFalse(dict.getTenantCommon())) {
            queryWrapper.eq(TenantGlobalDictItem::getTenantId, TokenData.takeFromRequest().getTenantId());
        }
        queryWrapper.orderByAsc(TenantGlobalDictItem::getShowOrder);
        return tenantGlobalDictItemMapper.selectList(queryWrapper);
    }
}
