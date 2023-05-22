package com.course.app.webadmin.upms.model;

import java.io.Serializable;
import java.time.LocalDateTime;

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
@TableName("process_detail")
public class ProcessDetail implements Serializable {

	private static final long serialVersionUID = 1L;
	/**
	 * 流程主键ID
	 */
	@TableId(value = "process_id", type = IdType.INPUT)
	@JsonFormat(shape = JsonFormat.Shape.STRING)
	private Long processId;
	/**
	 * 业务关联主键ID，例如学生注册审核时传学生ID、学费减免传减免流水ID等
	 */
	@TableField("out_busi_id")
	private Long outBusiId;
	/**
	 * 节点步骤
	 */
	@TableField("process_step")
	private Integer processStep;
	/**
	 * 节点步骤名称
	 */
	@TableField("process_nm")
	private String processNm;
	/**
	 * 当前审批人员Id
	 */
	@TableField("approve_id")
	private Long approveId;
	/**
	 * 当前审批人员姓名
	 */
	@TableField("approve_name")
	private String approveName;
	/**
	 * 当前流程节点到达时间
	 */
	@TableField("approve_reach_time")
	private LocalDateTime approveReachTime;
	/**
	 * 当前流程节点审批完成时间
	 */
	@TableField("approve_finish_time")
	private LocalDateTime approveFinishTime;
	/**
	 * 审核状态(0:待审核 1:同意 2:拒绝)
	 */
	@TableField("process_flag")
	private Integer processFlag;
	/**
	 * 创建者Id
	 */
	@TableField("create_user_id")
	private Long createUserId;
	/**
	 * 创建时间
	 */
	@TableField("create_time")
	private LocalDateTime createTime;

	public static String convertProcessFlag(Integer processFlag) {
		switch (processFlag) {
		case 0:
			return "待审核";
		case 1:
			return "同意";
		case 2:
			return "拒绝";
		default:
			return "未知状态";
		}
	}
}
