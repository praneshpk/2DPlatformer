package core.objects;

import core.Main;
import processing.core.PApplet;
import processing.core.PVector;

import java.awt.*;
import java.io.Serializable;
import java.util.Random;
import java.util.UUID;

import static core.util.Constants.*;

/**
 * Player class responsible for movement
 * <p>
 * Adapted from:
 * https://www.openprocessing.org/sketch/92234
 */
public class Player implements Collidable, Serializable
{
    public UUID id;
    public float dir, left, right, up;

    protected Rectangle rect;
    protected PVector pos;
    protected PVector velocity;
    private static float jumpSpeed = 6;
    private static float walkSpeed = 3;
    protected int ground = GROUND;
    private int c;

    public Player(UUID id)
    {
        Random r = new Random();
        this.id = id;
        pos = SPAWN[r.nextInt(SPAWN.length)].sub(0, PLAYER_SZ);
        rect = new Rectangle((int) pos.x, (int) pos.y, PLAYER_SZ, PLAYER_SZ);
        dir = 1;
        velocity = new PVector(0, 0);
        c = r.nextInt(7) * 30;
    }

    /**
     * Updates the player's current state
     * <p>
     * Adapted from
     * https://www.openprocessing.org/sketch/92234
     */
    public void update(long cycle)
    {
//        Collidable collision = Main.collision(getRect());
//        //System.err.println(collision);
//        //collision = null;
//        if (collision != null) {
//            collision.handle(this);
//            if (collision instanceof DeathZone)
//                return;
//        } else {
//            ground = GROUND;
//        }

        if(cycle == 0)
            ground = GROUND;

        if (pos.y < ground) {
            velocity.y += GRAVITY;
        } else
            velocity.y = 0;

        if (pos.y >= ground && up != 0)
            velocity.y = -jumpSpeed;

        velocity.x = walkSpeed * (left + right);

        PVector nextPos = new PVector(pos.x, pos.y);
        nextPos.add(velocity);

        if (nextPos.x > 0 && nextPos.x < (WIDTH - PLAYER_SZ))
            pos.x = nextPos.x;
        pos.y = nextPos.y;
        rect.x = (int) pos.x;
        rect.y = (int) pos.y;

    }

    @Override
    public void handle(Collidable p)
    {
        System.err.println("Collision with " + this);
    }

    public Rectangle getRect()
    {
        return rect;
    }

    public void display(PApplet p, long cycle)
    {
        p.fill(p.color(c));
        p.noStroke();
        p.rect(pos.x, pos.y, PLAYER_SZ, PLAYER_SZ);
    }

    @Override
    public String toString()
    {
        return "Player-" + id.toString().substring(0, 8);
    }
}
