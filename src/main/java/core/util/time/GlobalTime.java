package core.util.time;

public class GlobalTime extends Time
{
    public GlobalTime(int tic)
    {
        this.tic = tic;
    }

    @Override
    public long getTime()
    {
        if(pause == 0)
            return (System.currentTimeMillis() - start - pausedTime) / tic;
        else
            return (pause - start) / tic;
    }

    @Override
    public void start()
    {
        start = System.currentTimeMillis();
        pause = 0;
        pausedTime = 0;
    }
}
