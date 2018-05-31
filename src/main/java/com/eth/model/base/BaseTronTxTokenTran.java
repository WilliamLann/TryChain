package com.eth.model.base;

import com.jfinal.plugin.activerecord.Model;
import com.jfinal.plugin.activerecord.IBean;

/**
 * Generated by JFinal, do not modify this file.
 */
@SuppressWarnings("serial")
public abstract class BaseTronTxTokenTran<M extends BaseTronTxTokenTran<M>> extends Model<M> implements IBean {

	public void setHash(java.lang.String hash) {
		set("hash", hash);
	}
	
	public java.lang.String getHash() {
		return getStr("hash");
	}

	public void setToken(java.lang.String token) {
		set("token", token);
	}
	
	public java.lang.String getToken() {
		return getStr("token");
	}

	public void setOwnerAddress(java.lang.String ownerAddress) {
		set("ownerAddress", ownerAddress);
	}
	
	public java.lang.String getOwnerAddress() {
		return getStr("ownerAddress");
	}

	public void setToAddress(java.lang.String toAddress) {
		set("toAddress", toAddress);
	}
	
	public java.lang.String getToAddress() {
		return getStr("toAddress");
	}

	public void setAmount(java.lang.String amount) {
		set("amount", amount);
	}
	
	public java.lang.String getAmount() {
		return getStr("amount");
	}

}