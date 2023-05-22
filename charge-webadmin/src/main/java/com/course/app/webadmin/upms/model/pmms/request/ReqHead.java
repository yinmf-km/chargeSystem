package com.course.app.webadmin.upms.model.pmms.request;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ReqHead {

	@JsonProperty("trans_time")
	private String transTime;
	/** 版本号，默认上送1.0 */
	@JsonProperty("version")
	private String version;
}
