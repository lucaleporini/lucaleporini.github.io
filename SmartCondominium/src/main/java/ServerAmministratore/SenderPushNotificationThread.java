package ServerAmministratore;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.stub.StreamObserver;
import protosPackage.AdminServicesGrpc;
import protosPackage.AdminServicesOuterClass;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class SenderPushNotificationThread implements Runnable {
    private String house;
    private int option;
    public SenderPushNotificationThread(String id_house, int op) {
        house= id_house;
        option = op;
    }

    @Override
    public void run() {
        //System.out.println("SenderPushNotificationThread: RUN");
        HashMap<String, String> admins = ServerSingleton.getInstance().getAdmins();
        for (Map.Entry<String, String> entry : admins.entrySet()) {
            //System.out.println("CONTATTO:" + entry.getValue());
            //plaintext channel on the address (ip/port) which offers the GreetingService service
            ManagedChannel channel = ManagedChannelBuilder.forTarget(entry.getValue()).usePlaintext(true).build();
            //creating an asynchronous stub on the channel
            AdminServicesGrpc.AdminServicesStub stub = AdminServicesGrpc.newStub(channel);
            //creating the HelloResponse object which will be provided as input to the RPC method
            AdminServicesOuterClass.HouseNP houseNP = AdminServicesOuterClass.HouseNP.newBuilder()
                    .setId(house).setOption(option).build();
            //calling the RPC method. since it is asynchronous, we need to define handlers
            stub.notify(houseNP, new StreamObserver<AdminServicesOuterClass.ResponseNP>() {
                //this hanlder takes care of each item received in the stream
                public void onNext(AdminServicesOuterClass.ResponseNP response) {
                    System.out.println("NOTIFICA: OK");
                }

                //if there are some errors, this method will be called
                public void onError(Throwable throwable) {
                    System.out.println("Error! " + throwable.getMessage());
                }

                //when the stream is completed (the server called "onCompleted") just close the channel
                public void onCompleted() {
                    channel.shutdownNow();
                }
            });
            //you need this. otherwise the method will terminate before that answers from the server are received
            try {
                channel.awaitTermination(10, TimeUnit.SECONDS);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}