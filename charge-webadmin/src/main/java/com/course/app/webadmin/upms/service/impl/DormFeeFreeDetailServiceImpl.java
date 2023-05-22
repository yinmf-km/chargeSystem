package com.course.app.webadmin.upms.service.impl;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.course.app.common.core.base.dao.BaseDaoMapper;
import com.course.app.common.core.base.service.BaseService;
import com.course.app.common.core.constant.GlobalDeletedFlag;
import com.course.app.common.core.object.MyRelationParam;
import com.course.app.common.core.object.TokenData;
import com.course.app.common.sequence.wrapper.IdGeneratorWrapper;
import com.course.app.webadmin.upms.dao.DormFeeFreeDetailMapper;
import com.course.app.webadmin.upms.model.DormFeeFreeDetail;
import com.course.app.webadmin.upms.service.DormFeeFreeDetailService;
import com.github.pagehelper.Page;

import cn.hutool.core.collection.CollUtil;

/**
 * 住宿费减免明细数据操作服务类。
 * @author 云翼
 * @date 2023-02-21
 */
@Service("dormFeeFreeDetailService")
public class DormFeeFreeDetailServiceImpl extends BaseService<DormFeeFreeDetail, Long>
		implements DormFeeFreeDetailService {

	@Autowired
	private DormFeeFreeDetailMapper dormFeeFreeDetailMapper;
	@Autowired
	private IdGeneratorWrapper idGenerator;

	/**
	 * 返回当前Service的主表Mapper对象。
	 * @return 主表Mapper对象。
	 */
	@Override
	protected BaseDaoMapper<DormFeeFreeDetail> mapper() {
		return dormFeeFreeDetailMapper;
	}

	/**
	 * 保存新增对象。
	 * @param dormFeeFreeDetail 新增对象。
	 * @return 返回新增对象。
	 */
	@Transactional(rollbackFor = Exception.class)
	@Override
	public DormFeeFreeDetail saveNew(DormFeeFreeDetail dormFeeFreeDetail) {
		dormFeeFreeDetailMapper.insert(this.buildDefaultValue(dormFeeFreeDetail));
		return dormFeeFreeDetail;
	}

	/**
	 * 利用数据库的insertList语法，批量插入对象列表。
	 * @param dormFeeFreeDetailList 新增对象列表。
	 */
	@Transactional(rollbackFor = Exception.class)
	@Override
	public void saveNewBatch(List<DormFeeFreeDetail> dormFeeFreeDetailList) {
		if (CollUtil.isNotEmpty(dormFeeFreeDetailList)) {
			dormFeeFreeDetailList.forEach(this::buildDefaultValue);
			dormFeeFreeDetailMapper.insertList(dormFeeFreeDetailList);
		}
	}

	/**
	 * 更新数据对象。
	 * @param dormFeeFreeDetail 更新的对象。
	 * @param originalDormFeeFreeDetail 原有数据对象。
	 * @return 成功返回true，否则false。
	 */
	@Transactional(rollbackFor = Exception.class)
	@Override
	public boolean update(DormFeeFreeDetail dormFeeFreeDetail, DormFeeFreeDetail originalDormFeeFreeDetail) {
		dormFeeFreeDetail.setCreateUserId(originalDormFeeFreeDetail.getCreateUserId());
		dormFeeFreeDetail.setCreateTime(originalDormFeeFreeDetail.getCreateTime());
		// 这里重点提示，在执行主表数据更新之前，如果有哪些字段不支持修改操作，请用原有数据对象字段替换当前数据字段。
		UpdateWrapper<DormFeeFreeDetail> uw = this.createUpdateQueryForNullValue(dormFeeFreeDetail,
				dormFeeFreeDetail.getId());
		return dormFeeFreeDetailMapper.update(dormFeeFreeDetail, uw) == 1;
	}

	/**
	 * 删除指定数据。
	 * @param id 主键Id。
	 * @return 成功返回true，否则false。
	 */
	@Transactional(rollbackFor = Exception.class)
	@Override
	public boolean remove(Long id) {
		return dormFeeFreeDetailMapper.deleteById(id) == 1;
	}

	/**
	 * 获取单表查询结果。由于没有关联数据查询，因此在仅仅获取单表数据的场景下，效率更高。 如果需要同时获取关联数据，请移步(getDormFeeFreeDetailListWithRelation)方法。
	 * @param filter 过滤对象。
	 * @param orderBy 排序参数。
	 * @return 查询结果集。
	 */
	@Override
	public List<DormFeeFreeDetail> getDormFeeFreeDetailList(DormFeeFreeDetail filter, String orderBy) {
		return dormFeeFreeDetailMapper.getDormFeeFreeDetailList(filter, orderBy);
	}

	/**
	 * 获取主表的查询结果，以及主表关联的字典数据和一对一从表数据，以及一对一从表的字典数据。 该查询会涉及到一对一从表的关联过滤，或一对多从表的嵌套关联过滤，因此性能不如单表过滤。
	 * 如果仅仅需要获取主表数据，请移步(getDormFeeFreeDetailList)，以便获取更好的查询性能。
	 * @param filter 主表过滤对象。
	 * @param orderBy 排序参数。
	 * @return 查询结果集。
	 */
	@Override
	public List<DormFeeFreeDetail> getDormFeeFreeDetailListWithRelation(DormFeeFreeDetail filter, String orderBy) {
		List<DormFeeFreeDetail> resultList = dormFeeFreeDetailMapper.getDormFeeFreeDetailList(filter, orderBy);
		// 在缺省生成的代码中，如果查询结果resultList不是Page对象，说明没有分页，那么就很可能是数据导出接口调用了当前方法。
		// 为了避免一次性的大量数据关联，规避因此而造成的系统运行性能冲击，这里手动进行了分批次读取，开发者可按需修改该值。
		int batchSize = resultList instanceof Page ? 0 : 1000;
		this.buildRelationForDataList(resultList, MyRelationParam.normal(), batchSize);
		return resultList;
	}

	private DormFeeFreeDetail buildDefaultValue(DormFeeFreeDetail dormFeeFreeDetail) {
		if (dormFeeFreeDetail.getId() == null) {
			dormFeeFreeDetail.setId(idGenerator.nextLongId());
		}
		TokenData tokenData = TokenData.takeFromRequest();
		dormFeeFreeDetail.setCreateUserId(tokenData.getUserId());
		dormFeeFreeDetail.setCreateTime(new Date());
		dormFeeFreeDetail.setDeletedFlag(GlobalDeletedFlag.NORMAL);
		return dormFeeFreeDetail;
	}
}
