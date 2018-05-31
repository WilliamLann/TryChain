package org.web3j.sample;

import java.math.BigInteger;
import java.util.concurrent.CompletableFuture;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.web3j.crypto.Credentials;
import org.web3j.crypto.WalletUtils;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.http.HttpService;
import org.web3j.sample.contracts.generated.StandardToken;
import org.web3j.sample.contracts.generated.TryChain;
import org.web3j.tx.Contract;

import com.eth.model.LynxUtil;
import com.eth.model.LynxUser;
import com.jfinal.kit.PropKit;

public class DeployContractTest {
    private static final Logger log = LoggerFactory.getLogger(Application.class);

    public static void main(String[] args) throws Exception {
        new DeployContractTest().run();
    }

    private void run() throws Exception {
    	LynxUtil.startARP();
    	Web3j web3j = Web3j.build(new HttpService(PropKit.get("ethnet")));
    	LynxUser user = LynxUser.dao.findById(15);
        log.info(user.getEthAddress());
        Credentials credentials =WalletUtils.loadCredentials(user.getPassword(),PropKit.get("static.new_path")+user.getEthFilename());
        CompletableFuture<TryChain> contract = TryChain.deploy(web3j, credentials,BigInteger.valueOf(10010000000L), Contract.GAS_LIMIT).sendAsync();
    }
}
