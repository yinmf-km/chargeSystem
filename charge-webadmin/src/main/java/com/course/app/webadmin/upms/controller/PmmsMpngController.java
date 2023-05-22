package com.course.app.webadmin.upms.controller;

import java.util.UUID;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bocom.api.DefaultBocomClient;
import com.course.app.common.core.annotation.MyRequestBody;
import com.course.app.common.core.constant.ErrorCodeEnum;
import com.course.app.common.core.object.ResponseResult;
import com.course.app.common.core.util.MyCommonUtil;
import com.course.app.common.sequence.wrapper.IdGeneratorWrapper;
import com.course.app.webadmin.upms.model.StudentFeeDetail;
import com.course.app.webadmin.upms.model.SysStudent;
import com.course.app.webadmin.upms.model.pmms.request.MPNG020701RequestV1;
import com.course.app.webadmin.upms.model.pmms.request.MPNG020702RequestV1;
import com.course.app.webadmin.upms.model.pmms.request.MPNG020703RequestV1;
import com.course.app.webadmin.upms.model.pmms.request.MPNG020703RequestV1.MPNG020703RequestV1Biz.ReqBody;
import com.course.app.webadmin.upms.model.pmms.request.MPNG210003RequestV1;
import com.course.app.webadmin.upms.model.pmms.request.PmmsMpngNotifyRequestV1;
import com.course.app.webadmin.upms.model.pmms.request.ReqHead;
import com.course.app.webadmin.upms.model.pmms.response.MPNG020701ResponseV1;
import com.course.app.webadmin.upms.model.pmms.response.MPNG020702ResponseV1;
import com.course.app.webadmin.upms.model.pmms.response.MPNG020703ResponseV1;
import com.course.app.webadmin.upms.model.pmms.response.MPNG210003ResponseV1;
import com.course.app.webadmin.upms.service.PayFeeDetailService;
import com.course.app.webadmin.upms.service.RefundFeeDetailService;
import com.course.app.webadmin.upms.service.StudentFeeDetailService;
import com.course.app.webadmin.upms.service.SysStudentService;

import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import io.swagger.annotations.Api;

/**
 * @Description 交行相关交易控制器
 * @author yinmf
 * @Title PmmsMpngController.java
 * @Package com.course.app.webadmin.upms.controller
 * @date 2023年5月7日 下午7:04:43
 * @version V1.0
 */
@RestController
@Api(tags = "交行相关交易管理接口")
@RequestMapping("/admin/upms/pmmsMpng")
public class PmmsMpngController extends H5BaseController {

	@Value("${pmms-mpng.my-private-key}")
	private String MY_PRIVATE_KEY;
	@Value("${pmms-mpng.apigw-public-key}")
	private String APIGW_PUBLIC_KEY;
	@Value("${pmms-mpng.bocom-prd-key}")
	private String BOCOM_PRD_KEY;
	@Value("${pmms-mpng.app-id}")
	private String APP_ID;
	@Value("${pmms-mpng.apigw-url-address}")
	private String APIGW_URL_ADDRESS;
	@Value("${pmms-mpng.pay-notify-url}")
	private String PAY_NOTIFY_URL;
	@Value("${pmms-mpng.refund-notify-url}")
	private String REFUND_NOTIFY_URL;
	@Value("${pmms-mpng.version}")
	private String VERSION;
	@Value("${pmms-mpng.mer-ptc-id}")
	private String MER_PTC_ID;
	@Value("${pmms-mpng.tranScene}")
	private String TRANSCENE;
	@Autowired
	private IdGeneratorWrapper idGenerator;
	@Autowired
	private PayFeeDetailService payFeeDetailService;
	@Autowired
	private RefundFeeDetailService refundFeeDetailService;
	@Autowired
	private SysStudentService sysStudentService;
	@Autowired
	private StudentFeeDetailService studentFeeDetailService;

	/**
	 * @Method singleStrokePay
	 * @Return ResponseResult<Void>
	 * @Description 单笔条码跳转支付接口
	 * @Title PmmsMpngController.java
	 * @Package com.course.app.webadmin.upms.controller
	 * @date 2023年5月7日 下午8:41:12
	 * @version V1.0
	 */
	@PostMapping("/singleStrokePay")
	public ResponseResult<JSONObject> singleStrokePay(@MyRequestBody String feeId) throws Exception {
		if (MyCommonUtil.existBlankArgument(feeId)) {
			return ResponseResult.error(ErrorCodeEnum.ARGUMENT_NULL_EXIST);
		}
		StudentFeeDetail studentFeeDetail = studentFeeDetailService.getById(feeId);
		if (null == studentFeeDetail) {
			return ResponseResult.error(ErrorCodeEnum.FEE_NOT_EXIST);
		}
		SysStudent student = sysStudentService.getById(studentFeeDetail.getStudentId());
		if (null == student) {
			return ResponseResult.error(ErrorCodeEnum.STUDENT_NOT_EXIST);
		}
		DefaultBocomClient client = new DefaultBocomClient(APP_ID, MY_PRIVATE_KEY, APIGW_PUBLIC_KEY);
		//测试环境可以忽略SSL证书告警，生产环境不可忽略
		client.ignoreSSLHostnameVerifier();
		MPNG210003RequestV1 request = new MPNG210003RequestV1();
		request.setServiceUrl(APIGW_URL_ADDRESS + "/api/pmssMpng/MPNG210003/v1");
		MPNG210003RequestV1.MPNG210003RequestV1Biz bizContent = new MPNG210003RequestV1.MPNG210003RequestV1Biz();
		ReqHead reqHead = new ReqHead();
		bizContent.setReqHead(reqHead);
		DateTime now = new DateTime();
		reqHead.setTransTime(DateUtil.format(now, DatePattern.PURE_DATETIME_FORMATTER));
		reqHead.setVersion(VERSION);
		MPNG210003RequestV1.MPNG210003RequestV1Biz.ReqBody reqBody = new MPNG210003RequestV1.MPNG210003RequestV1Biz.ReqBody();
		bizContent.setReqBody(reqBody);
		reqBody.setTranScene(TRANSCENE);
		reqBody.setMerPtcId(MER_PTC_ID);
		reqBody.setMerTradeTime(DateUtil.format(now, DatePattern.PURE_TIME_FORMAT));
		reqBody.setNotifyUrl(PAY_NOTIFY_URL);
		reqBody.setMerTradeDate(DateUtil.format(now, DatePattern.PURE_DATE_FORMATTER));
		String payMerTranNo = idGenerator.nextStringId();
		reqBody.setPayMerTranNo(payMerTranNo);
		reqBody.setTotalAmount(studentFeeDetail.getFeeAmount());
		reqBody.setLocation("ONLINE");
		reqBody.setCurrency("CNY");
		request.setBizContent(bizContent);
		MPNG210003ResponseV1 response = client.execute(request, UUID.randomUUID().toString().replace("-", ""));
		if (response.isSuccess()) {
			payFeeDetailService.insertPayLog(reqHead, reqBody, student, studentFeeDetail);
			return ResponseResult.success(JSONUtil.parseObj(response.getRspBody()));
		} else {
			return ResponseResult.error(response.getRspCode(), response.getRspMsg());
		}
	}

	/**
	 * @Method refund
	 * @Return ResponseResult<Void>
	 * @Description 退款接口
	 * @Title PmmsMpngController.java
	 * @Package com.course.app.webadmin.upms.controller
	 * @date 2023年5月7日 下午8:41:12
	 * @version V1.0
	 */
	@PostMapping("/refund")
	public ResponseResult<JSONObject> refund(@MyRequestBody String amount, @MyRequestBody String studentId,
			@MyRequestBody String payMerTranNo) throws Exception {
		if (MyCommonUtil.existBlankArgument(amount, studentId, payMerTranNo)) {
			return ResponseResult.error(ErrorCodeEnum.ARGUMENT_NULL_EXIST);
		}
		SysStudent student = sysStudentService.getById(studentId);
		if (null == student) {
			return ResponseResult.error(ErrorCodeEnum.STUDENT_NOT_EXIST);
		}
		DefaultBocomClient client = new DefaultBocomClient(APP_ID, MY_PRIVATE_KEY, APIGW_PUBLIC_KEY);
		//测试环境可以忽略SSL证书告警，生产环境不可忽略
		client.ignoreSSLHostnameVerifier();
		MPNG020701RequestV1 request = new MPNG020701RequestV1();
		request.setServiceUrl(APIGW_URL_ADDRESS + "/api/pmssMpng/MPNG020701/v1");
		MPNG020701RequestV1.MPNG020701RequestV1Biz bizContent = new MPNG020701RequestV1.MPNG020701RequestV1Biz();
		ReqHead reqHead = new ReqHead();
		bizContent.setReqHead(reqHead);
		DateTime now = new DateTime();
		reqHead.setTransTime(DateUtil.format(now, DatePattern.PURE_DATETIME_FORMATTER));
		reqHead.setVersion(VERSION);
		MPNG020701RequestV1.MPNG020701RequestV1Biz.ReqBody reqBody = new MPNG020701RequestV1.MPNG020701RequestV1Biz.ReqBody();
		bizContent.setReqBody(reqBody);
		reqBody.setAmount(amount);
		reqBody.setTranScene(TRANSCENE);
		reqBody.setMerPtcId(MER_PTC_ID);
		reqBody.setNotifyUrl(REFUND_NOTIFY_URL);
		reqBody.setMerTradeDate(DateUtil.format(now, DatePattern.PURE_DATE_FORMATTER));
		if (StringUtils.isNotBlank(payMerTranNo)) {
			reqBody.setPayMerTranNo(payMerTranNo);
		}
		String refundMerTranNo = idGenerator.nextStringId();
		reqBody.setRefundMerTranNo(refundMerTranNo);
		reqBody.setCurrency("CNY");
		reqBody.setMerRefundTime(DateUtil.format(now, DatePattern.PURE_TIME_FORMAT));
		reqBody.setMerRefundDate(DateUtil.format(now, DatePattern.PURE_DATE_FORMATTER));
		request.setBizContent(bizContent);
		MPNG020701ResponseV1 response = client.execute(request, UUID.randomUUID().toString().replace("-", ""));
		if (response.isSuccess()) {
			refundFeeDetailService.insertRefundLog(reqHead, reqBody, student);
			return ResponseResult.success(JSONUtil.parseObj(response.getRspBody()));
		} else {
			return ResponseResult.error(response.getRspCode(), response.getRspMsg());
		}
	}

	/**
	 * @Method queryRefundOrder
	 * @Return ResponseResult<Void>
	 * @Description 退款查询接口
	 * @Title PmmsMpngController.java
	 * @Package com.course.app.webadmin.upms.controller
	 * @date 2023年5月7日 下午8:41:12
	 * @version V1.0
	 */
	@PostMapping("/queryRefundOrder")
	public ResponseResult<JSONObject> queryRefundOrder(@MyRequestBody String refundMerTranNo) throws Exception {
		DefaultBocomClient client = new DefaultBocomClient(APP_ID, MY_PRIVATE_KEY, APIGW_PUBLIC_KEY);
		//测试环境可以忽略SSL证书告警，生产环境不可忽略
		client.ignoreSSLHostnameVerifier();
		MPNG020703RequestV1 request = new MPNG020703RequestV1();
		request.setServiceUrl(APIGW_URL_ADDRESS + "/api/pmssMpng/MPNG020703/v1");
		MPNG020703RequestV1.MPNG020703RequestV1Biz bizContent = new MPNG020703RequestV1.MPNG020703RequestV1Biz();
		ReqHead reqHead = new ReqHead();
		bizContent.setReqHead(reqHead);
		DateTime now = new DateTime();
		reqHead.setTransTime(DateUtil.format(now, DatePattern.PURE_DATETIME_FORMATTER));
		reqHead.setVersion(VERSION);
		ReqBody reqBody = new ReqBody();
		bizContent.setReqBody(reqBody);
		reqBody.setTranScene(TRANSCENE);
		reqBody.setRefundMerTranNo(refundMerTranNo);
		reqBody.setMerPtcId(MER_PTC_ID);
		reqBody.setMerRefundDate(DateUtil.format(now, DatePattern.PURE_DATE_FORMATTER));
		request.setBizContent(bizContent);
		MPNG020703ResponseV1 response = client.execute(request, UUID.randomUUID().toString().replace("-", ""));
		if (response.isSuccess()) {
			return ResponseResult.success(JSONUtil.parseObj(response.getRspBody()));
		} else {
			return ResponseResult.error(response.getRspCode(), response.getRspMsg());
		}
	}

	/**
	 * @Method queryPayOrder
	 * @Return ResponseResult<Void>
	 * @Description 查询交易订单信息
	 * @Title PmmsMpngController.java
	 * @Package com.course.app.webadmin.upms.controller
	 * @date 2023年5月7日 下午8:41:12
	 * @version V1.0
	 */
	@PostMapping("/queryPayOrder")
	public ResponseResult<JSONObject> queryPayOrder(@MyRequestBody String payMerTranNo) throws Exception {
		DefaultBocomClient client = new DefaultBocomClient(APP_ID, MY_PRIVATE_KEY, APIGW_PUBLIC_KEY);
		//测试环境可以忽略SSL证书告警，生产环境不可忽略
		client.ignoreSSLHostnameVerifier();
		MPNG020702RequestV1 request = new MPNG020702RequestV1();
		request.setServiceUrl(APIGW_URL_ADDRESS + "/api/pmssMpng/MPNG020702/v1");
		MPNG020702RequestV1.MPNG020702RequestV1Biz bizContent = new MPNG020702RequestV1.MPNG020702RequestV1Biz();
		ReqHead reqHead = new ReqHead();
		bizContent.setReqHead(reqHead);
		DateTime now = new DateTime();
		reqHead.setTransTime(DateUtil.format(now, DatePattern.PURE_DATETIME_FORMATTER));
		reqHead.setVersion(VERSION);
		MPNG020702RequestV1.MPNG020702RequestV1Biz.ReqBody reqBody = new MPNG020702RequestV1.MPNG020702RequestV1Biz.ReqBody();
		bizContent.setReqBody(reqBody);
		reqBody.setMerTradeDate(DateUtil.format(now, DatePattern.PURE_DATE_FORMATTER));
		reqBody.setPayMerTranNo(payMerTranNo);
		reqBody.setTranScene(TRANSCENE);
		reqBody.setMerPtcId(MER_PTC_ID);
		request.setBizContent(bizContent);
		MPNG020702ResponseV1 response = client.execute(request, UUID.randomUUID().toString().replace("-", ""));
		if (response.isSuccess()) {
			return ResponseResult.success(JSONUtil.parseObj(response.getRspBody()));
		} else {
			return ResponseResult.error(response.getRspCode(), response.getRspMsg());
		}
	}

	/**
	 * 支付通知接口
	 */
	@PostMapping("/payNotifyCust")
	public ResponseResult<Void> payNotifyCust(@MyRequestBody PmmsMpngNotifyRequestV1.PmmsMpngNotifyRequestV1Biz request)
			throws Exception {
		payFeeDetailService.updatePayLog(request);
		return ResponseResult.success();
	}

	/**
	 * 退费通知接口
	 */
	@PostMapping("/refundNotifyCust")
	public ResponseResult<Void> refundNotifyCust(
			@MyRequestBody PmmsMpngNotifyRequestV1.PmmsMpngNotifyRequestV1Biz request) throws Exception {
		refundFeeDetailService.updateRefundLog(request);
		return ResponseResult.success();
	}
}
