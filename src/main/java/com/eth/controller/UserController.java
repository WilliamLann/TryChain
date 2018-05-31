package com.eth.controller;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Date;
import java.util.UUID;

import com.alibaba.fastjson.JSONObject;
import com.eth.config.WeixinConfig;
import com.eth.model.LynxUtil;
import com.eth.model.TbcAddress;
import com.eth.model.TbcTxInter;
import com.eth.model.LynxUser;
import com.jfinal.core.Controller;
import com.jfinal.kit.PropKit;

public class UserController  extends Controller {
	private static final String WX_AUTH_APP_ID ="wx.auth.appId";
	private static final String WX_AUTH_APP_SECRET ="wx.auth.appSecret";
	private static final String WX_AUTH_CALLBACK_URL = "wx.auth.callbackUrl";
	private static final String WX_MAUTH_APP_ID ="wx.mauth.appId";
	private static final String WX_MAUTH_APP_SECRET ="wx.mauth.appSecret";
	private static final String CodeUrl = "https://open.weixin.qq.com/connect/oauth2/authorize?appid=APPID&redirect_uri=REDIRECT_URI&response_type=code&scope=SCOPE&state=STATE#wechat_redirect";
	private static final String TokenUrl = "https://api.weixin.qq.com/sns/oauth2/access_token?appid=APPID&secret=SECRET&code=CODE&grant_type=authorization_code";
	private static final String UserUrl = "https://api.weixin.qq.com/sns/userinfo?access_token=ACCESS_TOKEN&openid=OPENID&lang=zh_CN";
    private static final String ShortUrl = "https://api.weixin.qq.com/cgi-bin/shorturl?access_token=ACCESS_TOKEN";
    private static final String ShortParams = "{\"action\":\"long2short\",\"long_url\":\"LONG_URL\"}";
	
    //渲染注册页面
    public void register(){
    	String smsCaptcha = Long.toString(new Date().getTime());
    	String id = getPara("id");
    	setSessionAttr("smsCaptcha", smsCaptcha);
    	setAttr("nav", "register");
    	setAttr("ethUsername", getSessionAttr("ethUsername"));
    	setAttr("redirectUrl", getPara("redirectUrl", "/tbc/tbc"));
    	setAttr("smsCaptcha", smsCaptcha);
    	LynxUser user = LynxUser.dao.findById(id);
    	if(user!=null){
    		setAttr("username", user.getUserName());
    		setAttr("userid", user.getUserId());
    	}
    	render("/user/register.html");
    }

    public void getUsername(){
    	String id = getPara("id");
    	LynxUser user = LynxUser.dao.findById(id);
    	if(user!=null)
    		renderJson(LynxUtil.jsonMsg(true, "", user.getUserName()));
    	else
    		renderJson(LynxUtil.jsonMsg(false, "", null));
    }
    
    //渲染登录页面接口
    public void login(){
    	String smsCaptcha = Long.toString(new Date().getTime());
    	setSessionAttr("smsCaptcha", smsCaptcha);
    	setAttr("nav", "login");
    	setAttr("ethUsername", getSessionAttr("ethUsername"));
    	setAttr("redirectUrl", "/tbc/tbc");
    	setAttr("smsCaptcha", smsCaptcha);
		setAttr("callbackUrl", PropKit.get(WX_AUTH_CALLBACK_URL));
		setAttr("appId", PropKit.get(WX_AUTH_APP_ID));
    	render("/user/login.html");
    }
    
  //渲染邀请页面
    public void invite(){
    	String id = getSessionAttr("ethUserid", "");
    	setAttr("ethUsername", getSessionAttr("ethUsername"));
    	String url = PropKit.get(WX_AUTH_CALLBACK_URL)+"/user/register?id="+id;
    	setAttr("url", url);
    	setAttr("urlMsg", "试试区块链，和我一起在这里走进区块链世界吧! "+url);
    	render("/user/invite.html");
    }
	
	//发送短信接口
	public void sendSmsCode() {
		String mobile = getPara("mobile");
		if (LynxUtil.isEmpty(mobile) || !mobile.matches("^1\\d{10}$")) {
    		renderJson(LynxUtil.jsonMsg(false, "发送", "手机号输入有误"));
        	return;
		}
		String captcha = getPara("captcha");
		if (getCookie("ignore")==null && (LynxUtil.isEmpty(captcha) || !captcha.equals(getSessionAttr("smsCaptcha")))) {
			renderJson(LynxUtil.jsonMsg(false, "发送", "校验码有误，请刷新后重试"));
			return;
		}
		String smsCode = LynxUtil.randomSMSCode(6);
		//发送短信验证码
		Boolean sendSuccess = LynxUtil.sendCode(mobile, smsCode);
		//Boolean sendSuccess = true;
		if(sendSuccess){
			setSessionAttr("mobile", mobile);
			setSessionAttr("smsCode", smsCode);
		}
		//发送短信验证码
		renderJson(sendSuccess?LynxUtil.jsonMsg(true, "发送", "发送成功"):LynxUtil.jsonMsg(false, "发送", "手机号错误或发送太频繁"));
		//renderJson(LynxUtil.jsonMsg(true, "发送", "测试短信验证码："+smsCode));
	}
    
    //判断用户是否存在，不存在返回true，注册时检验用
    public void checkUsernameNotExist(){
        String username = getPara("username");
        if(LynxUtil.isEmpty(username)){
        	renderJson(false);
        	return;
        }
        renderJson(!LynxUser.dao.usernameExist(username));
    }
    
    //判断用户是否存在，存在返回true，账号密码登录时检验用
    public void checkUsernameExist(){
        String username = getPara("username");
        if(LynxUtil.isEmpty(username)){
        	renderJson(false);
        	return;
        }
        renderJson(LynxUser.dao.usernameExist(username));
    }
	
	//注册提交接口
    public void registerSubmit() throws Exception{
        String username = getPara("username");
        String password = getPara("password");
        String repassword = getPara("repassword");
        String code = getPara("code");
        if(LynxUtil.isEmpty(username) || LynxUtil.isEmpty(password)){
    		renderJson(LynxUtil.jsonMsg(false, "注册", "账号或密码不能为空"));
        	return;
        }
        if(LynxUtil.isEmpty(repassword) || !password.equals(repassword)){
    		renderJson(LynxUtil.jsonMsg(false, "注册", "两次输入密码不一致"));
        	return;
        }
        if(LynxUser.dao.findByUsername(username)!=null){
    		renderJson(LynxUtil.jsonMsg(false, "注册", "账号已存在"));
        	return;
        }
        if(LynxUtil.isEmpty(code) || !code.equals(getSessionAttr("smsCode")) || !username.equals(getSessionAttr("mobile"))){
    		renderJson(LynxUtil.jsonMsg(false, "注册", "验证码与手机号不符"));
        	return;
        }
    	LynxUser user = createUser(username, password, null, getPara("id", ""));
    	createState(user);
		renderJson(LynxUtil.jsonMsg(true, "注册", "注册成功"));
    }
    
    //登录提交接口
    public void loginSubmit() {
        String username = getPara("username");
        String password = getPara("password");
        if(LynxUtil.isEmpty(username) || LynxUtil.isEmpty(password)){
    		renderJson(LynxUtil.jsonMsg(false, "登录", "账号或密码不能为空"));
        	return;
        }
        LynxUser user = LynxUser.dao.findByUsername(username);
        if(user==null || !password.equals(user.getPassword())){
    		renderJson(LynxUtil.jsonMsg(false, "登录", "账号或密码错误"));
        	return;
        }
    	createState(user);
		renderJson(LynxUtil.jsonMsg(true, "登录", "登录成功"));
//        Subject subject = SecurityUtils.getSubject();
//        UsernamePasswordToken token = new UsernamePasswordToken(username, password);
//        try {
//            subject.login(token);
//            //renderJson(CommonService.ajaxSuccess());
//        } catch (AuthenticationException e) {
//            //虽然在realm中有具体的错误信息，但是安全起见，统一返回登录失败
//            //renderJson(CommonService.ajaxError("登陆失败"));
//        }
    }
    
    //快捷登录提交接口
    public void mobileLoginSubmit() throws Exception{
        String username = getPara("mobile");
        String code = getPara("code");
        if(LynxUtil.isEmpty(username) || LynxUtil.isEmpty(code)){
    		renderJson(LynxUtil.jsonMsg(false, "登录", "账号或验证码不能为空"));
        	return;
        }
        if(!code.equals(getSessionAttr("smsCode")) || !username.equals(getSessionAttr("mobile"))){
    		renderJson(LynxUtil.jsonMsg(false, "登录", "验证码与手机号不符"));
        	return;
        }
        LynxUser user = LynxUser.dao.findByUsername(username);
        if(user==null){
    		user = createUser(username, username+new Date().getTime(), null);
        }
    	createState(user);
		renderJson(LynxUtil.jsonMsg(true, "登录", "登录成功"));
    }
    
    //微信扫码登录、网页授权登录(state="STATE_")回调接口
    public void callback() throws Exception{
		String code = getPara("code");
		String state = getPara("state", "/");
        if(LynxUtil.isEmpty(code)){
    		setAttr("errMsg", "微信登录错误，未获取到code");
        	render("/common/error.html");
        	return;
        }
        String tokenUrl = TokenUrl.replace("APPID", PropKit.get(WX_AUTH_APP_ID)).replace("SECRET", PropKit.get(WX_AUTH_APP_SECRET)).replace("CODE", code);
        String id = null;
        if(state.indexOf("STATE_")==0){
        	tokenUrl = TokenUrl.replace("APPID", PropKit.get(WX_MAUTH_APP_ID)).replace("SECRET", PropKit.get(WX_MAUTH_APP_SECRET)).replace("CODE", code);
        	String[] ss = state.split("_");
        	if(ss.length>1) id = state.split("_")[1];
        	state = "/m/pages/index.html";
        }
		JSONObject json = JSONObject.parseObject(LynxUtil.loadJson(tokenUrl,"utf-8"));
		System.out.println(json);
		String unionid = json.getString("unionid");
		if(LynxUtil.isEmpty(unionid)){
    		setAttr("errMsg", "微信登录错误，无效的code");
        	render("/common/error.html");
			return;
		}
		String userUrl = UserUrl.replace("ACCESS_TOKEN", json.getString("access_token")).replace("OPENID", json.getString("openid"));
		JSONObject userJson = JSONObject.parseObject(LynxUtil.loadJson(userUrl,"utf-8"));
		setSessionAttr("ethUserimg", userJson.getString("headimgurl"));
		LynxUser user = LynxUser.dao.findByWechat(unionid);
		if(user==null){
			user = createUser(userJson.getString("nickname"), unionid, unionid, id);
		}
    	createState(user);
        redirect(state);
    }

    public void getInviteUrl(){
    	String id = getSessionAttr("ethUserid", "");
    	String url = PropKit.get(WX_AUTH_CALLBACK_URL)+"/user/redirectWeixin?id="+id;
    	renderJson(LynxUtil.jsonMsg(!LynxUtil.isEmpty(id), "", url));
    }
    
    //有次数限制，不使用
//    public void getShortMobileInviteUrl(){
//    	String id = getSessionAttr("ethUserid", "");
//    	String url = ShortUrl.replace("ACCESS_TOKEN", WeixinConfig.getAccessToken());
//    	String long_url = CodeUrl.replace("APPID", PropKit.get(WX_MAUTH_APP_ID)).replace("REDIRECT_URI", PropKit.get(WX_AUTH_CALLBACK_URL)+"/user/callback")
//    			.replace("SCOPE", "snsapi_userinfo").replace("STATE", "STATE_"+id);
//    	String params = ShortParams.replace("LONG_URL", long_url);
//    	JSONObject json = JSONObject.parseObject(LynxUtil.loadJsonByPost(url, "utf-8", params));
//    	renderJson(LynxUtil.jsonMsg(!LynxUtil.isEmpty(id), "", json.getString("short_url")));
//    }
    
    public void redirectWeixin(){
    	String id = getPara("id", "");
    	String userAgent = getHeader("User-Agent");
    	String url = PropKit.get(WX_AUTH_CALLBACK_URL)+"/user/register?id="+id;
    	if(userAgent.indexOf("MicroMessenger")>0)
    		url = CodeUrl.replace("APPID", PropKit.get(WX_MAUTH_APP_ID)).replace("REDIRECT_URI", PropKit.get(WX_AUTH_CALLBACK_URL)+"/user/callback")
    			.replace("SCOPE", "snsapi_userinfo").replace("STATE", "STATE_"+getPara("id", ""));
    	else if(userAgent.matches("(.*)(iPhone|iPod|Android|ios|iPad|webOS|BlackBerry)(.*)")){
    		url = PropKit.get(WX_AUTH_CALLBACK_URL)+"/m/pages/index.html?id="+id;
    	}
    	redirect(url);
    }
    
    public void getWeixinJSConfig(){
    	JSONObject json = new JSONObject();
    	String appId = PropKit.get(WX_MAUTH_APP_ID);
    	String url = getPara("url", "");
    	System.out.println(url);
    	int timestamp = new Long(new Date().getTime()/1000).intValue();
    	String nonceStr = UUID.randomUUID().toString();
    	String s = "jsapi_ticket="+WeixinConfig.getJsapiTicket()+"&noncestr="+nonceStr+"&timestamp="+timestamp+"&url="+url;
    	String signature = LynxUtil.SHA1(s);
    	JSONObject config = new JSONObject();
    	config.put("appId", appId);
    	config.put("timestamp", timestamp);
    	config.put("nonceStr", nonceStr);
    	config.put("signature", signature);
    	String title = "试试区块链";
    	String desc = "快来试试区块链吧！";
    	String link = PropKit.get(WX_AUTH_CALLBACK_URL)+"/user/redirectWeixin?id="+getSessionAttr("ethUserid", "");
    	String imgUrl = "http://www.trychain.net/favicon.ico";
    	JSONObject content = new JSONObject();
    	content.put("title", title);
    	content.put("desc", desc);
    	content.put("link", link);
    	content.put("imgUrl", imgUrl);
    	json.put("config", config);
    	json.put("content", content);
    	renderJson(json);
    }
    
    //创建用户登录状态
    private void createState(LynxUser user){
    	getSession().setMaxInactiveInterval(7*24*60*60);
    	setSessionAttr("ethUsername", user.getUserName());
    	setSessionAttr("ethAddress", user.getEthAddress());
    	setSessionAttr("ethUserid", user.getUserId().toString());
    	String userimg = getSessionAttr("ethUserimg");
    	try {
			setCookie("ethUsername", URLEncoder.encode(user.getUserName(), "UTF-8"), 7*24*60*60, "/");
	    	setCookie("ethAddress", URLEncoder.encode(user.getEthAddress(), "UTF-8"), 7*24*60*60, "/");
	    	if(userimg!=null) setCookie("ethUserimg", URLEncoder.encode(userimg, "UTF-8"), 7*24*60*60, "/");
	    	else removeCookie("ethUserimg", "/");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
    }
    
    //创建新用户
    private LynxUser createUser(String username, String password, String unionid) throws Exception {
    	return createUser(username, password, unionid, null);
    }
    private LynxUser createUser(String username, String password, String unionid, String id) throws Exception {
		LynxUser userAdmin = LynxUser.dao.findById(15);
    	LynxUser user = new LynxUser();
    	TbcAdmin admin = TbcAdmin.getInstance();
    	String eth_filename = admin.newWallet(password);
    	int idx = eth_filename.lastIndexOf(".json");
    	String ethAddress = "0x"+eth_filename.substring(idx-40,idx);
    	user.setEthFilename(eth_filename);
    	user.setEthAddress(ethAddress);
    	user.setUserName(username);
    	user.setPassword(password);
    	user.setWechat(unionid);
    	TbcAddress tbc = new TbcAddress();
    	tbc.setAddress(ethAddress);
    	tbc.setValue(0L);
    	tbc.setPending(0L);
    	tbc.save();
    	long value = 256;
    	TbcTxInter.trade(userAdmin.getEthAddress(), ethAddress, value, "系统赠送");
    	if(!LynxUtil.isEmpty(id)){
    		LynxUser u = LynxUser.dao.findById(id);
    		if(u!=null){
    			String address = u.getEthAddress();
    			user.setParentEthAddress(address);
    	    	TbcTxInter.trade(userAdmin.getEthAddress(), address, value/2, "邀请奖励");
    	    	if(u.getParentEthAddress()!=null){
    	    		TbcTxInter.trade(userAdmin.getEthAddress(), u.getParentEthAddress(), value/4, "邀请奖励");
    	    	}
    		}
    	}
    	user.save();
    	return user;
    }
    
    //退出接口
    public void logout(){
        String redirectUrl = getPara("redirectUrl", "/");
    	removeSessionAttr("ethUsername");
    	removeSessionAttr("ethAddress");
    	removeSessionAttr("ethUserid");
    	removeSessionAttr("ethUserimg");
    	removeCookie("ethUsername", "/");
    	removeCookie("ethAddress", "/");
    	removeCookie("ethUserimg", "/");
        redirect(redirectUrl);
    }
}
