package com.eth.model.base;

import com.jfinal.plugin.activerecord.Model;
import com.jfinal.plugin.activerecord.IBean;

/**
 * Generated by JFinal, do not modify this file.
 */
@SuppressWarnings("serial")
public abstract class BaseTbcAddress<M extends BaseTbcAddress<M>> extends Model<M> implements IBean {

	public void setAddress(java.lang.String address) {
		set("address", address);
	}
	
	public java.lang.String getAddress() {
		return getStr("address");
	}

	public void setValue(java.lang.Long value) {
		set("value", value);
	}
	
	public java.lang.Long getValue() {
		return getLong("value");
	}

	public void setPending(java.lang.Long pending) {
		set("pending", pending);
	}
	
	public java.lang.Long getPending() {
		return getLong("pending");
	}

}