package core.util;

import org.junit.Assert;
import org.junit.Test;

import static org.junit.Assert.*;

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
        Assert.assertEquals(gt.getTime(), lt.getTime() * 1000, 10);
        gt.pause();
        Thread.sleep(1000);
        gt.unPause();
        Assert.assertEquals(gt.getTime(), lt.getTime() * 1000, 10);
        lt.pause();
        Thread.sleep(2000);
        Assert.assertEquals(1, lt.getTime());
        lt.unPause();
        Assert.assertEquals(3, lt.getTime());
        Assert.assertEquals(gt.getTime(), lt.getTime() * 1000, 10);
        System.err.println("global: " + gt.getTime() + " local: " + lt.getTime());
    }
}