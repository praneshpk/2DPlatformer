package core.util.time;

public class LocalTime extends Time
{
    Time anchor;

    public LocalTime(Time anchor, int tic)
    {
        this.anchor = anchor;
        this.tic = tic;
    }

    @Override
    public long getTime()
    {
        if(marker == 0)
            return (long)((anchor.getTime() - pausedTime) / tic);
        else
            return (long)((marker - pausedTime) / tic);
    }

    @Override
    public void start()
    {
        start = anchor.getTime();
        marker = 0;
        pausedTime = 0;
    }

    public void reset()
    {
        pausedTime = anchor.getTime() - start;
    }

    @Override
    public void pause()
    {
        marker = anchor.getTime();
    }

    public void unPause()
    {
        pausedTime += anchor.getTime() - marker;
        marker = 0;
    }

}
