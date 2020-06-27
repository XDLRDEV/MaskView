package fisco;

import org.fisco.bcos.channel.client.TransactionSucCallback;
import org.fisco.bcos.web3j.abi.TypeReference;
import org.fisco.bcos.web3j.abi.datatypes.Function;
import org.fisco.bcos.web3j.abi.datatypes.Type;
import org.fisco.bcos.web3j.abi.datatypes.generated.Int256;
import org.fisco.bcos.web3j.crypto.Credentials;
import org.fisco.bcos.web3j.protocol.Web3j;
import org.fisco.bcos.web3j.protocol.core.RemoteCall;
import org.fisco.bcos.web3j.protocol.core.methods.response.TransactionReceipt;
import org.fisco.bcos.web3j.tx.Contract;
import org.fisco.bcos.web3j.tx.TransactionManager;
import org.fisco.bcos.web3j.tx.gas.ContractGasProvider;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.Collections;

/**
 * <p>Auto generated code.
 * <p><strong>Do not modify!</strong>
 * <p>Please use the <a href="https://docs.web3j.io/command_line.html">web3j command line tools</a>,
 * or the org.fisco.bcos.web3j.codegen.SolidityFunctionWrapperGenerator in the 
 * <a href="https://github.com/web3j/web3j/tree/master/codegen">codegen module</a> to update.
 *
 * <p>Generated with web3j version none.
 */
@SuppressWarnings("unchecked")
public class CopyrightDemo extends Contract {
    public static String BINARY = "608060405234801561001057600080fd5b50610811806100206000396000f300608060405260043610610062576000357c0100000000000000000000000000000000000000000000000000000000900463ffffffff1680632e8781221461006757806369cf51df14610166578063c6bcffe01461023d578063c80f620114610288575b600080fd5b34801561007357600080fd5b50610164600480360381019080803567ffffffffffffffff169060200190929190803567ffffffffffffffff169060200190929190803567ffffffffffffffff169060200190929190803567ffffffffffffffff169060200190929190803590602001908201803590602001908080601f0160208091040260200160405190810160405280939291908181526020018383808284378201915050505050509192919290803590602001908201803590602001908080601f0160208091040260200160405190810160405280939291908181526020018383808284378201915050505050509192919290505050610319565b005b34801561017257600080fd5b5061023b600480360381019080803567ffffffffffffffff169060200190929190803567ffffffffffffffff169060200190929190803590602001908201803590602001908080601f0160208091040260200160405190810160405280939291908181526020018383808284378201915050505050509192919290803590602001908201803590602001908080601f016020809104026020016040519081016040528093929190818152602001838380828437820191505050505050919291929050505061055d565b005b34801561024957600080fd5b50610272600480360381019080803567ffffffffffffffff16906020019092919050505061065b565b6040518082815260200191505060405180910390f35b34801561029457600080fd5b50610303600480360381019080803567ffffffffffffffff169060200190929190803590602001908201803590602001908080601f016020809104026020016040519081016040528093929190818152602001838380828437820191505050505050919291929050505061068e565b6040518082815260200191505060405180910390f35b60c0604051908101604052808767ffffffffffffffff1681526020018667ffffffffffffffff1681526020018567ffffffffffffffff1681526020018467ffffffffffffffff168152602001838152602001828152506002836040518082805190602001908083835b6020831015156103a75780518252602082019150602081019050602083039250610382565b6001836020036101000a038019825116818451168082178552505050505050905001915050908152602001604051809103902060008201518160000160006101000a81548167ffffffffffffffff021916908367ffffffffffffffff16021790555060208201518160000160086101000a81548167ffffffffffffffff021916908367ffffffffffffffff16021790555060408201518160000160106101000a81548167ffffffffffffffff021916908367ffffffffffffffff16021790555060608201518160000160186101000a81548167ffffffffffffffff021916908367ffffffffffffffff16021790555060808201518160010190805190602001906104b2929190610740565b5060a08201518160020190805190602001906104cf929190610740565b509050508367ffffffffffffffff166000808867ffffffffffffffff1667ffffffffffffffff168152602001908152602001600020600201600082825403925050819055508367ffffffffffffffff166000808767ffffffffffffffff1667ffffffffffffffff16815260200190815260200160002060020160008282540192505081905550505050505050565b6080604051908101604052808567ffffffffffffffff1681526020018467ffffffffffffffff16815260200183815260200182815250600160008667ffffffffffffffff1667ffffffffffffffff16815260200190815260200160002060008201518160000160006101000a81548167ffffffffffffffff021916908367ffffffffffffffff16021790555060208201518160000160086101000a81548167ffffffffffffffff021916908367ffffffffffffffff1602179055506040820151816001019080519060200190610634929190610740565b506060820151816002019080519060200190610651929190610740565b5090505050505050565b60008060008367ffffffffffffffff1667ffffffffffffffff168152602001908152602001600020600201549050919050565b60006060604051908101604052808467ffffffffffffffff16815260200183815260200160648152506000808567ffffffffffffffff1667ffffffffffffffff16815260200190815260200160002060008201518160000160006101000a81548167ffffffffffffffff021916908367ffffffffffffffff1602179055506020820151816001019080519060200190610728929190610740565b50604082015181600201559050506001905092915050565b828054600181600116156101000203166002900490600052602060002090601f016020900481019282601f1061078157805160ff19168380011785556107af565b828001600101855582156107af579182015b828111156107ae578251825591602001919060010190610793565b5b5090506107bc91906107c0565b5090565b6107e291905b808211156107de5760008160009055506001016107c6565b5090565b905600a165627a7a723058203c6ba74834bd03bf8b64ecd9bd58c3b0d7d61ad4f2a33d4d7e57e51d6b23590c0029";

    public static final String ABI = "[{\"constant\":false,\"inputs\":[{\"name\":\"purchasePhone\",\"type\":\"uint64\"},{\"name\":\"sellerPhone\",\"type\":\"uint64\"},{\"name\":\"price\",\"type\":\"uint64\"},{\"name\":\"tag\",\"type\":\"uint64\"},{\"name\":\"imgName\",\"type\":\"string\"},{\"name\":\"key\",\"type\":\"string\"}],\"name\":\"Trade\",\"outputs\":[],\"payable\":false,\"stateMutability\":\"nonpayable\",\"type\":\"function\"},{\"constant\":false,\"inputs\":[{\"name\":\"userPhone\",\"type\":\"uint64\"},{\"name\":\"tag\",\"type\":\"uint64\"},{\"name\":\"imgName\",\"type\":\"string\"},{\"name\":\"key\",\"type\":\"string\"}],\"name\":\"Confirm\",\"outputs\":[],\"payable\":false,\"stateMutability\":\"nonpayable\",\"type\":\"function\"},{\"constant\":true,\"inputs\":[{\"name\":\"userPhone\",\"type\":\"uint64\"}],\"name\":\"QueryValue\",\"outputs\":[{\"name\":\"\",\"type\":\"int256\"}],\"payable\":false,\"stateMutability\":\"view\",\"type\":\"function\"},{\"constant\":false,\"inputs\":[{\"name\":\"userPhone\",\"type\":\"uint64\"},{\"name\":\"date\",\"type\":\"string\"}],\"name\":\"CreateUser\",\"outputs\":[{\"name\":\"\",\"type\":\"int256\"}],\"payable\":false,\"stateMutability\":\"nonpayable\",\"type\":\"function\"}]";

    public static final String FUNC_TRADE = "Trade";

    public static final String FUNC_CONFIRM = "Confirm";

    public static final String FUNC_QUERYVALUE = "QueryValue";

    public static final String FUNC_CREATEUSER = "CreateUser";

    @Deprecated
    protected CopyrightDemo(String contractAddress, Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit) {
        super(BINARY, contractAddress, web3j, credentials, gasPrice, gasLimit);
    }

    protected CopyrightDemo(String contractAddress, Web3j web3j, Credentials credentials, ContractGasProvider contractGasProvider) {
        super(BINARY, contractAddress, web3j, credentials, contractGasProvider);
    }

    @Deprecated
    protected CopyrightDemo(String contractAddress, Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        super(BINARY, contractAddress, web3j, transactionManager, gasPrice, gasLimit);
    }

    protected CopyrightDemo(String contractAddress, Web3j web3j, TransactionManager transactionManager, ContractGasProvider contractGasProvider) {
        super(BINARY, contractAddress, web3j, transactionManager, contractGasProvider);
    }

    public RemoteCall<TransactionReceipt> Trade(BigInteger purchasePhone, BigInteger sellerPhone, BigInteger price, BigInteger tag, String imgName, String key) {
        final Function function = new Function(
                FUNC_TRADE, 
                Arrays.<Type>asList(new org.fisco.bcos.web3j.abi.datatypes.generated.Uint64(purchasePhone), 
                new org.fisco.bcos.web3j.abi.datatypes.generated.Uint64(sellerPhone), 
                new org.fisco.bcos.web3j.abi.datatypes.generated.Uint64(price), 
                new org.fisco.bcos.web3j.abi.datatypes.generated.Uint64(tag), 
                new org.fisco.bcos.web3j.abi.datatypes.Utf8String(imgName), 
                new org.fisco.bcos.web3j.abi.datatypes.Utf8String(key)), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public void Trade(BigInteger purchasePhone, BigInteger sellerPhone, BigInteger price, BigInteger tag, String imgName, String key, TransactionSucCallback callback) {
        final Function function = new Function(
                FUNC_TRADE, 
                Arrays.<Type>asList(new org.fisco.bcos.web3j.abi.datatypes.generated.Uint64(purchasePhone), 
                new org.fisco.bcos.web3j.abi.datatypes.generated.Uint64(sellerPhone), 
                new org.fisco.bcos.web3j.abi.datatypes.generated.Uint64(price), 
                new org.fisco.bcos.web3j.abi.datatypes.generated.Uint64(tag), 
                new org.fisco.bcos.web3j.abi.datatypes.Utf8String(imgName), 
                new org.fisco.bcos.web3j.abi.datatypes.Utf8String(key)), 
                Collections.<TypeReference<?>>emptyList());
        asyncExecuteTransaction(function, callback);
    }

    public String TradeSeq(BigInteger purchasePhone, BigInteger sellerPhone, BigInteger price, BigInteger tag, String imgName, String key) {
        final Function function = new Function(
                FUNC_TRADE, 
                Arrays.<Type>asList(new org.fisco.bcos.web3j.abi.datatypes.generated.Uint64(purchasePhone), 
                new org.fisco.bcos.web3j.abi.datatypes.generated.Uint64(sellerPhone), 
                new org.fisco.bcos.web3j.abi.datatypes.generated.Uint64(price), 
                new org.fisco.bcos.web3j.abi.datatypes.generated.Uint64(tag), 
                new org.fisco.bcos.web3j.abi.datatypes.Utf8String(imgName), 
                new org.fisco.bcos.web3j.abi.datatypes.Utf8String(key)), 
                Collections.<TypeReference<?>>emptyList());
        return createTransactionSeq(function);
    }

    public RemoteCall<TransactionReceipt> Confirm(BigInteger userPhone, BigInteger tag, String imgName, String key) {
        final Function function = new Function(
                FUNC_CONFIRM, 
                Arrays.<Type>asList(new org.fisco.bcos.web3j.abi.datatypes.generated.Uint64(userPhone), 
                new org.fisco.bcos.web3j.abi.datatypes.generated.Uint64(tag), 
                new org.fisco.bcos.web3j.abi.datatypes.Utf8String(imgName), 
                new org.fisco.bcos.web3j.abi.datatypes.Utf8String(key)), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public void Confirm(BigInteger userPhone, BigInteger tag, String imgName, String key, TransactionSucCallback callback) {
        final Function function = new Function(
                FUNC_CONFIRM, 
                Arrays.<Type>asList(new org.fisco.bcos.web3j.abi.datatypes.generated.Uint64(userPhone), 
                new org.fisco.bcos.web3j.abi.datatypes.generated.Uint64(tag), 
                new org.fisco.bcos.web3j.abi.datatypes.Utf8String(imgName), 
                new org.fisco.bcos.web3j.abi.datatypes.Utf8String(key)), 
                Collections.<TypeReference<?>>emptyList());
        asyncExecuteTransaction(function, callback);
    }

    public String ConfirmSeq(BigInteger userPhone, BigInteger tag, String imgName, String key) {
        final Function function = new Function(
                FUNC_CONFIRM, 
                Arrays.<Type>asList(new org.fisco.bcos.web3j.abi.datatypes.generated.Uint64(userPhone), 
                new org.fisco.bcos.web3j.abi.datatypes.generated.Uint64(tag), 
                new org.fisco.bcos.web3j.abi.datatypes.Utf8String(imgName), 
                new org.fisco.bcos.web3j.abi.datatypes.Utf8String(key)), 
                Collections.<TypeReference<?>>emptyList());
        return createTransactionSeq(function);
    }

    public RemoteCall<BigInteger> QueryValue(BigInteger userPhone) {
        final Function function = new Function(FUNC_QUERYVALUE, 
                Arrays.<Type>asList(new org.fisco.bcos.web3j.abi.datatypes.generated.Uint64(userPhone)), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Int256>() {}));
        return executeRemoteCallSingleValueReturn(function, BigInteger.class);
    }

    public RemoteCall<TransactionReceipt> CreateUser(BigInteger userPhone, String date) {
        final Function function = new Function(
                FUNC_CREATEUSER, 
                Arrays.<Type>asList(new org.fisco.bcos.web3j.abi.datatypes.generated.Uint64(userPhone), 
                new org.fisco.bcos.web3j.abi.datatypes.Utf8String(date)), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public void CreateUser(BigInteger userPhone, String date, TransactionSucCallback callback) {
        final Function function = new Function(
                FUNC_CREATEUSER, 
                Arrays.<Type>asList(new org.fisco.bcos.web3j.abi.datatypes.generated.Uint64(userPhone), 
                new org.fisco.bcos.web3j.abi.datatypes.Utf8String(date)), 
                Collections.<TypeReference<?>>emptyList());
        asyncExecuteTransaction(function, callback);
    }

    public String CreateUserSeq(BigInteger userPhone, String date) {
        final Function function = new Function(
                FUNC_CREATEUSER, 
                Arrays.<Type>asList(new org.fisco.bcos.web3j.abi.datatypes.generated.Uint64(userPhone), 
                new org.fisco.bcos.web3j.abi.datatypes.Utf8String(date)), 
                Collections.<TypeReference<?>>emptyList());
        return createTransactionSeq(function);
    }

    @Deprecated
    public static CopyrightDemo load(String contractAddress, Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit) {
        return new CopyrightDemo(contractAddress, web3j, credentials, gasPrice, gasLimit);
    }

    @Deprecated
    public static CopyrightDemo load(String contractAddress, Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        return new CopyrightDemo(contractAddress, web3j, transactionManager, gasPrice, gasLimit);
    }

    public static CopyrightDemo load(String contractAddress, Web3j web3j, Credentials credentials, ContractGasProvider contractGasProvider) {
        return new CopyrightDemo(contractAddress, web3j, credentials, contractGasProvider);
    }

    public static CopyrightDemo load(String contractAddress, Web3j web3j, TransactionManager transactionManager, ContractGasProvider contractGasProvider) {
        return new CopyrightDemo(contractAddress, web3j, transactionManager, contractGasProvider);
    }

    public static RemoteCall<CopyrightDemo> deploy(Web3j web3j, Credentials credentials, ContractGasProvider contractGasProvider) {
        return deployRemoteCall(CopyrightDemo.class, web3j, credentials, contractGasProvider, BINARY, "");
    }

    @Deprecated
    public static RemoteCall<CopyrightDemo> deploy(Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit) {
        return deployRemoteCall(CopyrightDemo.class, web3j, credentials, gasPrice, gasLimit, BINARY, "");
    }

    public static RemoteCall<CopyrightDemo> deploy(Web3j web3j, TransactionManager transactionManager, ContractGasProvider contractGasProvider) {
        return deployRemoteCall(CopyrightDemo.class, web3j, transactionManager, contractGasProvider, BINARY, "");
    }

    @Deprecated
    public static RemoteCall<CopyrightDemo> deploy(Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        return deployRemoteCall(CopyrightDemo.class, web3j, transactionManager, gasPrice, gasLimit, BINARY, "");
    }
}
