package Amministratore;

import com.google.gson.Gson;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;

import commonUtils.Houses;
import commonUtils.Stats;
import io.grpc.Server;
import io.grpc.ServerBuilder;

import javax.ws.rs.core.MediaType;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Random;


public class Amministratore {
    private static final String SERVER = "localhost";
    private static final int SERVER_PORT = 1337;
    private static BufferedReader input;

    public static void main(String[] args) throws IOException {

        ClientResponse clientResponsePost;
        int id;
        do {
            Random rand = new Random();
            id = rand.nextInt(256);

            Client client = Client.create();
            WebResource webResourcePost = client
                    .resource("http://" + SERVER + ":" + SERVER_PORT + "/server/addAdmin");
            clientResponsePost = webResourcePost.type(MediaType.APPLICATION_JSON)
                    .post(ClientResponse.class, id+"");

        }while (clientResponsePost.getStatus() != 201);

        System.out.print("ID AMMINISTRATORE: "+id);

        //AVVIO SERVER GRPC
        try {
            Server serverGRPC = ServerBuilder.forPort(id+5000).addService(new AdminServicesServerImpl()).build();
            serverGRPC.start();
        } catch (IOException e) {
            e.printStackTrace();
        }

        int action = 0;
        input = new BufferedReader(new InputStreamReader(System.in));

        //MENU DI GESTIONE DELL'AMMINISTRATORE
        do{
            System.out.println("\n--------------------------------------- AMMINISTRATORE -------------------------------------");
            System.out.println("1 -- Elenco delle case presenti nella rete");
            System.out.println("2 -- Ultime n statistiche (con timestamp) relative ad una specifica casa");
            System.out.println("3 -- Ultime n statistiche (con timestamp) condominiali");
            System.out.println("4 -- Deviazione standard e media delle ultime n statistiche prodotte da una specifica casa");
            System.out.println("5 -- Deviazione standard e media delle ultime n statistiche complessive condominiali");
            System.out.println("6 -- Esci");
            System.out.println("--> Inserisci azione");
            System.out.println("--------------------------------------------------------------------------------------------\n");


            //controllo dell'inserimento dell'azione
            Boolean actionCheck;
            do{
                actionCheck = false;
                try{
                    action = Integer.parseInt(input.readLine());
                }catch (NumberFormatException e) {
                    actionCheck = true;
                }
            }while(actionCheck || action<=0 || action>6);

            //input per le richieste
            switch (action){
                case 1:
                    getAllHouses();
                    break;
                case 2:
                    getNStatsOfHouse();
                    break;

                case 3:
                    getNStatsOfCondominium();
                    break;

                case 4:
                    getMeanDevOfHouse();
                    break;

                case 5:
                    getMeanDevOfCondominium();
                    break;
            }
        }while (action!=6);
        exitToServer(id);
    }

    //request 1: get all houses in condominium
    private static void getAllHouses(){
        Client client = Client.create();
        WebResource restResource1 = client
                .resource("http://"+SERVER+":"+SERVER_PORT+"/server/listHouses");
        ClientResponse clientResponse1 = restResource1
                .accept("application/json")
                .get(ClientResponse.class);

        if (clientResponse1.getStatus() == 400) {
            System.out.println(clientResponse1.getEntity(String.class));
        }
        if (clientResponse1.getStatus() == 201) {
            Gson gson = new Gson();
            Houses listHouses = gson.fromJson(clientResponse1.getEntity(String.class), Houses.class);
            System.out.println(listHouses.toString());
        }

    }

    //request 2: get N statistics of a specific house
    private static void getNStatsOfHouse(){
        int n = requestN();
        String id_house = requestIdHouse();

        Client client = Client.create();
        WebResource restResource2 = client.resource("http://"+SERVER+":"+SERVER_PORT+"/server/statistics")
                .queryParam("n", ""+n).queryParam("id_house",id_house);

        ClientResponse clientResponse2 = restResource2
                .accept("application/json")
                .get(ClientResponse.class);

        //check the status of POST request
        if (clientResponse2.getStatus() == 404) {
            System.out.println(clientResponse2.getEntity(String.class));
        }
        if (clientResponse2.getStatus() == 400){
            System.out.println(clientResponse2.getEntity(String.class));
        }
        if (clientResponse2.getStatus() == 201){
            Gson gson = new Gson();
            Stats s = gson.fromJson(clientResponse2.getEntity(String.class), Stats.class);
            System.out.println(s.toString());
        }
    }

    //request 3: get last N statistics of condominium
    private static void getNStatsOfCondominium(){
        int n = requestN();

        Client client = Client.create();
        WebResource restResource3 = client.resource("http://"+SERVER+":"+SERVER_PORT+"/server/statistics")
                .queryParam("n", ""+n); //DA CONTROLLARE: passaggio STRINGHE NON interi

        ClientResponse clientResponse3 = restResource3
                .accept("application/json")
                .get(ClientResponse.class);

        //check the status of POST request
        if (clientResponse3.getStatus() == 400) {
            System.out.println(clientResponse3.getEntity(String.class));
        }
        if (clientResponse3.getStatus() == 201){
            Gson gson = new Gson();
            Stats stats = gson.fromJson(clientResponse3.getEntity(String.class), Stats.class);
            System.out.println(stats.toString());
        }
    }

    //request 4: get mean and standard deviation of last n statistic of a specific house
    private static void getMeanDevOfHouse(){
        int n = requestN();
        String id_house = requestIdHouse();

        Client client = Client.create();
        WebResource restResource4 = client.resource("http://"+SERVER+":"+SERVER_PORT+"/server/statistics")
                .queryParam("n", ""+n).queryParam("id_house",id_house).queryParam("mean_dev", "true");

        ClientResponse clientResponse4 = restResource4
                .accept("application/json")
                .get(ClientResponse.class);

        //check the status of POST request
        if (clientResponse4.getStatus() == 404) {
            System.out.println(clientResponse4.getEntity(String.class));
        }
        if (clientResponse4.getStatus() == 400){
            System.out.println(clientResponse4.getEntity(String.class));
        }
        if (clientResponse4.getStatus() == 201){
            System.out.println(clientResponse4.getEntity(String.class));
        }
    }

    //request 5: get mean and standard deviation of last n statistic of a specific house
    private static void getMeanDevOfCondominium(){
        int n = requestN();
        Client client = Client.create();
        WebResource restResource5 = client.resource("http://"+SERVER+":"+SERVER_PORT+"/server/statistics")
                .queryParam("n", ""+n).queryParam("mean_dev", "true");

        ClientResponse clientResponse5 = restResource5
                .accept("application/json")
                .get(ClientResponse.class);

        //check the status of POST request
        if (clientResponse5.getStatus() == 404) {
            System.out.println(clientResponse5.getEntity(String.class));
        }
        if (clientResponse5.getStatus() == 400){
            System.out.println(clientResponse5.getEntity(String.class));
        }
        if (clientResponse5.getStatus() == 201){
            System.out.println(clientResponse5.getEntity(String.class));
        }
    }




    //Parameters request for statistics --> N e ID_HOUSE -------------------------------------------------------
    private static Integer requestN(){
        int n = 0;
        Boolean check_n = false;
        do {
            System.out.print("Inserisci n: ");
            try {
                n = Integer.parseInt(input.readLine());
                check_n = true;
            } catch (NumberFormatException e) {
                System.out.println("Errore nell'inserimento di n");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }while(!check_n);
        return n;
    }

    private static String requestIdHouse(){
        String id_house = "";
        Boolean check_id_house = false;
        do {
            System.out.print("Inserisci ID di una casa: ");
            try {
                id_house = input.readLine();
                Integer.parseInt(id_house);
                check_id_house = true;
            } catch (NumberFormatException e) {
                System.out.println("Errore nell'inserimento dell'ID della casa");
            } catch (IOException e) {
                System.out.println("Errore nell'inserimento dell'ID della casa");
            }
        }while(!check_id_house);
        return id_house;
    }

    /*---------------------------------------------------------------------------------------------------*/

    private static void exitToServer(int id){
        Client client = Client.create();
        WebResource webResourcePost = client
                .resource("http://" + SERVER + ":" + SERVER_PORT + "/server/removeAdmin");
        ClientResponse clientResponsePost = webResourcePost.type(MediaType.APPLICATION_JSON)
                .post(ClientResponse.class, id+"");

        if(clientResponsePost.getStatus() == 201){
            System.out.println("EXIT");
        }
    }
}
