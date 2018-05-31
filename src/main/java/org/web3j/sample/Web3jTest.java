package org.web3j.sample;

import java.io.IOException;
import java.math.BigInteger;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameter;
import org.web3j.protocol.core.Request;
import org.web3j.protocol.core.methods.response.EthAccounts;
import org.web3j.protocol.core.methods.response.EthBlock.Block;
import org.web3j.protocol.core.methods.response.EthBlock.TransactionResult;
import org.web3j.protocol.core.methods.response.EthTransaction;
import org.web3j.protocol.core.methods.response.Transaction;
import org.web3j.protocol.http.HttpService;

public class Web3jTest {
    private static final Logger log = LoggerFactory.getLogger(Application.class);

	public static void main(String[] args) throws Exception {
		// TODO Auto-generated method stub
        Web3j web3j = Web3j.build(new HttpService("http://39.107.60.59:8545"));  // FIXME: Enter your Infura token here;
        log.info("Connected to Ethereum client version: "
                + web3j.web3ClientVersion().send().getWeb3ClientVersion());
        
        EthTransaction  has = web3j.ethGetTransactionByHash("").send();
        has.getResult();
        //address
        //账号相关 
        DefaultBlockParameter latest = DefaultBlockParameter.valueOf("latest");
        EthAccounts account = web3j.ethAccounts().send();
        String address1 = account.getAccounts().get(1);
        for(int i = 0;i<account.getAccounts().size();i++) {
            //获取当前账号地址
        	String address = account.getAccounts().get(i);
        	//使用地址和区块位置获取bal
        	BigInteger bal = web3j.ethGetBalance(address, latest).send().getBalance();
        	BigInteger bal2 = web3j.ethGetBalance(address, DefaultBlockParameter.valueOf(BigInteger.valueOf(5))).send().getBalance();
        	log.info(address+"bal on block 5:"+bal2+"");
        	log.info(address+"bal on block latest:"+bal);
        	//获取code 暂时不知道用来做什么
        	String code = web3j.ethGetCode(address,latest).send().getCode();
        	//log.info(address+"code:"+code);
        	BigInteger txcount  = web3j.ethGetTransactionCount(address, latest).send().getTransactionCount();
        	log.info(address+"txcount:"+txcount);

        }
        //Block
        //区块相关
        //获取当前的区块高度
        BigInteger num =  web3j.ethBlockNumber().send().getBlockNumber();
        for(int i = 40;i<=num.intValue();i++) {
            DefaultBlockParameter bnum = DefaultBlockParameter.valueOf(BigInteger.valueOf(i));
            Block block = web3j.ethGetBlockByNumber(bnum,true).send().getBlock();
        	log.info("block author:"+block.getAuthor());
        	log.info("block author:"+block.getMiner());
        	log.info("block hash:"+block.getHash());
        	log.info("block parentHash:"+block.getParentHash());
        	log.info("block GasLimit:"+block.getGasLimit());
        	log.info("block GasUsed:"+block.getGasUsed());
        	log.info("block tx:"+block.getTransactions());
        	List<TransactionResult> txs = block.getTransactions();
        	BigInteger bal = web3j.ethGetBalance(address1, bnum).send().getBalance();

        	log.info(address1+" on block "+i+":"+bal );

        	for(int j = 0;j<txs.size();j++)
        	{
                //tx
                //交易相关的内容
            	Transaction tx = (Transaction) txs.get(j);
            	
            	log.info("tx hash:"+tx.getHash());
            	log.info("tx Input:"+tx.getInput());
            	
            	log.info("tx From:"+tx.getFrom()+"  To:"+tx.getTo()+"  Value:"+tx.getValue());
            	log.info("tx gas:"+tx.getGas()+"  gas price:"+tx.getGasPrice());
            	
        	}
        }




	}

}
