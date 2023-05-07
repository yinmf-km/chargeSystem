package com.course.app.webadmin.upms.model.pmms.request;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.Setter;

import com.course.app.webadmin.upms.model.pmms.response.MPNG210006ResponseV1;

import com.bocom.api.AbstractBocomRequest;
import com.bocom.api.BizContent;
import java.util.List;

public class MPNG210006RequestV1 extends AbstractBocomRequest<MPNG210006ResponseV1> {

	@Override
	public Class<MPNG210006ResponseV1> getResponseClass() {
		return MPNG210006ResponseV1.class;
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
		return MPNG210006RequestV1Biz.class;
	}

	@Setter
	@Getter
	public static class MPNG210006RequestV1Biz implements BizContent {

		@JsonProperty("req_head")
		private ReqHead reqHead;
		@JsonProperty("req_body")
		private ReqBody reqBody;

		@Setter
		@Getter
		public static class ReqBody {

			/** 交易失效时间 */
			@JsonProperty("valid_period")
			private String validPeriod;
			/** 纬度（该字段必输） */
			@JsonProperty("latitude")
			private String latitude;
			/** 付款码文本 */
			@JsonProperty("scan_code_text")
			private String scanCodeText;
			/** 终端号（该字段必输） */
			@JsonProperty("terminal_info")
			private String terminalInfo;
			/** 服务商编号 */
			@JsonProperty("partner_id")
			private String partnerId;
			/** 商户内部备注 */
			@JsonProperty("mer_memo")
			private String merMemo;
			/** "额外返回的字段" */
			@JsonProperty("require_fields")
			private List<RequireFields> requireFields;
			/** 币种 */
			@JsonProperty("currency")
			private String currency;
			/** "基站信息" */
			@JsonProperty("location_info")
			private LocationInfo locationInfo;
			/** 经度（该字段必输） */
			@JsonProperty("longitude")
			private String longitude;
			/** 终端批次号 */
			@JsonProperty("term_batch_no")
			private String termBatchNo;
			/** 交易场景 */
			@JsonProperty("tran_scene")
			private String tranScene;
			/** ip */
			@JsonProperty("ip")
			private String ip;
			/** "附加交易信息" */
			@JsonProperty("addi_trade_data")
			private AddiTradeData addiTradeData;
			/** 商户编号 */
			@JsonProperty("mer_ptc_id")
			private String merPtcId;
			/** "终端信息（该字段必输）" */
			@JsonProperty("term_info")
			private TermInfo termInfo;
			/** 商户侧交易时间 */
			@JsonProperty("mer_trade_time")
			private String merTradeTime;
			/** 终端流水号 */
			@JsonProperty("term_pos_no")
			private String termPosNo;
			/** 商户侧交易日期 */
			@JsonProperty("mer_trade_date")
			private String merTradeDate;
			/** 门店编号 */
			@JsonProperty("shop_id")
			private String shopId;
			/** 商户交易编号 */
			@JsonProperty("pay_mer_tran_no")
			private String payMerTranNo;
			/** 商户订单总金额（元） */
			@JsonProperty("total_amount")
			private String totalAmount;
			/** 线上或线下 */
			@JsonProperty("location")
			private String location;
			/** 商品详情 */
			@JsonProperty("detail")
			private String detail;
			/** 交易内容 */
			@JsonProperty("tran_content")
			private String tranContent;
		}
	}
}