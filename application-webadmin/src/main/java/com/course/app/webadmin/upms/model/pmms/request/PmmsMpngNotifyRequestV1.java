package com.course.app.webadmin.upms.model.pmms.request;

import com.bocom.api.AbstractBocomRequest;
import com.bocom.api.BizContent;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.Setter;

import com.course.app.webadmin.upms.model.pmms.response.PmmsMpngNotifyResponseV1;

public class PmmsMpngNotifyRequestV1 extends AbstractBocomRequest<PmmsMpngNotifyResponseV1> {

	@Override
	public Class<PmmsMpngNotifyResponseV1> getResponseClass() {
		return PmmsMpngNotifyResponseV1.class;
	}

	@Override
	public boolean isNeedEncrypt() {
		return false;
	}

	@Override
	public String getMethod() {
		return "POST";
	}

	@Override
	public Class<? extends BizContent> getBizContentClass() {
		return PmmsMpngNotifyRequestV1Biz.class;
	}

	@Setter
	@Getter
	public static class PmmsMpngNotifyRequestV1Biz implements BizContent {

		/** 该参数必输，为通知第三方的URL. */
		@JsonProperty("notify_url")
		private String notifyUrl;
		/**
		 * 交易类型 PAY-支付
		 */
		@JsonProperty("tran_type")
		private String tranType;
		/** 商户交易编号 */
		@JsonProperty("mer_tran_no")
		private String merTranNo;
		/**
		 * 交易状态 SUCCESS：交易成功 FAILURE 交易失败
		 */
		@JsonProperty("tran_state")
		private String tranState;
		/** 交易状态码 */
		@JsonProperty("tran_state_code")
		private String tranStateCode;
		/** 交易状态提示 */
		@JsonProperty("tran_state_msg")
		private String tranStateMsg;
		/** 服务商编号 */
		@JsonProperty("partner_id")
		private String partnerId;
		/** 商户编号 */
		@JsonProperty("mer_ptc_id")
		private String merPtcId;
		/** 交易终态时间 */
		@JsonProperty("final_time")
		private String finalTime;
		/** 商户订单总金额 */
		@JsonProperty("total_amount")
		private String totalAmount;
		/** 买家实付金额 */
		@JsonProperty("buyer_pay_amount")
		private String buyerPayAmount;
		/** 第三方活动优惠金额 */
		@JsonProperty("trd_dsct_amount")
		private String trdDsctAmount;
		/** 支付优惠金额 */
		@JsonProperty("pay_dsct_amount")
		private String payDsctAmount;
		/** 交易使用积分 */
		@JsonProperty("points")
		private String points;
		/** 积分抵扣金额 */
		@JsonProperty("points_deduction_amount")
		private String pointsDeductionAmount;
		/** 优惠券抵扣金额 */
		@JsonProperty("coupon_total_amount")
		private String couponTotalAmount;
		/** 分期数 */
		@JsonProperty("instlmt_no")
		private String instlmtNo;
		/** 币种 */
		@JsonProperty("currency")
		private String currency;
		/** 交易内容 */
		@JsonProperty("tran_content")
		private String tranContent;
		/** mer_memo */
		@JsonProperty("mer_memo")
		private String merMemo;
		/** "需要返回的值" */
		@JsonProperty("require_values")
		private RequireValues requireValues;

		@Setter
		@Getter
		public static class RequireValues {

			/** 银行端交易流水 */
			@JsonProperty("bank_tran_no")
			private String bankTranNo;
			/**
			 * 微信、 支付宝、 银联
			 */
			@JsonProperty("third_party")
			private String thirdParty;
			/** 第三方渠道交易流水号 */
			@JsonProperty("third_party_tran_no")
			private String thirdPartyTranNo;
			/** 微信支付宝详细付款信息 */
			@JsonProperty("payment_info")
			private String paymentInfo;
			/** 微信支付宝详细退款信息 */
			@JsonProperty("refund_info")
			private String refundInfo;
		}
	}
}