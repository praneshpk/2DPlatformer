package core.util;

import core.util.time.GlobalTime;
import core.util.time.LocalTime;
import static org.junit.Assert.*;


import org.junit.Test;

public class LocalTimeTest
{

    @Test
    public void getTime() throws InterruptedException
    {
        GlobalTime gt = new GlobalTime(1);
        LocalTime lt = new LocalTime(gt, 1000);
        gt.start();
        lt.start();
        Thread.sleep(1000);
        assertEquals(gt.getTime(), lt.getTime() * 1000, 10);
        gt.pause();
        Thread.sleep(1000);
        gt.unPause();
        assertEquals(gt.getTime(), lt.getTime() * 1000, 10);
        lt.pause();
        Thread.sleep(2000);
        assertEquals(1, lt.getTime());
        lt.unPause();
        assertEquals(3, lt.getTime());
        assertEquals(gt.getTime(), lt.getTime() * 1000, 10);
        System.err.println("global: " + gt.getTime() + " local: " + lt.getTime());
    }
}