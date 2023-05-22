package com.course.app.webadmin.upms.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.course.app.common.core.base.dao.BaseDaoMapper;
import com.course.app.common.core.base.service.BaseService;
import com.course.app.common.core.object.MyRelationParam;
import com.course.app.common.sequence.wrapper.IdGeneratorWrapper;
import com.course.app.webadmin.upms.dao.PrePaymentFeeDetailMapper;
import com.course.app.webadmin.upms.model.PrePaymentFeeDetail;
import com.course.app.webadmin.upms.service.PrePaymentFeeDetailService;
import com.github.pagehelper.Page;

import cn.hutool.core.collection.CollUtil;

/**
 * 预缴费明细数据操作服务类。
 * @author 云翼
 * @date 2023-02-21
 */
@Service("prePaymentFeeDetailService")
public class PrePaymentFeeDetailServiceImpl extends BaseService<PrePaymentFeeDetail, Long>
		implements PrePaymentFeeDetailService {

	@Autowired
	private PrePaymentFeeDetailMapper prePaymentFeeDetailMapper;
	@Autowired
	private IdGeneratorWrapper idGenerator;

	/**
	 * 返回当前Service的主表Mapper对象。
	 * @return 主表Mapper对象。
	 */
	@Override
	protected BaseDaoMapper<PrePaymentFeeDetail> mapper() {
		return prePaymentFeeDetailMapper;
	}

	/**
	 * 保存新增对象。
	 * @param prePaymentFeeDetail 新增对象。
	 * @return 返回新增对象。
	 */
	@Transactional(rollbackFor = Exception.class)
	@Override
	public PrePaymentFeeDetail saveNew(PrePaymentFeeDetail prePaymentFeeDetail) {
		prePaymentFeeDetailMapper.insert(this.buildDefaultValue(prePaymentFeeDetail));
		return prePaymentFeeDetail;
	}

	/**
	 * 利用数据库的insertList语法，批量插入对象列表。
	 * @param prePaymentFeeDetailList 新增对象列表。
	 */
	@Transactional(rollbackFor = Exception.class)
	@Override
	public void saveNewBatch(List<PrePaymentFeeDetail> prePaymentFeeDetailList) {
		if (CollUtil.isNotEmpty(prePaymentFeeDetailList)) {
			prePaymentFeeDetailList.forEach(this::buildDefaultValue);
			prePaymentFeeDetailMapper.insertList(prePaymentFeeDetailList);
		}
	}

	/**
	 * 更新数据对象。
	 * @param prePaymentFeeDetail 更新的对象。
	 * @param originalPrePaymentFeeDetail 原有数据对象。
	 * @return 成功返回true，否则false。
	 */
	@Transactional(rollbackFor = Exception.class)
	@Override
	public boolean update(PrePaymentFeeDetail prePaymentFeeDetail, PrePaymentFeeDetail originalPrePaymentFeeDetail) {
		// 这里重点提示，在执行主表数据更新之前，如果有哪些字段不支持修改操作，请用原有数据对象字段替换当前数据字段。
		UpdateWrapper<PrePaymentFeeDetail> uw = this.createUpdateQueryForNullValue(prePaymentFeeDetail,
				prePaymentFeeDetail.getPrePayMerTranNo());
		return prePaymentFeeDetailMapper.update(prePaymentFeeDetail, uw) == 1;
	}

	/**
	 * 删除指定数据。
	 * @param prePayMerTranNo 主键Id。
	 * @return 成功返回true，否则false。
	 */
	@Transactional(rollbackFor = Exception.class)
	@Override
	public boolean remove(Long prePayMerTranNo) {
		return prePaymentFeeDetailMapper.deleteById(prePayMerTranNo) == 1;
	}

	/**
	 * 获取单表查询结果。由于没有关联数据查询，因此在仅仅获取单表数据的场景下，效率更高。 如果需要同时获取关联数据，请移步(getPrePaymentFeeDetailListWithRelation)方法。
	 * @param filter 过滤对象。
	 * @param orderBy 排序参数。
	 * @return 查询结果集。
	 */
	@Override
	public List<PrePaymentFeeDetail> getPrePaymentFeeDetailList(PrePaymentFeeDetail filter, String orderBy) {
		return prePaymentFeeDetailMapper.getPrePaymentFeeDetailList(filter, orderBy);
	}

	/**
	 * 获取主表的查询结果，以及主表关联的字典数据和一对一从表数据，以及一对一从表的字典数据。 该查询会涉及到一对一从表的关联过滤，或一对多从表的嵌套关联过滤，因此性能不如单表过滤。
	 * 如果仅仅需要获取主表数据，请移步(getPrePaymentFeeDetailList)，以便获取更好的查询性能。
	 * @param filter 主表过滤对象。
	 * @param orderBy 排序参数。
	 * @return 查询结果集。
	 */
	@Override
	public List<PrePaymentFeeDetail> getPrePaymentFeeDetailListWithRelation(PrePaymentFeeDetail filter,
			String orderBy) {
		List<PrePaymentFeeDetail> resultList = prePaymentFeeDetailMapper.getPrePaymentFeeDetailList(filter, orderBy);
		// 在缺省生成的代码中，如果查询结果resultList不是Page对象，说明没有分页，那么就很可能是数据导出接口调用了当前方法。
		// 为了避免一次性的大量数据关联，规避因此而造成的系统运行性能冲击，这里手动进行了分批次读取，开发者可按需修改该值。
		int batchSize = resultList instanceof Page ? 0 : 1000;
		this.buildRelationForDataList(resultList, MyRelationParam.normal(), batchSize);
		return resultList;
	}

	private PrePaymentFeeDetail buildDefaultValue(PrePaymentFeeDetail prePaymentFeeDetail) {
		if (prePaymentFeeDetail.getPrePayMerTranNo() == null) {
			prePaymentFeeDetail.setPrePayMerTranNo(idGenerator.nextLongId());
		}
		return prePaymentFeeDetail;
	}
}
