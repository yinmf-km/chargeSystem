package com.course.app.webadmin.upms.model.pmms.request;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.Setter;

import com.course.app.webadmin.upms.model.pmms.response.MPNG020703ResponseV1;

import com.bocom.api.AbstractBocomRequest;
import com.bocom.api.BizContent;

public class MPNG020703RequestV1 extends AbstractBocomRequest<MPNG020703ResponseV1> {

	@Override
	public Class<MPNG020703ResponseV1> getResponseClass() {
		return MPNG020703ResponseV1.class;
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
		return MPNG020703RequestV1Biz.class;
	}

	@Setter
	@Getter
	public static class MPNG020703RequestV1Biz implements BizContent {

		@JsonProperty("req_head")
		private ReqHead reqHead;
		@JsonProperty("req_body")
		private ReqBody reqBody;

		@Setter
		@Getter
		public static class ReqBody {

			/** 服务商编号 */
			@JsonProperty("partner_id")
			private String partnerId;
			/** 交易场景，原支付交易的场景：如B2C-API-DISPLAYCODE */
			@JsonProperty("tran_scene")
			private String tranScene;
			/** 商户退款的交易编号，需确保当日不重复 */
			@JsonProperty("refund_mer_tran_no")
			private String refundMerTranNo;
			/** 商户编号 */
			@JsonProperty("mer_ptc_id")
			private String merPtcId;
			/** 商户侧退款日期，格式：hhmmss */
			@JsonProperty("mer_refund_date")
			private String merRefundDate;
			/** 交行内部订单号，交行内部订单号和商户交易编号二选一，若送了优先使用系统订单号查询 */
			@JsonProperty("sys_order_no")
			private String sysOrderNo;
		}
	}
}