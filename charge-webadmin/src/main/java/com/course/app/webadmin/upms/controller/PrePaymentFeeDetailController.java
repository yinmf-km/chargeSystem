package com.course.app.webadmin.upms.controller;

import com.course.app.common.log.annotation.OperationLog;
import com.course.app.common.log.model.constant.SysOperationLogType;
import com.github.pagehelper.page.PageMethod;
import com.course.app.webadmin.upms.vo.*;
import com.course.app.webadmin.upms.dto.*;
import com.course.app.webadmin.upms.model.*;
import com.course.app.webadmin.upms.service.*;
import com.course.app.common.core.object.*;
import com.course.app.common.core.util.*;
import com.course.app.common.core.constant.*;
import com.course.app.common.core.annotation.MyRequestBody;
import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.*;

/**
 * 预缴费明细操作控制器类。
 *
 * @author 云翼
 * @date 2023-02-21
 */
@Api(tags = "预缴费明细管理接口")
@Slf4j
@RestController
@RequestMapping("/admin/app/prePaymentFeeDetail")
public class PrePaymentFeeDetailController {

    @Autowired
    private PrePaymentFeeDetailService prePaymentFeeDetailService;

    /**
     * 新增预缴费明细数据。
     *
     * @param prePaymentFeeDetailDto 新增对象。
     * @return 应答结果对象，包含新增对象主键Id。
     */
    @ApiOperationSupport(ignoreParameters = {"prePaymentFeeDetailDto.prePayMerTranNo"})
    @OperationLog(type = SysOperationLogType.ADD)
    @PostMapping("/add")
    public ResponseResult<Long> add(@MyRequestBody PrePaymentFeeDetailDto prePaymentFeeDetailDto) {
        String errorMessage = MyCommonUtil.getModelValidationError(prePaymentFeeDetailDto, false);
        if (errorMessage != null) {
            return ResponseResult.error(ErrorCodeEnum.DATA_VALIDATED_FAILED, errorMessage);
        }
        PrePaymentFeeDetail prePaymentFeeDetail = MyModelUtil.copyTo(prePaymentFeeDetailDto, PrePaymentFeeDetail.class);
        prePaymentFeeDetail = prePaymentFeeDetailService.saveNew(prePaymentFeeDetail);
        return ResponseResult.success(prePaymentFeeDetail.getPrePayMerTranNo());
    }

    /**
     * 更新预缴费明细数据。
     *
     * @param prePaymentFeeDetailDto 更新对象。
     * @return 应答结果对象。
     */
    @OperationLog(type = SysOperationLogType.UPDATE)
    @PostMapping("/update")
    public ResponseResult<Void> update(@MyRequestBody PrePaymentFeeDetailDto prePaymentFeeDetailDto) {
        String errorMessage = MyCommonUtil.getModelValidationError(prePaymentFeeDetailDto, true);
        if (errorMessage != null) {
            return ResponseResult.error(ErrorCodeEnum.DATA_VALIDATED_FAILED, errorMessage);
        }
        PrePaymentFeeDetail prePaymentFeeDetail = MyModelUtil.copyTo(prePaymentFeeDetailDto, PrePaymentFeeDetail.class);
        PrePaymentFeeDetail originalPrePaymentFeeDetail = prePaymentFeeDetailService.getById(prePaymentFeeDetail.getPrePayMerTranNo());
        if (originalPrePaymentFeeDetail == null) {
            // NOTE: 修改下面方括号中的话述
            errorMessage = "数据验证失败，当前 [数据] 并不存在，请刷新后重试！";
            return ResponseResult.error(ErrorCodeEnum.DATA_NOT_EXIST, errorMessage);
        }
        if (!prePaymentFeeDetailService.update(prePaymentFeeDetail, originalPrePaymentFeeDetail)) {
            return ResponseResult.error(ErrorCodeEnum.DATA_NOT_EXIST);
        }
        return ResponseResult.success();
    }

    /**
     * 删除预缴费明细数据。
     *
     * @param prePayMerTranNo 删除对象主键Id。
     * @return 应答结果对象。
     */
    @OperationLog(type = SysOperationLogType.DELETE)
    @PostMapping("/delete")
    public ResponseResult<Void> delete(@MyRequestBody Long prePayMerTranNo) {
        if (MyCommonUtil.existBlankArgument(prePayMerTranNo)) {
            return ResponseResult.error(ErrorCodeEnum.ARGUMENT_NULL_EXIST);
        }
        return this.doDelete(prePayMerTranNo);
    }

    /**
     * 列出符合过滤条件的预缴费明细列表。
     *
     * @param prePaymentFeeDetailDtoFilter 过滤对象。
     * @param orderParam 排序参数。
     * @param pageParam 分页参数。
     * @return 应答结果对象，包含查询结果集。
     */
    @PostMapping("/list")
    public ResponseResult<MyPageData<PrePaymentFeeDetailVo>> list(
            @MyRequestBody PrePaymentFeeDetailDto prePaymentFeeDetailDtoFilter,
            @MyRequestBody MyOrderParam orderParam,
            @MyRequestBody MyPageParam pageParam) {
        if (pageParam != null) {
            PageMethod.startPage(pageParam.getPageNum(), pageParam.getPageSize());
        }
        PrePaymentFeeDetail prePaymentFeeDetailFilter = MyModelUtil.copyTo(prePaymentFeeDetailDtoFilter, PrePaymentFeeDetail.class);
        String orderBy = MyOrderParam.buildOrderBy(orderParam, PrePaymentFeeDetail.class);
        List<PrePaymentFeeDetail> prePaymentFeeDetailList =
                prePaymentFeeDetailService.getPrePaymentFeeDetailListWithRelation(prePaymentFeeDetailFilter, orderBy);
        return ResponseResult.success(MyPageUtil.makeResponseData(prePaymentFeeDetailList, PrePaymentFeeDetail.INSTANCE));
    }

    /**
     * 查看指定预缴费明细对象详情。
     *
     * @param prePayMerTranNo 指定对象主键Id。
     * @return 应答结果对象，包含对象详情。
     */
    @GetMapping("/view")
    public ResponseResult<PrePaymentFeeDetailVo> view(@RequestParam Long prePayMerTranNo) {
        PrePaymentFeeDetail prePaymentFeeDetail = prePaymentFeeDetailService.getByIdWithRelation(prePayMerTranNo, MyRelationParam.full());
        if (prePaymentFeeDetail == null) {
            return ResponseResult.error(ErrorCodeEnum.DATA_NOT_EXIST);
        }
        PrePaymentFeeDetailVo prePaymentFeeDetailVo = PrePaymentFeeDetail.INSTANCE.fromModel(prePaymentFeeDetail);
        return ResponseResult.success(prePaymentFeeDetailVo);
    }

    private ResponseResult<Void> doDelete(Long prePayMerTranNo) {
        String errorMessage;
        // 验证关联Id的数据合法性
        PrePaymentFeeDetail originalPrePaymentFeeDetail = prePaymentFeeDetailService.getById(prePayMerTranNo);
        if (originalPrePaymentFeeDetail == null) {
            // NOTE: 修改下面方括号中的话述
            errorMessage = "数据验证失败，当前 [对象] 并不存在，请刷新后重试！";
            return ResponseResult.error(ErrorCodeEnum.DATA_NOT_EXIST, errorMessage);
        }
        if (!prePaymentFeeDetailService.remove(prePayMerTranNo)) {
            errorMessage = "数据操作失败，删除的对象不存在，请刷新后重试！";
            return ResponseResult.error(ErrorCodeEnum.DATA_NOT_EXIST, errorMessage);
        }
        return ResponseResult.success();
    }
}
