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
 * 宿舍信息操作控制器类。
 *
 * @author 云翼
 * @date 2023-02-21
 */
@Api(tags = "宿舍信息管理接口")
@Slf4j
@RestController
@RequestMapping("/admin/app/sysDorm")
public class SysDormController {

    @Autowired
    private SysDormService sysDormService;

    /**
     * 新增宿舍信息数据。
     *
     * @param sysDormDto 新增对象。
     * @return 应答结果对象，包含新增对象主键Id。
     */
    @ApiOperationSupport(ignoreParameters = {"sysDormDto.dormId"})
    @OperationLog(type = SysOperationLogType.ADD)
    @PostMapping("/add")
    public ResponseResult<Long> add(@MyRequestBody SysDormDto sysDormDto) {
        String errorMessage = MyCommonUtil.getModelValidationError(sysDormDto, false);
        if (errorMessage != null) {
            return ResponseResult.error(ErrorCodeEnum.DATA_VALIDATED_FAILED, errorMessage);
        }
        SysDorm sysDorm = MyModelUtil.copyTo(sysDormDto, SysDorm.class);
        sysDorm = sysDormService.saveNew(sysDorm);
        return ResponseResult.success(sysDorm.getDormId());
    }

    /**
     * 更新宿舍信息数据。
     *
     * @param sysDormDto 更新对象。
     * @return 应答结果对象。
     */
    @OperationLog(type = SysOperationLogType.UPDATE)
    @PostMapping("/update")
    public ResponseResult<Void> update(@MyRequestBody SysDormDto sysDormDto) {
        String errorMessage = MyCommonUtil.getModelValidationError(sysDormDto, true);
        if (errorMessage != null) {
            return ResponseResult.error(ErrorCodeEnum.DATA_VALIDATED_FAILED, errorMessage);
        }
        SysDorm sysDorm = MyModelUtil.copyTo(sysDormDto, SysDorm.class);
        SysDorm originalSysDorm = sysDormService.getById(sysDorm.getDormId());
        if (originalSysDorm == null) {
            // NOTE: 修改下面方括号中的话述
            errorMessage = "数据验证失败，当前 [数据] 并不存在，请刷新后重试！";
            return ResponseResult.error(ErrorCodeEnum.DATA_NOT_EXIST, errorMessage);
        }
        if (!sysDormService.update(sysDorm, originalSysDorm)) {
            return ResponseResult.error(ErrorCodeEnum.DATA_NOT_EXIST);
        }
        return ResponseResult.success();
    }

    /**
     * 删除宿舍信息数据。
     *
     * @param dormId 删除对象主键Id。
     * @return 应答结果对象。
     */
    @OperationLog(type = SysOperationLogType.DELETE)
    @PostMapping("/delete")
    public ResponseResult<Void> delete(@MyRequestBody Long dormId) {
        if (MyCommonUtil.existBlankArgument(dormId)) {
            return ResponseResult.error(ErrorCodeEnum.ARGUMENT_NULL_EXIST);
        }
        return this.doDelete(dormId);
    }

    /**
     * 列出符合过滤条件的宿舍信息列表。
     *
     * @param sysDormDtoFilter 过滤对象。
     * @param orderParam 排序参数。
     * @param pageParam 分页参数。
     * @return 应答结果对象，包含查询结果集。
     */
    @PostMapping("/list")
    public ResponseResult<MyPageData<SysDormVo>> list(
            @MyRequestBody SysDormDto sysDormDtoFilter,
            @MyRequestBody MyOrderParam orderParam,
            @MyRequestBody MyPageParam pageParam) {
        if (pageParam != null) {
            PageMethod.startPage(pageParam.getPageNum(), pageParam.getPageSize());
        }
        SysDorm sysDormFilter = MyModelUtil.copyTo(sysDormDtoFilter, SysDorm.class);
        String orderBy = MyOrderParam.buildOrderBy(orderParam, SysDorm.class);
        List<SysDorm> sysDormList = sysDormService.getSysDormListWithRelation(sysDormFilter, orderBy);
        return ResponseResult.success(MyPageUtil.makeResponseData(sysDormList, SysDorm.INSTANCE));
    }

    /**
     * 查看指定宿舍信息对象详情。
     *
     * @param dormId 指定对象主键Id。
     * @return 应答结果对象，包含对象详情。
     */
    @GetMapping("/view")
    public ResponseResult<SysDormVo> view(@RequestParam Long dormId) {
        SysDorm sysDorm = sysDormService.getByIdWithRelation(dormId, MyRelationParam.full());
        if (sysDorm == null) {
            return ResponseResult.error(ErrorCodeEnum.DATA_NOT_EXIST);
        }
        SysDormVo sysDormVo = SysDorm.INSTANCE.fromModel(sysDorm);
        return ResponseResult.success(sysDormVo);
    }

    private ResponseResult<Void> doDelete(Long dormId) {
        String errorMessage;
        // 验证关联Id的数据合法性
        SysDorm originalSysDorm = sysDormService.getById(dormId);
        if (originalSysDorm == null) {
            // NOTE: 修改下面方括号中的话述
            errorMessage = "数据验证失败，当前 [对象] 并不存在，请刷新后重试！";
            return ResponseResult.error(ErrorCodeEnum.DATA_NOT_EXIST, errorMessage);
        }
        if (!sysDormService.remove(dormId)) {
            errorMessage = "数据操作失败，删除的对象不存在，请刷新后重试！";
            return ResponseResult.error(ErrorCodeEnum.DATA_NOT_EXIST, errorMessage);
        }
        return ResponseResult.success();
    }
}
