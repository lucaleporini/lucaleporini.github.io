package Casa;

import com.google.gson.Gson;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import commonUtils.House;
import commonUtils.Houses;
import commonUtils.Stat;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.stub.StreamObserver;
import protosPackage.HouseServicesGrpc;
import protosPackage.HouseServicesOuterClass;
import simulation_src_2019.BufferImpl;
import simulation_src_2019.Measurement;
import simulation_src_2019.SmartMeterSimulator;

import javax.ws.rs.core.MediaType;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class HouseSingleton {
    private static HouseSingleton instance;
    private final String SERVER = "localhost";
    private final int SERVER_PORT = 1337;

    private House currentHouse;

    //elenco di case
    private Houses listHouses;

    //gestione della casa coordinatore
    //private volatile Boolean exiting;
    private Object dummyCoordinator;
    private House houseCoordinator;
    private Boolean coordinator;

    //gestione SMART METER
    private SmartMeterSimulator smartMeterThread;
    private Thread senderLocalStatsThread;
    private ArrayList<Measurement> smartMeter;

    //gestione del costo complessivo
    private HashMap<String, Stat> housesUsage;


    //richiesta di boost
    private Thread boostManagarThread;
    
    private int boostRequestCount;
    private long timestampBoostRequest;

    private Boolean boostRequest;
    private volatile boolean enterCriticalRegion;

    private Object dummyBoostRequest;
    private Houses housesToReply;
    private Houses replyQueue;



    private HouseSingleton(){
        currentHouse = new House();

        coordinator = false;

        // = false;
        smartMeter = new ArrayList<>();
        houseCoordinator = null;
        dummyCoordinator = new Object();

        housesUsage = new HashMap<>();

        boostRequestCount = 0;
        timestampBoostRequest = 0;

        boostRequest = false;
        enterCriticalRegion = false;

        dummyBoostRequest = new Object();

        housesToReply = new Houses();
        replyQueue = new Houses();

    }

    public static HouseSingleton getInstance() {
        if(instance==null)
            synchronized(HouseSingleton.class) {
                if(instance == null) {
                    instance = new HouseSingleton();
                }
            }
        return instance;
    }

    //GETTER E SETTER ---------------------------------------------------------------------------------------------
    public Houses getListHouses() {
        synchronized (listHouses) {
            return listHouses.clone();
        }
    }

    public void setListHouses(Houses listHouses) {
        this.listHouses = listHouses;
    }

    public House getCurrentHouse() {
        return currentHouse;
    }

    public void setCurrentHouse(House currentHouse) {
        this.currentHouse = currentHouse;
    }

    //Sincronizzo su dummyCoordinator per escludere la possibilità che non ci sia più un coordinatore
    public Boolean getCoordinator() {
        synchronized (dummyCoordinator) {
            return coordinator;
        }
    }

    public void setCoordinator(Boolean coordinator) {
        synchronized (dummyCoordinator) {
            this.coordinator = coordinator;
        }
    }

    public void setHouseCoordinator(House houseCoordinator) {
        synchronized (dummyCoordinator) {
            this.houseCoordinator = houseCoordinator;
        }
    }


    /*GESTIONE DELLO SMART METER ------------------------------------------------------------------------------------*/
    public ArrayList<Measurement> getSmartMeter() {
        return smartMeter;
    }

    public void startSmartMeter() {
        smartMeterThread = new SmartMeterSimulator(currentHouse.getId(), new BufferImpl());
        smartMeterThread.start();
        System.out.println("SMARTMETER: START");
    }

    public void addMeasurement(Measurement m){
        synchronized (smartMeter) {
            this.smartMeter.add(m);
            //STAMPA DELLO SMARTMETER
            //if (smartMeter.size()%2==0) System.out.println("SMARTMETER: SIZE "+smartMeter.size());
            if (this.smartMeter.size() >= 24) {
                this.smartMeter.notify();
            }
        }
    }

    /*CALCOLO DEL CONSUMO COMPLESSIVO ------------------------------------------------------------------------*/

    public void addHouseStatistic(String id_house, double s, long timestamp){

        HashMap<String, Stat> housesUsageCopy = new HashMap<>();
        synchronized (housesUsage) {
            if (housesUsage.get(id_house) == null) {
                housesUsage.put(id_house, new Stat(s, timestamp));
            } else {
                housesUsage.replace(id_house, new Stat(s, timestamp));
            }

            boolean all;
            synchronized (listHouses) {
                all = (listHouses.getSize() <= housesUsage.size());
            }

            if(all){
                for (Map.Entry<String, Stat> entry : housesUsage.entrySet())
                    housesUsageCopy.put(entry.getKey(), entry.getValue());
                housesUsage.clear();
            }
        }

        if(!housesUsageCopy.isEmpty()){
            computeCondominiumUsage(housesUsageCopy, timestamp);
        }
    }

    //calcolo del consumo complessivo
    public void computeCondominiumUsage(HashMap<String, Stat> housesUsage, long timestamp) {
        double totalUsage = 0;
        for(Stat stat: housesUsage.values())
            totalUsage += stat.getValue();
        Stat condominiumUsage = new Stat(totalUsage, timestamp);
        System.out.println("CONSUMO COMPLESSIVO | "+condominiumUsage.toString());

        if(getCoordinator()){
            System.out.println("----- INVIO CONSUMO COMPLESSIVO AL SERVER -----");

            //INVIO DELLA STATISTICA LOCALE AL SERVER
            Gson gson = new Gson();
            String condominiumUsageJ = gson.toJson(condominiumUsage);
            Client client = Client.create();
            WebResource webResourcePost = client
                    .resource("http://" + SERVER + ":" + SERVER_PORT + "/server/addCondominiumStatistic");
            ClientResponse clientResponsePost = webResourcePost
                    .type(MediaType.APPLICATION_JSON)
                    .post(ClientResponse.class, condominiumUsageJ);

            //check the status of POST request
            if (clientResponsePost.getStatus() == 406) {
                System.out.println("-----\nWARNING! Add CONDOMINIUM statistic\n-----");
            }
            if (clientResponsePost.getStatus() == 201) {
                //System.out.println("-------------------------------");
            }

        }
    }

    //GESTIONE START E STOP THREAD ------------------------------------------------------------------------------------
    public void startSenderLocalStatsThread(){
        senderLocalStatsThread = new Thread((new SenderLocalStatsThread()));
        senderLocalStatsThread.start();
        System.out.println("SenderLocalStatsThread: START");
    }

    public void startBoostManagerThread(){
        boostManagarThread = new Thread(new BoostManagerThread());
        boostManagarThread.start();
        System.out.println("BoostManagarThread: START");
    }

    public void startBoost() throws InterruptedException{
        smartMeterThread.boost();
    }

    public void stopAllThreads(){
        senderLocalStatsThread.interrupt();
        boostManagarThread.interrupt();
        smartMeterThread.stopMeGently();
    }

    /*ADD, GET, REMOVE, EXIT -----------------------------------------------------------------------------------------*/
    public HouseServicesOuterClass.HouseResponse enterHouse(House enterHouse) {
        HouseServicesOuterClass.HouseResponse.Builder response = HouseServicesOuterClass.HouseResponse.newBuilder();
        synchronized (dummyCoordinator){
            response.setCoordinator(coordinator);
        }
        synchronized (listHouses) {
            System.out.println("--- CASA " + enterHouse.getId() + ": ENTER IN NETWORK");
            //Aggiungo la casa alla lista di case
            listHouses.addHouse(enterHouse);
        }
        return response.build();
    }

    public Boolean removeHouse(House houseToRemove){
        removeReply(houseToRemove.getId());

        //controllo ingresso case - situazione NESSUN coordinatore
        /*try {
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }*/

        synchronized (listHouses){
            listHouses.remove(houseToRemove.getId());
            synchronized (dummyCoordinator) {
                if(!coordinator) {
                    if (houseCoordinator == null || houseToRemove.getId().equals(houseCoordinator.getId())) {
                        House newCoordinator = listHouses.getHouseWithMaxId();
                        if (newCoordinator.getId().equals(currentHouse.getId())) {
                            coordinator = true;
                            houseCoordinator = currentHouse;
                            return true;
                        } else {

                            coordinator = false;
                            houseCoordinator = null;
                        }
                    }
                }
                return false;
            }
        }
    }


    public void notifyToAllNewCoordinator() throws InterruptedException {
        HashMap<String, House> listHouses = getListHouses().getHashMap();
        House currentHouse = getCurrentHouse();
        for (Map.Entry<String, House> entry : listHouses.entrySet()) {
            if(!entry.getKey().equals(currentHouse.getId())) {
                ManagedChannel channel = ManagedChannelBuilder
                        .forTarget(entry.getValue().getIp() + ":" + entry.getValue().getPort())
                        .usePlaintext(true).build();
                HouseServicesGrpc.HouseServicesStub stub = HouseServicesGrpc.newStub(channel);
                HouseServicesOuterClass.House newCoordinatorHouse = HouseServicesOuterClass.House.newBuilder()
                        .setId(currentHouse.getId()).setPort(currentHouse.getPort()).setIp(currentHouse.getIp()).build();
                stub.newCoordinator(newCoordinatorHouse, new StreamObserver<HouseServicesOuterClass.Response>() {
                    public void onNext(HouseServicesOuterClass.Response response) {
                        //System.out.println("notifyToAllNewCoordinator - CASA "+entry.getValue().getId()+": "+response.getText());
                    }

                    public void onError(Throwable throwable) {
                    }

                    public void onCompleted() {
                        channel.shutdownNow();
                    }
                });

                channel.awaitTermination(5, TimeUnit.SECONDS);
            }
        }
    }

    public void removeHouseFromList(House houseToRemove){
        synchronized (listHouses){
            listHouses.remove(houseToRemove.getId());
        }
    }

    public Boolean checkCoordinator(){
        try {
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        //System.out.println("CHECK COORDINATOR");

        synchronized (listHouses){
            synchronized (dummyCoordinator) {
                //System.out.println(listHouses.toString());

                if(houseCoordinator == null){
                    House newCoordinator = listHouses.getHouseWithMaxId();
                    if (newCoordinator.getId().equals(currentHouse.getId())) {
                        coordinator = true;
                        houseCoordinator = currentHouse;
                        return true;
                    }
                }
                return false;
            }
        }
    }

    public void setNewCoordinator(House newCoordinator){
        synchronized (dummyCoordinator){
            coordinator = false;
            houseCoordinator = newCoordinator;
        }
    }


    //------------------------------------------------------------------------------------------------------------

    // GESTIONE USCITA INCONTROLLATA ---------------------------
    public void removeHouseFromServer(House houseToRemove){
        Client client = Client.create();
        Gson gson = new Gson();
        String houseToRemoveJ = gson.toJson(houseToRemove);
        WebResource webResource = client
                .resource("http://" + SERVER + ":" + SERVER_PORT + "/server/removeHouse");
        ClientResponse clientResponsePost = webResource.type(MediaType.APPLICATION_JSON)
                .post(ClientResponse.class, houseToRemoveJ);

        if (clientResponsePost.getStatus() == 201) {
            //System.out.println("\n |CASA ELIMINATA| (" + clientResponsePost.getStatus() + ")\n");
        }
    }




    // RICHIESTA EXTRA CONSUMO ---------------------------------------------------------------------------------------


    public void setEnterCriticalRegion(Boolean enterCriticalRegion) {
        this.enterCriticalRegion = enterCriticalRegion;
    }

    public Long getTimestampBoostRequest() {
        synchronized (dummyBoostRequest) {
            return timestampBoostRequest;
        }
    }

    public Houses getHousesToReply() {
        synchronized (housesToReply) {
            Houses houses = housesToReply.clone();
            housesToReply.clear();
            return houses;
        }
    }

    //SETTO LA CODA DI REPLY UGUALE ALLA TOTALITà DELLE CASE
    public Houses setReplyQueue() {
        synchronized (replyQueue) {
            synchronized (listHouses){
                this.replyQueue = listHouses.clone();
                this.replyQueue.remove(currentHouse.getId());
                return this.replyQueue.clone();
            }
        }
    }

    public void addBoostRequest(){
        synchronized (dummyBoostRequest){
            boostRequestCount += 1;
            dummyBoostRequest.notify();
        }
        // inivio la notifica di boost al server
        Client client = Client.create();
        WebResource webResourcePost = client
                .resource("http://" + SERVER + ":" + SERVER_PORT + "/server/boostNotification");
        ClientResponse clientResponsePost = webResourcePost.type(MediaType.APPLICATION_JSON)
                .post(ClientResponse.class, currentHouse.getId());

        if (clientResponsePost.getStatus() == 406){
            System.out.println(clientResponsePost.getStatus());
        }
        if(clientResponsePost.getStatus() == 201){
            //System.out.println("INVIO NOTIFICA AMMINISTRATORE: OK");
        }
    }

    //THREAD -----------------------------------------------------------------
    public void setBoostRequest(Boolean boostRequest) {
        if(boostRequest){
            //FASE DI RICHIESTA DI BOOST
            Date date= new Date();
            long timestamp = date.getTime();
            synchronized (dummyBoostRequest) {
                this.boostRequest = boostRequest;
                this.timestampBoostRequest = timestamp;
            }
        }else{
            synchronized (dummyBoostRequest) {
                this.boostRequest = boostRequest;
            }
        }
    }

    //controllo del numero di richieste di boost
    public void checkBoostRequestCount() throws InterruptedException{
        synchronized (dummyBoostRequest) {
            while (boostRequestCount < 1) {
                dummyBoostRequest.wait();
            }
            setBoostRequest(true);
            boostRequestCount -=1;
        }
    }

    //ATTENDO L'ARRIVO DELLE REPLY NECESSARIE PER ESEGUIRE IL BOOST
    public void waitAllReply() throws InterruptedException{
        synchronized (replyQueue){
            while (replyQueue.getSize() > 1) {
                    replyQueue.wait();
            }
        }
    }


    //RISPOSTA A UNA RICHIESTA DI BOOST DA PARTE DI UN'ALTRA CASA
    public HouseServicesOuterClass.BoostResponse checkToReply(House houseOfNetwork, long timestampOfOtherHouse) {
        HouseServicesOuterClass.House house = HouseServicesOuterClass.House.newBuilder()
                .setId(currentHouse.getId()).setIp(currentHouse.getIp()).setPort(currentHouse.getPort()).build();
        HouseServicesOuterClass.BoostResponse.Builder response = HouseServicesOuterClass.BoostResponse.newBuilder().setHouse(house);

        synchronized (dummyBoostRequest) {
            if (!boostRequest) {
                return response.setText("REPLY").build();
            } else if(!enterCriticalRegion && (timestampOfOtherHouse < timestampBoostRequest || timestampBoostRequest == 0)) {
                return response.setText("REPLY").build();
            }
            synchronized (housesToReply) {
                //inserisco in code di reply la casa
                //System.out.println("RESPONSE "+houseOfNetwork.getId()+": NOT REPLY");
                housesToReply.addHouse(houseOfNetwork);
                return response.setText("OK").build();
            }
        }

    }

    //ELIMINARE DALLA LISTA DI RICHIESTE LE CASE CHE FANNO REPLY
    public void removeReply(String id_house){
        synchronized (replyQueue){
            replyQueue.remove(id_house);

            //per avere due case in boost contemporaneamente
            if ((replyQueue.getSize() < 2)) {
                replyQueue.clear();
                replyQueue.notify();
            }
        }
    }
}
