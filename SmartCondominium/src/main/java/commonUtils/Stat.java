package commonUtils;


import simulation_src_2019.Measurement;

import java.util.ArrayList;

public class Stat {
    private double value;
    private long timestamp;

    public Stat(Double value, long timestamp){
        this.value = value;
        this.timestamp = timestamp;
    }
    public Stat(){}

    public Stat(ArrayList<Measurement> measurements){
        this.value =  mean(measurements);
        this.timestamp = measurements.get(measurements.size()-1).getTimestamp();
    }

    // GETTER and SETTER ----------------------------------------------------------------------------------------------

    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    // ----------------------------------------------------------------------------------------------------------------

    public Stat clone(){
        return new Stat(this.value, this.timestamp);
    }
    public double mean(ArrayList<Measurement> measures){
        double mean = 0.0;
        for (Measurement m: measures) mean += m.getValue();
        return mean/measures.size();
    }

    public String toString(){
        return "TS: "+timestamp+" --> "+value;
    }

}
