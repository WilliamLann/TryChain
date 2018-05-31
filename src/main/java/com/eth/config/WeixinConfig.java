package com.eth.config;

import java.util.Date;

import com.alibaba.fastjson.JSONObject;
import com.eth.model.LynxUtil;
import com.jfinal.kit.PropKit;

public class WeixinConfig {
	private static String access_token;
	private static long token_expires;
	private static String jsapi_ticket;
	private static long ticket_expires;
	private static final String WX_MAUTH_APP_ID ="wx.mauth.appId";
	private static final String WX_MAUTH_APP_SECRET ="wx.mauth.appSecret";
	private static final String TokenUrl = "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=APPID&secret=APPSECRET";
	private static final String JsapiTicketUrl = "https://api.weixin.qq.com/cgi-bin/ticket/getticket?access_token=ACCESS_TOKEN&type=jsapi";
	public static String getAccessToken(){
		if(access_token == null || new Date().getTime()/1000 > token_expires){
			String url = TokenUrl.replace("APPID", PropKit.get(WX_MAUTH_APP_ID)).replace("APPSECRET", PropKit.get(WX_MAUTH_APP_SECRET));
			JSONObject json = JSONObject.parseObject(LynxUtil.loadJson(url,"utf-8"));
			access_token = json.getString("access_token");
			token_expires = new Date().getTime()/1000 + json.getLongValue("expires_in");
		}
		System.out.println(access_token);
		return access_token;
	}
	private static void refreshAccessToken(){
		String url = TokenUrl.replace("APPID", PropKit.get(WX_MAUTH_APP_ID)).replace("APPSECRET", PropKit.get(WX_MAUTH_APP_SECRET));
		JSONObject json = JSONObject.parseObject(LynxUtil.loadJson(url,"utf-8"));
		access_token = json.getString("access_token");
		token_expires = new Date().getTime()/1000 + json.getLongValue("expires_in");
	}
	public static String getJsapiTicket(){
		if(jsapi_ticket == null || new Date().getTime()/1000 > ticket_expires){
			String url = JsapiTicketUrl.replace("ACCESS_TOKEN", getAccessToken());
			JSONObject json = JSONObject.parseObject(LynxUtil.loadJson(url,"utf-8"));
			jsapi_ticket = json.getString("ticket");
			if(jsapi_ticket==null){
				refreshAccessToken();
				url = JsapiTicketUrl.replace("ACCESS_TOKEN", getAccessToken());
				json = JSONObject.parseObject(LynxUtil.loadJson(url,"utf-8"));
				jsapi_ticket = json.getString("ticket");
			}
			ticket_expires = new Date().getTime()/1000 + json.getLongValue("expires_in");
		}
		System.out.println(jsapi_ticket);
		return jsapi_ticket;
		
	}
}
