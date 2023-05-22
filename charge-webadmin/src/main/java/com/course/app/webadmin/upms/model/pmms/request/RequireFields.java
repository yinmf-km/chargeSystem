package com.course.app.webadmin.upms.model.pmms.request;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class RequireFields {

	/** 额外返回的属性 */
	@JsonProperty("require_field")
	private String requireField;
}
