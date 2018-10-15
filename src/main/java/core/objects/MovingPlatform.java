package core.objects;

import core.GameConstants;
import processing.core.PApplet;
import processing.core.PVector;

import java.awt.*;

public class MovingPlatform extends StaticPlatform implements Collidable, GameConstants {

    private PVector dir;
    private long start = System.currentTimeMillis();
    private int time;

    /**
     * Creates a basic Moving Platform object with a random color
     * and default time value of 3s
     *
     * @param p PApplet to draw to
     * @param pos position of the moving platform
     * @param dir direction / speed of the moving platform
     */
    public MovingPlatform(PApplet p, PVector pos, PVector dir)
    {
        super(p,pos,MV_PLATORM[0],MV_PLATORM[1],new Color((int)(Math.random() * 0x1000000)));
        this.dir = dir;
        this.time = 3;
    }

    public MovingPlatform(PApplet p, PVector pos, float w, float h, Color c, PVector dir, int time)
    {
        super(p,pos,w,h,c);
        this.dir = dir;
        this.time = time;
    }

    @Override
    public void update()
    {
        long elapsed = (System.currentTimeMillis() - start)/1000;
        if(elapsed < time) {
            pos.x -= dir.x/10;
            pos.y -= dir.y/10;
        } else if(elapsed >= time && elapsed < time * 2) {
            pos.x += dir.x/10;
            pos.y += dir.y/10;
        } else {
            start = System.currentTimeMillis();
        }

        // Update hitbox
        rect.x = (int) pos.x;
        rect.y = (int) pos.y;

    }
    public PVector getDir() { return dir; }
}
