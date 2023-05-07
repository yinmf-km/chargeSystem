package com.course.app.webadmin.upms.model.pmms.request;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.Setter;

import com.course.app.webadmin.upms.model.pmms.response.MPNG020705ResponseV1;

import com.bocom.api.AbstractBocomRequest;
import com.bocom.api.BizContent;

public class MPNG020705RequestV1 extends AbstractBocomRequest<MPNG020705ResponseV1> {

	@Override
	public Class<MPNG020705ResponseV1> getResponseClass() {
		return MPNG020705ResponseV1.class;
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
		return MPNG020705RequestV1Biz.class;
	}

	@Setter
	@Getter
	public static class MPNG020705RequestV1Biz implements BizContent {

		@JsonProperty("req_head")
		private ReqHead reqHead;
		@JsonProperty("req_body")
		private ReqBody reqBody;

		@Setter
		@Getter
		public static class ReqBody {

			/** 商户侧原交易日期 */
			@JsonProperty("mer_trade_date")
			private String merTradeDate;
			/** 服务商编号 */
			@JsonProperty("partner_id")
			private String partnerId;
			/** 支付交易商户交易编号 */
			@JsonProperty("pay_mer_tran_no")
			private String payMerTranNo;
			/** 商户编号 */
			@JsonProperty("mer_ptc_id")
			private String merPtcId;
			/** 商户交易编号 */
			@JsonProperty("close_mer_tran_no")
			private String closeMerTranNo;
		}
	}
}