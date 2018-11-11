package core.util;

import core.util.time.GlobalTime;
import static org.junit.Assert.*;
import org.junit.Test;

public class GlobalTimeTest
{

    @Test
    public void getTime() throws InterruptedException
    {
        GlobalTime t = new GlobalTime(1);
        t.start();
        Thread.sleep(1000);
        assertEquals(1000, t.getTime(), 10);
        t.pause();
        assertEquals(1000, t.getTime(), 10);
        Thread.sleep(1000);
        t.unPause();
        Thread.sleep(1000);
        assertEquals(2000, t.getTime(), 10);
    }
}