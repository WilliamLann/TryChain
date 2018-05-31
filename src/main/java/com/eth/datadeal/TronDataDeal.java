package com.eth.datadeal;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigInteger;
import java.net.URI;
import java.net.URL;
import java.net.URLConnection;
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

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.eth.model.EthAddress;
import com.eth.model.EthAddressType;
import com.eth.model.LynxUtil;
import com.eth.model.TronAddress;
import com.eth.model.TronBlock;
import com.eth.model.TronTx;
import com.eth.model.TronTxFrozen;
import com.eth.model.TronTxTokenTran;
import com.eth.model.TronTxTran;
import com.eth.model.TronTxVotes;
import com.eth.model.EthToken;
import com.eth.model.EthTokenAddress;
import com.eth.model.EthTx;
import com.eth.model.EthTokenTx;
import com.eth.model.LynxUser;
import com.jfinal.kit.PropKit;
import com.jfinal.plugin.activerecord.Db;

import rx.Subscription;

public class TronDataDeal {
	private static final Logger log = LoggerFactory.getLogger(Application.class);
	static Web3j web3j;
	static Credentials credentialsAdmin;
	static boolean hist_flag = false;

	public static void main(String[] args) throws Exception {
		LynxUtil.startARP();
		TronDataDeal deal = new TronDataDeal();
		deal.updateLatestBlock();
	}

	public TronDataDeal() {

	}

	public void test() {
		try {
			String url = "https://api.tronscan.org/api/block";
			String json = getHttpResponse(url);
			System.out.println(json);
			JSONObject blocks = JSONObject.parseObject(json);
			if (blocks.getInteger("total") > 0) {
				for (int i = 1; i < blocks.getInteger("total"); i++) {
					System.out.println(i);
					try {
						dealBlock(i);
						dealBlockTxs(i);
					} catch (Exception e) {
						System.out.println(e.getMessage());
					}

				}

			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
	public void updateLatestBlock() throws InterruptedException {
		while(true) {
			int maxnumber = Db.queryInt("select max(number) from tron_block");
			int next = maxnumber+1;
			System.out.println(next);
			try {
				dealBlock(next);
				dealBlockTxs(next);
			} catch (Exception e) {
				System.out.println(e.getMessage());
			}
			Thread.sleep(200); 
		}
		
	}

	private void dealBlockTxs(int block) {
		String blocktxurl = "https://api.tronscan.org/api/transaction?block=" + block;		
		//String blocktxurl = "https://api.tronscan.org/api/transaction";
		String blocktxjson = getHttpResponse(blocktxurl);
		JSONObject blocktxresult = JSONObject.parseObject(blocktxjson);
		if (blocktxresult.getInteger("total") >= 0) {
			String data = blocktxresult.getString("data");
			JSONArray arr = JSONArray.parseArray(data);
			for (int j = 0; j < arr.size(); j++) {
				JSONObject tx = arr.getJSONObject(j);
				TronTx tronTx = new TronTx();
				tronTx.setHash(tx.getString("hash"));
				tronTx.setBlock(tx.getLong("block"));
				if (tx.getString("timestamp").equals("0")) {
					Date date = new Date();
					date.setTime(100000L);
					tronTx.setTimestamp(date);
				} else {
					tronTx.setTimestamp(tx.getTimestamp("timestamp"));

				}
				tronTx.setConfirmed(tx.getBoolean("confirmed"));
				tronTx.setOwnerAddress(tx.getString("ownerAddress"));
				tronTx.setContractType(tx.getString("contractType"));
				String contractType = tx.getString("contractType");
				JSONObject contractData = JSONObject.parseObject(tx.getString("contractData"));
				switch(contractType){
				case "1":
					TronTxTran tran = new TronTxTran();
					tran.setHash(tx.getString("hash"));
					tran.setTranFrom(contractData.getString("from"));
					tran.setTranTo(contractData.getString("to"));
					tran.setTranValue(contractData.getString("amount"));
					try {
						tran.save();
					}catch (Exception e) {
						tran.delete();
						tran.save();
					}
					dealAccount(contractData.getString("from"));
					dealAccount(contractData.getString("to"));

				    break;
				case "2":
				    //...;
				    break;
				case "4":
					JSONArray votes = JSONArray.parseArray(contractData.getString("votes"));
					for(int i = 0;i<votes.size();i++) {
						JSONObject voteobject = votes.getJSONObject(i);
						TronTxVotes vote = new TronTxVotes();
						vote.setHash(tx.getString("hash"));
						vote.setNumber((long)i);
						vote.setVoteAddress(voteobject.getString("voteAddress"));
						vote.setVoteCount(voteobject.getString("voteCount"));
						try {
							vote.save();
						}catch (Exception e) {
							vote.delete();
							vote.save();
						}
					}
				    break;
				case "9":
					TronTxTokenTran tokentran = new TronTxTokenTran();
					tokentran.setHash(tx.getString("hash"));
					tokentran.setOwnerAddress(contractData.getString("ownerAddress"));
					tokentran.setToAddress(contractData.getString("toAddress"));
					tokentran.setAmount(contractData.getString("amount"));
					tokentran.setToken(contractData.getString("token"));
					try {
						tokentran.save();
					}catch (Exception e) {
						tokentran.delete();
						tokentran.save();
					}
					dealAccount(contractData.getString("toAddress"));
				    break;
				case "11":
					TronTxFrozen fronzen = new TronTxFrozen();
					fronzen.setHash(tx.getString("hash"));
					fronzen.setOwnerAddress(contractData.getString("ownerAddress"));
					fronzen.setFrozenBalance(contractData.getString("frozenBalance"));
					fronzen.setFrozenDuration(contractData.getString("frozenDuration"));
					try {
						fronzen.save();
					}catch (Exception e) {
						fronzen.delete();
						fronzen.save();
					}
				    break;
		        default:
					System.out.println("未处理类型"+contractType);
					break;
				}

				
				dealAccount(tx.getString("ownerAddress"));
				try {
					tronTx.save();
				} catch (Exception e) {
					tronTx.delete();
					tronTx.save();
				}
			}
		}
	}

	private void dealAccount(String string) {
		String accounturl = "https://api.tronscan.org/api/account?address="+string;
		String accountjson = getHttpResponse(accounturl);
		JSONObject accountresult = JSONObject.parseObject(accountjson);
		if (accountresult.getInteger("total") > 0) {
			String data = accountresult.getString("data");
			// System.out.println(data);
			JSONArray arr = JSONArray.parseArray(data);
			JSONObject account = arr.getJSONObject(0);			
			TronAddress tronAddress = new TronAddress();
			tronAddress.setAddress(account.getString("address"));
			tronAddress.setName(account.getString("name"));
			tronAddress.setBalance(account.getString("balance"));
			tronAddress.setPower(account.getString("power"));
			tronAddress.setDateCreated(account.getTimestamp("dateCreated"));
			tronAddress.setDateUpdate(account.getTimestamp("dateUpdated"));
			try {
				tronAddress.save();
			} catch (Exception e) {
				tronAddress.delete();
				tronAddress.save();
			}
			JSONObject tokenBalances = JSONObject.parseObject(account.getString("tokenBalances"));

		}
		
	}

	private void dealBlock(int i) {
		String blockurl = "https://api.tronscan.org/api/block?number=" + i;
		String blockjson = getHttpResponse(blockurl);
		//System.out.println(blockjson);

		JSONObject blockresult = JSONObject.parseObject(blockjson);
		if (blockresult.getInteger("total") > 0) {
			String data = blockresult.getString("data");
			// System.out.println(data);
			JSONArray arr = JSONArray.parseArray(data);
			JSONObject block = arr.getJSONObject(0);
			TronBlock tronBlock = new TronBlock();
			tronBlock.setConfirmed(block.getBoolean("confirmed"));
			tronBlock.setHash(block.getString("hash"));
			tronBlock.setNrOfTrx(block.getInteger("nrOfTrx"));
			tronBlock.setNumber(block.getLong("number"));
			tronBlock.setParentHash(block.getString("parentHash"));
			tronBlock.setSize(block.getLong("size"));
			if (i > 0) {
				tronBlock.setTimestamp(block.getDate("timestamp"));
			} else {
				Date date = new Date();
				date.setTime(100000L);
				tronBlock.setTimestamp(date);
			}
			tronBlock.setTxTrieRoot(block.getString("txTrieRoot"));
			tronBlock.setWitnessAddress(block.getString("witnessAddress"));
			tronBlock.setWitnessId(block.getInteger("witnessId"));

			try {
				tronBlock.save();
			} catch (Exception e) {
				tronBlock.delete();
				tronBlock.save();
			}
		}
	}

	public static String getHttpResponse(String allConfigUrl) {
		BufferedReader in = null;
		StringBuffer result = null;
		try {

			URI uri = new URI(allConfigUrl);
			URL url = uri.toURL();
			URLConnection connection = url.openConnection();
			connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
			connection.setRequestProperty("Charset", "utf-8");

			connection.connect();

			result = new StringBuffer();
			// 读取URL的响应
			in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
			String line;
			while ((line = in.readLine()) != null) {
				result.append(line);
			}

			return result.toString();

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (in != null) {
					in.close();
				}
			} catch (Exception e2) {
				e2.printStackTrace();
			}
		}

		return null;

	}
}
