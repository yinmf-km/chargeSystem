package com.course.app.webadmin.upms.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.course.app.common.core.annotation.MyRequestBody;
import com.course.app.common.core.constant.ErrorCodeEnum;
import com.course.app.common.core.object.MyOrderParam;
import com.course.app.common.core.object.MyPageData;
import com.course.app.common.core.object.MyPageParam;
import com.course.app.common.core.object.MyRelationParam;
import com.course.app.common.core.object.ResponseResult;
import com.course.app.common.core.util.MyCommonUtil;
import com.course.app.common.core.util.MyModelUtil;
import com.course.app.common.core.util.MyPageUtil;
import com.course.app.common.log.annotation.OperationLog;
import com.course.app.common.log.model.constant.SysOperationLogType;
import com.course.app.webadmin.upms.dto.CollegeScoreDto;
import com.course.app.webadmin.upms.model.CollegeScore;
import com.course.app.webadmin.upms.service.CollegeScoreService;
import com.course.app.webadmin.upms.vo.CollegeScoreVo;
import com.github.pagehelper.page.PageMethod;
import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;

import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;

/**
 * 高考成绩信息操作控制器类。
 * @author 云翼
 * @date 2023-02-21
 */
@Api(tags = "高考成绩信息管理接口")
@Slf4j
@RestController
@RequestMapping("/admin/app/collegeScore")
public class CollegeScoreController {

	@Autowired
	private CollegeScoreService collegeScoreService;

	/**
	 * 新增高考成绩信息数据。
	 * @param collegeScoreDto 新增对象。
	 * @return 应答结果对象，包含新增对象主键Id。
	 */
	@ApiOperationSupport(ignoreParameters = { "collegeScoreDto.id" })
	@OperationLog(type = SysOperationLogType.ADD)
	@PostMapping("/add")
	public ResponseResult<Long> add(@MyRequestBody CollegeScoreDto collegeScoreDto) {
		String errorMessage = MyCommonUtil.getModelValidationError(collegeScoreDto, false);
		if (errorMessage != null) {
			return ResponseResult.error(ErrorCodeEnum.DATA_VALIDATED_FAILED, errorMessage);
		}
		CollegeScore collegeScore = MyModelUtil.copyTo(collegeScoreDto, CollegeScore.class);
		collegeScore = collegeScoreService.saveNew(collegeScore);
		return ResponseResult.success(collegeScore.getId());
	}

	/**
	 * 更新高考成绩信息数据。
	 * @param collegeScoreDto 更新对象。
	 * @return 应答结果对象。
	 */
	@OperationLog(type = SysOperationLogType.UPDATE)
	@PostMapping("/update")
	public ResponseResult<Void> update(@MyRequestBody CollegeScoreDto collegeScoreDto) {
		String errorMessage = MyCommonUtil.getModelValidationError(collegeScoreDto, true);
		if (errorMessage != null) {
			return ResponseResult.error(ErrorCodeEnum.DATA_VALIDATED_FAILED, errorMessage);
		}
		CollegeScore collegeScore = MyModelUtil.copyTo(collegeScoreDto, CollegeScore.class);
		CollegeScore originalCollegeScore = collegeScoreService.getById(collegeScore.getId());
		if (originalCollegeScore == null) {
			// NOTE: 修改下面方括号中的话述
			errorMessage = "数据验证失败，当前 [数据] 并不存在，请刷新后重试！";
			return ResponseResult.error(ErrorCodeEnum.DATA_NOT_EXIST, errorMessage);
		}
		if (!collegeScoreService.update(collegeScore, originalCollegeScore)) {
			return ResponseResult.error(ErrorCodeEnum.DATA_NOT_EXIST);
		}
		return ResponseResult.success();
	}

	/**
	 * 删除高考成绩信息数据。
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
	 * 列出符合过滤条件的高考成绩信息列表。
	 * @param collegeScoreDtoFilter 过滤对象。
	 * @param orderParam 排序参数。
	 * @param pageParam 分页参数。
	 * @return 应答结果对象，包含查询结果集。
	 */
	@PostMapping("/list")
	public ResponseResult<MyPageData<CollegeScoreVo>> list(@MyRequestBody CollegeScoreDto collegeScoreDtoFilter,
			@MyRequestBody MyOrderParam orderParam, @MyRequestBody MyPageParam pageParam) {
		if (pageParam != null) {
			PageMethod.startPage(pageParam.getPageNum(), pageParam.getPageSize());
		}
		CollegeScore collegeScoreFilter = MyModelUtil.copyTo(collegeScoreDtoFilter, CollegeScore.class);
		String orderBy = MyOrderParam.buildOrderBy(orderParam, CollegeScore.class);
		List<CollegeScore> collegeScoreList = collegeScoreService.getCollegeScoreListWithRelation(collegeScoreFilter,
				orderBy);
		return ResponseResult.success(MyPageUtil.makeResponseData(collegeScoreList, CollegeScore.INSTANCE));
	}

	/**
	 * 查看指定高考成绩信息对象详情。
	 * @param id 指定对象主键Id。
	 * @return 应答结果对象，包含对象详情。
	 */
	@GetMapping("/view")
	public ResponseResult<CollegeScoreVo> view(@RequestParam Long id) {
		CollegeScore collegeScore = collegeScoreService.getByIdWithRelation(id, MyRelationParam.full());
		if (collegeScore == null) {
			return ResponseResult.error(ErrorCodeEnum.DATA_NOT_EXIST);
		}
		CollegeScoreVo collegeScoreVo = CollegeScore.INSTANCE.fromModel(collegeScore);
		return ResponseResult.success(collegeScoreVo);
	}

	private ResponseResult<Void> doDelete(Long id) {
		String errorMessage;
		// 验证关联Id的数据合法性
		CollegeScore originalCollegeScore = collegeScoreService.getById(id);
		if (originalCollegeScore == null) {
			// NOTE: 修改下面方括号中的话述
			errorMessage = "数据验证失败，当前 [对象] 并不存在，请刷新后重试！";
			return ResponseResult.error(ErrorCodeEnum.DATA_NOT_EXIST, errorMessage);
		}
		if (!collegeScoreService.remove(id)) {
			errorMessage = "数据操作失败，删除的对象不存在，请刷新后重试！";
			return ResponseResult.error(ErrorCodeEnum.DATA_NOT_EXIST, errorMessage);
		}
		return ResponseResult.success();
	}
}
