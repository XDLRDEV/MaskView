//package fisco;
//
//import java.math.BigInteger;
//
//public class TestCopyrightBlockServer {
//
//    public static void main(String[] args) throws Exception {
//        System.out.println("start .....");
//
//        CopyrightDemoBlockServer copyTradeServer = new CopyrightDemoBlockServer();
//        copyTradeServer.initialize();
//
//        /**
//         * 创建用户（初始化十，用户初始金额为100）
//         * 输入：string(用户id)
//         */
//        System.out.println("create user ......");
//        BigInteger user1 = BigInteger.valueOf(11);
//        BigInteger user2 = BigInteger.valueOf(12);
//        System.out.println(
//                CopyrightDemoBlockServer.CreateUser(user1, "201211111111")
//        );
//
//
//        /**
//         * 查询用户额度信息
//         * 返回当前用户的账户金额
//         */
//        System.out.printf("user amount is : %d\n", CopyrightDemoBlockServer.QueryValue(user1));
//
//        /**
//         * 确权
//         */
//        System.out.println("create data");
//        BigInteger tag = BigInteger.valueOf(1);
//        String key = "0101100111A137EA9B6E2BA6EA9B7EA9B6EA9B6EA9B6A137EA9B6E2BA6EA9B7EA9B6EA9B6EA9B6A137EA9B6E2BA6EA9B7EA9B6EA9B6EA9B6A137EA9B6E2BA6EA9B7EA9B6EA9B6EA9B6A137EA9B6E2BA6EA9B7EA9B6EA9B6EA9B6A137EA9B6E2BA6EA9B7EA9B6EA9B6EA9B6A137EA9B6E2BA6EA9B7EA9B6EA9B6EA9B6A137EA9B6E2BA6EA9B7EA9B6EA9B6EA9B6A137EA9B6E2BA6EA9B7EA9B6EA9B6EA9B6A137EA9B6E2BA6EA9B7EA9B6EA9B6EA9B6A137EA9B6E2BA6EA9B7EA9B6EA9B6EA9B6A137EA9B6E2BA6EA9B7EA9B6EA9B6EA9B6A137EA9B6E2BA6EA9B7EA9B6EA9B6EA9B6A137EA9B6E2BA6EA9B7EA9B6EA9B6EA9B6A137EA9B6E2BA6EA9B7EA9B6EA9B6EA9B6A137EA9B6E2BA6EA9B7EA9B6EA9B6EA9B6A137EA9B6E2BA6EA9B7EA9B6EA9B6EA9B6A137EA9B6E2BA6EA9B7EA9B6EA9B6EA9B6A137EA9B6E2BA6EA9B7EA9B6EA9B6EA9B6A137EA9B6E2BA6EA9B7EA9B6EA9B6EA9B6A137EA9B6E2BA6EA9B7EA9B6EA9B6EA9B6A137EA9B6E2BA6EA9B7EA9B6EA9B6EA9B6A137EA9B6E2BA6EA9B7EA9B6EA9B6EA9B6A137EA9B6E2BA6EA9B7EA9B6EA9B6EA9B6A137EA9B6E2BA6EA9B7EA9B6EA9B6EA9B6A137EA9B6E2BA6EA9B7EA9B6EA9B6EA9B6A137EA9B6E2BA6EA9B7EA9B6EA9B6EA9B6A137EA9B6E2BA6EA9B7EA9B6EA9B6EA9B6A137EA9B6E2BA6EA9B7EA9B6EA9B6EA9B6A137EA9B6E2BA6EA9B7EA9B6EA9B6EA9B6A137EA9B6E2BA6EA9B7EA9B6EA9B6EA9B6A137EA9B6E2BA6EA9B7EA9B6EA9B6EA9B6A137EA9B6E2BA6EA9B7EA9B6EA9B6EA9B6A137EA9B6E2BA6EA9B7EA9B6EA9B6EA9B6A137EA9B6E2BA6EA9B7EA9B6EA9B6EA9B6A137EA9B6E2BA6EA9B7EA9B6EA9B6EA9B6";
//        System.out.println(CopyrightDemoBlockServer.Confirm(user1, tag, "name", key)
//        );
//
//        /**
//         * 交易
//         * from:卖方，付款方
//         * to: 买方，收钱方
//         * value:付款金额
//         * dataHash：购买资产的哈希
//         * preTradeHash：前一条交易的交易哈希txId，如果是首次交易，则写"null"
//         */
//        System.out.println("Trade ......");
//        BigInteger value = BigInteger.valueOf(1);
//        System.out.println(CopyrightDemoBlockServer.Trade(user2, user1, value, BigInteger.valueOf(1), "name", key));
//
//        System.exit(0);
//    }
//
//}
