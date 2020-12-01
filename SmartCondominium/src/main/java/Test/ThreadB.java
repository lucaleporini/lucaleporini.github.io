package Test;

public class ThreadB implements Runnable {
    @Override
    public void run() {
        Singleton singleton = Singleton.getInstance();
        System.out.println("THREAD B get instance");
        singleton.method1();
    }
}
