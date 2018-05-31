package org.web3j.sample.contracts.generated;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import org.web3j.abi.EventEncoder;
import org.web3j.abi.TypeReference;
import org.web3j.abi.datatypes.Address;
import org.web3j.abi.datatypes.Bool;
import org.web3j.abi.datatypes.Event;
import org.web3j.abi.datatypes.Function;
import org.web3j.abi.datatypes.Type;
import org.web3j.abi.datatypes.Utf8String;
import org.web3j.abi.datatypes.generated.Uint256;
import org.web3j.crypto.Credentials;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameter;
import org.web3j.protocol.core.RemoteCall;
import org.web3j.protocol.core.methods.request.EthFilter;
import org.web3j.protocol.core.methods.response.Log;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.tx.Contract;
import org.web3j.tx.TransactionManager;
import rx.Observable;
import rx.functions.Func1;

/**
 * <p>Auto generated code.
 * <p><strong>Do not modify!</strong>
 * <p>Please use the <a href="https://docs.web3j.io/command_line.html">web3j command line tools</a>,
 * or the org.web3j.codegen.SolidityFunctionWrapperGenerator in the 
 * <a href="https://github.com/web3j/web3j/tree/master/codegen">codegen module</a> to update.
 *
 * <p>Generated with web3j version 3.3.1.
 */
public class TryChain extends Contract {
    private static final String BINARY = "606060405260018054600160a060020a03191633600160a060020a03161790556b033b2e3c9fd0803ce800000060058190556aa56fa5b99019a5c8000000600681905561005991906401000000006100ba8102610d911704565b6007556901b1ae4d6e2ef50000006008556009805460ff19169055341561007f57600080fd5b60018054600160a060020a03191633600160a060020a03908116919091179182905560065491166000908152600260205260409020556100cc565b6000828211156100c657fe5b50900390565b610dde806100db6000396000f3006060604052600436106101035763ffffffff60e060020a60003504166306fdde03811461010d578063095ea7b31461019757806318160ddd146101cd57806323b872dd146101f2578063313ce5671461021a5780633ccfd60b1461022d5780633fa4f2451461024057806342966c681461025357806370a082311461026957806395d89b41146102885780639b1cbccc1461029b578063a9059cbb146102ae578063aa6ca80814610103578063c108d542146102d0578063c489744b146102e3578063d8a5436014610308578063dd62ed3e1461031b578063e58fc54c14610340578063efca2eed1461035f578063f2fde38b14610372578063f9f92be414610391575b61010b6103b0565b005b341561011857600080fd5b610120610499565b60405160208082528190810183818151815260200191508051906020019080838360005b8381101561015c578082015183820152602001610144565b50505050905090810190601f1680156101895780820380516001836020036101000a031916815260200191505b509250505060405180910390f35b34156101a257600080fd5b6101b9600160a060020a03600435166024356104d0565b604051901515815260200160405180910390f35b34156101d857600080fd5b6101e061057c565b60405190815260200160405180910390f35b34156101fd57600080fd5b6101b9600160a060020a0360043581169060243516604435610582565b341561022557600080fd5b6101e0610712565b341561023857600080fd5b61010b610717565b341561024b57600080fd5b6101e0610771565b341561025e57600080fd5b61010b600435610777565b341561027457600080fd5b6101e0600160a060020a0360043516610865565b341561029357600080fd5b610120610880565b34156102a657600080fd5b6101b96108b7565b34156102b957600080fd5b6101b9600160a060020a0360043516602435610924565b34156102db57600080fd5b6101b9610a2d565b34156102ee57600080fd5b6101e0600160a060020a0360043581169060243516610a36565b341561031357600080fd5b6101e0610aa7565b341561032657600080fd5b6101e0600160a060020a0360043581169060243516610aad565b341561034b57600080fd5b6101b9600160a060020a0360043516610ad8565b341561036a57600080fd5b6101e0610bdc565b341561037d57600080fd5b61010b600160a060020a0360043516610be2565b341561039c57600080fd5b6101b9600160a060020a0360043516610c39565b600954600090819060ff16156103c557600080fd5b600160a060020a03331660009081526004602052604090205460ff16156103eb57600080fd5b60075460085411156103fe576007546008555b600754600854111561040f57600080fd5b505060085433906104208282610c4e565b50600081111561044e57600160a060020a0382166000908152600460205260409020805460ff191660011790555b60055460065410610467576009805460ff191660011790555b6104926201869f610486620186a0600854610d4f90919063ffffffff16565b9063ffffffff610d6616565b6008555050565b60408051908101604052600881527f547279436861696e000000000000000000000000000000000000000000000000602082015281565b600081158015906105055750600160a060020a0333811660009081526003602090815260408083209387168352929052205415155b1561051257506000610576565b600160a060020a03338116600081815260036020908152604080832094881680845294909152908190208590557f8c5be1e5ebec7d5bd14f71427d1e84f3dd0314c0f7b2291e5b200ac8c7c3b9259085905190815260200160405180910390a35060015b92915050565b60055481565b60006060606436101561059157fe5b600160a060020a03841615156105a657600080fd5b600160a060020a0385166000908152600260205260409020548311156105cb57600080fd5b600160a060020a03808616600090815260036020908152604080832033909416835292905220548311156105fe57600080fd5b600160a060020a038516600090815260026020526040902054610627908463ffffffff610d9116565b600160a060020a038087166000908152600260209081526040808320949094556003815283822033909316825291909152205461066a908463ffffffff610d9116565b600160a060020a03808716600090815260036020908152604080832033851684528252808320949094559187168152600290915220546106b0908463ffffffff610da316565b600160a060020a03808616600081815260026020526040908190209390935591908716907fddf252ad1be2c89b69c2b068fc378daa952ba7f163c4a11628f55a4df523b3ef9086905190815260200160405180910390a3506001949350505050565b601281565b60015460009033600160a060020a0390811691161461073557600080fd5b50600154600160a060020a0330811631911681156108fc0282604051600060405180830381858888f19350505050151561076e57600080fd5b50565b60085481565b60015460009033600160a060020a0390811691161461079557600080fd5b600160a060020a0333166000908152600260205260409020548211156107ba57600080fd5b5033600160a060020a0381166000908152600260205260409020546107df9083610d91565b600160a060020a03821660009081526002602052604090205560055461080b908363ffffffff610d9116565b600555600654610821908363ffffffff610d9116565b600655600160a060020a0381167fcc16f5dbb4873280815c1ee09dbd06736cffcc184412cf7a71a0fdb75d397ca58360405190815260200160405180910390a25050565b600160a060020a031660009081526002602052604090205490565b60408051908101604052600381527f5452590000000000000000000000000000000000000000000000000000000000602082015281565b60015460009033600160a060020a039081169116146108d557600080fd5b60095460ff16156108e557600080fd5b6009805460ff191660011790557f7f95d919e78bdebe8a285e6e33357c2fcb65ccf66e72d7573f9f8f6caad0c4cc60405160405180910390a150600190565b60006040604436101561093357fe5b600160a060020a038416151561094857600080fd5b600160a060020a03331660009081526002602052604090205483111561096d57600080fd5b600160a060020a033316600090815260026020526040902054610996908463ffffffff610d9116565b600160a060020a0333811660009081526002602052604080822093909355908616815220546109cb908463ffffffff610da316565b600160a060020a0380861660008181526002602052604090819020939093559133909116907fddf252ad1be2c89b69c2b068fc378daa952ba7f163c4a11628f55a4df523b3ef9086905190815260200160405180910390a35060019392505050565b60095460ff1681565b60008281600160a060020a0382166370a082318560405160e060020a63ffffffff8416028152600160a060020a039091166004820152602401602060405180830381600087803b1515610a8857600080fd5b5af11515610a9557600080fd5b50505060405180519695505050505050565b60075481565b600160a060020a03918216600090815260036020908152604080832093909416825291909152205490565b6001546000908190819033600160a060020a03908116911614610afa57600080fd5b83915081600160a060020a03166370a082313060405160e060020a63ffffffff8416028152600160a060020a039091166004820152602401602060405180830381600087803b1515610b4b57600080fd5b5af11515610b5857600080fd5b5050506040518051600154909250600160a060020a03808516925063a9059cbb91168360405160e060020a63ffffffff8516028152600160a060020a0390921660048301526024820152604401602060405180830381600087803b1515610bbe57600080fd5b5af11515610bcb57600080fd5b505050604051805195945050505050565b60065481565b60015433600160a060020a03908116911614610bfd57600080fd5b600160a060020a0381161561076e5760018054600160a060020a03831673ffffffffffffffffffffffffffffffffffffffff1990911617905550565b60046020526000908152604090205460ff1681565b60095460009060ff1615610c6157600080fd5b600654610c74908363ffffffff610da316565b600655600754610c8a908363ffffffff610d9116565b600755600160a060020a038316600090815260026020526040902054610cb6908363ffffffff610da316565b600160a060020a0384166000818152600260205260409081902092909255907f8940c4b8e215f8822c5c8f0056c12652c746cbc57eedbd2a440b175971d47a779084905190815260200160405180910390a2600160a060020a03831660007fddf252ad1be2c89b69c2b068fc378daa952ba7f163c4a11628f55a4df523b3ef8460405190815260200160405180910390a3506001610576565b6000808284811515610d5d57fe5b04949350505050565b6000828202831580610d825750828482811515610d7f57fe5b04145b1515610d8a57fe5b9392505050565b600082821115610d9d57fe5b50900390565b600082820183811015610d8a57fe00a165627a7a72305820d76469de945ee1fa952a1344640e997b3f86300207607e4243d437e425274e390029";

    protected TryChain(String contractAddress, Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit) {
        super(BINARY, contractAddress, web3j, credentials, gasPrice, gasLimit);
    }

    protected TryChain(String contractAddress, Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        super(BINARY, contractAddress, web3j, transactionManager, gasPrice, gasLimit);
    }

    public List<TransferEventResponse> getTransferEvents(TransactionReceipt transactionReceipt) {
        final Event event = new Event("Transfer", 
                Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {}, new TypeReference<Address>() {}),
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}));
        List<Contract.EventValuesWithLog> valueList = extractEventParametersWithLog(event, transactionReceipt);
        ArrayList<TransferEventResponse> responses = new ArrayList<TransferEventResponse>(valueList.size());
        for (Contract.EventValuesWithLog eventValues : valueList) {
            TransferEventResponse typedResponse = new TransferEventResponse();
            typedResponse.log = eventValues.getLog();
            typedResponse._from = (String) eventValues.getIndexedValues().get(0).getValue();
            typedResponse._to = (String) eventValues.getIndexedValues().get(1).getValue();
            typedResponse._value = (BigInteger) eventValues.getNonIndexedValues().get(0).getValue();
            responses.add(typedResponse);
        }
        return responses;
    }

    public Observable<TransferEventResponse> transferEventObservable(DefaultBlockParameter startBlock, DefaultBlockParameter endBlock) {
        final Event event = new Event("Transfer", 
                Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {}, new TypeReference<Address>() {}),
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}));
        EthFilter filter = new EthFilter(startBlock, endBlock, getContractAddress());
        filter.addSingleTopic(EventEncoder.encode(event));
        return web3j.ethLogObservable(filter).map(new Func1<Log, TransferEventResponse>() {
            @Override
            public TransferEventResponse call(Log log) {
                Contract.EventValuesWithLog eventValues = extractEventParametersWithLog(event, log);
                TransferEventResponse typedResponse = new TransferEventResponse();
                typedResponse.log = log;
                typedResponse._from = (String) eventValues.getIndexedValues().get(0).getValue();
                typedResponse._to = (String) eventValues.getIndexedValues().get(1).getValue();
                typedResponse._value = (BigInteger) eventValues.getNonIndexedValues().get(0).getValue();
                return typedResponse;
            }
        });
    }

    public List<ApprovalEventResponse> getApprovalEvents(TransactionReceipt transactionReceipt) {
        final Event event = new Event("Approval", 
                Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {}, new TypeReference<Address>() {}),
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}));
        List<Contract.EventValuesWithLog> valueList = extractEventParametersWithLog(event, transactionReceipt);
        ArrayList<ApprovalEventResponse> responses = new ArrayList<ApprovalEventResponse>(valueList.size());
        for (Contract.EventValuesWithLog eventValues : valueList) {
            ApprovalEventResponse typedResponse = new ApprovalEventResponse();
            typedResponse.log = eventValues.getLog();
            typedResponse._owner = (String) eventValues.getIndexedValues().get(0).getValue();
            typedResponse._spender = (String) eventValues.getIndexedValues().get(1).getValue();
            typedResponse._value = (BigInteger) eventValues.getNonIndexedValues().get(0).getValue();
            responses.add(typedResponse);
        }
        return responses;
    }

    public Observable<ApprovalEventResponse> approvalEventObservable(DefaultBlockParameter startBlock, DefaultBlockParameter endBlock) {
        final Event event = new Event("Approval", 
                Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {}, new TypeReference<Address>() {}),
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}));
        EthFilter filter = new EthFilter(startBlock, endBlock, getContractAddress());
        filter.addSingleTopic(EventEncoder.encode(event));
        return web3j.ethLogObservable(filter).map(new Func1<Log, ApprovalEventResponse>() {
            @Override
            public ApprovalEventResponse call(Log log) {
                Contract.EventValuesWithLog eventValues = extractEventParametersWithLog(event, log);
                ApprovalEventResponse typedResponse = new ApprovalEventResponse();
                typedResponse.log = log;
                typedResponse._owner = (String) eventValues.getIndexedValues().get(0).getValue();
                typedResponse._spender = (String) eventValues.getIndexedValues().get(1).getValue();
                typedResponse._value = (BigInteger) eventValues.getNonIndexedValues().get(0).getValue();
                return typedResponse;
            }
        });
    }

    public List<DistrEventResponse> getDistrEvents(TransactionReceipt transactionReceipt) {
        final Event event = new Event("Distr", 
                Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {}),
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}));
        List<Contract.EventValuesWithLog> valueList = extractEventParametersWithLog(event, transactionReceipt);
        ArrayList<DistrEventResponse> responses = new ArrayList<DistrEventResponse>(valueList.size());
        for (Contract.EventValuesWithLog eventValues : valueList) {
            DistrEventResponse typedResponse = new DistrEventResponse();
            typedResponse.log = eventValues.getLog();
            typedResponse.to = (String) eventValues.getIndexedValues().get(0).getValue();
            typedResponse.amount = (BigInteger) eventValues.getNonIndexedValues().get(0).getValue();
            responses.add(typedResponse);
        }
        return responses;
    }

    public Observable<DistrEventResponse> distrEventObservable(DefaultBlockParameter startBlock, DefaultBlockParameter endBlock) {
        final Event event = new Event("Distr", 
                Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {}),
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}));
        EthFilter filter = new EthFilter(startBlock, endBlock, getContractAddress());
        filter.addSingleTopic(EventEncoder.encode(event));
        return web3j.ethLogObservable(filter).map(new Func1<Log, DistrEventResponse>() {
            @Override
            public DistrEventResponse call(Log log) {
                Contract.EventValuesWithLog eventValues = extractEventParametersWithLog(event, log);
                DistrEventResponse typedResponse = new DistrEventResponse();
                typedResponse.log = log;
                typedResponse.to = (String) eventValues.getIndexedValues().get(0).getValue();
                typedResponse.amount = (BigInteger) eventValues.getNonIndexedValues().get(0).getValue();
                return typedResponse;
            }
        });
    }

    public List<DistrFinishedEventResponse> getDistrFinishedEvents(TransactionReceipt transactionReceipt) {
        final Event event = new Event("DistrFinished", 
                Arrays.<TypeReference<?>>asList(),
                Arrays.<TypeReference<?>>asList());
        List<Contract.EventValuesWithLog> valueList = extractEventParametersWithLog(event, transactionReceipt);
        ArrayList<DistrFinishedEventResponse> responses = new ArrayList<DistrFinishedEventResponse>(valueList.size());
        for (Contract.EventValuesWithLog eventValues : valueList) {
            DistrFinishedEventResponse typedResponse = new DistrFinishedEventResponse();
            typedResponse.log = eventValues.getLog();
            responses.add(typedResponse);
        }
        return responses;
    }

    public Observable<DistrFinishedEventResponse> distrFinishedEventObservable(DefaultBlockParameter startBlock, DefaultBlockParameter endBlock) {
        final Event event = new Event("DistrFinished", 
                Arrays.<TypeReference<?>>asList(),
                Arrays.<TypeReference<?>>asList());
        EthFilter filter = new EthFilter(startBlock, endBlock, getContractAddress());
        filter.addSingleTopic(EventEncoder.encode(event));
        return web3j.ethLogObservable(filter).map(new Func1<Log, DistrFinishedEventResponse>() {
            @Override
            public DistrFinishedEventResponse call(Log log) {
                Contract.EventValuesWithLog eventValues = extractEventParametersWithLog(event, log);
                DistrFinishedEventResponse typedResponse = new DistrFinishedEventResponse();
                typedResponse.log = log;
                return typedResponse;
            }
        });
    }

    public List<BurnEventResponse> getBurnEvents(TransactionReceipt transactionReceipt) {
        final Event event = new Event("Burn", 
                Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {}),
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}));
        List<Contract.EventValuesWithLog> valueList = extractEventParametersWithLog(event, transactionReceipt);
        ArrayList<BurnEventResponse> responses = new ArrayList<BurnEventResponse>(valueList.size());
        for (Contract.EventValuesWithLog eventValues : valueList) {
            BurnEventResponse typedResponse = new BurnEventResponse();
            typedResponse.log = eventValues.getLog();
            typedResponse.burner = (String) eventValues.getIndexedValues().get(0).getValue();
            typedResponse.value = (BigInteger) eventValues.getNonIndexedValues().get(0).getValue();
            responses.add(typedResponse);
        }
        return responses;
    }

    public Observable<BurnEventResponse> burnEventObservable(DefaultBlockParameter startBlock, DefaultBlockParameter endBlock) {
        final Event event = new Event("Burn", 
                Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {}),
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}));
        EthFilter filter = new EthFilter(startBlock, endBlock, getContractAddress());
        filter.addSingleTopic(EventEncoder.encode(event));
        return web3j.ethLogObservable(filter).map(new Func1<Log, BurnEventResponse>() {
            @Override
            public BurnEventResponse call(Log log) {
                Contract.EventValuesWithLog eventValues = extractEventParametersWithLog(event, log);
                BurnEventResponse typedResponse = new BurnEventResponse();
                typedResponse.log = log;
                typedResponse.burner = (String) eventValues.getIndexedValues().get(0).getValue();
                typedResponse.value = (BigInteger) eventValues.getNonIndexedValues().get(0).getValue();
                return typedResponse;
            }
        });
    }

    public RemoteCall<String> name() {
        final Function function = new Function("name", 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Utf8String>() {}));
        return executeRemoteCallSingleValueReturn(function, String.class);
    }

    public RemoteCall<TransactionReceipt> approve(String _spender, BigInteger _value) {
        final Function function = new Function(
                "approve", 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(_spender), 
                new org.web3j.abi.datatypes.generated.Uint256(_value)), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteCall<BigInteger> totalSupply() {
        final Function function = new Function("totalSupply", 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}));
        return executeRemoteCallSingleValueReturn(function, BigInteger.class);
    }

    public RemoteCall<TransactionReceipt> transferFrom(String _from, String _to, BigInteger _amount) {
        final Function function = new Function(
                "transferFrom", 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(_from), 
                new org.web3j.abi.datatypes.Address(_to), 
                new org.web3j.abi.datatypes.generated.Uint256(_amount)), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteCall<BigInteger> decimals() {
        final Function function = new Function("decimals", 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}));
        return executeRemoteCallSingleValueReturn(function, BigInteger.class);
    }

    public RemoteCall<TransactionReceipt> withdraw() {
        final Function function = new Function(
                "withdraw", 
                Arrays.<Type>asList(), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteCall<BigInteger> value() {
        final Function function = new Function("value", 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}));
        return executeRemoteCallSingleValueReturn(function, BigInteger.class);
    }

    public RemoteCall<TransactionReceipt> burn(BigInteger _value) {
        final Function function = new Function(
                "burn", 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.generated.Uint256(_value)), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteCall<BigInteger> balanceOf(String _owner) {
        final Function function = new Function("balanceOf", 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(_owner)), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}));
        return executeRemoteCallSingleValueReturn(function, BigInteger.class);
    }

    public RemoteCall<String> symbol() {
        final Function function = new Function("symbol", 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Utf8String>() {}));
        return executeRemoteCallSingleValueReturn(function, String.class);
    }

    public RemoteCall<TransactionReceipt> finishDistribution() {
        final Function function = new Function(
                "finishDistribution", 
                Arrays.<Type>asList(), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteCall<TransactionReceipt> transfer(String _to, BigInteger _amount) {
        final Function function = new Function(
                "transfer", 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(_to), 
                new org.web3j.abi.datatypes.generated.Uint256(_amount)), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteCall<TransactionReceipt> getTokens(BigInteger weiValue) {
        final Function function = new Function(
                "getTokens", 
                Arrays.<Type>asList(), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function, weiValue);
    }

    public RemoteCall<Boolean> distributionFinished() {
        final Function function = new Function("distributionFinished", 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Bool>() {}));
        return executeRemoteCallSingleValueReturn(function, Boolean.class);
    }

    public RemoteCall<BigInteger> getTokenBalance(String tokenAddress, String who) {
        final Function function = new Function("getTokenBalance", 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(tokenAddress), 
                new org.web3j.abi.datatypes.Address(who)), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}));
        return executeRemoteCallSingleValueReturn(function, BigInteger.class);
    }

    public RemoteCall<BigInteger> totalRemaining() {
        final Function function = new Function("totalRemaining", 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}));
        return executeRemoteCallSingleValueReturn(function, BigInteger.class);
    }

    public RemoteCall<BigInteger> allowance(String _owner, String _spender) {
        final Function function = new Function("allowance", 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(_owner), 
                new org.web3j.abi.datatypes.Address(_spender)), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}));
        return executeRemoteCallSingleValueReturn(function, BigInteger.class);
    }

    public RemoteCall<TransactionReceipt> withdrawForeignTokens(String _tokenContract) {
        final Function function = new Function(
                "withdrawForeignTokens", 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(_tokenContract)), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteCall<BigInteger> totalDistributed() {
        final Function function = new Function("totalDistributed", 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}));
        return executeRemoteCallSingleValueReturn(function, BigInteger.class);
    }

    public RemoteCall<TransactionReceipt> transferOwnership(String newOwner) {
        final Function function = new Function(
                "transferOwnership", 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(newOwner)), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteCall<Boolean> blacklist(String param0) {
        final Function function = new Function("blacklist", 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(param0)), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Bool>() {}));
        return executeRemoteCallSingleValueReturn(function, Boolean.class);
    }

    public static RemoteCall<TryChain> deploy(Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit) {
        return deployRemoteCall(TryChain.class, web3j, credentials, gasPrice, gasLimit, BINARY, "");
    }

    public static RemoteCall<TryChain> deploy(Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        return deployRemoteCall(TryChain.class, web3j, transactionManager, gasPrice, gasLimit, BINARY, "");
    }

    public static TryChain load(String contractAddress, Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit) {
        return new TryChain(contractAddress, web3j, credentials, gasPrice, gasLimit);
    }

    public static TryChain load(String contractAddress, Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        return new TryChain(contractAddress, web3j, transactionManager, gasPrice, gasLimit);
    }

    public static class TransferEventResponse {
        public Log log;

        public String _from;

        public String _to;

        public BigInteger _value;
    }

    public static class ApprovalEventResponse {
        public Log log;

        public String _owner;

        public String _spender;

        public BigInteger _value;
    }

    public static class DistrEventResponse {
        public Log log;

        public String to;

        public BigInteger amount;
    }

    public static class DistrFinishedEventResponse {
        public Log log;
    }

    public static class BurnEventResponse {
        public Log log;

        public String burner;

        public BigInteger value;
    }
}
