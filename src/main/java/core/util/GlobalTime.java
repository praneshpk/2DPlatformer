package core.util;

public class GlobalTime extends Time
{
    @Override
    public long getTime()
    {
        if(pause == 0)
            return System.currentTimeMillis() - start - pausedTime;
        else
            return pause - start;
    }

    @Override
    public void start()
    {
        pause = 0;
        pausedTime = 0;
        start = System.currentTimeMillis();
    }

    @Override
    public void pause()
    {
        pause = System.currentTimeMillis();
    }

    @Override
    public void unPause()
    {
        pausedTime = System.currentTimeMillis() - pause;
        pause = 0;
    }
}
