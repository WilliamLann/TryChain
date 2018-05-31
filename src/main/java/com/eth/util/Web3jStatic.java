package com.eth.util;

import java.io.IOException;
import java.math.BigInteger;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.web3j.crypto.CipherException;
import org.web3j.crypto.Credentials;
import org.web3j.crypto.WalletUtils;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.methods.response.Web3ClientVersion;
import org.web3j.protocol.http.HttpService;
import org.web3j.sample.Application;
import org.web3j.sample.contracts.generated.StandardToken;
import org.web3j.tx.Contract;

import com.eth.controller.TBCTransactionManager;
import com.eth.controller.TbcAdmin;
import com.eth.model.LynxUser;
import com.jfinal.kit.PropKit;

public class Web3jStatic {
	private static final Logger log = LoggerFactory.getLogger(Application.class);
	private static Web3jStatic instance;
	public StandardToken token;
	public String admin_address;
	public Web3j web3j;

	private Web3jStatic() throws IOException, CipherException {
		web3j = Web3j.build(new HttpService(PropKit.get("ethnet")));
		String conAddress = PropKit.get("conAddress");
		LynxUser userAdmin = LynxUser.dao.findById(15);
		admin_address = userAdmin.getEthAddress();
		Credentials credentials = WalletUtils.loadCredentials(userAdmin.getPassword(),
				PropKit.get("static.new_path") + userAdmin.getEthFilename());
		token = StandardToken.load(conAddress, web3j, credentials, BigInteger.valueOf(1000000000L), Contract.GAS_LIMIT);
	}

	public static Web3jStatic getInstance() throws IOException, CipherException {
		if (instance == null) {
			instance = new Web3jStatic();
		}
		return instance;
	}
}
