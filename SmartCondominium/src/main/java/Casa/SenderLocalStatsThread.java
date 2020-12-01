package Casa;

import com.google.gson.Gson;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import commonUtils.House;
import commonUtils.Stat;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.stub.StreamObserver;
import protosPackage.HouseServicesGrpc;
import protosPackage.HouseServicesOuterClass;
import simulation_src_2019.Measurement;

import javax.ws.rs.core.MediaType;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class SenderLocalStatsThread implements Runnable {
    protected volatile boolean stopCondition = false;
    private static final String SERVER = "localhost";
    private static final int SERVER_PORT = 1337;

    public SenderLocalStatsThread(){}

    @Override
    public void run() {
        ArrayList<Measurement> smartmeter = HouseSingleton.getInstance().getSmartMeter();
        ArrayList<Measurement> smartmeterCopy = new ArrayList<>();

        while(!stopCondition) {
            try{
                synchronized (smartmeter) {
                    //DIMENSIONE DELLA FINESTRA: 24
                    while (smartmeter.size() < 24) {
                        smartmeter.wait();
                    }
                    //SLIDE WINDOW -- 24 elementi, OVERLAP --> 50%
                    for (int i = 0; i < 24; i++) {
                        smartmeterCopy.add(new Measurement(smartmeter.get(i).getId(),
                                smartmeter.get(i).getType(), smartmeter.get(i).getValue(),
                                smartmeter.get(i).getTimestamp()));
                    }
                    for (int i = 0; i < 12; i++) smartmeter.remove(0);
                }

                String id_house = smartmeterCopy.get(0).getId();
                //stat_house contiene:
                //value = media dei valori nello smartmeter
                //timestamp = tempo in cui viene effettuata la media
                Stat stat_house = new Stat(smartmeterCopy);

                //INVIO DELLA STATISTICA LOCALE AL SERVER
                Gson gson = new Gson();
                String stat_houseJ = gson.toJson(stat_house);
                Client client = Client.create();
                WebResource webResourcePost = client
                        .resource("http://" + SERVER + ":" + SERVER_PORT + "/server/addLocalStatistic");
                ClientResponse clientResponsePost = webResourcePost
                        .header("id_house", id_house)
                        .type(MediaType.APPLICATION_JSON)
                        .post(ClientResponse.class, stat_houseJ);

                //check the status of POST request
                if (clientResponsePost.getStatus() == 406){
                    System.out.println("WARNING! ADD LOCAL STATISTIC");
                }
                if(clientResponsePost.getStatus() == 201){
                    //System.out.println("LOCAL STATISTIC ADDED --------------------------------");
                }

                //INVIO STATISTICA LOCALE AD OGNI CASA NELLA RETE
                HashMap<String, House> listHouses = HouseSingleton.getInstance().getListHouses().getHashMap();
                for (Map.Entry<String, House> entry : listHouses.entrySet()) {
                    ManagedChannel channel = ManagedChannelBuilder
                            .forTarget(entry.getValue().getIp()+":" + entry.getValue().getPort())
                            .usePlaintext(true).build();
                    HouseServicesGrpc.HouseServicesStub stub = HouseServicesGrpc.newStub(channel);
                    HouseServicesOuterClass.Statistic request = HouseServicesOuterClass.Statistic.newBuilder()
                            .setValue(stat_house.getValue()).setTimestamp(stat_house.getTimestamp()).setId(id_house).build();
                    stub.addStatistic(request, new StreamObserver<HouseServicesOuterClass.Response>() {
                        public void onNext(HouseServicesOuterClass.Response response) {
                        }
                        public void onError(Throwable throwable) {
                            System.out.println("CASA "+entry.getValue().getId()+": "+throwable.getMessage()+" (SLST)");
                            //cominco al server di rimuovere la casa
                            HouseSingleton.getInstance().removeHouseFromServer(entry.getValue());

                            //rimuovo la casa, controllo se era coordinatore, se si controllo se sono il prossimo coordinatore
                            //torna true o false se devo notificare agli altri che sono il nuovo coordinatore
                            if(HouseSingleton.getInstance().removeHouse(entry.getValue())){
                                //notifica a tutte le case il nuovo coordinatore
                                System.out.println("\nI'M NEW COORDINATOR\n");
                                try {
                                    HouseSingleton.getInstance().notifyToAllNewCoordinator();
                                } catch (InterruptedException e) {
                                    stopCondition = true;
                                    System.out.println("SenderLocalStatsThread: STOP");
                                }
                            }
                        }
                        public void onCompleted() {
                            channel.shutdownNow();
                        }
                    });

                    try {
                        channel.awaitTermination(5, TimeUnit.SECONDS);
                    } catch (InterruptedException e) {
                        stopCondition = true;
                        System.out.println("SenderLocalStatsThread: STOP");
                    }
                }
                smartmeterCopy.clear();
            }catch (InterruptedException e) {
                stopCondition = true;
                System.out.println("SenderLocalStatsThread: STOP");
            }
        }
    }
}
