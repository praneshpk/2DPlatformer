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
        gt.start();
        Thread.sleep(2000);
        LocalTime lt = new LocalTime(gt, 1);
        lt.start();
        gt.pause();
        Thread.sleep(2000);
        gt.unPause();
        System.out.println("GT: " + gt.getTime());
        System.out.println("LT: " + lt.getTime());
        gt.pause();
        System.out.println("GT: " + gt.getTime());
        System.out.println("LT: " + lt.getTime());
        Thread.sleep(2000);
        gt.unPause();
        System.out.println("GT: " + gt.getTime());
        System.out.println("LT: " + lt.getTime());
        lt.pause();
        Thread.sleep(2000);
        lt.unPause();
        System.out.println("GT: " + gt.getTime());
        System.out.println("LT: " + lt.getTime());
        lt.reset();
        System.out.println("GT: " + gt.getTime());
        System.out.println("LT: " + lt.getTime());
        Thread.sleep(2000);
        System.out.println("GT: " + gt.getTime());
        System.out.println("LT: " + lt.getTime());
    }
}