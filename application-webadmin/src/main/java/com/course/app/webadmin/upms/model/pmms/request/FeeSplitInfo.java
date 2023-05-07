package com.course.app.webadmin.upms.model.pmms.request;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class FeeSplitInfo {

	/** 服务商分润金额 */
	@JsonProperty("partner_amount")
	private String partnerAmount;
	/** 商户结算金额 */
	@JsonProperty("mer_amount")
	private String merAmount;
}
