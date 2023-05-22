package com.course.app.webadmin.app.util;

import org.springframework.stereotype.Component;

import com.aliyuncs.CommonRequest;
import com.aliyuncs.CommonResponse;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.exceptions.ServerException;
import com.aliyuncs.http.MethodType;
import com.aliyuncs.profile.DefaultProfile;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class SendSmsUtil {

	public static final String VALIDATE_CODE = "SMS_8976282";//注册短信模板1
	public static final String accessKeyId = "LTAIIY8EhuTcnvxx";
	public static final String accessSecret = "pqsv5FRSckHgQo14ek3lKMcCaW4yJQ";
	public static final String signName = "身份验证";//注册平台名称 
	public static final String product = "来自曲靖一中占溢清源学校的";

	//验证码${code}，您正在进行${product}身份验证，打死不要告诉别人哦！
	/**
	 * 发送短信
	 */
	public static Boolean sendShortMessage(String phoneNumbers, String code) throws ClientException {
		DefaultProfile profile = DefaultProfile.getProfile("cn-hangzhou", accessKeyId, accessSecret);
		IAcsClient client = new DefaultAcsClient(profile);
		CommonRequest request = new CommonRequest();
		request.setSysMethod(MethodType.POST);
		request.setSysDomain("dysmsapi.aliyuncs.com");
		request.setSysVersion("2017-05-25");
		request.setSysAction("SendSms");
		request.putQueryParameter("RegionId", "cn-hangzhou");
		request.putQueryParameter("PhoneNumbers", phoneNumbers);
		request.putQueryParameter("SignName", signName);
		request.putQueryParameter("TemplateCode", VALIDATE_CODE);
		request.putQueryParameter("TemplateParam", "{\"product\":\"" + product + "\", \"code\":\"" + code + "\"}");
		CommonResponse response = null;
		try {
			response = client.getCommonResponse(request);
			log.info("response =" + response);
		} catch (ServerException e) {
			e.printStackTrace();
			log.error("验证动态码短信下发失败", e.getLocalizedMessage());
			return false;
		} catch (ClientException e) {
			e.printStackTrace();
			log.error("验证动态码短信下发失败2 ", e.getLocalizedMessage());
			return false;
		}
		return true;
	}
}
