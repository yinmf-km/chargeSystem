package com.course.app.webadmin.upms.model.pmms.request;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class LocationInfo {

	/** 电信网络识别码 */
	@JsonProperty("telecom_network_id")
	private String telecomnetworkid;
	/** 基站信号3(SIG(移动、联通)， 16 进制) */
	@JsonProperty("lbs_signal3")
	private String lbssignal3;
	/** 基站信号1（SIG(移动、联通)， 16 进制） */
	@JsonProperty("lbs_signal1")
	private String lbssignal1;
	/** 基站信号2（SIG(移动、联通)， 16 进制） */
	@JsonProperty("lbs_signal2")
	private String lbssignal2;
	/** 位置区域码1（LAC(移动、联通)， 16 进制） */
	@JsonProperty("location_cd1")
	private String locationcd1;
	/** ICCID（SIM卡卡号） */
	@JsonProperty("icc_id")
	private String iccid;
	/** 位置区域码3（LAC(移动、联通)， 16 进制） */
	@JsonProperty("location_cd3")
	private String locationcd3;
	/** 电信基站信号（SIG（电信），16进制） */
	@JsonProperty("telecom_lbs_signal")
	private String telecomlbssignal;
	/** 移动国家代码（基站信息，由国际电联(ITU)统一分配的移动 国家代码（MCC）。 中国为 460） */
	@JsonProperty("mobile_country_cd")
	private String mobilecountrycd;
	/** 位置区域码2（LAC(移动、联通)， 16 进制） */
	@JsonProperty("location_cd2")
	private String locationcd2;
	/** 电信系统识别码（NID（电信），电信网络识别码,由电信各由地 级分公司分配。每个地级市可能有1到3个NID） */
	@JsonProperty("telecom_system_id")
	private String telecomsystemid;
	/** 移动网络代码（基站信息，由国际电联(ITU)统一分配的移动 网络号码（MNC）。 移动： 00、 02、 04、 07；联通： 01、 06、 09； 电信： 03、 05、 11） */
	@JsonProperty("mobile_network_num")
	private String mobilenetworknum;
	/** 基站编号2（CID(移动、联通)， 16 进制） */
	@JsonProperty("lbs_num2")
	private String lbsnum2;
	/** 电信基站（BID（电信），电信网络中的小区识别码，等 效于基站） */
	@JsonProperty("telecom_lbs")
	private String telecomlbs;
	/** 基站编号1（CID(移动、联通)， 16 进制） */
	@JsonProperty("lbs_num1")
	private String lbsnum1;
	/** 基站编号3（CID(移动、联通)， 16 进制） */
	@JsonProperty("lbs_num3")
	private String lbsnum3;
}
