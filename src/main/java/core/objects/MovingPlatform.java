package core.objects;

import core.util.Constants;
import processing.core.PVector;

import java.awt.*;

public class MovingPlatform extends StaticPlatform
{
    private PVector lo, hi, dir;
    private float time, elapsed;

    /**
     * Creates a basic Moving Platform object with a random color
     * and default time value of 3s
     *
     * @param pos position of the moving platform
     * @param dir direction / speed of the moving platform
     */
    public MovingPlatform(PVector pos, PVector dir)
    {
        super(pos, MV_PLATORM[0], MV_PLATORM[1], new Color((int) (Math.random() * 0x1000000)));
        type = Type.MOVING_PLATFORM;
        this.dir = dir;
        this.time = 3/(float)TIC;
        this.lo = new PVector(pos.x, pos.y);
        this.hi = new PVector(pos.x - dir.x * time, pos.y - dir.y * time);
    }

    public MovingPlatform(PVector pos, int w, int h, PVector dir, int t, Color c)
    {
        super(pos, w, h, c);
        type = Type.MOVING_PLATFORM;
        this.dir = dir;
        this.time = t /(float)TIC;
        this.lo = new PVector(pos.x, pos.y);
        this.hi = new PVector(pos.x - dir.x * time, pos.y - dir.y * time);
    }

    public void reset() {
        this.lo = new PVector(pos.x, pos.y);
        this.hi = new PVector(pos.x - dir.x * time, pos.y - dir.y * time);
    }

    public PVector getDir() {
        return dir;
    }

    public float getTime() {
        return time;
    }

    public void setTime(float time) {
        this.time = time;
    }

    @Override
    public void update(float cycle)
    {
        elapsed = cycle % (time * 2 * 1000) / 1000f;
        if (elapsed < time ) {
            pos.x = lo.x - (dir.x * elapsed);
            pos.y = lo.y - (dir.y * elapsed);
        }
        if (elapsed >= time && elapsed <= time * 2) {
            pos.x = hi.x + (dir.x * (elapsed % time));
            pos.y = hi.y + (dir.y * (elapsed % time));
        }

        // Update hitbox
        rect.x = (int) pos.x;
        rect.y = (int) pos.y;

    }
}
