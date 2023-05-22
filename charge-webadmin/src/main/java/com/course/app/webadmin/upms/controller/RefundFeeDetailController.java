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
import com.course.app.webadmin.upms.dto.RefundFeeDetailDto;
import com.course.app.webadmin.upms.model.RefundFeeDetail;
import com.course.app.webadmin.upms.service.RefundFeeDetailService;
import com.course.app.webadmin.upms.vo.RefundFeeDetailVo;
import com.github.pagehelper.page.PageMethod;
import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;

import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;

/**
 * 退费明细操作控制器类。
 * @author 云翼
 * @date 2023-02-21
 */
@Api(tags = "退费明细管理接口")
@Slf4j
@RestController
@RequestMapping("/admin/app/refundFeeDetail")
public class RefundFeeDetailController {

	@Autowired
	private RefundFeeDetailService refundFeeDetailService;

	/**
	 * 新增退费明细数据。
	 * @param refundFeeDetailDto 新增对象。
	 * @return 应答结果对象，包含新增对象主键Id。
	 */
	@ApiOperationSupport(ignoreParameters = { "refundFeeDetailDto.refundMerTranNo" })
	@OperationLog(type = SysOperationLogType.ADD)
	@PostMapping("/add")
	public ResponseResult<String> add(@MyRequestBody RefundFeeDetailDto refundFeeDetailDto) {
		String errorMessage = MyCommonUtil.getModelValidationError(refundFeeDetailDto, false);
		if (errorMessage != null) {
			return ResponseResult.error(ErrorCodeEnum.DATA_VALIDATED_FAILED, errorMessage);
		}
		RefundFeeDetail refundFeeDetail = MyModelUtil.copyTo(refundFeeDetailDto, RefundFeeDetail.class);
		refundFeeDetail = refundFeeDetailService.saveNew(refundFeeDetail);
		return ResponseResult.success(refundFeeDetail.getRefundMerTranNo());
	}

	/**
	 * 更新退费明细数据。
	 * @param refundFeeDetailDto 更新对象。
	 * @return 应答结果对象。
	 */
	@OperationLog(type = SysOperationLogType.UPDATE)
	@PostMapping("/update")
	public ResponseResult<Void> update(@MyRequestBody RefundFeeDetailDto refundFeeDetailDto) {
		String errorMessage = MyCommonUtil.getModelValidationError(refundFeeDetailDto, true);
		if (errorMessage != null) {
			return ResponseResult.error(ErrorCodeEnum.DATA_VALIDATED_FAILED, errorMessage);
		}
		RefundFeeDetail refundFeeDetail = MyModelUtil.copyTo(refundFeeDetailDto, RefundFeeDetail.class);
		RefundFeeDetail originalRefundFeeDetail = refundFeeDetailService.getById(refundFeeDetail.getRefundMerTranNo());
		if (originalRefundFeeDetail == null) {
			// NOTE: 修改下面方括号中的话述
			errorMessage = "数据验证失败，当前 [数据] 并不存在，请刷新后重试！";
			return ResponseResult.error(ErrorCodeEnum.DATA_NOT_EXIST, errorMessage);
		}
		if (!refundFeeDetailService.update(refundFeeDetail, originalRefundFeeDetail)) {
			return ResponseResult.error(ErrorCodeEnum.DATA_NOT_EXIST);
		}
		return ResponseResult.success();
	}

	/**
	 * 删除退费明细数据。
	 * @param refundMerTranNo 删除对象主键Id。
	 * @return 应答结果对象。
	 */
	@OperationLog(type = SysOperationLogType.DELETE)
	@PostMapping("/delete")
	public ResponseResult<Void> delete(@MyRequestBody Long refundMerTranNo) {
		if (MyCommonUtil.existBlankArgument(refundMerTranNo)) {
			return ResponseResult.error(ErrorCodeEnum.ARGUMENT_NULL_EXIST);
		}
		return this.doDelete(refundMerTranNo);
	}

	/**
	 * 列出符合过滤条件的退费明细列表。
	 * @param refundFeeDetailDtoFilter 过滤对象。
	 * @param orderParam 排序参数。
	 * @param pageParam 分页参数。
	 * @return 应答结果对象，包含查询结果集。
	 */
	@PostMapping("/list")
	public ResponseResult<MyPageData<RefundFeeDetailVo>> list(
			@MyRequestBody RefundFeeDetailDto refundFeeDetailDtoFilter, @MyRequestBody MyOrderParam orderParam,
			@MyRequestBody MyPageParam pageParam) {
		if (pageParam != null) {
			PageMethod.startPage(pageParam.getPageNum(), pageParam.getPageSize());
		}
		RefundFeeDetail refundFeeDetailFilter = MyModelUtil.copyTo(refundFeeDetailDtoFilter, RefundFeeDetail.class);
		String orderBy = MyOrderParam.buildOrderBy(orderParam, RefundFeeDetail.class);
		List<RefundFeeDetail> refundFeeDetailList = refundFeeDetailService
				.getRefundFeeDetailListWithRelation(refundFeeDetailFilter, orderBy);
		return ResponseResult.success(MyPageUtil.makeResponseData(refundFeeDetailList, RefundFeeDetail.INSTANCE));
	}

	/**
	 * 查看指定退费明细对象详情。
	 * @param refundMerTranNo 指定对象主键Id。
	 * @return 应答结果对象，包含对象详情。
	 */
	@GetMapping("/view")
	public ResponseResult<RefundFeeDetailVo> view(@RequestParam String refundMerTranNo) {
		RefundFeeDetail refundFeeDetail = refundFeeDetailService.getByIdWithRelation(refundMerTranNo,
				MyRelationParam.full());
		if (refundFeeDetail == null) {
			return ResponseResult.error(ErrorCodeEnum.DATA_NOT_EXIST);
		}
		RefundFeeDetailVo refundFeeDetailVo = RefundFeeDetail.INSTANCE.fromModel(refundFeeDetail);
		return ResponseResult.success(refundFeeDetailVo);
	}

	private ResponseResult<Void> doDelete(Long refundMerTranNo) {
		String errorMessage;
		// 验证关联Id的数据合法性
		RefundFeeDetail originalRefundFeeDetail = refundFeeDetailService.getById(refundMerTranNo);
		if (originalRefundFeeDetail == null) {
			// NOTE: 修改下面方括号中的话述
			errorMessage = "数据验证失败，当前 [对象] 并不存在，请刷新后重试！";
			return ResponseResult.error(ErrorCodeEnum.DATA_NOT_EXIST, errorMessage);
		}
		if (!refundFeeDetailService.remove(refundMerTranNo)) {
			errorMessage = "数据操作失败，删除的对象不存在，请刷新后重试！";
			return ResponseResult.error(ErrorCodeEnum.DATA_NOT_EXIST, errorMessage);
		}
		return ResponseResult.success();
	}
}
