package commonUtils;


import javax.xml.bind.annotation.XmlRootElement;
import java.util.ArrayList;

@XmlRootElement
public class House implements Comparable<House>{

    private String id;
    private String ip;
    private int port;

    public House(){}

    public House(String id_house, String ip_house, int port_house){
        this.id = id_house;
        this.ip = ip_house;
        this.port = port_house;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    @Override
    public House clone(){
        return new House(this.id, this.ip, this.port);
    }

    @Override
    public int compareTo(House o) {
        return this.getId().compareTo(o.id);
    }
}
