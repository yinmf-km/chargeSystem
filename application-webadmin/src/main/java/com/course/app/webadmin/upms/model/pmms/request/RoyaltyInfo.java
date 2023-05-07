package com.course.app.webadmin.upms.model.pmms.request;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class RoyaltyInfo {

	/** 分账金额 */
	@JsonProperty("amount")
	private String amount;
	/** 分账的序号 */
	@JsonProperty("serial_no")
	private String serialNo;
}
