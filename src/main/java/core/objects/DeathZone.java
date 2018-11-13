package core.objects;

import core.util.Constants;
import processing.core.PApplet;
import processing.core.PVector;

import java.awt.*;
import java.util.Random;

public class DeathZone implements Collidable, Constants
{

    final Type type = Type.DEATH_ZONE;
    protected final Rectangle rect;
    protected final PVector pos;

    public DeathZone(PVector pos, float w, float h)
    {
        this.pos = pos;
        rect = new Rectangle((int) pos.x, (int) pos.y, (int) w, (int) h);
    }

    @Override
    public Type type() { return type; }

    @Override
    public void display(PApplet p, long cycle)
    {

    }

    @Override
    public void update(long cycle)
    {

    }

    @Override
    public void handle(Collidable p)
    {
        if (p instanceof Player) {
            Player tmp = (Player) p;
            Random r = new Random();
            tmp.pos = SPAWN[r.nextInt(SPAWN.length)].sub(0, PLAYER_SZ);
            tmp.rect = new Rectangle((int) tmp.pos.x, (int) tmp.pos.y, PLAYER_SZ, PLAYER_SZ);
            tmp.dir = 1;
            tmp.left = 0;
            tmp.right = 0;
            tmp.up = 0;
            tmp.velocity = new PVector(0, 0);
            p = tmp;
        }
    }

    @Override
    public Rectangle getRect()
    {
        return rect;
    }

    @Override
    public PVector getPos()
    {
        return pos;
    }

    @Override
    public int compareTo(Collidable o)
    {
        return type.compareTo(o.type());
    }
}
