package ServerAmministratore;

import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.UriInfo;
import java.util.ArrayList;

import com.google.gson.Gson;
import commonUtils.House;
import commonUtils.Houses;
import commonUtils.Stat;
import commonUtils.Stats;

@Path("/server")
public class ServerAmministratore {
    @Context
    protected UriInfo uriInfo;


    //gestione CASA CLIENT -------------------------------------------------------------------------------------
    @Path("/enterHouse")
    @POST
    @Consumes({"application/json", "application/xml"})
    //@Produces({"application/json", "application/xml"})
    public Response enterHouse(House newHouse){
        //addHouse --> listOfHouse o null
        Houses listHouses = ServerSingleton.getInstance().addHouse(newHouse);
        if(listHouses!=null){
            Gson gson = new Gson();
            String listOfHousesJ = gson.toJson(listHouses);

            System.out.println("\nCASA "+ newHouse.getId()+" ENTER IN NETWORK");
            //NOTIFICO L'AMMINISTRATORE
            ServerSingleton.getInstance().startSenderPushNotification(newHouse.getId(), 0);
            //System.out.println(listOfHousesJ);
            return Response.status(Status.CREATED).entity(listOfHousesJ).build();
        }else return Response.status(Status.NOT_ACCEPTABLE).build();
    }

    @Path("/addLocalStatistic")
    @POST
    @Consumes({"application/json", "application/xml"})
    //@Produces({"application/json", "application/xml"})
    public Response addLocalStatistic(@HeaderParam("id_house") String id_house, String stat_houseJ) throws Exception{
        Gson gson = new Gson();
        Stat stat_house = gson.fromJson(stat_houseJ, Stat.class);
        ServerSingleton.getInstance().insertLocalStatistic(id_house, stat_house);
        return Response.status(Status.CREATED).build();

    }


    @Path("/addCondominiumStatistic")
    @POST
    @Consumes({"application/json", "application/xml"})
    //@Produces({"application/json", "application/xml"})
    public Response addCondominiumStatistic(String stat_condominiumJ){
        Gson gson = new Gson();
        Stat statCondominium = gson.fromJson(stat_condominiumJ, Stat.class);
        System.out.println("\nCONDOMINIUM ADD "+statCondominium.getValue()+" | "+statCondominium.getTimestamp());
        ServerSingleton.getInstance().insertCondominiumStatistic(statCondominium);
        return Response.status(Status.CREATED).build();
    }

    @Path("/exitHouse")
    @POST
    @Consumes({"application/json", "application/xml"})
    public Response exitHouse(String houseToRemoveJ){
        Gson gson = new Gson();
        House houseToremove = gson.fromJson(houseToRemoveJ, House.class);
        System.out.println("\nCASA "+ houseToremove.getId()+" EXIT FROM NETWORK");
        if (ServerSingleton.getInstance().removeHouse(houseToremove) != null){
            //NOTIFICHE PUSH
            ServerSingleton.getInstance().startSenderPushNotification(houseToremove.getId(), 1);
        }
        return Response.status(Status.CREATED).build();
    }


    /*--------------------------------------------------------------------------------------------------------------*/
    //GESTIONE USCITA INCONTROLLATA
    @Path("/removeHouse")
    @POST
    @Consumes({"application/json", "application/xml"})
    public Response removeExitedHouse(String houseToRemoveJ){
        Gson gson = new Gson();
        House houseToremove = gson.fromJson(houseToRemoveJ, House.class);

        if (ServerSingleton.getInstance().removeHouse(houseToremove) != null){
            System.out.println("\nCASA "+ houseToremove.getId()+" UNCONTROLLED EXIT");
            //NOTIFICHE PUSH
            ServerSingleton.getInstance().startSenderPushNotification(houseToremove.getId(), 1);
        }

        return Response.status(Status.CREATED).build();
    }


    /*--------------------------------------------------------------------------------------------------------------*/



    //gestione AMMINISTRATORE -------------------------------------------------------------------------------------

    //AGGIUNTA DI UN NUOVO AMMINISTRATORE
    @Path("/addAdmin")
    @POST
    @Consumes({"application/json", "application/xml"})
    //@Produces({"application/json", "application/xml"})
    public Response addAdmin(String id){
        if(ServerSingleton.getInstance().addAdmin(id)){
            return Response.status(Status.CREATED).build();
        }
        return Response.status(Status.NOT_ACCEPTABLE).build();
    }

    //RIMOZIONE DI UN AMMINISTRATORE
    @Path("/removeAdmin")
    @POST
    @Consumes({"application/json", "application/xml"})
    //@Produces({"application/json", "application/xml"})
    public Response removeAdmin(String id){
        ServerSingleton.getInstance().removeAdmin(id);
        return Response.status(Status.CREATED).build();
    }


    //REQUEST 1
    @Path("/listHouses")
    @GET
    @Produces({"application/json", "application/xml"})
    public Response singletonListClient(){
        Gson gson = new Gson();
        Houses houses = ServerSingleton.getInstance().getListHouses();
        if (houses.isEmpty()) return Response.status(Status.BAD_REQUEST).entity("Non sono presenti ancora case").build();
        return Response.status(Status.CREATED).entity(gson.toJson(houses)).build();
    }



    @Path("/statistics")
    @GET
    @Produces({"application/json", "application/xml"})
    public Response statistics(@QueryParam("id_house") String id_house,@QueryParam("n") String n_stats, @QueryParam("mean_dev") Boolean mean_dev){
        int n = Integer.parseInt(n_stats);
        if(n<1) return Response.status(Status.BAD_REQUEST).entity("N must be positive").build();

        //controllo i parametri della richiesta dell'amministratore
        if(!uriInfo.getQueryParameters().containsKey("mean_dev")){
            if(uriInfo.getQueryParameters().containsKey("id_house")){
                //REQUEST 2
                //nStatsOfHouse --> arraylist<Double> o null
                Stats statsHouse = ServerSingleton.getInstance().nStatsOfHouse(n,id_house);
                if(statsHouse==null) return Response.status(Status.NOT_FOUND).entity("La casa non è presente").build();

                //serializzazione in JSON tramite Gson
                Gson gson = new Gson();
                String statsHouseJ = gson.toJson(statsHouse);
                return Response.status(Status.CREATED).entity(statsHouseJ).build();

            }else{
                //REQUEST 3
                //nStatsOfHouse --> arraylist<Double> o null
                Stats statsCondominio = ServerSingleton.getInstance().nStatsOfCondominio(n);
                if(statsCondominio==null) return Response.status(Status.BAD_REQUEST).entity("Non ci sono abbastanza statistiche").build();

                //serializzazione in JSON tramite Gson
                Gson gson = new Gson();
                String statsCondominioJ = gson.toJson(statsCondominio);
                return Response.status(Status.CREATED).entity(statsCondominioJ).build();
            }
        }else{
            if(uriInfo.getQueryParameters().containsKey("id_house")){
                //REQUEST 4
                //meanNstats --> double o null
                ArrayList<Double> result = ServerSingleton.getInstance().meanDevNstatsofHouse(n,id_house);
                if(result==null)
                    return Response.status(Status.NOT_FOUND).entity("Non ci sono abbastanza statistiche o id della casa è mancante").build();
                String response = "HOUSE "+id_house+" -- MEAN: "+result.get(0)+" | DEV_ST: "+result.get(1);
                return Response.status(Status.CREATED).entity(response).build();

            }else{
                //REQUEST 5
                //meanNstats --> double o null
                ArrayList<Double> result = ServerSingleton.getInstance().meanDevNstatsofCondominium(n);
                if(result==null)
                    return Response.status(Status.NOT_FOUND).entity("Non sono presenti abbastanza statistiche").build();

                //serializzazione in JSON
                String response = "CONDOMINIUM -- MEAN: "+result.get(0)+" | DEV_ST: "+result.get(1);
                return Response.status(Status.CREATED).entity(response).build();

            }
        }
    }

    /*--------------------------------------------------------------------------------------------------------------*/

    //BOOST NOTIFICATION
    @Path("/boostNotification")
    @POST
    @Consumes({"application/json", "application/xml"})
    //@Produces({"application/json", "application/xml"})
    public Response boostNotification(String id_house){
        //System.out.println("INVIO NOTIFICA: EXTRA CONSUMO");
        ServerSingleton.getInstance().startSenderPushNotification(id_house, 2);
        return Response.status(Status.CREATED).entity("OK").build();
    }

}
