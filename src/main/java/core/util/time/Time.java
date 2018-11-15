package core.util.time;

import java.io.Serializable;

public abstract class Time implements Serializable
{
    protected long start;
    protected long marker;
    protected long pausedTime;
    protected float tic;

    public float getTic() { return tic; }

    public void setTic(float tic) { this.tic = tic; }

    public abstract long getTime();

    public abstract void start();

    public abstract void pause();

    public abstract void unPause();

}
