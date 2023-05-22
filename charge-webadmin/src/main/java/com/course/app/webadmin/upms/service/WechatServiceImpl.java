package com.course.app.webadmin.upms.service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.apache.commons.lang3.StringUtils;
import org.redisson.api.RBucket;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.course.app.webadmin.constant.ComConstant.CacheTime;
import com.course.app.webadmin.upms.model.template.MessageDestination;
import com.course.app.webadmin.upms.model.template.TemplateData;
import com.course.app.webadmin.upms.service.impl.WechatService;

import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class WechatServiceImpl implements WechatService<Object> {

	@Autowired
	private RedissonClient redissonClient;

	@Override
	public String getAccessToken() {
		RBucket<String> cachedData = redissonClient.getBucket(cacahe_key_token);
		if (null != cachedData && StringUtils.isNotBlank(cachedData.get())) {
			return cachedData.get();
		}
		String url = URL_token.replace("APPID", AppId).replace("APPSECRET", AppSecret);
		String s = httpGetToken(url);
		log.info("httpGetToken :" + s);
		JSONObject jsonObject = JSONUtil.parseObj(s);
		if (null == jsonObject || !jsonObject.containsKey("access_token")) {
			return null;
		}
		String token = jsonObject.getStr("access_token");
		String expiresIin = jsonObject.getStr("expires_in");
		cachedData.set(token, CacheTime.EXPIR_TWO_HOUR, TimeUnit.SECONDS);
		log.info("AccessTonken:" + token + "\t" + "是否过期:" + expiresIin);
		return token;
	}

	@Override
	public String httpGetToken(String url) {
		try {
			URL urlObject = new URL(url);
			URLConnection urlConnection = urlObject.openConnection();
			InputStream inputStream = urlConnection.getInputStream();
			byte[] b = new byte[1024];
			int len;
			StringBuilder sb = new StringBuilder();
			while ((len = inputStream.read(b)) != -1) {
				sb.append(new String(b, 0, len));
			}
			return sb.toString();
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public String postTemple(String url, MessageDestination messageDestination) {
		PrintWriter out = null;
		BufferedReader in = null;
		String result = "";
		String param = JSONUtil.toJsonStr(messageDestination);
		try {
			URL realUrl = new URL(url);
			// 打开和URL之间的连接
			URLConnection conn = realUrl.openConnection();
			// 设置通用的请求属性
			conn.setRequestProperty("accept", "*/*");
			conn.setRequestProperty("connection", "Keep-Alive");
			conn.setRequestProperty("Accept-Charset", "utf-8");
			conn.setRequestProperty("user-agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
			// 发送POST请求必须设置如下两行
			conn.setDoOutput(true);
			conn.setDoInput(true);
			// 获取URLConnection对象对应的输出流
			out = new PrintWriter(conn.getOutputStream());
			// 发送请求参数
			out.print(param);
			// flush输出流的缓冲
			out.flush();
			// 定义BufferedReader输入流来读取URL的响应
			in = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));
			String line;
			while ((line = in.readLine()) != null) {
				result += line;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		//使用finally块来关闭输出流、输入流
		finally {
			try {
				if (out != null) {
					out.close();
				}
				if (in != null) {
					in.close();
				}
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}
		return result;
	}

	@Override
	public String postTemple(String openId, Map<String, TemplateData> map) {
		String accessToken = getAccessToken();
		if (null == accessToken) {
			log.info("accessToken post = 获取不正确 ");
			return "ok";
		}
		String url = "https://api.weixin.qq.com/cgi-bin/message/subscribe/send?access_token=" + accessToken;
		MessageDestination messageDestination = new MessageDestination();
		messageDestination.setTouser(openId);
		messageDestination.setTemplate_id(template_id);
		messageDestination.setPage(pageString);
		messageDestination.setData(map);
		messageDestination.setMiniprogram_state(miniprogram_state);
		messageDestination.setLang("zh_CN");
		//这个post方法我写在了上面的GetUtil工具类里面
		String post = postTemple(url, messageDestination);
		log.info("postTemple post = " + post);
		return "ok";
	}

	@Override
	@Async
	public void sendListTemplateData(List<JSONObject> listJsonObjects) {
		for (JSONObject jsonObject : listJsonObjects) {
			if (jsonObject.containsKey("openId")) {
				String openId = jsonObject.getStr("openId");
				Map<String, TemplateData> mapTemp = new HashMap<String, TemplateData>();
				mapTemp.put("thing1", new TemplateData(jsonObject.getStr("auditTitle")));
				mapTemp.put("thing2", new TemplateData(jsonObject.getStr("auditState")));
				mapTemp.put("date5", new TemplateData(jsonObject.getStr("auditTime")));
				mapTemp.put("thing6", new TemplateData(jsonObject.getStr("studentName")));
				postTemple(openId, mapTemp);
			}
		}
	}
}
