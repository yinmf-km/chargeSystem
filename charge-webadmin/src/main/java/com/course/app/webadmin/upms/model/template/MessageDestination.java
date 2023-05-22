package com.course.app.webadmin.upms.model.template;

import java.util.Map;

import lombok.Data;

//模板消息实体类
@Data
public class MessageDestination {

	//接收者（用户）的 openid
	private String touser;
	//所需下发的订阅模板id
	private String template_id;
	//点击模板卡片后的跳转页面，仅限本小程序内的页面。支持带参数,（示例index?foo=bar）。该字段不填则模板无跳
	private String page;
	//模板内容，格式形如 { "key1": { "value": any }, "key2": { "value": any } }
	private Map<String, TemplateData> data;
	private String miniprogram_state;
	private String lang;
}
