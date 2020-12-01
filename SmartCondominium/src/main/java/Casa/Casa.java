package Casa;

import com.google.gson.Gson;
import commonUtils.House;
import commonUtils.Houses;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.Server;
import io.grpc.ServerBuilder;
import io.grpc.stub.StreamObserver;
import protosPackage.HouseServicesGrpc;
import protosPackage.HouseServicesOuterClass;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;

import javax.ws.rs.core.MediaType;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.TimeUnit;

public class Casa {

    private static final String SERVER = "localhost";
    private static final int SERVER_PORT = 1337;
    private static House currentHouse;

    //richiesta di presentazione alle case
    public static void enterToNetwork(Houses listHouses){
        if(listHouses.getHashMap().size() == 1){
            //SONO LA PRIMA CASA AD ENTRARE NELLA RETE --> COORDINATORE
            HouseSingleton.getInstance().setCoordinator(true);
            HouseSingleton.getInstance().setHouseCoordinator(currentHouse);

        }else{
            //PRESENTAZIONE ALLE ALTRE CASE
            System.out.println("-----------------------------\nINIZIO PRESENTAZIONE");
            for (Map.Entry<String, House> entry : listHouses.getHashMap().entrySet()) {
                if(!currentHouse.getId().equals(entry.getKey())) {
                    System.out.print("presentationToHouse "+entry.getKey()+":");
                    int rand = (int)(Math.random()*10);
                    try {
                        Thread.sleep(1000*rand);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    presentationToHouse(entry.getValue());
                }
            }

            //controllo se entrando è stato assegnato un coordinatore
            //se non è così controllo se questa casa può diventarlo
            if(HouseSingleton.getInstance().checkCoordinator()){
                System.out.println("I'M NEW COORDINATOR");
                try {
                    HouseSingleton.getInstance().notifyToAllNewCoordinator();
                } catch (InterruptedException e) {
                    //e.printStackTrace();
                }
            }

            System.out.println("FINE PRESENTAZIONE\n-----------------------------");
        }
    }

    public static void presentationToHouse(House houseOfNetwork){
        ManagedChannel channel = ManagedChannelBuilder.forTarget(houseOfNetwork.getIp()+":"+houseOfNetwork.getPort()).usePlaintext(true).build();
        HouseServicesGrpc.HouseServicesStub stub = HouseServicesGrpc.newStub(channel);
        HouseServicesOuterClass.House houseRequest = HouseServicesOuterClass.House.newBuilder()
                .setId(currentHouse.getId()).setIp(currentHouse.getIp()).setPort(currentHouse.getPort()).build();
        stub.hello(houseRequest, new StreamObserver<HouseServicesOuterClass.HouseResponse>() {
            public void onNext(HouseServicesOuterClass.HouseResponse response) {
                if(response.getCoordinator()) {
                    System.out.println("OK - COORDINATOR");
                    HouseSingleton.getInstance().setHouseCoordinator(houseOfNetwork);
                }else{
                    System.out.println("OK");
                }
            }
            public void onError(Throwable throwable) {
                System.out.println(throwable.getMessage()+" (P)");
                HouseSingleton.getInstance().removeHouseFromServer(houseOfNetwork);
                HouseSingleton.getInstance().removeHouseFromList(houseOfNetwork);
            }
            public void onCompleted() {
                channel.shutdownNow();
            }
        });
        try {
            channel.awaitTermination(5, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            System.out.println("presentationToHouse CATCH");
            e.printStackTrace();
        }
    }


    //USCITA DELLA CASA
    public static void exitFromNetwork() {

        //COMUNICAZIONE AL SERVER AMMINISTRATORE
        Client client = Client.create();
        Gson gson = new Gson();
        String currentHouseJ = gson.toJson(currentHouse);
        WebResource webResource = client
                .resource("http://" + SERVER + ":" + SERVER_PORT + "/server/exitHouse");
        ClientResponse clientResponsePost = webResource.type(MediaType.APPLICATION_JSON)
                .post(ClientResponse.class, currentHouseJ);

        if (clientResponsePost.getStatus() == 201) {
            //System.out.println("\n |CASA ELIMINATA| (" + clientResponsePost.getStatus() + ")\n");
        }

        //INTERRUZIONE DEI THREAD
        HouseSingleton.getInstance().stopAllThreads();

        //COMUNICAZIONE A TUTTE LE CASE
        Houses listHouses = HouseSingleton.getInstance().getListHouses();


        //controllo se non è rimasta solo una casa
        if (listHouses.getHashMap().size() > 1) {
            for (Map.Entry<String, House> entry : listHouses.getHashMap().entrySet()) {
                exitToHouse(entry.getValue());
            }
        }
    }

    public static void exitToHouse(House houseOfNetwork){

        if (!currentHouse.getId().equals(houseOfNetwork.getId())) {
            ManagedChannel channel = ManagedChannelBuilder
                    .forTarget(houseOfNetwork.getIp()+":" + houseOfNetwork.getPort())
                    .usePlaintext(true).build();
            HouseServicesGrpc.HouseServicesStub stub = HouseServicesGrpc.newStub(channel);
            HouseServicesOuterClass.House houseRequest = HouseServicesOuterClass.House.newBuilder()
                    .setId(currentHouse.getId()).setIp(currentHouse.getIp()).setPort(currentHouse.getPort()).build();
            HouseServicesOuterClass.exitHouse exitRequest = HouseServicesOuterClass.exitHouse.newBuilder()
                    .setHouse(houseRequest).build();

            stub.exit(exitRequest, new StreamObserver<HouseServicesOuterClass.Response>() {
                public void onNext(HouseServicesOuterClass.Response response) {

                }
                public void onError(Throwable throwable) {

                }
                public void onCompleted() {
                    channel.shutdownNow();
                }
            });

            try {
                channel.awaitTermination(5, TimeUnit.SECONDS);
            } catch (InterruptedException e) {
                System.out.println("exitToHouse CATCH");
                e.printStackTrace();
            }
        }
    }



    public static void main(String[] args) throws IOException {

        // RICHIESTA DI INGRESSO NELLA RETE DI CASE
        ClientResponse clientResponsePost;
        Client client;
        BufferedReader input = new BufferedReader(new InputStreamReader(System.in));
        do {
            System.out.println("AVVIO");
            System.out.println("1 --> Manuale");
            System.out.println("2 --> Automatico");
            String action = input.readLine();
            String id_house = "0";
            String ip_house;
            int port_house = 0;
            switch (action){
                case "1":
                    System.out.print("ID: ");
                    Boolean accepted = false;
                    do {
                        try {
                            id_house = input.readLine();
                            port_house = 6000 + Integer.parseInt(id_house);
                            accepted = true;
                        }catch (Exception e){
                            //
                        }
                    }while (!accepted);
                    System.out.println("IP: " + port_house);
                    ip_house = "127.0.0." +id_house;
                    System.out.println("IP: " + ip_house);

                    //creazione casa
                    currentHouse = new House(id_house, ip_house, port_house);
                    HouseSingleton.getInstance().setCurrentHouse(currentHouse);
                    break;

                case "2":
                    Random rand = new Random();
                    int id = rand.nextInt(256);
                    System.out.println("ID: "+id);
                    id_house = ""+id;
                    ip_house = "127.0.0." +id_house;
                    port_house = 6000 + id;
                    System.out.println("IP: " + ip_house);
                    System.out.println("PORT: "+port_house+"\n-------------------------------");

                    //creazione casa
                    currentHouse = new House(id_house, ip_house, port_house);
                    HouseSingleton.getInstance().setCurrentHouse(currentHouse);
                    break;
            }

            // scrittura sul server
            client = Client.create();
            WebResource webResourcePost = client
                    .resource("http://" + SERVER + ":" + SERVER_PORT + "/server/enterHouse");
            clientResponsePost = webResourcePost.type(MediaType.APPLICATION_JSON)
                    .post(ClientResponse.class, currentHouse);

            //check the status of POST request
            if (clientResponsePost.getStatus() == 406){
                System.out.println("\nID CASA NON VALIDO ("+clientResponsePost.getStatus()+")\n");
            }
            if(clientResponsePost.getStatus() == 201){
                HouseSingleton.getInstance().setCurrentHouse(currentHouse);
            }

        }while (clientResponsePost.getStatus() != 201);


        Gson gson = new Gson();
        Houses listHouses = gson.fromJson(clientResponsePost.getEntity(String.class),Houses.class);
        HouseSingleton.getInstance().setListHouses(listHouses);

        Server serverGRPC = null;
        //AVVIO SERVER GRPC
        try {
            serverGRPC = ServerBuilder.forPort(currentHouse.getPort()).addService(new HouseServicesServerImpl()).build();
            serverGRPC.start();
            System.out.println("SERVER GRPC: START");
        } catch (IOException e) {
            System.out.println("SERVER GRPC NOT STARTED");
            e.printStackTrace();
        }

        enterToNetwork(listHouses);

        //AVVIO DELLO SMART METER
        HouseSingleton.getInstance().startSmartMeter();

        //INVIO STATS ALLE ALTRE CASE
        HouseSingleton.getInstance().startSenderLocalStatsThread();

        //AVVIO THREAD PER LE RICHIESTE DI BOOST
        HouseSingleton.getInstance().startBoostManagerThread();

        //USCITA DI UNA CASA DALLA RETE
        String exitHouse;
        do {
            exitHouse = input.readLine();
            if (exitHouse.equals("b")){
                System.out.println("--------------------------\nRICHIESTA BOOST\n--------------------------");
                HouseSingleton.getInstance().addBoostRequest();
            }
        }while (!exitHouse.equals("n"));

        input.close();
        exitFromNetwork();
        serverGRPC.shutdownNow();
        System.out.println("EXIT");
    }
}