package org.web3j.sample;

import java.math.BigDecimal;
import java.math.BigInteger;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.web3j.crypto.Credentials;
import org.web3j.crypto.WalletUtils;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.admin.Admin;
import org.web3j.protocol.core.DefaultBlockParameter;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.protocol.http.HttpService;
import org.web3j.sample.contracts.generated.Greeter;
import org.web3j.sample.contracts.generated.StandardToken;
import org.web3j.tx.Contract;
import org.web3j.tx.ManagedTransaction;
import org.web3j.tx.Transfer;
import org.web3j.utils.Convert;
import org.web3j.utils.Numeric;

/**
 * A simple web3j application that demonstrates a number of core features of web3j:
 *
 * <ol>
 *     <li>Connecting to a node on the Ethereum network</li>
 *     <li>Loading an Ethereum wallet file</li>
 *     <li>Sending Ether from one address to another</li>
 *     <li>Deploying a smart contract to the network</li>
 *     <li>Reading a value from the deployed smart contract</li>
 *     <li>Updating a value in the deployed smart contract</li>
 *     <li>Viewing an event logged by the smart contract</li>
 * </ol>
 *
 * <p>To run this demo, you will need to provide:
 *
 * <ol>
 *     <li>Ethereum client (or node) endpoint. The simplest thing to do is
 *     <a href="https://infura.io/register.html">request a free access token from Infura</a></li>
 *     <li>A wallet file. This can be generated using the web3j
 *     <a href="https://docs.web3j.io/command_line.html">command line tools</a></li>
 *     <li>Some Ether. This can be requested from the
 *     <a href="https://www.rinkeby.io/#faucet">Rinkeby Faucet</a></li>
 * </ol>
 *
 * <p>For further background information, refer to the project README.
 */
public class Application {

    private static final Logger log = LoggerFactory.getLogger(Application.class);

    public static void main(String[] args) throws Exception {
        new Application().run();
    }

    private void run() throws Exception {

        Admin admin = Admin.build(new HttpService("http://39.107.60.59:8545"));  
        
        // We start by creating a new web3j instance to connect to remote nodes on the network.
        // Note: if using web3j Android, use Web3jFactory.build(...
        Web3j web3j = Web3j.build(new HttpService("http://39.107.60.59:8545"));  // FIXME: Enter your Infura token here;
        log.info("Connected to Ethereum client version: "
                + web3j.web3ClientVersion().send().getWeb3ClientVersion());
      
        // We then need to load our Ethereum wallet file
        // FIXME: Generate a new wallet file using the web3j command line tools https://docs.web3j.io/command_line.html
        log.info(System.getProperty("user.dir"));
        Credentials credentials =
                WalletUtils.loadCredentials(
                        "lanwei",
                        "key1");
        log.info("Credentials loaded");
        
        /*
        // FIXME: Request some Ether for the Rinkeby test network at https://www.rinkeby.io/#faucet
        log.info("Sending 1 Wei ("
                + Convert.fromWei("1", Convert.Unit.ETHER).toPlainString() + " Ether)");
        TransactionReceipt transferReceipt = Transfer.sendFunds(
                web3j, credentials,
                "0x8fd7c4619497b07a8e746ca0505ca686aa54f2fc",  // you can put any address here
                BigDecimal.ONE, Convert.Unit.WEI)  // 1 wei = 10^-18 Ether
                .send();
        log.info("Transaction complete, view it at https://rinkeby.etherscan.io/tx/"
                + transferReceipt.getTransactionHash());
        
        // Now lets deploy a smart contract
        log.info("Deploying smart contract");
     
        StandardToken contract = StandardToken.deploy(
                web3j, credentials,
                BigInteger.valueOf(1L), Contract.GAS_LIMIT).send();
        String contractAddress = contract.getContractAddress();
        log.info("Smart contract deployed to address " + contractAddress);
        log.info("View contract at https://rinkeby.etherscan.io/address/" + contractAddress);
        // Lets modify the value in our smart contract
        log.info("Value totalSupply: " +contract.totalSupply().send());
         */
        
        String conAddress = "0x79218b1b09b65668e5604938b98ff21f5c65f456";
        StandardToken test = StandardToken.load(conAddress, web3j, credentials, BigInteger.valueOf(1L), Contract.GAS_LIMIT);
    	BigInteger bal1 = web3j.ethGetBalance("0x44ed4c6abc0d3f2be9fb1f2d8fed867034f9bccd", DefaultBlockParameter.valueOf("latest")).send().getBalance();
    	log.info("Value totalSupply: " +test.totalSupply().send());
    	BigInteger bal2 = web3j.ethGetBalance("0x44ed4c6abc0d3f2be9fb1f2d8fed867034f9bccd", DefaultBlockParameter.valueOf("latest")).send().getBalance();
    	log.info("Value totalSupply:"+bal1);
    	log.info("Value totalSupply:"+bal2);

         /*
        String conAddress = "0x5a7978a8d2841ed10e056599bd224f48fd86e0aa";
        Greeter test = Greeter.load(conAddress, web3j, credentials, BigInteger.valueOf(1L), Contract.GAS_LIMIT);
        log.info(test.greet().send());
        /*
        // Events enable us to log specific events happening during the execution of our smart
        // contract to the blockchain. Index events cannot be logged in their entirety.
        // For Strings and arrays, the hash of values is provided, not the original value.
        // For further information, refer to https://docs.web3j.io/filters.html#filters-and-events
        for (Greeter.ModifiedEventResponse event : contract.getModifiedEvents(transactionReceipt)) {
            log.info("Modify event fired, previous value: " + event.oldGreeting
                    + ", new value: " + event.newGreeting);
            log.info("Indexed event previous value: " + Numeric.toHexString(event.oldGreetingIdx)
                    + ", new value: " + Numeric.toHexString(event.newGreetingIdx));
        }
        */
    }
}
