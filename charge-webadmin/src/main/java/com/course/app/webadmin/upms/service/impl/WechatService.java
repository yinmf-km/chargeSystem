package com.course.app.webadmin.upms.service.impl;

import java.util.List;
import java.util.Map;

import com.course.app.webadmin.upms.model.template.MessageDestination;
import com.course.app.webadmin.upms.model.template.TemplateData;

import cn.hutool.json.JSONObject;

public interface WechatService<T> {

	String AppId = "wxf85d50bac7f51e85";
	String AppSecret = "7bf45d6b73523055f7ccaf056a8e0c05";
	String cacahe_key_token = "wechat_token";
	String template_id = "626offIq08yiLhcL5wWrTAexfUSvPHuFDNb1I1IxXAU";
	String pageString = "pages/results/index";
	String miniprogram_state = "trial";
	//跳转小程序类型：developer为开发版；trial为体验版；formal为正式版；默认为正式版
	String URL_token = "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=APPID&secret=APPSECRET";

	String getAccessToken();

	String httpGetToken(String url);

	String postTemple(String url, MessageDestination messageDestination);

	String postTemple(String openId, Map<String, TemplateData> map);

	void sendListTemplateData(List<JSONObject> listJsonObjects);
}
