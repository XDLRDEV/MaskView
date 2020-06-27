package grpc;

import fisco.CopyrightDemoBlockServer;
import io.grpc.Server;
import io.grpc.ServerBuilder;
import io.grpc.stub.StreamObserver;
import proto.ConfirmDemoGrpc;
import proto.CopyrightDemo;
import proto.CreateUserDemoGrpc;
import proto.TradeDemoGrpc;

import java.io.IOException;
import java.math.BigInteger;

public class CopyrightGrpcServer {
    private Server server;

//    public static void main(String[] args) throws IOException, InterruptedException {
//        final CopyrightGrpcServer server = new CopyrightGrpcServer();
//        server.start();
//        server.blockUntilShutdown();
//    }

    public void start() throws IOException {
        System.out.println("start .....");

        /* The port on which the server should run */
        int port = 1330;

        //增加交易功能TradeImpl()和查询交易功能GetTradeImpl()
        server = ServerBuilder.forPort(port)
                .addService(new CreateUserImpl())
                .addService(new ConfirmImpl())
                .addService(new TradeImpl())
                .build()
                .start();

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            // Use stderr here since the logger may have been reset by its JVM shutdown hook.
            System.err.println("*** shutting down gRPC server since JVM is shutting down");
            CopyrightGrpcServer.this.stop();
            System.err.println("*** server shut down");
        }));

    }

    private void stop() {
        if (server != null) {
            server.shutdown();
        }
    }

    /**
     * Await termination on the main thread since the grpc library uses daemon threads.
     */
    public void blockUntilShutdown() throws InterruptedException {
        if (server != null) {
            server.awaitTermination();
        }
    }

    /**
     * Main launches the server from the command line.
     */
//   public static void main(String[] args) throws IOException, InterruptedException {
//        final CopyrightGrpcServer server = new CopyrightGrpcServer();
//        server.start();
//        server.blockUntilShutdown();
//    }

    /**
     * 创建客户的grpc
     */
    static class CreateUserImpl extends CreateUserDemoGrpc.CreateUserDemoImplBase {
        @Override
        public void createUserDemo(CopyrightDemo.CreateUserRequest request, StreamObserver<CopyrightDemo.CreateUserResponse> responseObserver) {
//            super.createUserDemo(request, responseObserver);
            System.out.println("===========CreateUserImpl=============");

            System.out.println(request.getUserPhone());
            boolean code = false;
            try {
                if (CopyrightDemoBlockServer.CreateUser(BigInteger.valueOf(request.getUserPhone()), request.getDate()) == 1) {
                    code = true;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            System.out.println("执行上链操作....");
            CopyrightDemo.CreateUserResponse response = CopyrightDemo.CreateUserResponse.newBuilder().setResult(code).build();
            System.out.println(response.getResult());
            responseObserver.onNext(response);
            responseObserver.onCompleted();
        }
    }

    /**
     * 确权
     */
    static class ConfirmImpl extends ConfirmDemoGrpc.ConfirmDemoImplBase {
        @Override
        public void confirmDemo(CopyrightDemo.ConfirmRequest request, StreamObserver<CopyrightDemo.ConfirmResponse> responseObserver) {
//            super.confirmDemo(request, responseObserver);
            boolean code = false;
            try {
                if (CopyrightDemoBlockServer.Confirm(BigInteger.valueOf(request.getUserPhone()), BigInteger.valueOf(request.getTag()),
                        request.getImgName(), request.getKey()) == 1) {
                    code = true;
                }
            } catch (Exception e) {
                code = false;
                e.printStackTrace();
            }
            System.out.println("执行上链操作1....");
            CopyrightDemo.ConfirmResponse response = CopyrightDemo.ConfirmResponse.newBuilder().setResult(code).build();
            responseObserver.onNext(response);
            responseObserver.onCompleted();
        }

    }

    /**
     * 交易
     */
    static class TradeImpl extends TradeDemoGrpc.TradeDemoImplBase {
        @Override
        public void tradeDemo(CopyrightDemo.TradeRequest request, StreamObserver<CopyrightDemo.TradeResponse> responseObserver) {
//            super.tradeDemo(request, responseObserver);
            boolean code = false;
            try {
                if (CopyrightDemoBlockServer.Trade(BigInteger.valueOf(request.getPurchasePhone()), BigInteger.valueOf(request.getSellerPhone()), BigInteger.valueOf(request.getPrice()),
                        BigInteger.valueOf(request.getTag()), request.getImgName(), request.getKey()) == 1) {
                    code = true;
                }
            } catch (Exception e) {
                code = false;
                e.printStackTrace();
            }
            System.out.println("执行上链操作2....");
            CopyrightDemo.TradeResponse response = CopyrightDemo.TradeResponse.newBuilder().setResult(code).build();
            responseObserver.onNext(response);
            responseObserver.onCompleted();
        }
    }

}
