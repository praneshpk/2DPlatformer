package core.objects;

import core.util.Constants;
import processing.core.PApplet;
import processing.core.PVector;

import java.awt.*;
import java.util.Random;

public class DeathZone extends Collidable
{
    public DeathZone(PVector pos, float w, float h)
    {
        type = Type.DEATH_ZONE;
        this.pos = pos;
        rect = new Rectangle((int) pos.x, (int) pos.y, (int) w, (int) h);
    }

    @Override
    public void display(PApplet p, float cycle) { }

    @Override
    public void update(float cycle) { }

    @Override
    public void handle(Collidable p)
    {
        if (p instanceof Player) {
            Player tmp = (Player) p;
            //Random r = new Random();
            tmp.pos = SPAWN[(int)(p.pos.y + p.pos.x) % SPAWN.length].sub(0, PLAYER_SZ);
            tmp.rect = new Rectangle((int) tmp.pos.x, (int) tmp.pos.y, PLAYER_SZ, PLAYER_SZ);
            tmp.dir = 1;
            tmp.left = 0;
            tmp.right = 0;
            tmp.up = 0;
            tmp.velocity = new PVector(0, 0);
            p = tmp;
        }
    }
}
