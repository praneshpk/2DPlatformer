package core.util.time;

public class LocalTime extends Time
{
    GlobalTime anchor;

    public LocalTime(int tic)
    {
        anchor = new GlobalTime(tic);
        this.tic = tic;
    }

    public LocalTime(GlobalTime global, int tic)
    {
        anchor = global;
        this.tic = tic;
    }

    @Override
    public long getTime()
    {
        if(pause == 0)
            return (anchor.getTime() - pausedTime) / tic;
        else
            return pause / tic;
    }

    @Override
    public void start()
    {
        start = anchor.start;
        pause = 0;
        pausedTime = 0;
    }

    @Override
    public void pause()
    {
        pause = anchor.getTime();
    }

    @Override
    public void unPause()
    {
        pausedTime = pause / tic;
        pause = 0;
    }

}
