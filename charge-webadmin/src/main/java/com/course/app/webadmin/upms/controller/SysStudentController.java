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
 * 学生信息操作控制器类。
 *
 * @author 云翼
 * @date 2023-02-21
 */
@Api(tags = "学生信息管理接口")
@Slf4j
@RestController
@RequestMapping("/admin/app/sysStudent")
public class SysStudentController {

    @Autowired
    private SysStudentService sysStudentService;

    /**
     * 新增学生信息数据。
     *
     * @param sysStudentDto 新增对象。
     * @return 应答结果对象，包含新增对象主键Id。
     */
    @ApiOperationSupport(ignoreParameters = {"sysStudentDto.studentId"})
    @OperationLog(type = SysOperationLogType.ADD)
    @PostMapping("/add")
    public ResponseResult<Long> add(@MyRequestBody SysStudentDto sysStudentDto) {
        String errorMessage = MyCommonUtil.getModelValidationError(sysStudentDto, false);
        if (errorMessage != null) {
            return ResponseResult.error(ErrorCodeEnum.DATA_VALIDATED_FAILED, errorMessage);
        }
        SysStudent sysStudent = MyModelUtil.copyTo(sysStudentDto, SysStudent.class);
        sysStudent = sysStudentService.saveNew(sysStudent);
        return ResponseResult.success(sysStudent.getStudentId());
    }

    /**
     * 更新学生信息数据。
     *
     * @param sysStudentDto 更新对象。
     * @return 应答结果对象。
     */
    @OperationLog(type = SysOperationLogType.UPDATE)
    @PostMapping("/update")
    public ResponseResult<Void> update(@MyRequestBody SysStudentDto sysStudentDto) {
        String errorMessage = MyCommonUtil.getModelValidationError(sysStudentDto, true);
        if (errorMessage != null) {
            return ResponseResult.error(ErrorCodeEnum.DATA_VALIDATED_FAILED, errorMessage);
        }
        SysStudent sysStudent = MyModelUtil.copyTo(sysStudentDto, SysStudent.class);
        SysStudent originalSysStudent = sysStudentService.getById(sysStudent.getStudentId());
        if (originalSysStudent == null) {
            // NOTE: 修改下面方括号中的话述
            errorMessage = "数据验证失败，当前 [数据] 并不存在，请刷新后重试！";
            return ResponseResult.error(ErrorCodeEnum.DATA_NOT_EXIST, errorMessage);
        }
        if (!sysStudentService.update(sysStudent, originalSysStudent)) {
            return ResponseResult.error(ErrorCodeEnum.DATA_NOT_EXIST);
        }
        return ResponseResult.success();
    }

    /**
     * 删除学生信息数据。
     *
     * @param studentId 删除对象主键Id。
     * @return 应答结果对象。
     */
    @OperationLog(type = SysOperationLogType.DELETE)
    @PostMapping("/delete")
    public ResponseResult<Void> delete(@MyRequestBody Long studentId) {
        if (MyCommonUtil.existBlankArgument(studentId)) {
            return ResponseResult.error(ErrorCodeEnum.ARGUMENT_NULL_EXIST);
        }
        return this.doDelete(studentId);
    }

    /**
     * 列出符合过滤条件的学生信息列表。
     *
     * @param sysStudentDtoFilter 过滤对象。
     * @param orderParam 排序参数。
     * @param pageParam 分页参数。
     * @return 应答结果对象，包含查询结果集。
     */
    @PostMapping("/list")
    public ResponseResult<MyPageData<SysStudentVo>> list(
            @MyRequestBody SysStudentDto sysStudentDtoFilter,
            @MyRequestBody MyOrderParam orderParam,
            @MyRequestBody MyPageParam pageParam) {
        if (pageParam != null) {
            PageMethod.startPage(pageParam.getPageNum(), pageParam.getPageSize());
        }
        SysStudent sysStudentFilter = MyModelUtil.copyTo(sysStudentDtoFilter, SysStudent.class);
        String orderBy = MyOrderParam.buildOrderBy(orderParam, SysStudent.class);
        List<SysStudent> sysStudentList = sysStudentService.getSysStudentListWithRelation(sysStudentFilter, orderBy);
        return ResponseResult.success(MyPageUtil.makeResponseData(sysStudentList, SysStudent.INSTANCE));
    }

    /**
     * 查看指定学生信息对象详情。
     *
     * @param studentId 指定对象主键Id。
     * @return 应答结果对象，包含对象详情。
     */
    @GetMapping("/view")
    public ResponseResult<SysStudentVo> view(@RequestParam Long studentId) {
        SysStudent sysStudent = sysStudentService.getByIdWithRelation(studentId, MyRelationParam.full());
        if (sysStudent == null) {
            return ResponseResult.error(ErrorCodeEnum.DATA_NOT_EXIST);
        }
        SysStudentVo sysStudentVo = SysStudent.INSTANCE.fromModel(sysStudent);
        return ResponseResult.success(sysStudentVo);
    }

    private ResponseResult<Void> doDelete(Long studentId) {
        String errorMessage;
        // 验证关联Id的数据合法性
        SysStudent originalSysStudent = sysStudentService.getById(studentId);
        if (originalSysStudent == null) {
            // NOTE: 修改下面方括号中的话述
            errorMessage = "数据验证失败，当前 [对象] 并不存在，请刷新后重试！";
            return ResponseResult.error(ErrorCodeEnum.DATA_NOT_EXIST, errorMessage);
        }
        if (!sysStudentService.remove(studentId)) {
            errorMessage = "数据操作失败，删除的对象不存在，请刷新后重试！";
            return ResponseResult.error(ErrorCodeEnum.DATA_NOT_EXIST, errorMessage);
        }
        return ResponseResult.success();
    }
}
