package com.course.app.webadmin.upms.model.pmms.request;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.Setter;

import com.course.app.webadmin.upms.model.pmms.response.MPNG210106ResponseV1;

import com.bocom.api.AbstractBocomRequest;
import com.bocom.api.BizContent;

public class MPNG210106RequestV1 extends AbstractBocomRequest<MPNG210106ResponseV1> {

	@Override
	public Class<MPNG210106ResponseV1> getResponseClass() {
		return MPNG210106ResponseV1.class;
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
		return MPNG210106RequestV1Biz.class;
	}

	@Setter
	@Getter
	public static class MPNG210106RequestV1Biz implements BizContent {

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
			/** 商户编号 */
			@JsonProperty("mer_ptc_id")
			private String merPtcId;
			/** 子appid */
			@JsonProperty("sub_appid")
			private String subAppid;
			/** 用户授权码 */
			@JsonProperty("auth_code")
			private String authCode;
		}
	}
}