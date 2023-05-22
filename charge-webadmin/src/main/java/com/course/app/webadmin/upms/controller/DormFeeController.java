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
 * 住宿费信息操作控制器类。
 *
 * @author 云翼
 * @date 2023-02-21
 */
@Api(tags = "住宿费信息管理接口")
@Slf4j
@RestController
@RequestMapping("/admin/app/dormFee")
public class DormFeeController {

    @Autowired
    private DormFeeService dormFeeService;

    /**
     * 新增住宿费信息数据。
     *
     * @param dormFeeDto 新增对象。
     * @return 应答结果对象，包含新增对象主键Id。
     */
    @ApiOperationSupport(ignoreParameters = {"dormFeeDto.id"})
    @OperationLog(type = SysOperationLogType.ADD)
    @PostMapping("/add")
    public ResponseResult<Long> add(@MyRequestBody DormFeeDto dormFeeDto) {
        String errorMessage = MyCommonUtil.getModelValidationError(dormFeeDto, false);
        if (errorMessage != null) {
            return ResponseResult.error(ErrorCodeEnum.DATA_VALIDATED_FAILED, errorMessage);
        }
        DormFee dormFee = MyModelUtil.copyTo(dormFeeDto, DormFee.class);
        dormFee = dormFeeService.saveNew(dormFee);
        return ResponseResult.success(dormFee.getId());
    }

    /**
     * 更新住宿费信息数据。
     *
     * @param dormFeeDto 更新对象。
     * @return 应答结果对象。
     */
    @OperationLog(type = SysOperationLogType.UPDATE)
    @PostMapping("/update")
    public ResponseResult<Void> update(@MyRequestBody DormFeeDto dormFeeDto) {
        String errorMessage = MyCommonUtil.getModelValidationError(dormFeeDto, true);
        if (errorMessage != null) {
            return ResponseResult.error(ErrorCodeEnum.DATA_VALIDATED_FAILED, errorMessage);
        }
        DormFee dormFee = MyModelUtil.copyTo(dormFeeDto, DormFee.class);
        DormFee originalDormFee = dormFeeService.getById(dormFee.getId());
        if (originalDormFee == null) {
            // NOTE: 修改下面方括号中的话述
            errorMessage = "数据验证失败，当前 [数据] 并不存在，请刷新后重试！";
            return ResponseResult.error(ErrorCodeEnum.DATA_NOT_EXIST, errorMessage);
        }
        if (!dormFeeService.update(dormFee, originalDormFee)) {
            return ResponseResult.error(ErrorCodeEnum.DATA_NOT_EXIST);
        }
        return ResponseResult.success();
    }

    /**
     * 删除住宿费信息数据。
     *
     * @param id 删除对象主键Id。
     * @return 应答结果对象。
     */
    @OperationLog(type = SysOperationLogType.DELETE)
    @PostMapping("/delete")
    public ResponseResult<Void> delete(@MyRequestBody Long id) {
        if (MyCommonUtil.existBlankArgument(id)) {
            return ResponseResult.error(ErrorCodeEnum.ARGUMENT_NULL_EXIST);
        }
        return this.doDelete(id);
    }

    /**
     * 列出符合过滤条件的住宿费信息列表。
     *
     * @param dormFeeDtoFilter 过滤对象。
     * @param orderParam 排序参数。
     * @param pageParam 分页参数。
     * @return 应答结果对象，包含查询结果集。
     */
    @PostMapping("/list")
    public ResponseResult<MyPageData<DormFeeVo>> list(
            @MyRequestBody DormFeeDto dormFeeDtoFilter,
            @MyRequestBody MyOrderParam orderParam,
            @MyRequestBody MyPageParam pageParam) {
        if (pageParam != null) {
            PageMethod.startPage(pageParam.getPageNum(), pageParam.getPageSize());
        }
        DormFee dormFeeFilter = MyModelUtil.copyTo(dormFeeDtoFilter, DormFee.class);
        String orderBy = MyOrderParam.buildOrderBy(orderParam, DormFee.class);
        List<DormFee> dormFeeList = dormFeeService.getDormFeeListWithRelation(dormFeeFilter, orderBy);
        return ResponseResult.success(MyPageUtil.makeResponseData(dormFeeList, DormFee.INSTANCE));
    }

    /**
     * 查看指定住宿费信息对象详情。
     *
     * @param id 指定对象主键Id。
     * @return 应答结果对象，包含对象详情。
     */
    @GetMapping("/view")
    public ResponseResult<DormFeeVo> view(@RequestParam Long id) {
        DormFee dormFee = dormFeeService.getByIdWithRelation(id, MyRelationParam.full());
        if (dormFee == null) {
            return ResponseResult.error(ErrorCodeEnum.DATA_NOT_EXIST);
        }
        DormFeeVo dormFeeVo = DormFee.INSTANCE.fromModel(dormFee);
        return ResponseResult.success(dormFeeVo);
    }

    private ResponseResult<Void> doDelete(Long id) {
        String errorMessage;
        // 验证关联Id的数据合法性
        DormFee originalDormFee = dormFeeService.getById(id);
        if (originalDormFee == null) {
            // NOTE: 修改下面方括号中的话述
            errorMessage = "数据验证失败，当前 [对象] 并不存在，请刷新后重试！";
            return ResponseResult.error(ErrorCodeEnum.DATA_NOT_EXIST, errorMessage);
        }
        if (!dormFeeService.remove(id)) {
            errorMessage = "数据操作失败，删除的对象不存在，请刷新后重试！";
            return ResponseResult.error(ErrorCodeEnum.DATA_NOT_EXIST, errorMessage);
        }
        return ResponseResult.success();
    }
}
