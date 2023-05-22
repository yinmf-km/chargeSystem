package com.course.app.webadmin.upms.dto;

import java.util.Date;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import com.course.app.common.core.validator.UpdateGroup;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * RefundFeeDetailDto对象。
 * @author 云翼
 * @date 2023-02-21
 */
@ApiModel("RefundFeeDetailDto对象")
@Data
public class RefundFeeDetailDto {

	/**
	 * 商户退款交易编号。
	 */
	@ApiModelProperty(value = "商户退款交易编号", required = true)
	@NotBlank(message = "数据验证失败，商户退款交易编号不能为空！", groups = { UpdateGroup.class })
	private String refundMerTranNo;
	/**
	 * 退款学生。
	 */
	@ApiModelProperty(value = "退款学生", required = true)
	@NotNull(message = "数据验证失败，退款学生不能为空！")
	private Long studentId;
	/**
	 * 退款学生绑定手机号码。
	 */
	@ApiModelProperty(value = "退款学生绑定手机号码")
	private String phoneNum;
	/**
	 * 退款帐户。
	 */
	@ApiModelProperty(value = "退款帐户")
	private String refundAcct;
	/**
	 * 退款金额。
	 */
	@ApiModelProperty(value = "退款金额", required = true)
	@NotBlank(message = "数据验证失败，退款金额不能为空！")
	private String amount;
	/**
	 * 商户编号。
	 */
	@ApiModelProperty(value = "商户编号", required = true)
	@NotBlank(message = "数据验证失败，商户编号不能为空！")
	private String merPtcId;
	/**
	 * 交易时间。
	 */
	@ApiModelProperty(value = "交易时间", required = true)
	@NotNull(message = "数据验证失败，交易时间不能为空！")
	private Date transTime;
	/**
	 * 交易场景。
	 */
	@ApiModelProperty(value = "交易场景", required = true)
	@NotBlank(message = "数据验证失败，交易场景不能为空！")
	private String tranScene;
	/**
	 * 币种CNY。
	 */
	@ApiModelProperty(value = "币种CNY", required = true)
	@NotBlank(message = "数据验证失败，币种CNY不能为空！")
	private String currency;
	/**
	 * 商户侧退款时间。
	 */
	@ApiModelProperty(value = "商户侧退款时间")
	private Date merRefundTime;
	/**
	 * 退款单据号。
	 */
	@ApiModelProperty(value = "退款单据号")
	private String refundOrderNo;
	/**
	 * 合计已退款金额。
	 */
	@ApiModelProperty(value = "合计已退款金额")
	private String doneRefundAmount;
	/**
	 * 该笔退款对应的交易的订单金额。
	 */
	@ApiModelProperty(value = "该笔退款对应的交易的订单金额")
	private String totalAmount;
	/**
	 * 本次退款请求对应的退款金额。
	 */
	@ApiModelProperty(value = "本次退款请求对应的退款金额")
	private String refundAmount;
	/**
	 * 退款支付优惠金额。
	 */
	@ApiModelProperty(value = "退款支付优惠金额")
	private String payDsctAmount;
	/**
	 * 交易内容。
	 */
	@ApiModelProperty(value = "交易内容")
	private String tranContent;
	/**
	 * 交易状态(P:处理中 F:失败 S:成功)。
	 */
	@ApiModelProperty(value = "交易状态(P:处理中 F:失败 S:成功)")
	private String refundStatus;
	/**
	 * 订单状态(INITIAL:初始化 PAIED:交易成功 WAITPAY:等待支付 REFUNDED:部分退款 REFUNDALL:全部退款 CLOSED:订单关闭)。
	 */
	@ApiModelProperty(value = "订单状态(INITIAL:初始化 PAIED:交易成功 WAITPAY:等待支付 REFUNDED:部分退款 REFUNDALL:全部退款 CLOSED:订单关闭)")
	private String orderStatus;
}
