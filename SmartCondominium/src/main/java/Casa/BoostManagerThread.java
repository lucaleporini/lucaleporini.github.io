package Casa;

import commonUtils.House;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.stub.StreamObserver;
import protosPackage.HouseServicesGrpc;
import protosPackage.HouseServicesOuterClass;

import java.util.HashMap;
import java.util.concurrent.TimeUnit;

public class BoostManagerThread implements Runnable{
    private boolean stopCondition = false;

    public void replyToHouses(){
        HashMap<String, House> housesToReply = HouseSingleton.getInstance().getHousesToReply().getHashMap();
        House currentHouse = HouseSingleton.getInstance().getCurrentHouse();

        if(housesToReply.size()>0){
            for (String id_house : housesToReply.keySet()) {
                House houseToReply = housesToReply.get(id_house);
                ManagedChannel channel = ManagedChannelBuilder
                        .forTarget(houseToReply.getIp()+":" + houseToReply.getPort())
                        .usePlaintext(true).build();

                HouseServicesGrpc.HouseServicesStub stub = HouseServicesGrpc.newStub(channel);
                HouseServicesOuterClass.House houseRequest = HouseServicesOuterClass.House.newBuilder()
                        .setId(currentHouse.getId()).setIp(currentHouse.getIp()).setPort(currentHouse.getPort()).build();

                stub.reply(houseRequest, new StreamObserver<HouseServicesOuterClass.Response>() {

                    public void onNext(HouseServicesOuterClass.Response response) {
                        //System.out.println("BM | REPLY TO " + houseToReply.getId() + ": " + response.getText());
                    }
                    //if there are some errors, this method will be called
                    public void onError(Throwable throwable) {
                        //System.out.println("BM | CASA "+houseToReply.getId()+" NON DISPONIBILE (REPLY)");
                    }
                    public void onCompleted() {
                        channel.shutdownNow();
                    }
                });

                try {
                    channel.awaitTermination(5, TimeUnit.SECONDS);
                } catch (InterruptedException e) {
                    //a causa di stopAllThread
                    stopCondition = true;

                }
            }
        }

    }
    @Override
    public void run() {
        while (!stopCondition) {
            try {
                //controllo che ci siano delle richieste altrimenti WAIT
                HouseSingleton.getInstance().checkBoostRequestCount();

                long timestamp = HouseSingleton.getInstance().getTimestampBoostRequest();
                House currentHouse = HouseSingleton.getInstance().getCurrentHouse();

                //imposto la coda di richieste di REPLY per poter entrare nella regione critica
                HashMap<String, House> houses = HouseSingleton.getInstance().setReplyQueue().getHashMap();

                //System.out.println("BM | # REQUEST TO OTHER HOUSES: " + houses.size());
                if (houses.size() > 1) {
                    for (String id_house : houses.keySet()) {
                        House houseOfNetwork = houses.get(id_house);
                        ManagedChannel channel = ManagedChannelBuilder
                                .forTarget(houseOfNetwork.getIp()+":" + houseOfNetwork.getPort())
                                .usePlaintext(true).build();
                        HouseServicesGrpc.HouseServicesStub stub = HouseServicesGrpc.newStub(channel);
                        HouseServicesOuterClass.House houseRequest = HouseServicesOuterClass.House.newBuilder()
                                .setId(currentHouse.getId()).setIp(currentHouse.getIp()).setPort(currentHouse.getPort()).build();

                        HouseServicesOuterClass.BoostRequest boostRequest = HouseServicesOuterClass.BoostRequest.newBuilder()
                                .setHouse(houseRequest).setTimestamp(timestamp).build();

                        stub.requestBoost(boostRequest, new StreamObserver<HouseServicesOuterClass.BoostResponse>() {
                            public void onNext(HouseServicesOuterClass.BoostResponse response) {
                                //System.out.println("SBR | REQUEST BOOST TO " + response.getHouse().getId() + ": " + response.getText());
                                if (response.getText().equals("REPLY")) {
                                    HouseSingleton.getInstance().removeReply(response.getHouse().getId());
                                }
                            }

                            public void onError(Throwable throwable) {
                                //System.out.println("BM | CASA "+id_house+": NON DISPONIBILE (REQUEST)");
                                HouseSingleton.getInstance().removeReply(id_house);
                            }

                            public void onCompleted() {
                                channel.shutdownNow();
                            }
                        });

                        try {
                            channel.awaitTermination(1, TimeUnit.SECONDS);
                        } catch (InterruptedException e) {
                            stopCondition = true;
                            System.out.println("BoostManagerThread: STOP");
                        }
                    }

                    //ATTENDO LE REPLY DALLE CASE
                    HouseSingleton.getInstance().waitAllReply();
                }
            } catch (InterruptedException e) {
                stopCondition = true;
                System.out.println("BoostManagerThread: STOP");
            }

            //CONTROLLO SE LA CASA è IN USCITA
            if(!stopCondition) {
                try {
                    //System.out.println("BM | ENTRO NELLA REGIONE CRITICA");
                    HouseSingleton.getInstance().setEnterCriticalRegion(true);
                    System.out.println("BM | ESEGUO IL BOOST");
                    HouseSingleton.getInstance().startBoost();
                    //Thread.sleep(10000);
                    System.out.println("BM | FINE BOOST");
                    HouseSingleton.getInstance().setEnterCriticalRegion(false);
                    //System.out.println("BM | ESCO DALLA REGIONE CRITICA");

                    //racchiudere in un solo metodo l'uscita dalla regione e il boost request
                    HouseSingleton.getInstance().setBoostRequest(false);

                    //get houses to reply and clear hashmap
                    replyToHouses();

                } catch (InterruptedException e) {
                    stopCondition = true;
                    System.out.println("BoostManagerThread: STOP");

                }
            }
        }
    }
}
