package com.eth.controller;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;

import com.eth.model.EthAddress;
import com.eth.model.EthBlock;
import com.eth.model.EthToken;
import com.eth.model.EthTokenAddress;
import com.eth.model.EthTokenTx;
import com.eth.model.EthTx;
import com.jfinal.core.Controller;
import com.jfinal.plugin.activerecord.Page;

public class EthController extends Controller {

	// 查看区块
	public void block() {
		String number = getPara(0);
		int n = getParaToInt(1, 1);
		EthBlock block = EthBlock.dao.findById(new BigInteger(number));
    	setAttr("ethUsername", getSessionAttr("ethUsername"));
    	setAttr("redirectUrl", getRequest().getRequestURI());
		setAttr("block", block);
    	if(block!=null){
    		Page<EthTx> txs = block.getTxsPage(n, 20);
    		setAttr("txs", txs);
    	}
		render("/eth/block.html");
	}
	public void latestblocks() {
		int n = getParaToInt(0, 1);
		Page<EthBlock> blocksPage = EthBlock.dao.getLatestBlocksPage(n, 20);
		setAttr("nav", "block");
    	setAttr("ethUsername", getSessionAttr("ethUsername"));
    	setAttr("redirectUrl", getRequest().getRequestURI());
		setAttr("blocks", blocksPage);
		render("/eth/latestblocks.html");
	}	
	public void tx() {
		String hash = getPara(0);
		EthTx tx = EthTx.dao.findById(hash);
    	setAttr("ethUsername", getSessionAttr("ethUsername"));
    	setAttr("redirectUrl", getRequest().getRequestURI());
		setAttr("tx", tx);
		render("/eth/tx.html");
	}
	
	public void latesttxs() {
		int n = getParaToInt(0, 1);
		setAttr("nav", "tx");
    	setAttr("ethUsername", getSessionAttr("ethUsername"));
    	setAttr("redirectUrl", getRequest().getRequestURI());
		setAttr("txs", EthTx.dao.getLatestTxsPage(n,20));
		render("/eth/latesttxs.html");
	}
	
	public void address() {
		String addr = getPara(0);
		int n = getParaToInt(1, 1);
		EthAddress address = EthAddress.dao.findById(addr);
    	setAttr("ethUsername", getSessionAttr("ethUsername"));
    	setAttr("redirectUrl", getRequest().getRequestURI());
		setAttr("addr", address);
    	if(address != null){
    		Page<EthTx> txs = address.getTxsPage(n, 20);
    		setAttr("txs",txs);
    	}
		render("/eth/address.html");
	}
	public void topaddress(){
		int n = getParaToInt(0, 1);
		Page<EthAddress> addressPage = EthAddress.dao.getAddressPage(n, 20);
		setAttr("nav", "address");
    	setAttr("ethUsername", getSessionAttr("ethUsername"));
    	setAttr("redirectUrl", getRequest().getRequestURI());
		setAttr("address", addressPage);
		render("/eth/topaddress.html");
	}
	
	public void tokens(){
		setAttr("nav", "tokens");
		int n = getParaToInt(0, 1);
		Page<EthToken> tokensPage = EthToken.dao.getTokensPage(n,50);
		setAttr("tokens", tokensPage);
		render("/eth/tokens.html");
	}
	
	public void token(){
		String hash = getPara(0);
		int n = getParaToInt(1,1);
		EthToken token = EthToken.dao.findById(hash);
		BigDecimal total = new BigDecimal(token.getTokenTotalsupply());
		if(token != null){
			Page<EthTokenTx> txs = token.getEthTokenTxsPage(n,25);
			List<EthTokenAddress> holderAddress = token.getHolderAddress();
			for(int k = 0; k < holderAddress.size(); k++){
				String holderValue = holderAddress.get(k).getTokenHoldValue();
				BigDecimal holderVal = new BigDecimal(holderValue);
				BigDecimal divide = holderVal.divide(total, 4, BigDecimal.ROUND_HALF_DOWN);
				holderAddress.get(k).put("hah", divide);
			}
			for(int i = 0; i < txs.getList().size(); i++){
				String hash2 = txs.getList().get(i).getHash();
				EthTx tx = EthTx.dao.findById(hash2);
				txs.getList().get(i).put(tx);
			}
			EthTokenAddress holders = token.getHoldersNum();
			setAttr("txs", txs);
			setAttr("token", token);
			setAttr("address", hash);
			setAttr("holders", holders);
			setAttr("txNum", txs.getTotalRow());
			setAttr("holderAddress", holderAddress);
		}
		render("/eth/token.html");
	}
	
	public void tokenAddress(){
		String tokenAddress = getPara(0);
		String holder = getPara("a");
		int n = getParaToInt(1, 1);
		EthToken token = EthToken.dao.findById(tokenAddress);
		if(token != null){
			Page<EthTokenTx> tokenHolderTxs = EthTokenTx.dao.paginate(n, 25, "select *", "from eth_token_tx where token_from = ? or token_to = ?", holder, holder);
			for(int i = 0; i < tokenHolderTxs.getList().size(); i++){
				String hash2 = tokenHolderTxs.getList().get(i).getHash();
				EthTx tx = EthTx.dao.findById(hash2);
				tokenHolderTxs.getList().get(i).put(tx);
			}
			setAttr("token", token);
			setAttr("holder", holder);
			setAttr("tokenAddress", tokenAddress);
			setAttr("txNum", tokenHolderTxs.getTotalRow());
			setAttr("holderTxs", tokenHolderTxs);
		}
		render("/eth/tokenaddress.html");
	}
	
	public void holderAllAddress(){
		String tokenAddress = getPara(0);
		int n = getParaToInt(1, 1);
		EthToken token = EthToken.dao.findById(tokenAddress);
		BigDecimal total = new BigDecimal(token.getTokenTotalsupply());
		if(token != null){
			Page<EthTokenAddress> holderAllAddress = token.getHolderAllAddress(n, 50);
			for(int i = 0; i < holderAllAddress.getList().size(); i++){
				String holderValue = holderAllAddress.getList().get(i).getTokenHoldValue();
				BigDecimal holderVal = new BigDecimal(holderValue);
				BigDecimal divide = holderVal.divide(total, 4, BigDecimal.ROUND_HALF_DOWN);
				holderAllAddress.getList().get(i).put("cus",divide);
			}
			setAttr("token", token);
			setAttr("address", tokenAddress);
			setAttr("holderAddress", holderAllAddress);
		}
		render("/eth/tkHoldAddr.html");
	}
}
