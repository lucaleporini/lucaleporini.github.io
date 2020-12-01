package Test;

public class ThreadA implements Runnable{
    @Override
    public void run() {
        Singleton singleton = Singleton.getInstance();
        System.out.println("THREAD A get instance");
        System.out.println(singleton.method());
    }
}
