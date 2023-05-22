package com.course.app.webadmin.upms.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.course.app.common.core.base.dao.BaseDaoMapper;
import com.course.app.common.core.base.service.BaseService;
import com.course.app.common.core.object.MyRelationParam;
import com.course.app.common.core.util.MyModelUtil;
import com.course.app.common.sequence.wrapper.IdGeneratorWrapper;
import com.course.app.webadmin.upms.dao.PayFeeDetailMapper;
import com.course.app.webadmin.upms.model.PayFeeDetail;
import com.course.app.webadmin.upms.model.StudentFeeDetail;
import com.course.app.webadmin.upms.model.SysStudent;
import com.course.app.webadmin.upms.model.pmms.request.MPNG210003RequestV1.MPNG210003RequestV1Biz.ReqBody;
import com.course.app.webadmin.upms.model.pmms.request.PmmsMpngNotifyRequestV1.PmmsMpngNotifyRequestV1Biz;
import com.course.app.webadmin.upms.model.pmms.request.ReqHead;
import com.course.app.webadmin.upms.service.PayFeeDetailService;
import com.github.pagehelper.Page;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateUtil;

/**
 * 交易明细数据操作服务类。
 * @author 云翼
 * @date 2023-02-21
 */
@Service("payFeeDetailService")
public class PayFeeDetailServiceImpl extends BaseService<PayFeeDetail, String> implements PayFeeDetailService {

	@Autowired
	private PayFeeDetailMapper payFeeDetailMapper;
	@Autowired
	private IdGeneratorWrapper idGenerator;

	@Override
	public void insertPayLog(ReqHead reqHead, ReqBody reqBody, SysStudent student, StudentFeeDetail studentFeeDetail) {
		PayFeeDetail payFeeDetail = new PayFeeDetail();
		payFeeDetail.setPayMerTranNo(reqBody.getPayMerTranNo());
		payFeeDetail.setStudentId(student.getStudentId());
		payFeeDetail.setPhoneNum(student.getPhoneNum());
		//payFeeDetail.setPayAcct(null);
		//payFeeDetail.setTradeAmount(null);
		payFeeDetail.setTranScene(reqBody.getTranScene());
		payFeeDetail.setMerPtcId(reqBody.getMerPtcId());
		payFeeDetail.setCurrency(reqBody.getCurrency());
		payFeeDetail.setFeeId(studentFeeDetail.getFeeId());
		MyModelUtil.fillCommonsForInsert(payFeeDetail);
		save(payFeeDetail);
	}

	@Override
	public void updatePayLog(PmmsMpngNotifyRequestV1Biz request) {
		PayFeeDetail payFeeDetail = new PayFeeDetail();
		payFeeDetail.setPayMerTranNo(request.getMerTranNo());
		payFeeDetail.setTranStatus(request.getTranState());
		payFeeDetail.setTransTime(DateUtil.parse(request.getFinalTime(), DatePattern.NORM_DATETIME_PATTERN));
		//payFeeDetail.setTradeAmount(null);
		payFeeDetail.setBuyerPayAmount(request.getBuyerPayAmount());
		payFeeDetail.setTotalAmount(request.getTotalAmount());
		//payFeeDetail.setRefundedAmt(null);
		payFeeDetail.setTranContent(request.getTranContent());
		payFeeDetail.setPayDsctAmount(request.getPayDsctAmount());
		//payFeeDetail.setOrderStatus(null);
		MyModelUtil.fillCommonsForUpdate(payFeeDetail, null);
		saveOrUpdate(payFeeDetail);
	}

	/**
	 * 返回当前Service的主表Mapper对象。
	 * @return 主表Mapper对象。
	 */
	@Override
	protected BaseDaoMapper<PayFeeDetail> mapper() {
		return payFeeDetailMapper;
	}

	/**
	 * 保存新增对象。
	 * @param payFeeDetail 新增对象。
	 * @return 返回新增对象。
	 */
	@Transactional(rollbackFor = Exception.class)
	@Override
	public PayFeeDetail saveNew(PayFeeDetail payFeeDetail) {
		payFeeDetailMapper.insert(this.buildDefaultValue(payFeeDetail));
		return payFeeDetail;
	}

	/**
	 * 利用数据库的insertList语法，批量插入对象列表。
	 * @param payFeeDetailList 新增对象列表。
	 */
	@Transactional(rollbackFor = Exception.class)
	@Override
	public void saveNewBatch(List<PayFeeDetail> payFeeDetailList) {
		if (CollUtil.isNotEmpty(payFeeDetailList)) {
			payFeeDetailList.forEach(this::buildDefaultValue);
			payFeeDetailMapper.insertList(payFeeDetailList);
		}
	}

	/**
	 * 更新数据对象。
	 * @param payFeeDetail 更新的对象。
	 * @param originalPayFeeDetail 原有数据对象。
	 * @return 成功返回true，否则false。
	 */
	@Transactional(rollbackFor = Exception.class)
	@Override
	public boolean update(PayFeeDetail payFeeDetail, PayFeeDetail originalPayFeeDetail) {
		// 这里重点提示，在执行主表数据更新之前，如果有哪些字段不支持修改操作，请用原有数据对象字段替换当前数据字段。
		UpdateWrapper<PayFeeDetail> uw = this.createUpdateQueryForNullValue(payFeeDetail,
				payFeeDetail.getPayMerTranNo());
		return payFeeDetailMapper.update(payFeeDetail, uw) == 1;
	}

	/**
	 * 删除指定数据。
	 * @param payMerTranNo 主键Id。
	 * @return 成功返回true，否则false。
	 */
	@Transactional(rollbackFor = Exception.class)
	@Override
	public boolean remove(Long payMerTranNo) {
		return payFeeDetailMapper.deleteById(payMerTranNo) == 1;
	}

	/**
	 * 获取单表查询结果。由于没有关联数据查询，因此在仅仅获取单表数据的场景下，效率更高。 如果需要同时获取关联数据，请移步(getPayFeeDetailListWithRelation)方法。
	 * @param filter 过滤对象。
	 * @param orderBy 排序参数。
	 * @return 查询结果集。
	 */
	@Override
	public List<PayFeeDetail> getPayFeeDetailList(PayFeeDetail filter, String orderBy) {
		return payFeeDetailMapper.getPayFeeDetailList(filter, orderBy);
	}

	/**
	 * 获取主表的查询结果，以及主表关联的字典数据和一对一从表数据，以及一对一从表的字典数据。 该查询会涉及到一对一从表的关联过滤，或一对多从表的嵌套关联过滤，因此性能不如单表过滤。
	 * 如果仅仅需要获取主表数据，请移步(getPayFeeDetailList)，以便获取更好的查询性能。
	 * @param filter 主表过滤对象。
	 * @param orderBy 排序参数。
	 * @return 查询结果集。
	 */
	@Override
	public List<PayFeeDetail> getPayFeeDetailListWithRelation(PayFeeDetail filter, String orderBy) {
		List<PayFeeDetail> resultList = payFeeDetailMapper.getPayFeeDetailList(filter, orderBy);
		// 在缺省生成的代码中，如果查询结果resultList不是Page对象，说明没有分页，那么就很可能是数据导出接口调用了当前方法。
		// 为了避免一次性的大量数据关联，规避因此而造成的系统运行性能冲击，这里手动进行了分批次读取，开发者可按需修改该值。
		int batchSize = resultList instanceof Page ? 0 : 1000;
		this.buildRelationForDataList(resultList, MyRelationParam.normal(), batchSize);
		return resultList;
	}

	private PayFeeDetail buildDefaultValue(PayFeeDetail payFeeDetail) {
		if (payFeeDetail.getPayMerTranNo() == null) {
			payFeeDetail.setPayMerTranNo(idGenerator.nextStringId());
		}
		return payFeeDetail;
	}
}
