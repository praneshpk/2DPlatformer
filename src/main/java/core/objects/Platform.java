package core.objects;

import java.awt.*;


public class Platform {
    private Rectangle rect;
    private int x;
    private int y;

    public Platform(int x, int y, int w, int h, float[] color)
    {
        this.x = x;
        this.y = y;
        rect = new Rectangle(x, y, (int) w, (int) h);
    }

    public void display()
    {
        //
    }


}
