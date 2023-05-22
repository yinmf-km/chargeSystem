package com.course.app.webadmin.upms.model;

import java.util.Date;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.course.app.common.core.base.mapper.BaseModelMapper;
import com.course.app.common.core.base.model.BaseModel;
import com.course.app.webadmin.upms.vo.PayFeeDetailVo;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * PayFeeDetail实体对象。
 * @author 云翼
 * @date 2023-02-21
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName(value = "pay_fee_detail")
public class PayFeeDetail extends BaseModel {

	/**
	 * 交易编号。
	 */
	@TableId(value = "pay_mer_tran_no")
	private String payMerTranNo;
	/**
	 * 缴费学生。
	 */
	private Long studentId;
	/**
	 * 缴费学生绑定手机号码。
	 */
	private String phoneNum;
	/**
	 * 缴费帐户。
	 */
	private String payAcct;
	/**
	 * 缴费金额。
	 */
	private String tradeAmount;
	/**
	 * 商户编号。
	 */
	private String merPtcId;
	/**
	 * 交易时间。
	 */
	private Date transTime;
	/**
	 * 交易场景。
	 */
	private String tranScene;
	/**
	 * 币种CNY。
	 */
	private String currency;
	/**
	 * 买家实付金额。
	 */
	private String buyerPayAmount;
	/**
	 * 商户订单总金额。
	 */
	private String totalAmount;
	/**
	 * 商户已退款金额。
	 */
	private String refundedAmt;
	/**
	 * 交易内容。
	 */
	private String tranContent;
	/**
	 * 支付优惠金额。
	 */
	private String payDsctAmount;
	/**
	 * 交易状态(P:处理中 F:失败 S:成功)。
	 */
	private String tranStatus;
	/**
	 * 订单状态(INITIAL:初始化 PAIED:交易成功 WAITPAY:等待支付 REFUNDED:部分退款 REFUNDALL:全部退款 CLOSED:订单关闭)。
	 */
	private String orderStatus;
	/**
	 * 学生费用明细student_fee_detail主键ID
	 */
	private Long feeId;

	@Mapper
	public interface PayFeeDetailModelMapper extends BaseModelMapper<PayFeeDetailVo, PayFeeDetail> {
	}

	public static final PayFeeDetailModelMapper INSTANCE = Mappers.getMapper(PayFeeDetailModelMapper.class);

	public static String convertTranStatus(String tranStatus) {
		switch (tranStatus) {
		case "P":
			return "待缴费";
		case "F":
			return "缴费失败";
		case "S":
			return "已缴费";
		default:
			return "未知交易状态";
		}
	}
}
