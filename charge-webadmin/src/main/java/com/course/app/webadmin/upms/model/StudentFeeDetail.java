package com.course.app.webadmin.upms.model;

import java.io.Serializable;
import java.sql.Date;

import org.apache.commons.lang3.StringUtils;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@TableName("student_fee_detail")
public class StudentFeeDetail implements Serializable {

	private static final long serialVersionUID = 1L;
	/**
	 * 费用主键ID
	 */
	@TableId(value = "fee_id", type = IdType.INPUT)
	@JsonFormat(shape = JsonFormat.Shape.STRING)
	private Long feeId;
	/**
	 * 缴费学生
	 */
	@TableField("student_id")
	private Long studentId;
	/**
	 * 缴费金额
	 */
	@TableField("fee_amount")
	private String feeAmount;
	/**
	 * 缴费截至时间
	 */
	@TableField("fee_final_time")
	private Date feeFinalTime;
	/**
	 * 费用类型（学费、学杂费）
	 */
	@TableField("fee_type")
	private String feeType;
	/**
	 * 缴费归属学年
	 */
	@TableField("fee_attr_year")
	private Integer feeAttrYear;
	/**
	 * 状态(0:待缴费 1:已缴费)
	 */
	@TableField("fee_status")
	private Integer feeStatus;
	/**
	 * 创建时间
	 */
	@TableField("create_time")
	private Date createTime;

	public static String convertFeeStatus(Integer feeStatus) {
		switch (feeStatus) {
		case 0:
			return "待缴费";
		case 1:
			return "已缴费";
		default:
			return StringUtils.EMPTY;
		}
	}
}
