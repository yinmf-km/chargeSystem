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
 * 学费减免明细操作控制器类。
 *
 * @author 云翼
 * @date 2023-02-21
 */
@Api(tags = "学费减免明细管理接口")
@Slf4j
@RestController
@RequestMapping("/admin/app/schoolFeeFreeDetail")
public class SchoolFeeFreeDetailController {

    @Autowired
    private SchoolFeeFreeDetailService schoolFeeFreeDetailService;

    /**
     * 新增学费减免明细数据。
     *
     * @param schoolFeeFreeDetailDto 新增对象。
     * @return 应答结果对象，包含新增对象主键Id。
     */
    @ApiOperationSupport(ignoreParameters = {"schoolFeeFreeDetailDto.id"})
    @OperationLog(type = SysOperationLogType.ADD)
    @PostMapping("/add")
    public ResponseResult<Long> add(@MyRequestBody SchoolFeeFreeDetailDto schoolFeeFreeDetailDto) {
        String errorMessage = MyCommonUtil.getModelValidationError(schoolFeeFreeDetailDto, false);
        if (errorMessage != null) {
            return ResponseResult.error(ErrorCodeEnum.DATA_VALIDATED_FAILED, errorMessage);
        }
        SchoolFeeFreeDetail schoolFeeFreeDetail = MyModelUtil.copyTo(schoolFeeFreeDetailDto, SchoolFeeFreeDetail.class);
        schoolFeeFreeDetail = schoolFeeFreeDetailService.saveNew(schoolFeeFreeDetail);
        return ResponseResult.success(schoolFeeFreeDetail.getId());
    }

    /**
     * 更新学费减免明细数据。
     *
     * @param schoolFeeFreeDetailDto 更新对象。
     * @return 应答结果对象。
     */
    @OperationLog(type = SysOperationLogType.UPDATE)
    @PostMapping("/update")
    public ResponseResult<Void> update(@MyRequestBody SchoolFeeFreeDetailDto schoolFeeFreeDetailDto) {
        String errorMessage = MyCommonUtil.getModelValidationError(schoolFeeFreeDetailDto, true);
        if (errorMessage != null) {
            return ResponseResult.error(ErrorCodeEnum.DATA_VALIDATED_FAILED, errorMessage);
        }
        SchoolFeeFreeDetail schoolFeeFreeDetail = MyModelUtil.copyTo(schoolFeeFreeDetailDto, SchoolFeeFreeDetail.class);
        SchoolFeeFreeDetail originalSchoolFeeFreeDetail = schoolFeeFreeDetailService.getById(schoolFeeFreeDetail.getId());
        if (originalSchoolFeeFreeDetail == null) {
            // NOTE: 修改下面方括号中的话述
            errorMessage = "数据验证失败，当前 [数据] 并不存在，请刷新后重试！";
            return ResponseResult.error(ErrorCodeEnum.DATA_NOT_EXIST, errorMessage);
        }
        if (!schoolFeeFreeDetailService.update(schoolFeeFreeDetail, originalSchoolFeeFreeDetail)) {
            return ResponseResult.error(ErrorCodeEnum.DATA_NOT_EXIST);
        }
        return ResponseResult.success();
    }

    /**
     * 删除学费减免明细数据。
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
     * 列出符合过滤条件的学费减免明细列表。
     *
     * @param schoolFeeFreeDetailDtoFilter 过滤对象。
     * @param orderParam 排序参数。
     * @param pageParam 分页参数。
     * @return 应答结果对象，包含查询结果集。
     */
    @PostMapping("/list")
    public ResponseResult<MyPageData<SchoolFeeFreeDetailVo>> list(
            @MyRequestBody SchoolFeeFreeDetailDto schoolFeeFreeDetailDtoFilter,
            @MyRequestBody MyOrderParam orderParam,
            @MyRequestBody MyPageParam pageParam) {
        if (pageParam != null) {
            PageMethod.startPage(pageParam.getPageNum(), pageParam.getPageSize());
        }
        SchoolFeeFreeDetail schoolFeeFreeDetailFilter = MyModelUtil.copyTo(schoolFeeFreeDetailDtoFilter, SchoolFeeFreeDetail.class);
        String orderBy = MyOrderParam.buildOrderBy(orderParam, SchoolFeeFreeDetail.class);
        List<SchoolFeeFreeDetail> schoolFeeFreeDetailList =
                schoolFeeFreeDetailService.getSchoolFeeFreeDetailListWithRelation(schoolFeeFreeDetailFilter, orderBy);
        return ResponseResult.success(MyPageUtil.makeResponseData(schoolFeeFreeDetailList, SchoolFeeFreeDetail.INSTANCE));
    }

    /**
     * 查看指定学费减免明细对象详情。
     *
     * @param id 指定对象主键Id。
     * @return 应答结果对象，包含对象详情。
     */
    @GetMapping("/view")
    public ResponseResult<SchoolFeeFreeDetailVo> view(@RequestParam Long id) {
        SchoolFeeFreeDetail schoolFeeFreeDetail = schoolFeeFreeDetailService.getByIdWithRelation(id, MyRelationParam.full());
        if (schoolFeeFreeDetail == null) {
            return ResponseResult.error(ErrorCodeEnum.DATA_NOT_EXIST);
        }
        SchoolFeeFreeDetailVo schoolFeeFreeDetailVo = SchoolFeeFreeDetail.INSTANCE.fromModel(schoolFeeFreeDetail);
        return ResponseResult.success(schoolFeeFreeDetailVo);
    }

    private ResponseResult<Void> doDelete(Long id) {
        String errorMessage;
        // 验证关联Id的数据合法性
        SchoolFeeFreeDetail originalSchoolFeeFreeDetail = schoolFeeFreeDetailService.getById(id);
        if (originalSchoolFeeFreeDetail == null) {
            // NOTE: 修改下面方括号中的话述
            errorMessage = "数据验证失败，当前 [对象] 并不存在，请刷新后重试！";
            return ResponseResult.error(ErrorCodeEnum.DATA_NOT_EXIST, errorMessage);
        }
        if (!schoolFeeFreeDetailService.remove(id)) {
            errorMessage = "数据操作失败，删除的对象不存在，请刷新后重试！";
            return ResponseResult.error(ErrorCodeEnum.DATA_NOT_EXIST, errorMessage);
        }
        return ResponseResult.success();
    }
}
