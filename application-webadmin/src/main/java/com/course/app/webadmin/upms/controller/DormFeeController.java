package com.course.app.webadmin.upms.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.course.app.common.core.annotation.MyRequestBody;
import com.course.app.common.core.constant.ErrorCodeEnum;
import com.course.app.common.core.object.MyPageData;
import com.course.app.common.core.object.MyPageParam;
import com.course.app.common.core.object.ResponseResult;
import com.course.app.common.core.util.MyCommonUtil;
import com.course.app.common.core.util.MyModelUtil;
import com.course.app.common.core.util.MyPageUtil;
import com.course.app.common.log.annotation.OperationLog;
import com.course.app.common.log.model.constant.SysOperationLogType;
import com.course.app.common.sequence.wrapper.IdGeneratorWrapper;
import com.course.app.webadmin.upms.dto.DormFeeDto;
import com.course.app.webadmin.upms.model.DormFee;
import com.course.app.webadmin.upms.service.DormFeeService;
import com.course.app.webadmin.upms.vo.DormFeeVo;
import com.github.pagehelper.page.PageMethod;

import io.swagger.annotations.Api;

/**
 * @Description 住宿费信息管理控制器类
 * @author yinmf
 * @Title SysDormController.java
 * @Package com.course.app.webadmin.upms.controller
 * @date 2023年5月3日 下午10:47:22
 * @version V1.0
 */
@Api(tags = "住宿费信息管理接口")
@RestController
@RequestMapping("/admin/upms/dormFee")
public class DormFeeController {

    @Autowired
    private DormFeeService dormFeeService;
    @Autowired
    private IdGeneratorWrapper idGenerator;

    /**
     * 住宿费信息新增
     */
    @OperationLog(type = SysOperationLogType.ADD)
    @PostMapping("/add")
    public ResponseResult<Long> add(@MyRequestBody DormFeeDto dormFeeDto) {
        String errorMessage = MyCommonUtil.getModelValidationError(dormFeeDto, false);
        if (errorMessage != null) {
            return ResponseResult.error(ErrorCodeEnum.DATA_VALIDATED_FAILED, errorMessage);
        }
        DormFee dormFee = MyModelUtil.copyTo(dormFeeDto, DormFee.class);
        dormFee.setId(idGenerator.nextLongId());
        MyModelUtil.fillCommonsForInsert(dormFee);
        dormFeeService.save(dormFee);
        return ResponseResult.success(dormFee.getId());
    }

    /**
     * 住宿费信息修改
     */
    @OperationLog(type = SysOperationLogType.UPDATE)
    @PostMapping("/update")
    public ResponseResult<Void> update(@MyRequestBody DormFeeDto dormFeeDto) {
        String errorMessage = MyCommonUtil.getModelValidationError(dormFeeDto, false);
        if (errorMessage != null) {
            return ResponseResult.error(ErrorCodeEnum.DATA_VALIDATED_FAILED, errorMessage);
        }
        if (null == dormFeeService.getById(dormFeeDto.getId())) {
            return ResponseResult.error(ErrorCodeEnum.DATA_NOT_EXIST);
        }
        DormFee dormFeeNew = MyModelUtil.copyTo(dormFeeDto, DormFee.class);
        MyModelUtil.fillCommonsForInsert(dormFeeNew);
        dormFeeService.updateById(dormFeeNew);
        return ResponseResult.success();
    }

    /**
     * 住宿费信息删除
     */
    @OperationLog(type = SysOperationLogType.DELETE)
    @PostMapping("/delete")
    public ResponseResult<Void> delete(@MyRequestBody Long id) {
        if (MyCommonUtil.existBlankArgument(id)) {
            return ResponseResult.error(ErrorCodeEnum.ARGUMENT_NULL_EXIST);
        }
        if (null == dormFeeService.getById(id)) {
            return ResponseResult.error(ErrorCodeEnum.DATA_NOT_EXIST);
        }
        dormFeeService.removeById(id);
        return ResponseResult.success();
    }

    /**
     * 住宿费信息查询
     */
    @OperationLog(type = SysOperationLogType.LIST)
    @PostMapping("/list")
    public ResponseResult<MyPageData<DormFeeVo>> list(@MyRequestBody DormFeeDto dormFeeDtoFilter,
        @MyRequestBody MyPageParam pageParam) {
        if (pageParam != null) {
            PageMethod.startPage(pageParam.getPageNum(), pageParam.getPageSize());
        }
        DormFee dormFee = MyModelUtil.copyTo(dormFeeDtoFilter, DormFee.class);
        List<DormFee> dormFees = dormFeeService.getListByFilter(dormFee);
        return ResponseResult.success(MyPageUtil.makeResponseData(dormFees, DormFee.INSTANCE));
    }
}
