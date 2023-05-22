package com.course.app.webadmin.upms.model.pmms.request;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class SettleInfo {

	/** 对方行号 */
	@JsonProperty("opp_bank_no")
	private String oppBankNo;
	/** 对方行名 */
	@JsonProperty("opp_bank_name")
	private String oppBankName;
	/** 户名 */
	@JsonProperty("account_name")
	private String accountName;
	/** 账户 */
	@JsonProperty("account_no")
	private String accountNo;
	/** 商户清算的类型 */
	@JsonProperty("trans_type")
	private String transType;
}
