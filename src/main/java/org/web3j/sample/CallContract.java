package org.web3j.sample;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.security.Key;
import java.util.Arrays;
import java.util.Collections;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.web3j.abi.FunctionEncoder;
import org.web3j.abi.TypeReference;
import org.web3j.abi.datatypes.Function;
import org.web3j.abi.datatypes.Type;
import org.web3j.crypto.Credentials;
import org.web3j.crypto.WalletUtils;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameter;
import org.web3j.protocol.core.DefaultBlockParameterName;
import org.web3j.protocol.core.methods.request.Transaction;
import org.web3j.protocol.core.methods.response.EthGetTransactionCount;
import org.web3j.protocol.core.methods.response.EthSendTransaction;
import org.web3j.protocol.core.methods.response.EthTransaction;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.protocol.exceptions.TransactionException;
import org.web3j.protocol.http.HttpService;
import org.web3j.sample.contracts.generated.StandardToken;
import org.web3j.tx.Contract;
import org.web3j.tx.FastRawTransactionManager;
import org.web3j.tx.Transfer;
import org.web3j.utils.Convert;
import org.web3j.utils.Convert.Unit;

import com.eth.model.LynxUtil;
import com.eth.model.LynxUser;
import com.jfinal.kit.PropKit;

public class CallContract {
	private static final Logger log = LoggerFactory.getLogger(Application.class);
	private Web3j web3j;
	private String contractaddress;
	public static void main(String[] args) throws Exception {
		new CallContract().run();
	}

	private void run() throws Exception {
		LynxUtil.startARP();
		web3j = Web3j.build(new HttpService(PropKit.get("ethnet")));
		LynxUser admin = LynxUser.dao.findById(15);
        Credentials credentialsAdmin =WalletUtils.loadCredentials(admin.getPassword(),PropKit.get("static.new_path")+admin.getEthFilename());
        LynxUser user16 = LynxUser.dao.findById(19);

		contractaddress = PropKit.get("conAddress");

		FastRawTransactionManager tranMtest = new FastRawTransactionManager(web3j, credentialsAdmin);
		StandardToken contractAdmin = StandardToken.load(contractaddress, web3j, credentialsAdmin, BigInteger.valueOf(3010000000L),
				Contract.GAS_LIMIT);
		sendTBC(contractAdmin,user16.getEthAddress(),BigInteger.valueOf(21000000L));
		
		//sendTBCByAdmin(tranMtest,admin.getEthAddress(),user49.getEthAddress(),BigInteger.valueOf(1L) );
		//sendETH(credentialsAdmin,user49.getEthAddress(),BigDecimal.valueOf(0.1));
	}
	private void sendTBCByAdmin(FastRawTransactionManager tranMtest,String from,String to,BigInteger value) throws Exception {
		
		Function function = new Function(
                "AdminTran", 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(from), 
                new org.web3j.abi.datatypes.Address(to), 
                new org.web3j.abi.datatypes.generated.Uint256(value)), 
                Collections.<TypeReference<?>>emptyList());
		String encodedFunction = FunctionEncoder.encode(function);
		EthGetTransactionCount ethPending = web3j.ethGetTransactionCount("0x06221dcca4c7ec37a9a0af90eeee8f2fe1c5b47a", DefaultBlockParameterName.PENDING).sendAsync().get();
		log.info(ethPending.getTransactionCount().toString());
		tranMtest.setNonce(BigInteger.valueOf(4L));
		log.info(tranMtest.getCurrentNonce().toString());
		EthSendTransaction transaction = tranMtest.sendTransaction(BigInteger.valueOf(5000000000L), Contract.GAS_LIMIT, contractaddress, encodedFunction, BigInteger.ZERO);
    	log.info("TxHash:"+transaction.getTransactionHash());
	}
	
	private void sendTBC(StandardToken contract,String to,BigInteger value) throws Exception {
		log.info("before to:"+contract.balanceOf(to).send());
    	TransactionReceipt tr = contract.transfer(to, value).send();
    	log.info("TxHash:"+tr.getTransactionHash());
		log.info("after TBC to:"+contract.balanceOf(to).send());
    	
    	for(StandardToken.TransferEventResponse event : contract.getTransferEvents(tr)) {
            log.info("from: " + event.from
                    + " to: " + event.to+ " value: " + event.value);
    	}
    	
    	for(StandardToken.ApprovalEventResponse event : contract.getApprovalEvents(tr)) {
            log.info("owner: " + event.owner
                    + " spender: " + event.spender+ " value: " + event.value);
    	}
	}
	public void sendETH(Credentials credentials,String to,BigDecimal value) throws InterruptedException, IOException, TransactionException, Exception {
		
		TransactionReceipt tr = Transfer.sendFunds(
		        web3j, credentials, to,
		        value, Convert.Unit.ETHER).send();		
    	log.info("TxHash:"+tr.getTransactionHash());

	}

}
