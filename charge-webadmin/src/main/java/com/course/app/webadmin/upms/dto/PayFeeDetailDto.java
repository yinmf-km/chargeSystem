package com.course.app.webadmin.upms.dto;

import java.util.Date;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import com.course.app.common.core.validator.UpdateGroup;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * PayFeeDetailDto对象。
 * @author 云翼
 * @date 2023-02-21
 */
@ApiModel("PayFeeDetailDto对象")
@Data
public class PayFeeDetailDto {

	/**
	 * 交易编号。
	 */
	@ApiModelProperty(value = "交易编号", required = true)
	@NotBlank(message = "数据验证失败，交易编号不能为空！", groups = { UpdateGroup.class })
	private String payMerTranNo;
	/**
	 * 缴费学生。
	 */
	@ApiModelProperty(value = "缴费学生", required = true)
	@NotNull(message = "数据验证失败，缴费学生不能为空！")
	private Long studentId;
	/**
	 * 缴费学生绑定手机号码。
	 */
	@ApiModelProperty(value = "缴费学生绑定手机号码")
	private String phoneNum;
	/**
	 * 缴费帐户。
	 */
	@ApiModelProperty(value = "缴费帐户")
	private String payAcct;
	/**
	 * 缴费金额。
	 */
	@ApiModelProperty(value = "缴费金额")
	private String tradeAmount;
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
	 * 买家实付金额。
	 */
	@ApiModelProperty(value = "买家实付金额")
	private String buyerPayAmount;
	/**
	 * 商户订单总金额。
	 */
	@ApiModelProperty(value = "商户订单总金额")
	private String totalAmount;
	/**
	 * 商户已退款金额。
	 */
	@ApiModelProperty(value = "商户已退款金额")
	private String refundedAmt;
	/**
	 * 交易内容。
	 */
	@ApiModelProperty(value = "交易内容")
	private String tranContent;
	/**
	 * 支付优惠金额。
	 */
	@ApiModelProperty(value = "支付优惠金额")
	private String payDsctAmount;
	/**
	 * 交易状态(P:处理中 F:失败 S:成功)。
	 */
	@ApiModelProperty(value = "交易状态(P:处理中 F:失败 S:成功)")
	private String tranStatus;
	/**
	 * 订单状态(INITIAL:初始化 PAIED:交易成功 WAITPAY:等待支付 REFUNDED:部分退款 REFUNDALL:全部退款 CLOSED:订单关闭)。
	 */
	@ApiModelProperty(value = "订单状态(INITIAL:初始化 PAIED:交易成功 WAITPAY:等待支付 REFUNDED:部分退款 REFUNDALL:全部退款 CLOSED:订单关闭)")
	private String orderStatus;
}
