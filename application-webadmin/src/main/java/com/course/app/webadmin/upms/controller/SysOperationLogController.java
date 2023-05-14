package com.course.app.webadmin.upms.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.course.app.common.core.annotation.MyRequestBody;
import com.course.app.common.core.object.MyOrderParam;
import com.course.app.common.core.object.MyPageData;
import com.course.app.common.core.object.MyPageParam;
import com.course.app.common.core.object.ResponseResult;
import com.course.app.common.core.util.MyModelUtil;
import com.course.app.common.core.util.MyPageUtil;
import com.course.app.common.log.dto.SysOperationLogDto;
import com.course.app.common.log.model.SysOperationLog;
import com.course.app.common.log.service.SysOperationLogService;
import com.course.app.common.log.vo.SysOperationLogVo;
import com.github.pagehelper.Page;
import com.github.pagehelper.page.PageMethod;

import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;

/**
 * 操作日志接口控制器对象。
 * @author 云翼
 * @date 2023-02-21
 */
@Api(tags = "操作日志接口")
@Slf4j
@RestController
@RequestMapping("/admin/upms/sysOperationLog")
public class SysOperationLogController {

	@Autowired
	private SysOperationLogService operationLogService;

	/**
	 * 数据权限列表。
	 * @param sysOperationLogDtoFilter 操作日志查询过滤对象。
	 * @param orderParam 排序参数。
	 * @param pageParam 分页参数。
	 * @return 应答结果对象。包含操作日志列表。
	 */
	@PostMapping("/list")
	public ResponseResult<MyPageData<SysOperationLogVo>> list(@MyRequestBody SysOperationLogDto sysOperationLogDtoFilter, @MyRequestBody MyOrderParam orderParam, @MyRequestBody MyPageParam pageParam) {
		if (pageParam != null) {
			PageMethod.startPage(pageParam.getPageNum(), pageParam.getPageSize());
		}
		SysOperationLog filter = MyModelUtil.copyTo(sysOperationLogDtoFilter, SysOperationLog.class);
		String orderBy = MyOrderParam.buildOrderBy(orderParam, SysOperationLog.class);
		List<SysOperationLog> operationLogList = operationLogService.getSysOperationLogList(filter, orderBy);
		List<SysOperationLogVo> operationLogVoList = MyModelUtil.copyCollectionTo(operationLogList, SysOperationLogVo.class);
		long totalCount = 0L;
		if (operationLogList instanceof Page) {
			totalCount = ((Page<SysOperationLog>) operationLogList).getTotal();
		}
		return ResponseResult.success(MyPageUtil.makeResponseData(operationLogVoList, totalCount));
	}
}
