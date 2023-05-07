package com.course.app.webadmin.upms.model.pmms.request;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.Setter;

import com.course.app.webadmin.upms.model.pmms.response.MPNG020709ResponseV1;

import java.util.List;

import com.bocom.api.AbstractBocomRequest;
import com.bocom.api.BizContent;

public class MPNG020709RequestV1 extends AbstractBocomRequest<MPNG020709ResponseV1> {

	@Override
	public Class<MPNG020709ResponseV1> getResponseClass() {
		return MPNG020709ResponseV1.class;
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
		return MPNG020709RequestV1Biz.class;
	}

	@Setter
	@Getter
	public static class MPNG020709RequestV1Biz implements BizContent {

		@JsonProperty("req_head")
		private ReqHead reqHead;
		@JsonProperty("req_body")
		private ReqBody reqBody;

		@Setter
		@Getter
		public static class ReqBody {

			/** 原交易商户侧交易时间 */
			@JsonProperty("mer_trade_date")
			private String merTradeDate;
			/** 服务商编号 */
			@JsonProperty("partner_id")
			private String partnerId;
			/** 商户交易编号 */
			@JsonProperty("pay_mer_tran_no")
			private String payMerTranNo;
			/** 交易场景 */
			@JsonProperty("tran_scene")
			private String tranScene;
			/** "额外返回的字段" */
			@JsonProperty("require_fields")
			private List<RequireFields> requireFields;
			/** 商户编号 */
			@JsonProperty("mer_ptc_id")
			private String merPtcId;
			/** 交行系统订单号 */
			@JsonProperty("sys_order_no")
			private String sysOrderNo;
		}
	}
}