package com.course.app.webadmin.upms.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.course.app.common.core.annotation.MyRequestBody;
import com.course.app.common.core.constant.ErrorCodeEnum;
import com.course.app.common.core.object.MyOrderParam;
import com.course.app.common.core.object.MyPageData;
import com.course.app.common.core.object.MyPageParam;
import com.course.app.common.core.object.MyRelationParam;
import com.course.app.common.core.object.ResponseResult;
import com.course.app.common.core.util.MyCommonUtil;
import com.course.app.common.core.util.MyModelUtil;
import com.course.app.common.core.util.MyPageUtil;
import com.course.app.common.log.annotation.OperationLog;
import com.course.app.common.log.model.constant.SysOperationLogType;
import com.course.app.webadmin.upms.dto.PayFeeDetailDto;
import com.course.app.webadmin.upms.model.PayFeeDetail;
import com.course.app.webadmin.upms.service.PayFeeDetailService;
import com.course.app.webadmin.upms.vo.PayFeeDetailVo;
import com.github.pagehelper.page.PageMethod;
import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;

import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;

/**
 * 交易明细操作控制器类。
 * @author 云翼
 * @date 2023-02-21
 */
@Api(tags = "交易明细管理接口")
@Slf4j
@RestController
@RequestMapping("/admin/app/payFeeDetail")
public class PayFeeDetailController {

	@Autowired
	private PayFeeDetailService payFeeDetailService;

	/**
	 * 新增交易明细数据。
	 * @param payFeeDetailDto 新增对象。
	 * @return 应答结果对象，包含新增对象主键Id。
	 */
	@ApiOperationSupport(ignoreParameters = { "payFeeDetailDto.payMerTranNo" })
	@OperationLog(type = SysOperationLogType.ADD)
	@PostMapping("/add")
	public ResponseResult<String> add(@MyRequestBody PayFeeDetailDto payFeeDetailDto) {
		String errorMessage = MyCommonUtil.getModelValidationError(payFeeDetailDto, false);
		if (errorMessage != null) {
			return ResponseResult.error(ErrorCodeEnum.DATA_VALIDATED_FAILED, errorMessage);
		}
		PayFeeDetail payFeeDetail = MyModelUtil.copyTo(payFeeDetailDto, PayFeeDetail.class);
		payFeeDetail = payFeeDetailService.saveNew(payFeeDetail);
		return ResponseResult.success(payFeeDetail.getPayMerTranNo());
	}

	/**
	 * 更新交易明细数据。
	 * @param payFeeDetailDto 更新对象。
	 * @return 应答结果对象。
	 */
	@OperationLog(type = SysOperationLogType.UPDATE)
	@PostMapping("/update")
	public ResponseResult<Void> update(@MyRequestBody PayFeeDetailDto payFeeDetailDto) {
		String errorMessage = MyCommonUtil.getModelValidationError(payFeeDetailDto, true);
		if (errorMessage != null) {
			return ResponseResult.error(ErrorCodeEnum.DATA_VALIDATED_FAILED, errorMessage);
		}
		PayFeeDetail payFeeDetail = MyModelUtil.copyTo(payFeeDetailDto, PayFeeDetail.class);
		PayFeeDetail originalPayFeeDetail = payFeeDetailService.getById(payFeeDetail.getPayMerTranNo());
		if (originalPayFeeDetail == null) {
			// NOTE: 修改下面方括号中的话述
			errorMessage = "数据验证失败，当前 [数据] 并不存在，请刷新后重试！";
			return ResponseResult.error(ErrorCodeEnum.DATA_NOT_EXIST, errorMessage);
		}
		if (!payFeeDetailService.update(payFeeDetail, originalPayFeeDetail)) {
			return ResponseResult.error(ErrorCodeEnum.DATA_NOT_EXIST);
		}
		return ResponseResult.success();
	}

	/**
	 * 删除交易明细数据。
	 * @param payMerTranNo 删除对象主键Id。
	 * @return 应答结果对象。
	 */
	@OperationLog(type = SysOperationLogType.DELETE)
	@PostMapping("/delete")
	public ResponseResult<Void> delete(@MyRequestBody Long payMerTranNo) {
		if (MyCommonUtil.existBlankArgument(payMerTranNo)) {
			return ResponseResult.error(ErrorCodeEnum.ARGUMENT_NULL_EXIST);
		}
		return this.doDelete(payMerTranNo);
	}

	/**
	 * 列出符合过滤条件的交易明细列表。
	 * @param payFeeDetailDtoFilter 过滤对象。
	 * @param orderParam 排序参数。
	 * @param pageParam 分页参数。
	 * @return 应答结果对象，包含查询结果集。
	 */
	@PostMapping("/list")
	public ResponseResult<MyPageData<PayFeeDetailVo>> list(@MyRequestBody PayFeeDetailDto payFeeDetailDtoFilter,
			@MyRequestBody MyOrderParam orderParam, @MyRequestBody MyPageParam pageParam) {
		if (pageParam != null) {
			PageMethod.startPage(pageParam.getPageNum(), pageParam.getPageSize());
		}
		PayFeeDetail payFeeDetailFilter = MyModelUtil.copyTo(payFeeDetailDtoFilter, PayFeeDetail.class);
		String orderBy = MyOrderParam.buildOrderBy(orderParam, PayFeeDetail.class);
		List<PayFeeDetail> payFeeDetailList = payFeeDetailService.getPayFeeDetailListWithRelation(payFeeDetailFilter,
				orderBy);
		return ResponseResult.success(MyPageUtil.makeResponseData(payFeeDetailList, PayFeeDetail.INSTANCE));
	}

	/**
	 * 查看指定交易明细对象详情。
	 * @param payMerTranNo 指定对象主键Id。
	 * @return 应答结果对象，包含对象详情。
	 */
	@GetMapping("/view")
	public ResponseResult<PayFeeDetailVo> view(@RequestParam String payMerTranNo) {
		PayFeeDetail payFeeDetail = payFeeDetailService.getByIdWithRelation(payMerTranNo, MyRelationParam.full());
		if (payFeeDetail == null) {
			return ResponseResult.error(ErrorCodeEnum.DATA_NOT_EXIST);
		}
		PayFeeDetailVo payFeeDetailVo = PayFeeDetail.INSTANCE.fromModel(payFeeDetail);
		return ResponseResult.success(payFeeDetailVo);
	}

	private ResponseResult<Void> doDelete(Long payMerTranNo) {
		String errorMessage;
		// 验证关联Id的数据合法性
		PayFeeDetail originalPayFeeDetail = payFeeDetailService.getById(payMerTranNo);
		if (originalPayFeeDetail == null) {
			// NOTE: 修改下面方括号中的话述
			errorMessage = "数据验证失败，当前 [对象] 并不存在，请刷新后重试！";
			return ResponseResult.error(ErrorCodeEnum.DATA_NOT_EXIST, errorMessage);
		}
		if (!payFeeDetailService.remove(payMerTranNo)) {
			errorMessage = "数据操作失败，删除的对象不存在，请刷新后重试！";
			return ResponseResult.error(ErrorCodeEnum.DATA_NOT_EXIST, errorMessage);
		}
		return ResponseResult.success();
	}
}
