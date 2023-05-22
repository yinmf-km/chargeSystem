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
 * 学费信息操作控制器类。
 *
 * @author 云翼
 * @date 2023-02-21
 */
@Api(tags = "学费信息管理接口")
@Slf4j
@RestController
@RequestMapping("/admin/app/schoolFee")
public class SchoolFeeController {

    @Autowired
    private SchoolFeeService schoolFeeService;

    /**
     * 新增学费信息数据。
     *
     * @param schoolFeeDto 新增对象。
     * @return 应答结果对象，包含新增对象主键Id。
     */
    @ApiOperationSupport(ignoreParameters = {"schoolFeeDto.id"})
    @OperationLog(type = SysOperationLogType.ADD)
    @PostMapping("/add")
    public ResponseResult<Long> add(@MyRequestBody SchoolFeeDto schoolFeeDto) {
        String errorMessage = MyCommonUtil.getModelValidationError(schoolFeeDto, false);
        if (errorMessage != null) {
            return ResponseResult.error(ErrorCodeEnum.DATA_VALIDATED_FAILED, errorMessage);
        }
        SchoolFee schoolFee = MyModelUtil.copyTo(schoolFeeDto, SchoolFee.class);
        schoolFee = schoolFeeService.saveNew(schoolFee);
        return ResponseResult.success(schoolFee.getId());
    }

    /**
     * 更新学费信息数据。
     *
     * @param schoolFeeDto 更新对象。
     * @return 应答结果对象。
     */
    @OperationLog(type = SysOperationLogType.UPDATE)
    @PostMapping("/update")
    public ResponseResult<Void> update(@MyRequestBody SchoolFeeDto schoolFeeDto) {
        String errorMessage = MyCommonUtil.getModelValidationError(schoolFeeDto, true);
        if (errorMessage != null) {
            return ResponseResult.error(ErrorCodeEnum.DATA_VALIDATED_FAILED, errorMessage);
        }
        SchoolFee schoolFee = MyModelUtil.copyTo(schoolFeeDto, SchoolFee.class);
        SchoolFee originalSchoolFee = schoolFeeService.getById(schoolFee.getId());
        if (originalSchoolFee == null) {
            // NOTE: 修改下面方括号中的话述
            errorMessage = "数据验证失败，当前 [数据] 并不存在，请刷新后重试！";
            return ResponseResult.error(ErrorCodeEnum.DATA_NOT_EXIST, errorMessage);
        }
        if (!schoolFeeService.update(schoolFee, originalSchoolFee)) {
            return ResponseResult.error(ErrorCodeEnum.DATA_NOT_EXIST);
        }
        return ResponseResult.success();
    }

    /**
     * 删除学费信息数据。
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
     * 列出符合过滤条件的学费信息列表。
     *
     * @param schoolFeeDtoFilter 过滤对象。
     * @param orderParam 排序参数。
     * @param pageParam 分页参数。
     * @return 应答结果对象，包含查询结果集。
     */
    @PostMapping("/list")
    public ResponseResult<MyPageData<SchoolFeeVo>> list(
            @MyRequestBody SchoolFeeDto schoolFeeDtoFilter,
            @MyRequestBody MyOrderParam orderParam,
            @MyRequestBody MyPageParam pageParam) {
        if (pageParam != null) {
            PageMethod.startPage(pageParam.getPageNum(), pageParam.getPageSize());
        }
        SchoolFee schoolFeeFilter = MyModelUtil.copyTo(schoolFeeDtoFilter, SchoolFee.class);
        String orderBy = MyOrderParam.buildOrderBy(orderParam, SchoolFee.class);
        List<SchoolFee> schoolFeeList = schoolFeeService.getSchoolFeeListWithRelation(schoolFeeFilter, orderBy);
        return ResponseResult.success(MyPageUtil.makeResponseData(schoolFeeList, SchoolFee.INSTANCE));
    }

    /**
     * 查看指定学费信息对象详情。
     *
     * @param id 指定对象主键Id。
     * @return 应答结果对象，包含对象详情。
     */
    @GetMapping("/view")
    public ResponseResult<SchoolFeeVo> view(@RequestParam Long id) {
        SchoolFee schoolFee = schoolFeeService.getByIdWithRelation(id, MyRelationParam.full());
        if (schoolFee == null) {
            return ResponseResult.error(ErrorCodeEnum.DATA_NOT_EXIST);
        }
        SchoolFeeVo schoolFeeVo = SchoolFee.INSTANCE.fromModel(schoolFee);
        return ResponseResult.success(schoolFeeVo);
    }

    private ResponseResult<Void> doDelete(Long id) {
        String errorMessage;
        // 验证关联Id的数据合法性
        SchoolFee originalSchoolFee = schoolFeeService.getById(id);
        if (originalSchoolFee == null) {
            // NOTE: 修改下面方括号中的话述
            errorMessage = "数据验证失败，当前 [对象] 并不存在，请刷新后重试！";
            return ResponseResult.error(ErrorCodeEnum.DATA_NOT_EXIST, errorMessage);
        }
        if (!schoolFeeService.remove(id)) {
            errorMessage = "数据操作失败，删除的对象不存在，请刷新后重试！";
            return ResponseResult.error(ErrorCodeEnum.DATA_NOT_EXIST, errorMessage);
        }
        return ResponseResult.success();
    }
}
