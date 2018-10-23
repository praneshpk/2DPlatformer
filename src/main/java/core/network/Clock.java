package core.network;

public class Clock extends Thread {
    public long start;
    public long elapsed;

    public Clock()
    {
        super();
        start = System.currentTimeMillis();
    }

    public void run()
    {
        while(true) {
            elapsed = System.currentTimeMillis() - start;
        }
    }
}
