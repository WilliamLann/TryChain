package com.eth.model.base;

import com.jfinal.plugin.activerecord.Model;
import com.jfinal.plugin.activerecord.IBean;

/**
 * Generated by JFinal, do not modify this file.
 */
@SuppressWarnings("serial")
public abstract class BaseTronTx<M extends BaseTronTx<M>> extends Model<M> implements IBean {

	public void setHash(java.lang.String hash) {
		set("hash", hash);
	}
	
	public java.lang.String getHash() {
		return getStr("hash");
	}

	public void setBlock(java.lang.Long block) {
		set("block", block);
	}
	
	public java.lang.Long getBlock() {
		return getLong("block");
	}

	public void setTimestamp(java.util.Date timestamp) {
		set("timestamp", timestamp);
	}
	
	public java.util.Date getTimestamp() {
		return get("timestamp");
	}

	public void setConfirmed(java.lang.Boolean confirmed) {
		set("confirmed", confirmed);
	}
	
	public java.lang.Boolean getConfirmed() {
		return get("confirmed");
	}

	public void setOwnerAddress(java.lang.String ownerAddress) {
		set("ownerAddress", ownerAddress);
	}
	
	public java.lang.String getOwnerAddress() {
		return getStr("ownerAddress");
	}

	public void setContractType(java.lang.String contractType) {
		set("contractType", contractType);
	}
	
	public java.lang.String getContractType() {
		return getStr("contractType");
	}

}
