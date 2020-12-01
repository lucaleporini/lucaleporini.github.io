package commonUtils;

import javax.xml.bind.annotation.*;
import java.util.ArrayList;
import java.util.Arrays;

@XmlRootElement
@XmlAccessorType(XmlAccessType.NONE)
public class Stats {

    private ArrayList<Stat> stats;

    public Stats(ArrayList<Stat> s){
        this.stats = s;
    }
    public Stats(){}

    /*GETTER E SETTER ----------------------------------------------------------------*/

    @XmlElementWrapper
    @XmlElement(name="stat")
    public ArrayList<Stat> getStat() {
        return new ArrayList<>(stats);
    }

    public void setStat(ArrayList<Stat> stats) {
        this.stats= stats;
    }

    /*public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }*/
    /*---------------------------------------------------------------------------------------------------*/
    //Inserimento di una nuova statistica
    public void insertStat(Stat s){
        stats.add(0, s);
    }

    public boolean isEmpty(){
        return stats.isEmpty();
    }

    /*public Double mean(){
        double sum = 0;
        for (Stat s : stat) sum += s.getValue();
        return sum/stat.size();
    }*/

    //AMMINISTRATORE -----------------------------------------------------------------------------------------
    public ArrayList<Stat> getNstats(int n){
        if(stats.size()<n){
            return null;
        }else{
            return new ArrayList<>(stats.subList(0, n));
        }

    }

    //DA FARE: MEDIA E DEV STANDARD
    public ArrayList<Double> meanDevNstats(int n){
        ArrayList<Stat> n_stats = getNstats(n);
        if (n_stats!=null){
            ArrayList<Double> meanDev = new ArrayList<>();
            double sum = 0;
            for (Stat stat : n_stats) sum += stat.getValue();
            Double mean = sum/n_stats.size();
            meanDev.add(mean);
            sum = 0;
            for (Stat stat : n_stats) sum += Math.pow((stat.getValue() - mean), 2);
            Double stDev = Math.sqrt(sum/n);
            meanDev.add(stDev);
            return meanDev;

        }else return null;
    }

    public String toString(){
        String result = "";
        for (Stat stat: stats) result += "TS: "+stat.getTimestamp()+" --> "+stat.getValue()+"\n";
        return result;
    }

    public Stats clone(){
        ArrayList<Stat> statsClone = new ArrayList<>();
        for(Stat stat: stats) statsClone.add(stat.clone());
        return new Stats(statsClone);
    }
}
