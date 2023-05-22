package com.course.app.webadmin.upms.model.pmms.request;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.Setter;

import com.course.app.webadmin.upms.model.pmms.response.MPNG210001ResponseV1;

import java.util.List;

import com.bocom.api.AbstractBocomRequest;
import com.bocom.api.BizContent;

public class MPNG210001RequestV1 extends AbstractBocomRequest<MPNG210001ResponseV1> {

	@Override
	public Class<MPNG210001ResponseV1> getResponseClass() {
		return MPNG210001ResponseV1.class;
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
		return MPNG210001RequestV1Biz.class;
	}

	@Setter
	@Getter
	public static class MPNG210001RequestV1Biz implements BizContent {

		@JsonProperty("req_head")
		private ReqHead reqHead;
		@JsonProperty("req_body")
		private ReqBody reqBody;

		@Setter
		@Getter
		public static class ReqBody {

			/** 交易失效时间 */
			@JsonProperty("valid_period")
			private String validPeriod;
			/** 纬度 */
			@JsonProperty("latitude")
			private String latitude;
			/** 终端号 */
			@JsonProperty("terminal_info")
			private String terminalInfo;
			/** 经纬度信息 */
			@JsonProperty("location_id")
			private String locationId;
			/** 服务商编号 */
			@JsonProperty("partner_id")
			private String partnerId;
			/** 不带报文的前台跳转地址 */
			@JsonProperty("jump_url")
			private String jumpUrl;
			/** 商户内部备注 */
			@JsonProperty("mer_memo")
			private String merMemo;
			/** "额外返回的属性" */
			@JsonProperty("require_fields")
			private List<RequireFields> requireFields;
			/** 禁用付款渠道 */
			@JsonProperty("disable_pay_channels")
			private String disablePayChannels;
			/** 币种 */
			@JsonProperty("currency")
			private String currency;
			/** "基站信息" */
			@JsonProperty("location_info")
			private LocationInfo locationInfo;
			/** 经度 */
			@JsonProperty("longitude")
			private String longitude;
			/** "商户分账信息" */
			@JsonProperty("royalty_info")
			private List<RoyaltyInfo> royaltyInfo;
			/** 交易场景 */
			@JsonProperty("tran_scene")
			private String tranScene;
			/** 终端IP */
			@JsonProperty("ip")
			private String ip;
			/** 商户编号 */
			@JsonProperty("mer_ptc_id")
			private String merPtcId;
			/** "终端信息" */
			@JsonProperty("term_info")
			private TermInfo termInfo;
			/** 商户侧交易时间 */
			@JsonProperty("mer_trade_time")
			private String merTradeTime;
			/** 后台通知地址 */
			@JsonProperty("notify_url")
			private String notifyUrl;
			/** 商户侧交易日期 */
			@JsonProperty("mer_trade_date")
			private String merTradeDate;
			/** 门店编号 */
			@JsonProperty("shop_id")
			private String shopId;
			/** "服务商分润信息" */
			@JsonProperty("fee_split_info")
			private FeeSplitInfo feeSplitInfo;
			/** 商户交易编号 */
			@JsonProperty("pay_mer_tran_no")
			private String payMerTranNo;
			/** 商户订单总金额 */
			@JsonProperty("total_amount")
			private String totalAmount;
			/** 线上或线下 */
			@JsonProperty("location")
			private String location;
			/** 交易内容 */
			@JsonProperty("tran_content")
			private String tranContent;
		}
	}
}