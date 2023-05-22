package com.course.app.webadmin.upms.model.pmms.request;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class AddiTradeData {

	/** 用法标识 */
	@JsonProperty("method")
	private String method;
	/** "用法取值" */
	@JsonProperty("value")
	private Value value;

	@Setter
	@Getter
	public static class Value {

		/** 预留字段1 */
		@JsonProperty("reverse1")
		private String reverse1;
		/** 证件号 */
		@JsonProperty("card_no")
		private String cardNo;
		/** 持卡人姓名 */
		@JsonProperty("name")
		private String name;
		/** 付款人信息 */
		@JsonProperty("dynamic_token_out_biz_no")
		private String dynamicTokenOutBizNo;
		/** 证件类型 */
		@JsonProperty("card_type")
		private String cardType;
		/** 预留字段2 */
		@JsonProperty("reverse2")
		private String reverse2;
		/** 预留字段3 */
		@JsonProperty("reverse3")
		private String reverse3;
		/** 预留字段4 */
		@JsonProperty("reverse4")
		private String reverse4;
		/** 预留字段5 */
		@JsonProperty("reverse5")
		private String reverse5;
	}
}
