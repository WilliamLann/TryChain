package com.eth.controller;

import java.io.IOException;
import java.math.BigInteger;

import org.web3j.crypto.Credentials;
import org.web3j.crypto.RawTransaction;
import org.web3j.crypto.TransactionEncoder;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameterName;
import org.web3j.protocol.core.methods.response.EthGetTransactionCount;
import org.web3j.protocol.core.methods.response.EthSendTransaction;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.protocol.exceptions.TransactionException;
import org.web3j.tx.ChainId;
import org.web3j.tx.TransactionManager;
import org.web3j.tx.response.TransactionReceiptProcessor;
import org.web3j.utils.Numeric;

public class TBCTransactionManager extends TransactionManager{
    private final Web3j web3j;
    final Credentials credentials;

    public TBCTransactionManager(Web3j web3j, Credentials credentials) {
        super(web3j, credentials.getAddress());
        this.web3j = web3j;
        this.credentials = credentials;		// TODO Auto-generated constructor stub
	}

	
	@Override
	public EthSendTransaction sendTransaction(BigInteger gasPrice, BigInteger gasLimit, String to, String data,
			BigInteger value) throws IOException {
		BigInteger nonce = getNonce();
        RawTransaction rawTransaction = RawTransaction.createTransaction(
                nonce,
                gasPrice,
                gasLimit,
                to,
                value,
                data);

        return signAndSend(rawTransaction);
	}
    protected BigInteger getNonce() throws IOException {
        EthGetTransactionCount ethGetTransactionCount = web3j.ethGetTransactionCount(
                credentials.getAddress(), DefaultBlockParameterName.PENDING).send();

        return ethGetTransactionCount.getTransactionCount();
    }
    public EthSendTransaction signAndSend(RawTransaction rawTransaction)
            throws IOException {

        byte[] signedMessage;
        signedMessage = TransactionEncoder.signMessage(rawTransaction, credentials);

        String hexValue = Numeric.toHexString(signedMessage);

        return web3j.ethSendRawTransaction(hexValue).send();
    }
	
	
    protected TransactionReceipt executeTransaction(
            BigInteger gasPrice, BigInteger gasLimit, String to,
            String data, BigInteger value)
            throws IOException, TransactionException {

        EthSendTransaction ethSendTransaction = sendTransaction(
                gasPrice, gasLimit, to, data, value);
        return processResponse(ethSendTransaction);


    }
    private TransactionReceipt processResponse(EthSendTransaction transactionResponse)
            throws IOException, TransactionException {
        if (transactionResponse.hasError()) {
            throw new RuntimeException("Error processing transaction request: "
                    + transactionResponse.getError().getMessage());
        }

        String transactionHash = transactionResponse.getTransactionHash();

        TransactionReceipt re = new TransactionReceipt();
        re.setTransactionHash(transactionHash);
        return re;    }

}
