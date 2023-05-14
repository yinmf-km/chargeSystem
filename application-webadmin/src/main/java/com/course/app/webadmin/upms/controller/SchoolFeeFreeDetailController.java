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
import com.course.app.webadmin.upms.dto.SchoolFeeFreeDetailDto;
import com.course.app.webadmin.upms.model.SchoolFeeFreeDetail;
import com.course.app.webadmin.upms.service.SchoolFeeFreeDetailService;
import com.course.app.webadmin.upms.vo.SchoolFeeFreeDetailVo;
import com.github.pagehelper.page.PageMethod;

import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;

/**
 * @Description 学费减免明细控制器类
 * @author yinmf
 * @Title SysDormController.java
 * @Package com.course.app.webadmin.upms.controller
 * @date 2023年5月3日 下午10:47:22
 * @version V1.0
 */
@Api(tags = "学费减免明细接口")
@Slf4j
@RestController
@RequestMapping("/admin/upms/schoolFeeFreeDetail")
public class SchoolFeeFreeDetailController {

	@Autowired
	private SchoolFeeFreeDetailService schoolFeeFreeDetailService;
	@Autowired
	private IdGeneratorWrapper idGenerator;

	/**
	 * 学费减免新增
	 */
	@OperationLog(type = SysOperationLogType.ADD)
	@PostMapping("/add")
	public ResponseResult<Long> add(@MyRequestBody SchoolFeeFreeDetailDto schoolFeeFreeDetailDto) {
		String errorMessage = MyCommonUtil.getModelValidationError(schoolFeeFreeDetailDto, false);
		if (errorMessage != null) {
			return ResponseResult.error(ErrorCodeEnum.DATA_VALIDATED_FAILED, errorMessage);
		}
		SchoolFeeFreeDetail schoolFeeFreeDetail = MyModelUtil.copyTo(schoolFeeFreeDetailDto, SchoolFeeFreeDetail.class);
		schoolFeeFreeDetail.setId(idGenerator.nextLongId());
		MyModelUtil.fillCommonsForInsert(schoolFeeFreeDetail);
		schoolFeeFreeDetailService.save(schoolFeeFreeDetail);
		return ResponseResult.success(schoolFeeFreeDetail.getId());
	}

	/**
	 * 学费减免查询
	 */
	@OperationLog(type = SysOperationLogType.LIST)
	@PostMapping("/list")
	public ResponseResult<MyPageData<SchoolFeeFreeDetailVo>> list(@MyRequestBody SchoolFeeFreeDetailDto schoolFeeFreeDetailDtoFilter, @MyRequestBody MyPageParam pageParam) {
		if (pageParam != null) {
			PageMethod.startPage(pageParam.getPageNum(), pageParam.getPageSize());
		}
		SchoolFeeFreeDetail schoolFeeFreeDetail = MyModelUtil.copyTo(schoolFeeFreeDetailDtoFilter, SchoolFeeFreeDetail.class);
		List<SchoolFeeFreeDetail> schoolFeeFreeDetails = schoolFeeFreeDetailService.getListByFilter(schoolFeeFreeDetail);
		return ResponseResult.success(MyPageUtil.makeResponseData(schoolFeeFreeDetails, SchoolFeeFreeDetail.INSTANCE));
	}
}
