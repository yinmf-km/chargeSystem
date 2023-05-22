package com.course.app.webadmin.upms.service;

import java.util.List;

import com.course.app.common.core.base.service.IBaseService;
import com.course.app.webadmin.upms.model.PayFeeDetail;
import com.course.app.webadmin.upms.model.StudentFeeDetail;
import com.course.app.webadmin.upms.model.SysStudent;
import com.course.app.webadmin.upms.model.pmms.request.MPNG210003RequestV1;
import com.course.app.webadmin.upms.model.pmms.request.PmmsMpngNotifyRequestV1;
import com.course.app.webadmin.upms.model.pmms.request.ReqHead;

/**
 * 交易明细数据操作服务接口。
 * @author 云翼
 * @date 2023-02-21
 */
public interface PayFeeDetailService extends IBaseService<PayFeeDetail, String> {

	/**
	 * 保存新增对象。
	 * @param payFeeDetail 新增对象。
	 * @return 返回新增对象。
	 */
	PayFeeDetail saveNew(PayFeeDetail payFeeDetail);

	/**
	 * 利用数据库的insertList语法，批量插入对象列表。
	 * @param payFeeDetailList 新增对象列表。
	 */
	void saveNewBatch(List<PayFeeDetail> payFeeDetailList);

	/**
	 * 更新数据对象。
	 * @param payFeeDetail 更新的对象。
	 * @param originalPayFeeDetail 原有数据对象。
	 * @return 成功返回true，否则false。
	 */
	boolean update(PayFeeDetail payFeeDetail, PayFeeDetail originalPayFeeDetail);

	/**
	 * 删除指定数据。
	 * @param payMerTranNo 主键Id。
	 * @return 成功返回true，否则false。
	 */
	boolean remove(Long payMerTranNo);

	/**
	 * 获取单表查询结果。由于没有关联数据查询，因此在仅仅获取单表数据的场景下，效率更高。 如果需要同时获取关联数据，请移步(getPayFeeDetailListWithRelation)方法。
	 * @param filter 过滤对象。
	 * @param orderBy 排序参数。
	 * @return 查询结果集。
	 */
	List<PayFeeDetail> getPayFeeDetailList(PayFeeDetail filter, String orderBy);

	/**
	 * 获取主表的查询结果，以及主表关联的字典数据和一对一从表数据，以及一对一从表的字典数据。 该查询会涉及到一对一从表的关联过滤，或一对多从表的嵌套关联过滤，因此性能不如单表过滤。
	 * 如果仅仅需要获取主表数据，请移步(getPayFeeDetailList)，以便获取更好的查询性能。
	 * @param filter 主表过滤对象。
	 * @param orderBy 排序参数。
	 * @return 查询结果集。
	 */
	List<PayFeeDetail> getPayFeeDetailListWithRelation(PayFeeDetail filter, String orderBy);

	void insertPayLog(ReqHead reqHead, MPNG210003RequestV1.MPNG210003RequestV1Biz.ReqBody reqBody, SysStudent student,
			StudentFeeDetail studentFeeDetail);

	void updatePayLog(PmmsMpngNotifyRequestV1.PmmsMpngNotifyRequestV1Biz request);
}
