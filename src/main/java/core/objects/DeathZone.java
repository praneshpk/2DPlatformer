package core.objects;

import core.util.Constants;
import processing.core.PApplet;
import processing.core.PVector;

import java.awt.*;
import java.util.Random;

public class DeathZone implements Collidable, Constants {

    protected Rectangle rect;
    protected PVector pos;

    public DeathZone(PVector pos, float w, float h)
    {
        this.pos = pos;
        rect = new Rectangle((int) pos.x, (int) pos.y, (int) w, (int) h);
    }

    @Override
    public void display(PApplet p, long cycle) {

    }

    @Override
    public void update(long cycle) {

    }

    @Override
    public void handle(Player p) {
        Random r = new Random();
        p.pos = SPAWN[r.nextInt(SPAWN.length)].sub(0, PLAYER_SZ);
        p.rect = new Rectangle((int)p.pos.x, (int)p.pos.y, PLAYER_SZ, PLAYER_SZ);
        p.dir = 1;
        p.left = 0;
        p.right = 0;
        p.up = 0;
        p.velocity = new PVector(0, 0);
    }

    @Override
    public Rectangle getRect() { return rect; }
}
