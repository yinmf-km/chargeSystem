package com.course.app.webadmin.upms.controller;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.redisson.api.RBucket;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.course.app.common.core.object.TokenData;
import com.course.app.common.core.util.ContextUtil;
import com.course.app.common.core.util.IpUtil;
import com.course.app.common.core.util.JwtUtil;
import com.course.app.common.core.util.MyCommonUtil;
import com.course.app.common.core.util.RedisKeyUtil;
import com.course.app.webadmin.config.ApplicationConfig;
import com.course.app.webadmin.upms.model.SysStudent;

import cn.hutool.json.JSONObject;

public class H5BaseController {

	@Autowired
	RedissonClient redissonClient;
	@Autowired
	ApplicationConfig appConfig;

	public JSONObject initToken(SysStudent sysStudent) {
		int deviceType = MyCommonUtil.getDeviceType();
		Map<String, Object> claims = new HashMap<>(1);
		String sessionId = sysStudent.getStudentName() + "_" + deviceType + "_" + MyCommonUtil.generateUuid();
		claims.put("sessionId", sessionId);
		String token = JwtUtil.generateToken(claims, appConfig.getExpiration(), appConfig.getTokenSigningKey());
		JSONObject jsonData = new JSONObject();
		jsonData.set(TokenData.REQUEST_ATTRIBUTE_NAME, token);
		TokenData tokenData = this.buildTokenData(sysStudent, sessionId, deviceType);
		this.putTokenDataToSessionCache(tokenData);
		// 这里手动将TokenData存入request，便于OperationLogAspect统一处理操作日志。
		TokenData.addToRequest(tokenData);
		return jsonData;
	}

	public TokenData buildTokenData(SysStudent sysStudent, String sessionId, int deviceType) {
		TokenData tokenData = new TokenData();
		tokenData.setSessionId(sessionId);
		tokenData.setUserId(sysStudent.getStudentId());
		tokenData.setPhoneNum(sysStudent.getPhoneNum());
		tokenData.setLoginName(sysStudent.getStudentName());
		tokenData.setLoginIp(IpUtil.getRemoteIpAddress(ContextUtil.getHttpRequest()));
		tokenData.setLoginTime(new Date());
		tokenData.setDeviceType(deviceType);
		return tokenData;
	}

	public void putTokenDataToSessionCache(TokenData tokenData) {
		String sessionIdKey = RedisKeyUtil.makeSessionIdKey(tokenData.getSessionId());
		String sessionData = JSON.toJSONString(tokenData, SerializerFeature.WriteNonStringValueAsString);
		RBucket<String> bucket = redissonClient.getBucket(sessionIdKey);
		bucket.set(sessionData);
		bucket.expire(appConfig.getSessionExpiredSeconds(), TimeUnit.SECONDS);
	}
}
