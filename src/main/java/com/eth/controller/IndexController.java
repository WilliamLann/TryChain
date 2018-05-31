package com.eth.controller;

import java.util.ArrayList;
import java.util.List;

import com.eth.model.EthBlock;
import com.eth.model.LynxUtil;
import com.eth.model.EthTx;
import com.jfinal.core.Controller;

public class IndexController extends Controller {

	public void index() {
		setAttr("nav", "index");
    	setAttr("ethUsername", getSessionAttr("ethUsername"));
    	setAttr("redirectUrl", getRequest().getRequestURI());
    	
    	//最新的十个区块信息
		List<EthBlock> block_list = EthBlock.dao.getLatestBlock();
		setAttr("blocks", block_list);

		
		//最新的十条交易信息
		List<EthTx> tx_list = EthTx.dao.getLatestTx();
		setAttr("txs", tx_list);
		
		//最近几天每天的交易总条数
		System.out.println(LynxUtil.getCurrentTime());
		List<EthBlock> perDayTxSum = EthBlock.dao.getTxSum();
		System.out.println(LynxUtil.getCurrentTime());
		List<Object> list = new ArrayList<>();
		for(int i = 0;i < perDayTxSum.size();i++){
			String json = perDayTxSum.get(i).toJson();
			list.add(json);
		}
		setAttr("perDayTxSum", list);
		render("/index.html");
	}
	
	//首页搜索框
	public void search(){
		String key = getPara("searchValue").toLowerCase();
		if(LynxUtil.isEmpty(key) || !key.matches("^(0|[1-9]\\d*|0x[\\da-f]{40}|0x[\\da-f]{64})$")){
    		setAttr("errMsg", "关键字输入有误");
        	render("/common/error.html");
		}else if(key.matches("^0|[1-9]\\d*$")){
			redirect("/eth/block/"+key);
		}else if(key.matches("^0x[\\da-f]{40}$")){
			redirect("/eth/address/"+key);
		}else if(key.matches("^0x[\\da-f]{64}$")){
			EthBlock block = EthBlock.dao.findByHash(key);
			if(block!=null){
				redirect("/eth/block/"+block.getNumber());
			}else{
				redirect("/eth/tx/"+key);
			}
		}
	}
}
