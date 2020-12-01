package Casa;

import commonUtils.*;
import io.grpc.stub.StreamObserver;
import protosPackage.HouseServicesGrpc;
import protosPackage.HouseServicesOuterClass;


public class HouseServicesServerImpl extends HouseServicesGrpc.HouseServicesImplBase {

    //GESTIONE CASA: ENTER, EXIT, ADD STATISTIC
    @Override
    public void hello(HouseServicesOuterClass.House houseRequest, StreamObserver<HouseServicesOuterClass.HouseResponse> responseObserver) {
        //Casa nuova che si vuole inserire
        House house = new House(houseRequest.getId(), houseRequest.getIp(), houseRequest.getPort());

        //passo la risposta nello stream
        responseObserver.onNext(HouseSingleton.getInstance().enterHouse(house));

        //DA SISTEMARE L'ASSEGNAZIONE DEL COORDINATORE
        HouseServicesOuterClass.HouseResponse.Builder responseBuilder = HouseServicesOuterClass.HouseResponse.newBuilder();


        //completo e finisco la comunicazione
        responseObserver.onCompleted();
    }


    //TOREMOVE
    @Override
    public void exit(HouseServicesOuterClass.exitHouse exitRequest, StreamObserver<HouseServicesOuterClass.Response> responseObserver){
        System.out.println("\nCASA "+exitRequest.getHouse().getId()+": ESCE DALLA RETE\n");
        House houseToRemove = new House(exitRequest.getHouse().getId(), exitRequest.getHouse().getIp(), exitRequest.getHouse().getPort());

        /*try {
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }*/
        //rimuovo la casa dalla lista di case
        if(HouseSingleton.getInstance().removeHouse(houseToRemove)){
            System.out.println("I'M NEW COORDINATOR --- exit");
            try {
                HouseSingleton.getInstance().notifyToAllNewCoordinator();
            } catch (InterruptedException e) {
                //e.printStackTrace();
            }
        }

        HouseServicesOuterClass.Response.Builder response = HouseServicesOuterClass.Response.newBuilder();

        response.setText("OK");
        //passo la risposta nello stream
        responseObserver.onNext(response.build());

        //completo e finisco la comunicazione
        responseObserver.onCompleted();
    }

    //richiesta per agiungere una statistica locale
    @Override
    public void addStatistic(HouseServicesOuterClass.Statistic statistic, StreamObserver<HouseServicesOuterClass.Response> responseObserver){

        HouseSingleton.getInstance().addHouseStatistic(statistic.getId(), statistic.getValue(), statistic.getTimestamp());

        //costruisco la risposta di tipo Statistic.Response
        HouseServicesOuterClass.Response response= HouseServicesOuterClass.Response.newBuilder()
                .setText("OK").build();

        //passo la risposta nello stream
        responseObserver.onNext(response);

        //completo e finisco la comunicazione
        responseObserver.onCompleted();
    }

    //RICEVO UNA RICHIESTA DI BOOST
    @Override
    public void requestBoost(HouseServicesOuterClass.BoostRequest boostRequest, StreamObserver<HouseServicesOuterClass.BoostResponse> responseObserver){
        House requestBoostHouse = new House(boostRequest.getHouse().getId(), boostRequest.getHouse().getIp(), boostRequest.getHouse().getPort());
        //System.out.println("\nBOOST REQUEST -->  ID: "+requestBoostHouse.getId()+", IP: "+requestBoostHouse.getIp()+", PORT: "+requestBoostHouse.getPort()+
        //        " --- TIMESTAMP --> "+boostRequest.getTimestamp());
        responseObserver.onNext(HouseSingleton.getInstance().checkToReply(requestBoostHouse, boostRequest.getTimestamp()));
        responseObserver.onCompleted();
    }

    @Override
    public void reply(HouseServicesOuterClass.House houseRequest, StreamObserver<HouseServicesOuterClass.Response> responseObserver){
        HouseSingleton.getInstance().removeReply(houseRequest.getId());
        HouseServicesOuterClass.Response response= HouseServicesOuterClass.Response.newBuilder().setText("OK").build();
        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }


    //GESTIONE USCITA INCONTROLLATA: ASSEGNAZIONE NUOVO COORDINATORE
    @Override
    public void newCoordinator(HouseServicesOuterClass.House houseRequest, StreamObserver<HouseServicesOuterClass.Response> responseObserver){
        House newCoordinator = new House(houseRequest.getId(), houseRequest.getIp(), houseRequest.getPort());

        HouseSingleton.getInstance().setNewCoordinator(newCoordinator);
        System.out.println("NEW COORDINATOR SETTED: "+newCoordinator.getId());
        HouseServicesOuterClass.Response response= HouseServicesOuterClass.Response.newBuilder().setText("OK").build();
        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }
}
