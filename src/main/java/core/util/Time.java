package core.util;

public abstract class Time
{
    protected long start;
    protected long pausedTime;
    protected long pause;
    protected int tic;

    public abstract long getTime();

    public abstract void start();

    public abstract void pause();

    public abstract void unPause();

}
