package com.eth.model;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import com.alibaba.fastjson.JSONObject;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.dysmsapi.model.v20170525.SendSmsRequest;
import com.aliyuncs.dysmsapi.model.v20170525.SendSmsResponse;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.exceptions.ServerException;
import com.aliyuncs.http.MethodType;
import com.aliyuncs.profile.DefaultProfile;
import com.aliyuncs.profile.IClientProfile;
import com.jfinal.kit.PropKit;
import com.jfinal.plugin.activerecord.ActiveRecordPlugin;
import com.jfinal.plugin.activerecord.CaseInsensitiveContainerFactory;
import com.jfinal.plugin.druid.DruidPlugin;

import com.eth.model._MappingKit;

public class LynxUtil {
	public static String getCurrentTime() {
		Date d = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
		return sdf.format(d);
	}

	public static void startARP() {
		loadProp("application_prod.properties","application_test.properties", "application.properties");		
		System.out.println(PropKit.get("jdbc.url"));
		DruidPlugin druidDefault = new DruidPlugin(PropKit.get("jdbc.url"), PropKit.get("jdbc.username"),
				PropKit.get("jdbc.password"));
		// 配置ActiveRecord插件
		ActiveRecordPlugin arp = new ActiveRecordPlugin(druidDefault);
		// 配置属性名(字段名)大小写不敏感容器工厂
		arp.setContainerFactory(new CaseInsensitiveContainerFactory());
		// 显示SQL
		arp.setShowSql(false);
		// 所有配置在 MappingKit 中搞定
		_MappingKit.mapping(arp);
		// 与web环境唯一的不同是要手动调用一次相关插件的start()方法
		//druidDefault.start();
		druidDefault.start();
		arp.start();

	}
	private static void loadProp(String pro, String test, String dev) {
		try {
			PropKit.use(pro);
		}
		catch (Exception e) {
			try{
				PropKit.use(test);
			}
			catch(Exception e1) {
				PropKit.use(dev);
			}
		}
	}

	public static String loadJson(String url, String charset) {
		StringBuilder json = new StringBuilder();
		try {
			URL urlObject = new URL(url);
			URLConnection uc = urlObject.openConnection();
			BufferedReader in = new BufferedReader(new InputStreamReader(uc.getInputStream(), charset));
			String inputLine = null;
			while ((inputLine = in.readLine()) != null) {
				json.append(inputLine);
			}
			in.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return json.toString();
	}
	public static String loadJsonByPost(String url, String charset, String params) {
		StringBuilder json = new StringBuilder();
  		try {  
  			URL urlObject = new URL(url);
  			HttpURLConnection uc = (HttpURLConnection) urlObject.openConnection();
  			uc.setRequestMethod("POST");
  			uc.setDoOutput(true);
  			uc.setDoInput(true);
  			uc.setUseCaches(false);
  			if (params != null) {
  				OutputStreamWriter osw = new OutputStreamWriter(uc.getOutputStream(), charset);
  				osw.write(params);
  				osw.flush();
  				osw.close();
  			}
			BufferedReader in = new BufferedReader(new InputStreamReader(uc.getInputStream(), charset));
			String inputLine = null;
			while ((inputLine = in.readLine()) != null) {
				json.append(inputLine);
			}
			in.close();
  		} catch (Exception e) {
  			e.printStackTrace();
  		}
		return json.toString();
  	}

	public static JSONObject jsonMsg(Boolean success, String type, String msg) {
		JSONObject json = new JSONObject();
		json.put("success", success);
		json.put("type", type);
		json.put("msg", msg);
		return json;
	}
    
	/**
	 * 生成短信验证码*
	 * @param length 长度
	 * @return 指定长度的随机短信验证码
	 */
	public static final String randomSMSCode(int length) {
		boolean numberFlag = true;
		String retStr = "";
		String strTable = numberFlag ? "1234567890" : "1234567890abcdefghijkmnpqrstuvwxyz";
		int len = strTable.length();
		boolean bDone = true;
		do {
			retStr = "";
			int count = 0;
			for (int i = 0; i < length; i++) {
				double dblR = Math.random() * len;
				int intR = (int) Math.floor(dblR);
				char c = strTable.charAt(intR);
				if (('0' <= c) && (c <= '9')) {
					count++;
				}
				retStr += strTable.charAt(intR);
			}
			if (count >= 2) {
				bDone = false;
			}
		} while (bDone);
		return retStr;
	}

	/**
	 * 发送短信验证码*
	 * @param mobile 手机号码
	 * @param code 验证码
	 * @return 是否发送成功
	 */
	public static final boolean sendCode(String mobile, String code) {        
		//实现短信发送功能
		try {
			IClientProfile profile = DefaultProfile.getProfile("cn-hangzhou", PropKit.get("aliyunAccessKey"), PropKit.get("aliyunAccessSecret"));
			DefaultProfile.addEndpoint("cn-hangzhou", "cn-hangzhou", "Dysmsapi",  "dysmsapi.aliyuncs.com");
			IAcsClient client = new DefaultAcsClient(profile);
			SendSmsRequest request = new SendSmsRequest();
			request.setMethod(MethodType.POST);
			request.setPhoneNumbers(mobile);
			request.setSignName("铭泽数据");//控制台创建的签名名称
			request.setTemplateCode("SMS_129745419");//控制台创建的模板CODE
			request.setTemplateParam("{\"code\":\""+ code +"\"}");//短信模板中的变量；数字需要转换为字符串；个人用户每个变量长度必须小于15个字符。"
			SendSmsResponse httpResponse = client.getAcsResponse(request);
			if(httpResponse.getCode() != null && httpResponse.getCode().equals("OK")) {
				return true;
			}
		}catch (ServerException e) {
			e.printStackTrace();
		}catch (ClientException e) {
			e.printStackTrace();
		}
		return false;
    }

	public static boolean isEmpty(CharSequence cs) {
		return cs == null || cs.length() == 0;
	}

	//具体时间
	 public static String formatDuring(long time){
	    SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss z");
		String sd = sdf.format(new Date(time*1000));
	    return sd;
	}

	 public static String getFristDayOfThisMonth(){
		 Calendar cal = Calendar.getInstance();
		 int year = cal.get(Calendar.YEAR);
		 int month = cal.get(Calendar.MONTH)+1;
		 return year+"-"+month+"-"+"01";
	 }
	 public static String getPrevDay() {
		 Calendar cal = Calendar.getInstance();
		 cal.add(Calendar.DAY_OF_MONTH, -1);
		 int year = cal.get(Calendar.YEAR);
		 int month = cal.get(Calendar.MONTH)+1;
		 int day = cal.get(Calendar.DAY_OF_MONTH);
		 return year+"-"+month+"-"+day;
	 }
	 
	 public static String getToday() {
		 Calendar cal = Calendar.getInstance();
		 int year = cal.get(Calendar.YEAR);
		 int month = cal.get(Calendar.MONTH)+1;
		 int day = cal.get(Calendar.DAY_OF_MONTH);
		 System.out.println(year+"-"+month+"-"+day);
		 return year+"-"+month+"-"+day;
	 }
	 
	 public static String getLastMonth(){
		 Calendar cal = Calendar.getInstance();
		 cal.add(Calendar.MONTH, -1);
		 int year = cal.get(Calendar.YEAR);
		 int month = cal.get(Calendar.MONTH)+1;
		 int day = cal.get(Calendar.DAY_OF_MONTH);
		 System.out.println(year+"-"+month+"-"+day);
		 return year+"-"+month+"-"+day;
	 }

	//将时间戳转换成友好时间
	public static String format(long ms){
		long mss = System.currentTimeMillis() - ms*1000l;
		long days = mss / (1000 * 60 * 60 * 24);
		long hours = (mss % (1000 * 60 * 60 * 24)) / (1000 * 60 * 60);
		long minutes = (mss % (1000 * 60 * 60)) / (1000 * 60);
		long seconds = (mss % (1000 * 60)) / 1000;
		if (days < 1) {
			if (hours == 0 && minutes == 0 && seconds == 0) {
				return "刚刚";
			} else if(hours == 0 && minutes == 0){
				return seconds + "秒前";
			} else if (hours == 0 && minutes != 0) {
				return minutes + "分" + seconds + "秒前";
			} 
			return hours + "小时" + minutes + "分前";
		} else if (days >= 1 && days < 2) {
			return "昨天";
		} else if (days >= 2 && days < 3) {
			return "前天";
		} else if (days >= 3 && days <= 10) {
			return days + "天前";
		}
		return days + "天前";
	}
	public static String SHA1(String s){
		try {  
			MessageDigest digest = java.security.MessageDigest.getInstance("SHA-1");  
			digest.update(s.getBytes());  
			byte messageDigest[] = digest.digest();  
			StringBuffer hexString = new StringBuffer();  
			for (int i = 0; i < messageDigest.length; i++) {  
				String shaHex = Integer.toHexString(messageDigest[i] & 0xFF);  
				if (shaHex.length() < 2) {  
					hexString.append(0);  
				}  
					hexString.append(shaHex);  
			}  
			return hexString.toString();
		} catch (NoSuchAlgorithmException e) {  
			e.printStackTrace();  
		}
		return "";
	};
}
