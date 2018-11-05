package core.util;

import org.junit.Assert;
import org.junit.Test;

public class GlobalTimeTest
{

    @Test
    public void testTime() throws InterruptedException
    {
        GlobalTime t = new GlobalTime();
        t.start();
        Thread.sleep(1000);
        Assert.assertEquals(1000, t.getTime(), 10);
        t.pause();
        Assert.assertEquals(1000, t.getTime(), 10);
        Thread.sleep(1000);
        t.unPause();
        Thread.sleep(1000);
        Assert.assertEquals(2000, t.getTime(), 10);
    }
}