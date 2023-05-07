package com.course.app.webadmin.upms.model.pmms.request;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.Setter;

import com.course.app.webadmin.upms.model.pmms.response.MPNG020702ResponseV1;

import com.bocom.api.AbstractBocomRequest;
import com.bocom.api.BizContent;
import java.util.List;

public class MPNG020702RequestV1 extends AbstractBocomRequest<MPNG020702ResponseV1> {

	@Override
	public Class<MPNG020702ResponseV1> getResponseClass() {
		return MPNG020702ResponseV1.class;
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
		return MPNG020702RequestV1Biz.class;
	}

	@Setter
	@Getter
	public static class MPNG020702RequestV1Biz implements BizContent {

		@JsonProperty("req_head")
		private ReqHead reqHead;
		@JsonProperty("req_body")
		private ReqBody reqBody;

		@Setter
		@Getter
		public static class ReqBody {

			/** 原交易商户侧交易日期 yyyyMMdd */
			@JsonProperty("mer_trade_date")
			private String merTradeDate;
			/** 服务商编号 */
			@JsonProperty("partner_id")
			private String partnerId;
			/** 商户交易编号，商户自定义的订单号，当日不可重复 */
			@JsonProperty("pay_mer_tran_no")
			private String payMerTranNo;
			/** 交易场景，支付交易上送的交易场景，如B2C-API-DISPLAYCODE等 */
			@JsonProperty("tran_scene")
			private String tranScene;
			/**
			 * "目前支持的字段包括银行端交易流水号bank_tran_no、第三方渠道third_party、第三方渠道交易流水号third_party_tran_no、微信支付宝详细付款信息payment_info、微信支付宝详细付款信息refund_info"
			 */
			@JsonProperty("require_fields")
			private List<RequireFields> requireFields;
			/** 商户编号 */
			@JsonProperty("mer_ptc_id")
			private String merPtcId;
			/** 交行系统订单号，订单号和商户交易编号二选一，若送了优先使用系统订单号查询 */
			@JsonProperty("sys_order_no")
			private String sysOrderNo;
		}
	}
}