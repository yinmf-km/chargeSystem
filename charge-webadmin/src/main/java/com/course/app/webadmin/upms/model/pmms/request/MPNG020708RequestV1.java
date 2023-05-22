package com.course.app.webadmin.upms.model.pmms.request;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.Setter;

import com.course.app.webadmin.upms.model.pmms.response.MPNG020708ResponseV1;

import com.bocom.api.AbstractBocomRequest;
import com.bocom.api.BizContent;

public class MPNG020708RequestV1 extends AbstractBocomRequest<MPNG020708ResponseV1> {

	@Override
	public Class<MPNG020708ResponseV1> getResponseClass() {
		return MPNG020708ResponseV1.class;
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
		return MPNG020708RequestV1Biz.class;
	}

	@Setter
	@Getter
	public static class MPNG020708RequestV1Biz implements BizContent {

		@JsonProperty("req_head")
		private ReqHead reqHead;
		@JsonProperty("req_body")
		private ReqBody reqBody;

		@Setter
		@Getter
		public static class ReqBody {

			/** 交易金额，单位元 */
			@JsonProperty("amount")
			private String amount;
			/** 交易场景 */
			@JsonProperty("tran_scene")
			private String tranScene;
			/** 商户号 */
			@JsonProperty("mer_ptc_id")
			private String merPtcId;
			/** 后台通知地址 */
			@JsonProperty("notify_url")
			private String notifyUrl;
			/** 原交易商户侧交易日期 */
			@JsonProperty("mer_trade_date")
			private String merTradeDate;
			/** 门店号 */
			@JsonProperty("shop_id")
			private String shopId;
			/** 服务商编号 */
			@JsonProperty("partner_id")
			private String partnerId;
			/** 原支付交易商户交易编号 */
			@JsonProperty("pay_mer_tran_no")
			private String payMerTranNo;
			/** 商户内部备注 */
			@JsonProperty("mer_memo")
			private String merMemo;
			/** 商户退款的交易编号 */
			@JsonProperty("refund_mer_tran_no")
			private String refundMerTranNo;
			/** 币种 */
			@JsonProperty("currency")
			private String currency;
			/** 商户侧退款时间 格式：hhmmss */
			@JsonProperty("mer_refund_time")
			private String merRefundTime;
			/** 商户侧退款日期 格式：yyyyMMdd */
			@JsonProperty("mer_refund_date")
			private String merRefundDate;
			/** 交易内容，可查询 */
			@JsonProperty("tran_content")
			private String tranContent;
			/** 交行内部订单号，交行内部订单号和商户交易编号二选一，若同时上送优先使用系统订单号 */
			@JsonProperty("sys_order_no")
			private String sysOrderNo;
		}
	}
}