package com.course.app.webadmin.upms.dto;

import javax.validation.constraints.NotNull;

import com.course.app.common.core.validator.ConstDictRef;
import com.course.app.common.core.validator.UpdateGroup;
import com.course.app.webadmin.upms.model.constant.SysProcessFlag;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@ApiModel("DormFeeFreeDetailDto对象")
@Data
public class DormFeeFreeDetailDto {

	@ApiModelProperty(value = "主键Id",required = true)
	@NotNull(message = "主键Id不能为空！",groups = { UpdateGroup.class })
	private Long id;

	@ApiModelProperty(value = "学生ID",required = true)
	@NotNull(message = "学生ID不能为空！")
	private Long studentId;

	@ApiModelProperty(value = "审核状态(0:审核中 1:同意 2:不同意)",required = true)
	@NotNull(message = "审核状态不能为空！")
	@ConstDictRef(constDictClass = SysProcessFlag.class,message = "数据验证失败，审核状态(0:审核中 1:同意 2:不同意)为无效值！")
	private Integer processFlag;
}
