package com.eth.datadeal;

import java.io.IOException;
import java.math.BigInteger;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.web3j.crypto.CipherException;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.methods.response.Transaction;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.protocol.http.HttpService;
import org.web3j.sample.Application;
import org.web3j.sample.contracts.generated.StandardToken;
import com.eth.model.LynxUtil;
import com.eth.model.TbcAddress;
import com.eth.model.TbcTx;
import com.eth.util.Web3jStatic;
import com.jfinal.kit.PropKit;

public class TBCAccountDeal {
	private static final Logger log = LoggerFactory.getLogger(Application.class);
	Web3j web3j ;
	StandardToken token;


	public static void main(String[] args) throws Exception {
		LynxUtil.startARP();
		TBCAccountDeal deal = new TBCAccountDeal();
		deal.dealAccount();
	}
	


	public  void dealAccount() throws Exception {
		web3j = Web3j.build(new HttpService(PropKit.get("ethnet")));
		token = Web3jStatic.getInstance().token;
		log.info("开始处理tbc account的balance");
		List<TbcAddress> btcadds = TbcAddress.dao.find("select * from tbc_address");
		for (int i = 0; i < btcadds.size(); i++) {
			TbcAddress tbctx = btcadds.get(i);
			tbctx.setValue(token.balanceOf(tbctx.getAddress()).send().longValue());;
			tbctx.setPending(0L);
			tbctx.update();
		}
		log.info("tbc account的balance处理结束");	
	}
	private void dealtx(TransactionReceipt tr) throws IOException, CipherException {
		TbcTx tbctx = TbcTx.dao.findById(tr.getTransactionHash());
		Transaction tx = web3j.ethGetTransactionByHash(tr.getTransactionHash()).send().getResult();
		tbctx.setHash(tx.getHash());
		tbctx.setFrom(tx.getFrom());
		tbctx.setGas(tx.getGas());
		tbctx.setGasPrice(tx.getGasPrice());
		tbctx.setNonce(tx.getNonceRaw());
		tbctx.setR(tx.getR());
		tbctx.setS(tx.getS());
		tbctx.setTo(tx.getTo());
		tbctx.setV(tx.getV()+"");
		tbctx.setValue(tx.getValueRaw());
		tbctx.setStatus("success");
		if(tbctx.getTimestamp()==null) {
			com.eth.model.EthBlock bl = com.eth.model.EthBlock.dao.findById(tx.getBlockNumber());
			if(bl!=null) {
				tbctx.setTimestamp(bl.getTimestamp().toString());
			}
		}
		for (StandardToken.TransferEventResponse event : token.getTransferEvents(tr)) {
			tbctx.setTbcfrom(event.from);
			tbctx.setTbcto(event.to);
			tbctx.setTbcvalue(event.value.toString());
			log.debug("btctx hash : " + tr.getTransactionHash());
			log.debug("btctx from: " + event.from + " to: " + event.to + " value: " + event.value);
			// 处理btc账号的余额
			tbcValueChange(event.from, event.value, "sub","success");
			tbcValueChange(event.to, event.value, "add","success");
		}
		try {
			tbctx.update();
		} catch (Exception e) {
			log.error(e.getMessage());
			try {
				tbctx.save();
			} catch (Exception e1) {
				log.error(e1.getMessage());
			}
		}		
	}

	
	private void tbcValueChange(String address, BigInteger value, String method,String status) {
		TbcAddress tbc = TbcAddress.dao.findById(address);
		if (tbc != null) {
			if (method == "add") {
				if(status == "pending") {
					tbc.setPending(tbc.getPending() + value.longValue());
				}else {
					tbc.setValue(tbc.getValue() + value.longValue());
				}
			} else if (method == "sub") {
				if(status == "pending") {
					tbc.setPending(tbc.getPending() - value.longValue());
				}else {
					tbc.setValue(tbc.getValue() - value.longValue());
				}
			}
			tbc.update();
		} else {
			tbc = new TbcAddress();
			if (method == "add") {
				if(status == "pending") {
					tbc.setPending(value.longValue());
				}else {
					tbc.setValue(value.longValue());
				}
			} else if (method == "sub") {
				tbc.setValue(0L);
				tbc.setPending(0L);
			}
			tbc.setAddress(address);
			tbc.save();
		}

	}
}
