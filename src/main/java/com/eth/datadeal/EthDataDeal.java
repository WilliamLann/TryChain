package com.eth.datadeal;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.web3j.crypto.CipherException;
import org.web3j.crypto.Credentials;
import org.web3j.crypto.WalletUtils;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameter;
import org.web3j.protocol.core.methods.response.Transaction;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.protocol.core.methods.response.EthBlock;
import org.web3j.protocol.core.methods.response.EthBlock.TransactionResult;
import org.web3j.protocol.http.HttpService;
import org.web3j.sample.Application;
import org.web3j.sample.contracts.generated.StandardToken;
import org.web3j.tx.Contract;

import com.eth.model.EthAddress;
import com.eth.model.EthAddressType;
import com.eth.model.LynxUtil;
import com.eth.model.EthToken;
import com.eth.model.EthTokenAddress;
import com.eth.model.EthTx;
import com.eth.model.EthTokenTx;
import com.eth.model.LynxUser;
import com.jfinal.kit.PropKit;

import rx.Subscription;

public class EthDataDeal {
	private static final Logger log = LoggerFactory.getLogger(Application.class);
	static Web3j web3j ;
    static Credentials credentialsAdmin;
    static boolean hist_flag = false;
	public static void main(String[] args) throws Exception {
		LynxUtil.startARP();
		web3j = Web3j.build(new HttpService(PropKit.get("ethnet")));
		LynxUser admin = LynxUser.dao.findById(15);
		credentialsAdmin =WalletUtils.loadCredentials(admin.getPassword(),PropKit.get("static.new_path")+admin.getEthFilename());

		int start = 0;
		int end = 0;
		if(args.length<=0) {
			//如果不带参数 一直循环执行最新的三个区块
			end = web3j.ethBlockNumber().send().getBlockNumber().intValue();			
			//2018开始的记录
			start = 4800000;
			//20180305开始记录
			start = 5200000;
			//20180322
			start = 5300000;
			//20180407
			start = 5400000;
			//20180425
			//start = 5500000;
			start = 5586656;  
			//20180512
			//start = 5600000;
			//start = end-3;
			end = 5600000;
		}else if(args.length ==1) {
			start = Integer.valueOf(args[0]) ;
			end = web3j.ethBlockNumber().send().getBlockNumber().intValue();
		}else if (args.length ==2) {
			start = Integer.valueOf(args[0]) ;
			end = Integer.valueOf(args[1]) ;
		}else if(args.length == 3) {
			start = Integer.valueOf(args[0]) ;
			end = Integer.valueOf(args[1]) ;
		}

		EthDataDeal geth = new EthDataDeal();
		geth.dealBlocks(start, end);


	}
	public EthDataDeal(){
		hist_flag = true;
		web3j = Web3j.build(new HttpService(PropKit.get("ethnet")));
		LynxUser admin = LynxUser.dao.findById(15);
		try {
			credentialsAdmin =WalletUtils.loadCredentials(admin.getPassword(),PropKit.get("static.new_path")+admin.getEthFilename());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (CipherException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private  void dealPending() throws Exception {
		log.debug("开始获取pending的交易");
		Subscription subscription = web3j.blockObservable(false).subscribe(block -> {
			block.getBlock();
			log.debug("开始test2222的交易");
		});
		log.debug("开始test的交易");

	}

	public  void dealBlocks(int start, int end) throws InterruptedException {
		web3j = Web3j.build(new HttpService(PropKit.get("ethnet")));
		log.info("开始获取区块...开始区块:" + start+"  结束区块:"+end);
		for (int i = start; i <= end; i=i+1) {
			DefaultBlockParameter bnum = DefaultBlockParameter.valueOf(BigInteger.valueOf(i));
			int num = i;
			CompletableFuture<EthBlock> completableFuture = web3j.ethGetBlockByNumber(bnum, true).sendAsync();
			new Thread(new Runnable() {
				@Override
				public void run() {
					// 模拟执行耗时任务
					String starttime = LynxUtil.getCurrentTime();
					log.info("后台执行区块{"+num+"}处理中");
					try {
						EthBlock.Block ethblock = completableFuture.get().getBlock();
						dealBlock(ethblock);
						//dealLatestBlock(ethblock);
					} catch (Exception e) {
						log.error(e.toString());
					}
					String endtime = LynxUtil.getCurrentTime();
					log.info("后台执行区块{"+num+"}处理完成"+starttime+"|"+endtime);

				}


			}).start();
			Thread.sleep(200); 
		}
	}

	public  void dealBlock(EthBlock.Block ethblock) throws InterruptedException, ExecutionException, IOException{
		log.debug("block number:" + ethblock.getNumber());
		
		
		com.eth.model.EthBlock block = new com.eth.model.EthBlock();
		block.setDifficulty(ethblock.getDifficulty().toString());
		block.setGasLimit(ethblock.getGasLimit().toString());
		block.setGasUsed(ethblock.getGasUsed().toString());
		block.setHash(ethblock.getHash());
		block.setMiner(ethblock.getMiner());
		block.setMixHash(ethblock.getMixHash());
		block.setNonce(ethblock.getNonce().toString());
		block.setNumber(ethblock.getNumber().toString());
		block.setParentHash(ethblock.getParentHash());
		block.setReceiptsRoot(ethblock.getReceiptsRoot());
		block.setSha3Uncles(ethblock.getSha3Uncles());
		block.setSize(ethblock.getSize().toString());
		block.setStateRoot(ethblock.getStateRoot());
		block.setTimestamp(ethblock.getTimestamp().toString());
		block.setTotalDifficulty(ethblock.getTotalDifficulty().toString());
		block.setTransactionsRoot(ethblock.getTransactionsRoot());
		block.setTransactionsNum(ethblock.getTransactions().size());
		Date blockDate = new Date(ethblock.getTimestamp().longValue()*1000);
		block.setBlockDate(blockDate);
		try {
			block.save();
		} catch (Exception e) {
			log.debug(e.toString());
			try {
				block.delete();
				block.save();
			} catch (Exception e1) {
				log.debug(e1.toString());
			}
		}
		Long dbnumber = com.eth.model.EthBlock.dao.getCountByBlockNumber(ethblock.getNumber());
		int rawnumber = ethblock.getTransactions().size();
		if(hist_flag == true && rawnumber == dbnumber.intValue()) {
			log.info("区块" + ethblock.getNumber()+"数据已经处理,跳过");
			return;
		}
		List<TransactionResult> txlist = ethblock.getTransactions();
		log.debug("tx num:"+txlist.size());
		for(int txnum = 0;txnum <txlist.size();txnum++){
			Transaction tr = (Transaction) txlist.get(txnum);
			
			EthTx tx = new EthTx();
			tx.setBlockHash(tr.getBlockHash());
			tx.setBlockNumber(tr.getBlockNumber().toString());
			tx.setTimestamp(ethblock.getTimestamp().toString());
			tx.setFromAddress(tr.getFrom());
			tx.setToAddress(tr.getTo());
			tx.setGas(tr.getGas().toString());
			tx.setGasPrice(tr.getGasPrice().toString());
			tx.setHash(tr.getHash());
			tx.setNonce(tr.getNonce().toString());
			tx.setR(tr.getR());
			tx.setS(tr.getS());
			tx.setV(String.valueOf(tr.getV()));
			tx.setValue(tr.getValue().toString());
			tx.setBlockDate(blockDate);
			try {
				//处理交易信息内容
				//1.如果是以太坊交易,需要更新以太坊地址以及金额
				//2.如果是合约交易,需要更新合约,以及对应的Token金额
				dealTxInfo(tx);

			}catch (Exception e) {
				log.debug(e.toString());
			}

			try {
				tx.save();
			} catch (Exception e) {
				log.debug(e.toString());
				try {
					tx.delete();
					tx.save();
				} catch (Exception e1) {
					log.debug(e1.toString());
				}
			}
		}

	}
	private void dealTxInfo(EthTx tx) throws Exception {
		TransactionReceipt tr = web3j.ethGetTransactionReceipt(tx.getHash()).send().getResult();
		
		String to_address = tx.getToAddress();
		String contractFlag = GetErc20Flag(to_address);

		log.debug(tr.getTo()+"    contractFlag:"+contractFlag);
		
		if(contractFlag=="Y"||contractFlag.equals("Y")) {
			//合约地址处理
			try {
		        StandardToken contract = StandardToken.load(to_address, web3j, credentialsAdmin, BigInteger.valueOf(3010000000L),Contract.GAS_LIMIT);
				for(StandardToken.TransferEventResponse event : contract.getTransferEvents(tr)) {
		            log.debug("token tran hash:"+tx.getHash()+" from: " + event.from+ " to: " + event.to+ " value: " + event.value);
		            EthTokenTx tx_token = new EthTokenTx();
		            tx_token.setHash(tx.getHash());
		            tx_token.setTokenAddres(to_address);
		            tx_token.setTokenFrom(event.from);
		            tx_token.setTokenTo(event.to);
		            tx_token.setTokenValue(event.value.toString());
		            tx_token.setBlockDate(tx.getBlockDate());
		            try {
			            tx_token.save();
		            }catch (Exception e) {
			            tx_token.delete();
			            tx_token.save();
					}
		            dealToken_hold(to_address,event.from,tx.getBlockDate());
		            dealToken_hold(to_address,event.to,tx.getBlockDate());
		    	}		    	
			}catch (Exception e) {
				log.info(e.toString());
			}
			
		}else {
			
		}
			
	}

	private void dealToken_hold(String tokenAddress,String address, Date date) throws Exception {
		
		EthTokenAddress token = EthTokenAddress.dao.findById(tokenAddress,address);
		if(token!=null) {
			if(date.before(token.getTokenHoldUpdatetime())) {
				token.setTokenHoldUpdatetime(date);
				if(date.before(token.getTokenHoldCreatetime())) {
					token.setTokenHoldCreatetime(date);
				}
				String value = getTokenValue(tokenAddress,address);
				token.setTokenHoldValue(value);
				token.delete();
				token.save();
			}

		}else {
			token = new EthTokenAddress();
			token.setTokenAddress(tokenAddress);
			token.setTokenHoldAddress(address);
			token.setTokenHoldCreatetime(date);
			token.setTokenHoldUpdatetime(date);
			token.setTokenHoldValue(getTokenValue(tokenAddress,address));
			token.save();
		}
		
	}
	private String  getTokenValue(String tokenAddress, String address) throws Exception {
        StandardToken token = StandardToken.load(tokenAddress, web3j, credentialsAdmin, BigInteger.valueOf(3010000000L),Contract.GAS_LIMIT);
        String value = token.balanceOf(address).send().toString();
		return value;
		
	}
	private String GetErc20Flag(String address) throws Exception {
		EthAddressType add = EthAddressType.dao.findById(address);
		if(add!=null) {
			return add.getErc20Flag();
		}else {
			String code = web3j.ethGetCode(address, DefaultBlockParameter.valueOf("Latest")).send().getCode();
			if(code=="0x"||code.equals("0x")) {
				EthAddressType addr = new EthAddressType();
				addr.setAddress(address);
				addr.setContractFlag("N");
				addr.setErc20Flag("N");
				addr.setValue("0");
				addr.save();
				return "N";
			}else {
				EthAddressType addr = new EthAddressType();
				addr.setAddress(address);
				addr.setContractFlag("Y");
				addr.setValue("0");
				
				try {
					EthToken token = new EthToken();
			        StandardToken contract = StandardToken.load(address, web3j, credentialsAdmin, BigInteger.valueOf(3010000000L),Contract.GAS_LIMIT);
			        token.setTokenAddress(address);
			        token.setTokenName(contract.name().send());
			        token.setTokenSymbol(contract.symbol().send());
			        token.setTokenTotalsupply(contract.totalSupply().send().toString());
			        token.setTokenDecimals(contract.decimals().send().toString());
			        token.save();
				}catch(Exception e) {
					log.info(e.toString());
					addr.setErc20Flag("N");
					addr.save();
					return "N";
				}
				addr.setErc20Flag("Y");
				addr.save();
				return "Y";
			}
		}
	}

	public  void EthValueChange(String address, String value, String method) {
		EthAddress eth = EthAddress.dao.findById(address);
		if (eth != null) {
			if (method == "add") {
				BigInteger old = new BigInteger(eth.getEthHoldValue());
				BigInteger add = new BigInteger(value);
				eth.setEthHoldValue(old.add(add).toString());
			} else if (method == "sub") {
				BigInteger old = new BigInteger(eth.getEthHoldValue());
				BigInteger sub = new BigInteger(value);
				eth.setEthHoldValue(old.subtract(sub).toString());			
			}
			eth.update();
		} else {
			eth = new EthAddress();
			if (method == "add") {
				eth.setEthHoldValue(value);
			} else if (method == "sub") {
				eth.setEthHoldValue("0");
			}
			eth.setEthAddress(address);
			eth.save();
		}

	}
}
