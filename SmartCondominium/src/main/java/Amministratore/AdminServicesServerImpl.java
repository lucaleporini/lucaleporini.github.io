package Amministratore;


import io.grpc.stub.StreamObserver;
import protosPackage.AdminServicesGrpc;
import protosPackage.AdminServicesOuterClass;

public class AdminServicesServerImpl extends AdminServicesGrpc.AdminServicesImplBase {

    @Override
    public void notify(AdminServicesOuterClass.HouseNP houseNP, StreamObserver<AdminServicesOuterClass.ResponseNP> responseObserver) {
        switch (houseNP.getOption()){
            case 0:
                System.out.println("CASA "+houseNP.getId()+": ENTRA NELLA RETE");
                break;
            case 1:
                System.out.println("CASA "+houseNP.getId()+": ESCE DALLA RETE");
                break;
            case 2:
                System.out.println("CASA "+houseNP.getId()+": EXTRA CONSUMO");
                break;
        }

        responseObserver.onNext(AdminServicesOuterClass.ResponseNP.newBuilder().setText("OK").build());

        responseObserver.onCompleted();
    }
}