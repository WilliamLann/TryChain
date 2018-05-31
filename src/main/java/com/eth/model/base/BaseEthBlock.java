package com.eth.model.base;

import com.jfinal.plugin.activerecord.Model;
import com.jfinal.plugin.activerecord.IBean;

/**
 * Generated by JFinal, do not modify this file.
 */
@SuppressWarnings("serial")
public abstract class BaseEthBlock<M extends BaseEthBlock<M>> extends Model<M> implements IBean {

	public void setNumber(java.lang.String number) {
		set("number", number);
	}
	
	public java.lang.String getNumber() {
		return getStr("number");
	}

	public void setDifficulty(java.lang.String difficulty) {
		set("difficulty", difficulty);
	}
	
	public java.lang.String getDifficulty() {
		return getStr("difficulty");
	}

	public void setGasLimit(java.lang.String gasLimit) {
		set("gasLimit", gasLimit);
	}
	
	public java.lang.String getGasLimit() {
		return getStr("gasLimit");
	}

	public void setGasUsed(java.lang.String gasUsed) {
		set("gasUsed", gasUsed);
	}
	
	public java.lang.String getGasUsed() {
		return getStr("gasUsed");
	}

	public void setHash(java.lang.String hash) {
		set("hash", hash);
	}
	
	public java.lang.String getHash() {
		return getStr("hash");
	}

	public void setMiner(java.lang.String miner) {
		set("miner", miner);
	}
	
	public java.lang.String getMiner() {
		return getStr("miner");
	}

	public void setMixHash(java.lang.String mixHash) {
		set("mixHash", mixHash);
	}
	
	public java.lang.String getMixHash() {
		return getStr("mixHash");
	}

	public void setNonce(java.lang.String nonce) {
		set("nonce", nonce);
	}
	
	public java.lang.String getNonce() {
		return getStr("nonce");
	}

	public void setParentHash(java.lang.String parentHash) {
		set("parentHash", parentHash);
	}
	
	public java.lang.String getParentHash() {
		return getStr("parentHash");
	}

	public void setReceiptsRoot(java.lang.String receiptsRoot) {
		set("receiptsRoot", receiptsRoot);
	}
	
	public java.lang.String getReceiptsRoot() {
		return getStr("receiptsRoot");
	}

	public void setSha3Uncles(java.lang.String sha3Uncles) {
		set("sha3Uncles", sha3Uncles);
	}
	
	public java.lang.String getSha3Uncles() {
		return getStr("sha3Uncles");
	}

	public void setSize(java.lang.String size) {
		set("size", size);
	}
	
	public java.lang.String getSize() {
		return getStr("size");
	}

	public void setStateRoot(java.lang.String stateRoot) {
		set("stateRoot", stateRoot);
	}
	
	public java.lang.String getStateRoot() {
		return getStr("stateRoot");
	}

	public void setTimestamp(java.lang.String timestamp) {
		set("timestamp", timestamp);
	}
	
	public java.lang.String getTimestamp() {
		return getStr("timestamp");
	}

	public void setTotalDifficulty(java.lang.String totalDifficulty) {
		set("totalDifficulty", totalDifficulty);
	}
	
	public java.lang.String getTotalDifficulty() {
		return getStr("totalDifficulty");
	}

	public void setTransactionsRoot(java.lang.String transactionsRoot) {
		set("transactionsRoot", transactionsRoot);
	}
	
	public java.lang.String getTransactionsRoot() {
		return getStr("transactionsRoot");
	}

	public void setTransactionsNum(java.lang.Integer transactionsNum) {
		set("transactionsNum", transactionsNum);
	}
	
	public java.lang.Integer getTransactionsNum() {
		return getInt("transactionsNum");
	}

	public void setBlockDate(java.util.Date blockDate) {
		set("block_date", blockDate);
	}
	
	public java.util.Date getBlockDate() {
		return get("block_date");
	}

}
