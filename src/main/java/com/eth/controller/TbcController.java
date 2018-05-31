package com.eth.controller;

import java.math.BigInteger;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameter;
import org.web3j.sample.Application;
import org.web3j.sample.contracts.generated.StandardToken;

import com.eth.interceptor.LoginInterceptor;
import com.eth.model.LynxUtil;
import com.eth.model.TbcAddress;
import com.eth.model.TbcTx;
import com.eth.model.TbcTxInter;
import com.eth.model.LynxUser;
import com.jfinal.aop.Before;
import com.jfinal.core.Controller;

public class TbcController extends Controller{
    private static final Logger log = LoggerFactory.getLogger(Application.class);

    //动态获取 所有地址的TBC bal
    public void getTokenBal() throws Exception {
    	StandardToken token = TbcAdmin.getInstance().token;
        List<LynxUser> user_list = LynxUser.dao.findAll();
        Map<String, Object> result = new HashMap<String, Object>();
        for(int i = 0;i<user_list.size();i++) {
        	LynxUser user = user_list.get(i);
        	//TransactionReceipt tx = token.transfer(user.getEthAddress(), BigInteger.valueOf(10000)).send();
        	BigInteger token_bal = token.balanceOf(user.getEthAddress()).send();
        	result.put(user.getEthAddress(), token_bal);
//        	updateBtcAddress(user.getEthAddress(), token_bal.longValue());
        }
        renderJson(result);
    }
    //动态获取 所有地址的Eth bal
    public void getEthBal() throws Exception {
    	Web3j web3j = TbcAdmin.getInstance().web3j;
        List<LynxUser> user_list = LynxUser.dao.findAll();
        Map<String, Object> result = new HashMap<String, Object>();
        for(int i = 0;i<user_list.size();i++) {
        	LynxUser user = user_list.get(i);
        	DefaultBlockParameter latest = DefaultBlockParameter.valueOf("latest");
			BigInteger ethbal = web3j.ethGetBalance(user.getEthAddress(), latest).send().getBalance();
        	result.put(user.getEthAddress(), ethbal);
        }
        renderJson(result);
    }
    
    //更新btc_address表
    private void updateBtcAddress(String address,Long value) {
		TbcAddress tbc = TbcAddress.dao.findById(address);
    	if(tbc==null) {
    		tbc = new TbcAddress();
    		tbc.setAddress(address);
    		tbc.setValue(value);
    		tbc.save();
    	}else{
    		tbc.setValue(value);
    		tbc.update();
    	}
    }
    
    
    //渲染转账页面
    @Before(LoginInterceptor.class)
    public void tbc(){
    	int n = getParaToInt(0, 1);
    	String ethAddress = getSessionAttr("ethAddress");
    	TbcAddress tbc = TbcAddress.dao.findById(ethAddress);
    	setAttr("nav", "trade");
    	setAttr("redirectUrl", getRequest().getRequestURI());
    	setAttr("ethUsername", getSessionAttr("ethUsername"));
    	setAttr("tbc", tbc);
    	if(tbc!=null){
        	setAttr("tbcTxs", tbc.getTbcTxsInterPage(n, 20));    		
    	}
    	render("/tbc/tbc.html");
    }
    
    //渲染交易详情页面
    public void tx(){
    	String hash = getPara(0);
    	TbcTx tx = TbcTx.dao.findById(hash);
    	setAttr("redirectUrl", getRequest().getRequestURI());
    	setAttr("ethUsername", getSessionAttr("ethUsername"));
    	setAttr("tx", tx);
    	render("/tbc/tx.html");
    }
    
    //渲染地址页面
    public void address(){
    	String address = getPara(0);
    	int n = getParaToInt(1, 1);
    	TbcAddress tbc = TbcAddress.dao.findById(address);
    	setAttr("redirectUrl", getRequest().getRequestURI());
    	setAttr("ethUsername", getSessionAttr("ethUsername"));
    	setAttr("tbc", tbc);
    	if(tbc!=null){
        	setAttr("tbcTxs", tbc.getTbcTxsInterPage(n, 20));    		
    	}
    	render("/tbc/address.html");
    }
    
    //随机获取十条地址
    public void getAddressRand(){
    	renderJson(TbcAddress.dao.getAddressRand());
    }
    
    //转账地址是否存在，存在且不为登录地址时返回true，转账检验用
    public void checkAddress(){
        String to = getPara("to");
        if(LynxUtil.isEmpty(to) || to.equals(getSessionAttr("ethAddress"))){
        	renderJson(false);
        	return;
        }
        renderJson(TbcAddress.dao.findById(to)!=null);
    }
    
    //转账接口
    public void trade(){
    	String from = getSessionAttr("ethAddress");
    	String to = getPara("to");
    	long value = getParaToLong("value", 0L);
    	if(LynxUtil.isEmpty(from)){
    		renderJson(LynxUtil.jsonMsg(false, "转账", "登录状态错误"));
    		return;    		
    	}
    	if(from.equals(to)){
    		renderJson(LynxUtil.jsonMsg(false, "转账", "不能给自己转账"));
    		return;    		
    	}
    	if(LynxUtil.isEmpty(to) || !to.matches("^0x[0-9a-f]{40}$")){
    		renderJson(LynxUtil.jsonMsg(false, "转账", "转账地址格式错误"));
    		return;
    	}
    	TbcAddress tbcfrom = TbcAddress.dao.findById(from);
    	if(value > (tbcfrom.getValue() + Math.min(0L, tbcfrom.getPending()))){
    		renderJson(LynxUtil.jsonMsg(false, "转账", "转账金额大于可用余额"));
    		return;
    	}
    	TbcAddress tbcto = TbcAddress.dao.findById(to);
    	if(tbcto==null){
    		renderJson(LynxUtil.jsonMsg(false, "转账", "转账地址不存在"));
    		return;
    	}
    	TbcTxInter.trade(from, to, value, "转账");
    	renderJson(LynxUtil.jsonMsg(true, "转账", "转账提交成功"));
    }
    
    public void getTx(){
    	String hash = getPara("hash");
    	renderJson(TbcTx.dao.findById(hash));
    }
    public void getAddress(){
    	String addr = getPara("address");
    	int n = getParaToInt("p", 1);
    	TbcAddress address = TbcAddress.dao.findById(addr);
    	if(address!=null){
    		address.setTxsInter(address.getTbcTxsInterPage(n, 20));
    	}
    	renderJson(address);
    }
    public void getTbc(){
    	String addr = getSessionAttr("ethAddress");
    	int n = getParaToInt("p", 1);
    	TbcAddress address = TbcAddress.dao.findById(addr);
    	if(address!=null){
    		address.setTxsInter(address.getTbcTxsInterPage(n, 20));
    	}else{
        	removeCookie("ethUsername", "/");
        	removeCookie("ethAddress", "/");
    	}
    	renderJson(address);
    }
    public void getInviteCount(){
    	String addr = getSessionAttr("ethAddress");
    	renderJson(TbcTxInter.dao.getInviteCount(addr));
    }
    public void getInviteRecord(){
    	String addr = getSessionAttr("ethAddress");
    	renderJson(TbcTxInter.dao.getInviteRecord(addr));
    }
}
