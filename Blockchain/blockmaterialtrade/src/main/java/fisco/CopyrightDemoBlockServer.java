package fisco;

import grpc.CopyrightGrpcServer;
import org.fisco.bcos.channel.client.Service;
import org.fisco.bcos.web3j.crypto.Credentials;
import org.fisco.bcos.web3j.crypto.Keys;
import org.fisco.bcos.web3j.protocol.Web3j;
import org.fisco.bcos.web3j.protocol.channel.ChannelEthereumService;
import org.fisco.bcos.web3j.protocol.core.methods.response.TransactionReceipt;
import org.fisco.bcos.web3j.tx.gas.StaticGasProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.util.Properties;

public class CopyrightDemoBlockServer {
    static Logger logger = LoggerFactory.getLogger(CopyrightDemo.class);

    private static Web3j web3j;

    private static Credentials credentials;

    //合约地址,填写已经部署的合约地址
    public static String contractaddress = "0x17acfb54a3c64227a983ed54c4763424f06b8a9e";

    public CopyrightDemoBlockServer() {
        try {
            initialize();
        } catch (Exception e) {
            logger.debug("initialize failed");
        }
    }

    public void setWeb3j(Web3j web3j) {
        this.web3j = web3j;
    }

    public Credentials getCredentials() {
        return credentials;
    }

    public void setCredentials(Credentials credentials) {
        this.credentials = credentials;
    }

    public Web3j getWeb3j() {
        return web3j;
    }

    public void recordCreditCertAddr(String address) throws FileNotFoundException, IOException {
        Properties prop = new Properties();
        prop.setProperty("address", address);
        final Resource contractResource = new ClassPathResource("contract.properties");
        FileOutputStream fileOutputStream = new FileOutputStream(contractResource.getFile());
        prop.store(fileOutputStream, "contract address");
        contractaddress = address;
    }

    public static String loadCreditCertAddr() throws Exception {
        // load CreditCert contact address from contract.properties
        if (contractaddress == null) {
            Properties prop = new Properties();
            final Resource contractResource = new ClassPathResource("contract.properties");
            prop.load(contractResource.getInputStream());

            String contractAddress = prop.getProperty("address");
            if (contractAddress == null || contractAddress.trim().equals("")) {
                throw new Exception(" load CreditCert contract address failed, please deploy it first. ");
            }
            logger.info(" load CreditCert address from contract.properties, address is {}", contractAddress);
            return contractAddress;
        } else
            return contractaddress;
    }

    public void initialize() throws Exception {

        // init the Service
        @SuppressWarnings("resource")
        ApplicationContext context = new ClassPathXmlApplicationContext("classpath:applicationContext.xml");
        Service service = context.getBean(Service.class);
        service.run();

        ChannelEthereumService channelEthereumService = new ChannelEthereumService();
        channelEthereumService.setChannelService(service);
        Web3j web3j = Web3j.build(channelEthereumService, 1);

        // init Credentials
        Credentials credentials = Credentials.create(Keys.createEcKeyPair());

        setCredentials(credentials);
        setWeb3j(web3j);

        logger.debug(" web3j is " + web3j + " ,credentials is " + credentials);
    }

    private static BigInteger gasPrice = new BigInteger("30000000");
    private static BigInteger gasLimit = new BigInteger("30000000");

    public String deployCreditCertAndRecordAddr() {

        try {
            CopyrightDemo creditCert = CopyrightDemo.deploy(web3j, credentials, new StaticGasProvider(gasPrice, gasLimit)).send();
            System.out.println(" deploy CreditCert success, contract address is " + creditCert.getContractAddress());
            recordCreditCertAddr(creditCert.getContractAddress());
            return creditCert.getContractAddress();
        } catch (Exception e) {
            System.out.println(" deploy CreditCert contract failed, error message is  " + e.getMessage());
        }
        return null;
    }

    /**
     * 描述 : 初始化用户
     * 参数 ：
     * ID
     * 返回值：
     * 值返回0时，创建失败
     */
    public static int CreateUser(BigInteger userPhone, String date) throws Exception {
        System.out.println("---------------------CreateUser-------------------------------");
        try {
            String contractAddress = loadCreditCertAddr();
            CopyrightDemo creditCert = CopyrightDemo.load(contractAddress, web3j, credentials, new StaticGasProvider(gasPrice, gasLimit));
            logger.debug("CreateUser init!");
            TransactionReceipt receipt = creditCert.CreateUser(userPhone, date).send();

        } catch (Exception e) {
            logger.error(" init student info exception, error message is {}", e.getMessage());
            System.out.printf(" init student info failed, error message is %s\n", e.getMessage());
        }
        return 1;
    }

    /**
     * 确权
     *
     * @param userPhone
     * @param tag
     * @param imgName
     * @param key
     * @return
     * @throws Exception
     */
    public static int Confirm(BigInteger userPhone, BigInteger tag, String imgName, String key) throws Exception {
        System.out.println("---------------------Confirm-------------------------------");
        int code;
        try {
            String contractAddress = loadCreditCertAddr();
            CopyrightDemo confirm = CopyrightDemo.load(contractAddress, web3j, credentials, new StaticGasProvider(gasPrice, gasLimit));
            TransactionReceipt dataInfo = confirm.Confirm(userPhone, tag, imgName, key).send();
            System.out.println("TEST IS " + dataInfo.getBlockNumber());
            code = 1;
        } catch (Exception e) {
            code = 0;
            e.printStackTrace();
        }
        return code;
    }

    /**
     * 交易媒体素材
     *
     * @param purchasePhone
     * @param sellerPhone
     * @param price
     * @param tag
     * @param imgName
     * @param key
     * @return
     * @throws Exception
     */
    public static int Trade(BigInteger purchasePhone, BigInteger sellerPhone, BigInteger price, BigInteger tag, String imgName, String key) throws Exception {
        System.out.println("---------------------Trade-------------------------------");
        int code;
        try {
            String contractAddress = loadCreditCertAddr();
            CopyrightDemo confirm = CopyrightDemo.load(contractAddress, web3j, credentials, new StaticGasProvider(gasPrice, gasLimit));
            TransactionReceipt dataInfo = confirm.Trade(purchasePhone, sellerPhone, price, tag, imgName, key).send();
            System.out.println("TEST IS " + dataInfo.getBlockNumber());
            code = 1;
        } catch (Exception e) {
            code = 0;
            e.printStackTrace();
        }
        return code;
    }


    /*
    描述 : 查询用户资产情况
    参数 ：
        ID : 资产账户
    */
    public static int QueryValue(BigInteger userPhone) throws Exception {
        String contractAddress = loadCreditCertAddr();
        CopyrightDemo queryValue = CopyrightDemo.load(contractAddress, web3j, credentials, new StaticGasProvider(gasPrice, gasLimit));
        System.out.println("---------------------SearchValue-------------------------------");
        BigInteger Result = queryValue.QueryValue(userPhone).send();

        return Result.intValue();
    }


    //主函数
    public static void main(String[] args) throws Exception {

        CopyrightDemoBlockServer client = new CopyrightDemoBlockServer();
        client.initialize();

        final CopyrightGrpcServer server = new CopyrightGrpcServer();
        server.start();
        server.blockUntilShutdown();

    }
}


