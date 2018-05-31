package com.eth.controller;

import com.eth.model.TronAddress;
import com.eth.model.TronBlock;
import com.eth.model.TronTx;
import com.eth.model.TronTxFrozen;
import com.eth.model.TronTxTokenTran;
import com.eth.model.TronTxTran;
import com.eth.model.TronTxVotes;
import com.jfinal.core.Controller;
import com.jfinal.plugin.activerecord.Page;

public class TronController extends Controller {

	// 查看区块
	public void block() {
		String number = getPara(0);
		int n = getParaToInt(1, 1);
		TronBlock block = TronBlock.dao.findById(number);
    	setAttr("ethUsername", getSessionAttr("ethUsername"));
    	setAttr("redirectUrl", getRequest().getRequestURI());
		setAttr("block", block);
    	if(block!=null){
    		Page<TronTx> txs = block.getTxsPage(n, 20);
    		setAttr("txs", txs);
    	}
		render("/tron/block.html");
	}
	public void blocks() {
		int n = getParaToInt(0, 1);
		Page<TronBlock> blocksPage = TronBlock.dao.getBlocksPage(n, 20);
		setAttr("nav", "tronBlock");
    	setAttr("ethUsername", getSessionAttr("ethUsername"));
    	setAttr("redirectUrl", getRequest().getRequestURI());
		setAttr("blocks", blocksPage);
		render("/tron/blocks.html");
	}	
	public void tx() {
		String hash = getPara(0);
    	setAttr("ethUsername", getSessionAttr("ethUsername"));
    	setAttr("redirectUrl", getRequest().getRequestURI());
    	TronTx tx = TronTx.dao.findById(hash);
    	if(tx!=null){
			switch(tx.getContractType()){
			case "1":
				tx.setTxTran(TronTxTran.dao.findById(hash));
				break;
			case "4":
				tx.setTxVotes(TronTxVotes.dao.findTxVotesByHash(hash));
				break;
			case "9":
				tx.setTxToken(TronTxTokenTran.dao.findById(hash));
				break;
			case "11":
				tx.setTxFrozen(TronTxFrozen.dao.findById(hash));
				break;
			}
    	}
		setAttr("tx", tx);
		render("/tron/tx.html");
	}
	
	public void txs() {
		int n = getParaToInt(0, 1);
		setAttr("nav", "tronTx");
    	setAttr("ethUsername", getSessionAttr("ethUsername"));
    	setAttr("redirectUrl", getRequest().getRequestURI());
		setAttr("txs", TronTx.dao.getTxsPage(n,20));
		render("/tron/txs.html");
	}
	
	public void transfers() {
		int n = getParaToInt(0, 1);
		setAttr("nav", "tronTransfer");
    	setAttr("ethUsername", getSessionAttr("ethUsername"));
    	setAttr("redirectUrl", getRequest().getRequestURI());
		setAttr("txs", TronTx.dao.getTransfersPage(n,20));
		render("/tron/transfers.html");
	}
	
	public void account() {
		String addr = getPara(0);
		int n = getParaToInt(1, 1);
		TronAddress address = TronAddress.dao.findById(addr);
    	setAttr("ethUsername", getSessionAttr("ethUsername"));
    	setAttr("redirectUrl", getRequest().getRequestURI());
		setAttr("addr", address);
    	if(address != null){
    		Page<TronTx> txs = address.getTxsPage(n, 20);
    		setAttr("txs",txs);
    	}
		render("/tron/account.html");
	}
	public void accounts(){
		int n = getParaToInt(0, 1);
		Page<TronAddress> addressPage = TronAddress.dao.getAddressPage(n, 20);
		setAttr("nav", "tronAccount");
    	setAttr("ethUsername", getSessionAttr("ethUsername"));
    	setAttr("redirectUrl", getRequest().getRequestURI());
		setAttr("address", addressPage);
		render("/tron/accounts.html");
	}
}
