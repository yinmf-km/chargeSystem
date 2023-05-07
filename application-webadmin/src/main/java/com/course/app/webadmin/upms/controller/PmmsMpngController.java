package com.course.app.webadmin.upms.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.course.app.common.core.object.ResponseResult;

import io.swagger.annotations.Api;

/**
 * @Description 交行相关交易控制器
 * @author yinmf
 * @Title PmmsMpngController.java
 * @Package com.course.app.webadmin.upms.controller
 * @date 2023年5月7日 下午7:04:43
 * @version V1.0
 */
@Api(tags = "交行相关交易管理接口")
@RestController
@RequestMapping("/admin/upms/pmmsMpng")
public class PmmsMpngController {

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

	/**
	 * @Method singleStrokePay
	 * @Return ResponseResult<Void>
	 * @Description 单笔条码跳转支付接口
	 * @author yinmf
	 * @Title PmmsMpngController.java
	 * @Package com.course.app.webadmin.upms.controller
	 * @date 2023年5月7日 下午8:41:12
	 * @version V1.0
	 */
	@PostMapping("/singleStrokePay")
	public ResponseResult<Void> singleStrokePay() {
		return ResponseResult.success();
	}

	/**
	 * @Method refund
	 * @Return ResponseResult<Void>
	 * @Description 退款接口
	 * @author yinmf
	 * @Title PmmsMpngController.java
	 * @Package com.course.app.webadmin.upms.controller
	 * @date 2023年5月7日 下午8:41:12
	 * @version V1.0
	 */
	@PostMapping("/refund")
	public ResponseResult<Void> refund() {
		return ResponseResult.success();
	}

	/**
	 * @Method queryRefundOrder
	 * @Return ResponseResult<Void>
	 * @Description 退款查询接口
	 * @author yinmf
	 * @Title PmmsMpngController.java
	 * @Package com.course.app.webadmin.upms.controller
	 * @date 2023年5月7日 下午8:41:12
	 * @version V1.0
	 */
	@PostMapping("/queryRefundOrder")
	public ResponseResult<Void> queryRefundOrder() {
		return ResponseResult.success();
	}

	/**
	 * @Method queryPayOrder
	 * @Return ResponseResult<Void>
	 * @Description 查询订单
	 * @author yinmf
	 * @Title PmmsMpngController.java
	 * @Package com.course.app.webadmin.upms.controller
	 * @date 2023年5月7日 下午8:41:12
	 * @version V1.0
	 */
	@PostMapping("/queryPayOrder")
	public ResponseResult<Void> queryPayOrder() {
		return ResponseResult.success();
	}
}
