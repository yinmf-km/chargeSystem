package com.course.app.webadmin.upms.model.pmms.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.bocom.api.BocomResponse;

public class MPNG210003ResponseV1 extends BocomResponse {

	/** "" */
	@JsonProperty("rsp_body")
	private RspBody rspBody;

	public static class RspBody {

		/** 商户交易编号 */
		@JsonProperty("pay_mer_tran_no")
		private String payMerTranNo;

		/** 跳转地址 */
		@JsonProperty("redirect_url")
		private String redirectUrl;

		public String getPayMerTranNo() {
			return payMerTranNo;
		}

		public void setPayMerTranNo(String payMerTranNo) {
			this.payMerTranNo = payMerTranNo;
		}

		public String getRedirectUrl() {
			return redirectUrl;
		}

		public void setRedirectUrl(String redirectUrl) {
			this.redirectUrl = redirectUrl;
		}
	}

	/** "" */
	@JsonProperty("rsp_head")
	private RspHead rspHead;

	public static class RspHead {

		/** 交易标识 */
		@JsonProperty("trans_code")
		private String transCode;

		/** 返回码 */
		@JsonProperty("response_code")
		private String responseCode;

		/** 交易状态 P-处理中 F-失败 S-成功 */
		@JsonProperty("response_status")
		private String responseStatus;

		/** 响应时间 */
		@JsonProperty("response_time")
		private String responseTime;

		/** 返回码描述 */
		@JsonProperty("response_msg")
		private String responseMsg;

		public String getTransCode() {
			return transCode;
		}

		public void setTransCode(String transCode) {
			this.transCode = transCode;
		}

		public String getResponseCode() {
			return responseCode;
		}

		public void setResponseCode(String responseCode) {
			this.responseCode = responseCode;
		}

		public String getResponseStatus() {
			return responseStatus;
		}

		public void setResponseStatus(String responseStatus) {
			this.responseStatus = responseStatus;
		}

		public String getResponseTime() {
			return responseTime;
		}

		public void setResponseTime(String responseTime) {
			this.responseTime = responseTime;
		}

		public String getResponseMsg() {
			return responseMsg;
		}

		public void setResponseMsg(String responseMsg) {
			this.responseMsg = responseMsg;
		}
	}

	public RspBody getRspBody() {
		return rspBody;
	}

	public void setRspBody(RspBody rspBody) {
		this.rspBody = rspBody;
	}

	public RspHead getRspHead() {
		return rspHead;
	}

	public void setRspHead(RspHead rspHead) {
		this.rspHead = rspHead;
	}
}