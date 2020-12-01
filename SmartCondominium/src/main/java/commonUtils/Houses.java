package commonUtils;


import com.google.common.annotations.VisibleForTesting;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.*;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)

public class Houses {

    @XmlElement(name="house")
    private HashMap<String, House> listHouses;

    public Houses(){
        listHouses = new HashMap<>();
    }

    public Houses(HashMap<String, House> ListHouses){
        this.listHouses = ListHouses;
    }

    /*GETTER E SETTER -------------------------------------------------------------------------------*/
    public HashMap<String, House> getHashMap() {
        return (HashMap<String, House>) listHouses.clone();
    }

    public void setHashMap(HashMap<String, House> listHouses) {
        this.listHouses = listHouses;
    }

    /*-----------------------------------------------------------------------------------------------*/
    public Boolean isEmpty(){
        return listHouses.size() == 0;
    }

    @Override
    public Houses clone(){
        HashMap<String, House> houses = new HashMap<>();
        for (Map.Entry<String, House> entry : listHouses.entrySet()) {
            houses.put(entry.getKey(), entry.getValue().clone());
        }
        return new Houses(houses);
    }

    public int getSize(){
        return listHouses.size();
    }

    public void addHouse(House house){
        listHouses.put(house.getId(), house);
    }

    public House getHouse(String id_house){
        return listHouses.get(id_house);
    }

    public Boolean containsKey(String id_house){
        return listHouses.containsKey(id_house);
    }

    public House remove(String id_house){
        return listHouses.remove(id_house);
    }

    public String toString(){
        String result = "";
        for (Map.Entry<String, House> entry : listHouses.entrySet())
            result += "CASA: "+entry.getValue().getId()+" --> IP: "+entry.getValue().getIp()+" | PORT: "+entry.getValue().getPort()+"\n";
        return result;
    }

    public House getHouseFromID(String id_house){
        return listHouses.get(id_house).clone();
    }

    public void clear(){
        listHouses.clear();
    }

    public House getHouseWithMaxId(){
        ArrayList<String> id_houses = new ArrayList<>(listHouses.keySet());
        id_houses.sort((a, b) -> -a.compareTo(b));
        return listHouses.get(id_houses.get(0));


    }
}
