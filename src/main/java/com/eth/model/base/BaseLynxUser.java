package com.eth.model.base;

import com.jfinal.plugin.activerecord.Model;
import com.jfinal.plugin.activerecord.IBean;

/**
 * Generated by JFinal, do not modify this file.
 */
@SuppressWarnings("serial")
public abstract class BaseLynxUser<M extends BaseLynxUser<M>> extends Model<M> implements IBean {

	public void setUserId(java.lang.Integer userId) {
		set("user_id", userId);
	}
	
	public java.lang.Integer getUserId() {
		return getInt("user_id");
	}

	public void setUserName(java.lang.String userName) {
		set("user_name", userName);
	}
	
	public java.lang.String getUserName() {
		return getStr("user_name");
	}

	public void setPassword(java.lang.String password) {
		set("password", password);
	}
	
	public java.lang.String getPassword() {
		return getStr("password");
	}

	public void setPhone(java.lang.String phone) {
		set("phone", phone);
	}
	
	public java.lang.String getPhone() {
		return getStr("phone");
	}

	public void setWechat(java.lang.String wechat) {
		set("wechat", wechat);
	}
	
	public java.lang.String getWechat() {
		return getStr("wechat");
	}

	public void setEthAddress(java.lang.String ethAddress) {
		set("eth_address", ethAddress);
	}
	
	public java.lang.String getEthAddress() {
		return getStr("eth_address");
	}

	public void setParentEthAddress(java.lang.String parentEthAddress) {
		set("parent_eth_address", parentEthAddress);
	}
	
	public java.lang.String getParentEthAddress() {
		return getStr("parent_eth_address");
	}

	public void setEthFilename(java.lang.String ethFilename) {
		set("eth_filename", ethFilename);
	}
	
	public java.lang.String getEthFilename() {
		return getStr("eth_filename");
	}

}
