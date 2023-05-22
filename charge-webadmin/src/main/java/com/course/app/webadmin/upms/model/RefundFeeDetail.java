package com.course.app.webadmin.upms.model;

import java.util.Date;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.course.app.common.core.base.mapper.BaseModelMapper;
import com.course.app.common.core.base.model.BaseModel;
import com.course.app.webadmin.upms.vo.RefundFeeDetailVo;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * RefundFeeDetail实体对象。
 * @author 云翼
 * @date 2023-02-21
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName(value = "refund_fee_detail")
public class RefundFeeDetail extends BaseModel {

	/**
	 * 商户退款交易编号。
	 */
	@TableId(value = "refund_mer_tran_no")
	private String refundMerTranNo;
	/**
	 * 退款学生。
	 */
	private Long studentId;
	/**
	 * 退款学生绑定手机号码。
	 */
	private String phoneNum;
	/**
	 * 退款帐户。
	 */
	private String refundAcct;
	/**
	 * 退款金额。
	 */
	private String amount;
	/**
	 * 商户编号。
	 */
	private String merPtcId;
	/**
	 * 原支付交易商户交易编号
	 */
	private String payMerTranNo;
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
	 * 商户侧退款时间。
	 */
	private Date merRefundTime;
	/**
	 * 退款单据号。
	 */
	private String refundOrderNo;
	/**
	 * 合计已退款金额。
	 */
	private String doneRefundAmount;
	/**
	 * 该笔退款对应的交易的订单金额。
	 */
	private String totalAmount;
	/**
	 * 本次退款请求对应的退款金额。
	 */
	private String refundAmount;
	/**
	 * 退款支付优惠金额。
	 */
	private String payDsctAmount;
	/**
	 * 交易内容。
	 */
	private String tranContent;
	/**
	 * 交易状态(P:处理中 F:失败 S:成功)。
	 */
	private String refundStatus;
	/**
	 * 订单状态(INITIAL:初始化 PAIED:交易成功 WAITPAY:等待支付 REFUNDED:部分退款 REFUNDALL:全部退款 CLOSED:订单关闭)。
	 */
	private String orderStatus;
	/**
	 * 学生费用明细student_fee_detail主键ID
	 */
	private Long feeId;

	@Mapper
	public interface RefundFeeDetailModelMapper extends BaseModelMapper<RefundFeeDetailVo, RefundFeeDetail> {
	}

	public static final RefundFeeDetailModelMapper INSTANCE = Mappers.getMapper(RefundFeeDetailModelMapper.class);

	public static String convertRefundStatus(String refundStatus) {
		switch (refundStatus) {
		case "P":
			return "退费中";
		case "F":
			return "退费失败";
		case "S":
			return "已退费";
		default:
			return "未知交易状态";
		}
	}
}
