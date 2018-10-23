package core.objects;

import core.util.Constants;
import processing.core.PVector;

import java.awt.*;

public class MovingPlatform extends StaticPlatform implements Collidable, Constants {

    private PVector lo, hi, dir;
    private float time;


    /**
     * Creates a basic Moving Platform object with a random color
     * and default time value of 3s
     *
     * @param pos position of the moving platform
     * @param dir direction / speed of the moving platform
     */
    public MovingPlatform(PVector pos, PVector dir)
    {
        super(pos,MV_PLATORM[0],MV_PLATORM[1],new Color((int)(Math.random() * 0x1000000)));
        this.dir = dir;
        this.time = 3;
        this.lo = new PVector(pos.x, pos.y);
        this.hi = new PVector(pos.x - dir.x * time, pos.y - dir.y * time);
    }

    @Override
    public void update(long cycle)
    {
        float elapsed = cycle%(time*2*1000)/1000f;
        if(elapsed < time) {
            pos.x = lo.x - (dir.x * elapsed );
            pos.y = lo.y - (dir.y * elapsed );
        }
        if(elapsed >= time && elapsed <= time * 2) {
            pos.x = hi.x + (dir.x * (elapsed%time) );
            pos.y = hi.y + (dir.y * (elapsed%time) );
        }

        // Update hitbox
        rect.x = (int) pos.x;
        rect.y = (int) pos.y;

    }
    public PVector getDir() { return dir; }
}
