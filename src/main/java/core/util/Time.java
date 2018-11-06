package core.util;

public abstract class Time
{
    protected long start;
    protected long pausedTime;
    protected long pause;
    protected int tic;

    public abstract long getTime();

    public abstract void start();

    public void pause()
    {
        pause = System.currentTimeMillis();
    }

    public void unPause()
    {
        pausedTime = (System.currentTimeMillis() - pause) / tic;
        pause = 0;
    }

}
