package ServerAmministratore;

import commonUtils.*;

import java.util.*;

public class ServerSingleton {
    private static ServerSingleton instance;
    private Houses listHouses;
    private HashMap<String, Stats> statsHouses;
    private Stats statsCondominium;
    private HashMap<String, String> admins;

    private ServerSingleton(){
        listHouses = new Houses();
        statsHouses = new HashMap<>();
        statsCondominium = new Stats(new ArrayList<>());
        admins = new HashMap<>();
    }

    public static ServerSingleton getInstance() {
        if(instance==null)
            synchronized(ServerSingleton.class) {
                if(instance == null) {
                    instance = new ServerSingleton();
                }
            }
        return instance;
    }

    /*GETTER E SETTER ----------------------------------------------------------------*/

    //liste di case
    public Houses getListHouses() {
        synchronized (listHouses) {
            return listHouses.clone();
        }
    }

    public HashMap<String, String> getAdmins() {
        synchronized (admins) {
            return (HashMap<String, String>) admins.clone();
        }
    }

    /*---------------------------------------------------------------------------------------------------*/

    // gestione delle operazioni della CASA

    public Houses addHouse(House newHouse){
        synchronized (listHouses) {
            //controllo se l'id non è presente
            if (!listHouses.containsKey(newHouse.getId())) {
                listHouses.addHouse(newHouse);
                synchronized (statsHouses) {
                    //inizializzazione statsHouses --> da SOSTITUIRE con uno statsHouses vuoto
                    statsHouses.put(newHouse.getId(), new Stats(new ArrayList<>()));
                }
                return listHouses.clone();
            }
            return null;
        }
    }

    public House removeHouse(House house){
        synchronized (listHouses){
            synchronized (statsHouses){
                statsHouses.remove(house.getId());
                return(listHouses.remove(house.getId()));
            }
        }
    }

    //aggiunta di una statistica a una relativa casa --> CASA/SenderLocalStatsThread
    public void insertLocalStatistic(String id_house, Stat stat_house){
        synchronized (statsHouses){
            statsHouses.get(id_house).insertStat(stat_house);
        }
    }

    //aggiunta di una statistica al condiminio --> CASA/SenderCondominiumStatsThread
    public void insertCondominiumStatistic(Stat statCondominium){
        synchronized (statsCondominium){
            statsCondominium.insertStat(statCondominium);
        }
    }

    /*-------------------------------------------------------------------------------------------------------------*/

    //gestione delle statistiche --> AMMINISTRATORE

    public Boolean addAdmin(String id){
        int port = Integer.parseInt(id)+5000;
        synchronized (admins){
            if (admins.containsKey(id)){
                return false;
            }else{
                admins.put(id, "127.0.1."+id+":"+port);
                return true;
            }
        }
    }

    public void removeAdmin(String id){
        synchronized (admins){
            admins.remove(id);
        }
    }

    public HashMap<String, Stats> getStatsHouses() {
        synchronized (statsHouses) {
            return new HashMap<>(statsHouses);
        }
    }

    // request 2
    public Stats nStatsOfHouse(int n, String id_house){
        synchronized (statsHouses) {
            if (statsHouses.containsKey(id_house)) {
                //controllo che siano presenti almeno N statistiche
                ArrayList<Stat> stat = statsHouses.get(id_house).getNstats(n);
                if (stat != null) {
                    return new Stats(stat);
                } else return null;
            } else {
                return null;
            }
        }
    }

    //request 3
    public Stats nStatsOfCondominio(int n){
        synchronized (statsCondominium) {
            if (statsCondominium.getNstats(n) != null) {
                return new Stats(statsCondominium.getNstats(n));
            } else return null;
        }
    }

    //request4
    public ArrayList<Double> meanDevNstatsofHouse(int n, String id_house){
        synchronized (statsHouses) {
            //ritorna media e deviazione o null
            if (statsHouses.containsKey(id_house)) {
                return statsHouses.get(id_house).meanDevNstats(n);
            } else {
                return null;
            }
        }
    }

    //request5
    public ArrayList<Double> meanDevNstatsofCondominium(int n){
        synchronized (statsCondominium) {
            //ritorna media e deviazione o null
            return statsCondominium.meanDevNstats(n);
        }
    }


    /*-------------------------------------------------------------------------------------------------------------*/

    //GESTIONE NOTIFICHE PUSH
    public void startSenderPushNotification(String id_house, int option){
        synchronized (admins){
            if(admins.size()>0) new Thread(new SenderPushNotificationThread(id_house, option)).start();
        }
    }
}
