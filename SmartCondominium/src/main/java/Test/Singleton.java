package Test;

import java.util.ArrayList;

public class Singleton {
    private static Singleton instance;
    private ArrayList<String> arr;

    private Singleton(){
        arr = new ArrayList<>();
    }
    public static Singleton getInstance() {
        if(instance==null)
            synchronized(Singleton.class) {
                if(instance == null) {
                    instance = new Singleton();
                }
            }
        return instance;
    }

    public ArrayList<String> method(){
        synchronized(arr) {
            System.out.println("SONO IN METHOD");

            try {
                Thread.sleep(10000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            System.out.println("ESCO DA METHOD");
            return arr;
        }
    }

    public void method1(){
        synchronized (arr) {
            arr.add("ciao");
            System.out.println("*** SONO IN METHOD1");
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("****ESCO DA METHOD1");
        }
    }
}

