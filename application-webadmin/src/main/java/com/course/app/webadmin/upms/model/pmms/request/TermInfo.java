package com.course.app.webadmin.upms.model.pmms.request;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class TermInfo {

	/** 设备类型 */
	@JsonProperty("device_type")
	private String deviceType;
	/** 密文数据 */
	@JsonProperty("secret_text")
	private String secretText;
	/** 应用程序版本号 */
	@JsonProperty("app_version")
	private String appVersion;
	/** 终端序列号 */
	@JsonProperty("serial_num")
	private String serialNum;
	/** 加密随机因子 */
	@JsonProperty("encrypt_rand_num")
	private String encryptRandNum;
	/** 终端入网认证编号 */
	@JsonProperty("network_license")
	private String networkLicense;
}
