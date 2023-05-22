package com.course.app.webadmin.upms.model.pmms.request;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.Setter;

import com.course.app.webadmin.upms.model.pmms.response.MPNG210003ResponseV1;

import com.bocom.api.AbstractBocomRequest;
import com.bocom.api.BizContent;
import java.util.List;

public class MPNG210003RequestV1 extends AbstractBocomRequest<MPNG210003ResponseV1> {

	@Override
	public Class<MPNG210003ResponseV1> getResponseClass() {
		return MPNG210003ResponseV1.class;
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
		return MPNG210003RequestV1Biz.class;
	}

	@Setter
	@Getter
	public static class MPNG210003RequestV1Biz implements BizContent {

		@JsonProperty("req_head")
		private ReqHead reqHead;
		@JsonProperty("req_body")
		private ReqBody reqBody;

		@Setter
		@Getter
		public static class ReqBody {

			/** "商户分账信息" */
			@JsonProperty("royalty_info")
			private List<RoyaltyInfo> royaltyInfo;
			/** 交易失效时间 */
			@JsonProperty("valid_period")
			private String validPeriod;
			/** "结算信息" */
			@JsonProperty("settle_info")
			private SettleInfo settleInfo;
			/** 交易场景 */
			@JsonProperty("tran_scene")
			private String tranScene;
			/** 商户编号 */
			@JsonProperty("mer_ptc_id")
			private String merPtcId;
			/** 商户侧交易时间 */
			@JsonProperty("mer_trade_time")
			private String merTradeTime;
			/** 后台通知地址 */
			@JsonProperty("notify_url")
			private String notifyUrl;
			/** 商户侧交易日期 */
			@JsonProperty("mer_trade_date")
			private String merTradeDate;
			/** 门店id */
			@JsonProperty("shop_id")
			private String shopId;
			/** 服务商编号 */
			@JsonProperty("partner_id")
			private String partnerId;
			/** 商户交易编号 */
			@JsonProperty("pay_mer_tran_no")
			private String payMerTranNo;
			/** 不带报文的前台跳转地址 */
			@JsonProperty("jump_url")
			private String jumpUrl;
			/** 商户内部备注 */
			@JsonProperty("mer_memo")
			private String merMemo;
			/** 商户订单总金额 */
			@JsonProperty("total_amount")
			private String totalAmount;
			/** "额外返回的字段" */
			@JsonProperty("require_fields")
			private List<RequireFields> requireFields;
			/** 线上或线下 */
			@JsonProperty("location")
			private String location;
			/** 币种 */
			@JsonProperty("currency")
			private String currency;
			/** 交易内容 */
			@JsonProperty("tran_content")
			private String tranContent;
			/** 商户无优惠金额 */
			@JsonProperty("no_dsct_amount")
			private String noDsctAmount;
		}
	}
}