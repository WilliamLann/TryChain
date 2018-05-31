package com.eth.controller;

import java.io.File;
import java.io.IOException;
import java.math.BigInteger;
import java.security.InvalidAlgorithmParameterException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.util.Arrays;
import java.util.Collections;
import java.util.concurrent.ExecutionException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.web3j.abi.FunctionEncoder;
import org.web3j.abi.TypeReference;
import org.web3j.abi.datatypes.Function;
import org.web3j.abi.datatypes.Type;
import org.web3j.crypto.CipherException;
import org.web3j.crypto.Credentials;
import org.web3j.crypto.WalletUtils;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.methods.response.EthSendTransaction;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.protocol.http.HttpService;
import org.web3j.sample.Application;
import org.web3j.sample.contracts.generated.StandardToken;
import org.web3j.tx.Contract;
import org.web3j.tx.FastRawTransactionManager;
import com.eth.model.TbcAddress;
import com.eth.model.TbcTx;
import com.eth.model.LynxUser;
import com.jfinal.kit.PropKit;

public class TbcAdmin {
	private static final Logger log = LoggerFactory.getLogger(Application.class);
	private static TbcAdmin instance;
	public StandardToken token;
	public String admin_address;
	public Web3j web3j;
	public FastRawTransactionManager tranM;
	String conAddress;

	private TbcAdmin() throws IOException, CipherException {

		web3j = Web3j.build(new HttpService(PropKit.get("ethnet")));
		conAddress = PropKit.get("conAddress");
		LynxUser userAdmin = LynxUser.dao.findById(15);
		admin_address = userAdmin.getEthAddress();
		Credentials credentials = WalletUtils.loadCredentials(userAdmin.getPassword(),
				PropKit.get("static.new_path") + userAdmin.getEthFilename());
		tranM = new FastRawTransactionManager(web3j, credentials);
		//1000000000L = 1Gwei
		token = StandardToken.load(conAddress, web3j, tranM, BigInteger.valueOf(1000000000L), Contract.GAS_LIMIT);
//		Web3ClientVersion verion = web3j.web3ClientVersion().send();
	}

	public static TbcAdmin getInstance() throws IOException, CipherException {
		if (instance == null) {
			instance = new TbcAdmin();
		}
		return instance;
	}

	public String newWallet(String password) throws InvalidAlgorithmParameterException, NoSuchAlgorithmException,
			NoSuchProviderException, CipherException, IOException {
		File destinationDirectory = new File(PropKit.get("static.new_path"));
		String wallet = WalletUtils.generateNewWalletFile(password, destinationDirectory, false);
		return wallet;
	}

	// 从from地址转账给to地址value个TBC
	public void tranTokenBal(String from, String to, BigInteger value) throws Exception {
		log.info("from:" + from + "  to:" + to + "  value:" + value);
		//TransactionReceipt tx = token.AdminTran(from, to, value).send();
		Function function = new Function(
                "AdminTran", 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(from), 
                new org.web3j.abi.datatypes.Address(to), 
                new org.web3j.abi.datatypes.generated.Uint256(value)), 
                Collections.<TypeReference<?>>emptyList());
		String encodedFunction = FunctionEncoder.encode(function);
		System.out.println(tranM.getCurrentNonce());
		EthSendTransaction transaction = tranM.sendTransaction(BigInteger.valueOf(3000000000L), Contract.GAS_LIMIT, conAddress, encodedFunction, BigInteger.ZERO);
		String txHash = transaction.getTransactionHash();
		System.out.println(tranM.getCurrentNonce());
		System.out.println(txHash);
		TbcTx tbctx = new TbcTx();
		tbctx.setHash(txHash);
		tbctx.setStatus("pending");
		tbctx.setTimestampSubmit(String.valueOf(System.currentTimeMillis()));
		tbctx.setNonce("0x"+tranM.getCurrentNonce().toString(16));
		tbctx.setTbcfrom(from);
		tbctx.setTbcto(to);
		tbctx.setTbcvalue(value.toString());
		// 处理btc账号的余额
		tbcValueChange(from, value, "sub","pending");
		tbcValueChange(to, value, "add","pending");
		try {
			tbctx.save();
		} catch (Exception e) {
			log.error(e.toString());
			try {
				tbctx.update();
			} catch (Exception e1) {
				log.error(e1.toString());
			}
		}


	}

	public void dealTx(TransactionReceipt tr)
			throws InterruptedException, ExecutionException, IOException {
		//后台获取
		org.web3j.protocol.core.methods.response.Transaction tx = web3j.ethGetTransactionByHash(tr.getTransactionHash()).send().getResult();
		TbcTx tbctx = new TbcTx();
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
		for (StandardToken.TransferEventResponse event : token.getTransferEvents(tr)) {
			tbctx.setTbcfrom(event.from);
			tbctx.setTbcto(event.to);
			tbctx.setTbcvalue(event.value.toString());
			log.info("btctx hash : " + tr.getTransactionHash());
			log.info("btctx from: " + event.from + " to: " + event.to + " value: " + event.value);
			// 处理btc账号的余额
			tbcValueChange(event.from, event.value, "sub","pending");
			tbcValueChange(event.to, event.value, "add","pending");
		}
		try {
			tbctx.update();
		} catch (Exception e) {
			log.error(e.toString());
			try {
				tbctx.save();
			} catch (Exception e1) {
				log.error(e1.toString());
			}
		}


	}

	// 从Admin地址转账给to地址value个TBC
	public void tranTokenBal(String to, BigInteger value) throws Exception {
		TbcAddress tbc = TbcAddress.dao.findById(admin_address);
		tranTokenBal(admin_address, to, value);
    	tbc.setPending(tbc.getPending() - value.longValue());
    	tbc.update();
	}

	public void tbcValueChange(String address, BigInteger value, String method,String status) {
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
