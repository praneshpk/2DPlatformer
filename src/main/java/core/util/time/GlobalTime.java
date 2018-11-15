package core.util.time;

public class GlobalTime extends Time
{
    public GlobalTime(int tic) { this.tic = tic; }

    @Override
    public long getTime()
    {
        if(marker == 0)
            return (long)((System.currentTimeMillis() - start - pausedTime) / tic);
        else
            return (long)((marker - start - pausedTime) / tic);
    }

    @Override
    public void start()
    {
        start = System.currentTimeMillis();
        marker = 0;
        pausedTime = 0;
    }

    @Override
    public void pause()
    {
        marker = System.currentTimeMillis();
    }

    @Override
    public void unPause()
    {
        pausedTime += System.currentTimeMillis() - marker;
        marker = 0;

    }
}
