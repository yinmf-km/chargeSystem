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
import com.course.app.webadmin.upms.dto.DormFeeFreeDetailDto;
import com.course.app.webadmin.upms.model.DormFeeFreeDetail;
import com.course.app.webadmin.upms.service.DormFeeFreeDetailService;
import com.course.app.webadmin.upms.vo.DormFeeFreeDetailVo;
import com.github.pagehelper.page.PageMethod;

import io.swagger.annotations.Api;

/**
 * @Description 学费减免明细控制器类
 * @author yinmf
 * @Title SysDormController.java
 * @Package com.course.app.webadmin.upms.controller
 * @date 2023年5月3日 下午10:47:22
 * @version V1.0
 */
@Api(tags = "学费减免明细接口")
@RestController
@RequestMapping("/admin/upms/DormFeeFreeDetail")
public class DormFeeFreeDetailController {

	@Autowired
	private DormFeeFreeDetailService DormFeeFreeDetailService;
	@Autowired
	private IdGeneratorWrapper idGenerator;

	/**
	 * 学费减免新增
	 */
	@OperationLog(type = SysOperationLogType.ADD)
	@PostMapping("/add")
	public ResponseResult<Long> add(@MyRequestBody DormFeeFreeDetailDto dormFeeFreeDetailDto) {
		String errorMessage = MyCommonUtil.getModelValidationError(dormFeeFreeDetailDto, false);
		if (errorMessage != null) {
			return ResponseResult.error(ErrorCodeEnum.DATA_VALIDATED_FAILED, errorMessage);
		}
		DormFeeFreeDetail DormFeeFreeDetail = MyModelUtil.copyTo(dormFeeFreeDetailDto, DormFeeFreeDetail.class);
		DormFeeFreeDetail.setId(idGenerator.nextLongId());
		MyModelUtil.fillCommonsForInsert(DormFeeFreeDetail);
		DormFeeFreeDetailService.save(DormFeeFreeDetail);
		return ResponseResult.success(DormFeeFreeDetail.getId());
	}

	/**
	 * 学费减免查询
	 */
	@OperationLog(type = SysOperationLogType.LIST)
	@PostMapping("/list")
	public ResponseResult<MyPageData<DormFeeFreeDetailVo>> list(@MyRequestBody DormFeeFreeDetailDto dormFeeFreeDetailDtoFilter, @MyRequestBody MyPageParam pageParam) {
		if (pageParam != null) {
			PageMethod.startPage(pageParam.getPageNum(), pageParam.getPageSize());
		}
		DormFeeFreeDetail dormFeeFreeDetail = MyModelUtil.copyTo(dormFeeFreeDetailDtoFilter, DormFeeFreeDetail.class);
		List<DormFeeFreeDetail> dormFeeFreeDetails = DormFeeFreeDetailService.getListByFilter(dormFeeFreeDetail);
		return ResponseResult.success(MyPageUtil.makeResponseData(dormFeeFreeDetails, DormFeeFreeDetail.INSTANCE));
	}
}
