package core.objects;

import processing.core.PApplet;
import processing.core.PVector;

import java.awt.*;
import java.util.Random;
import java.util.UUID;

/**
 * Player class responsible for movement
 * <p>
 * Adapted from:
 * https://www.openprocessing.org/sketch/92234
 */
public class Player extends Collidable
{
    public final UUID id;
    public float dir, left, right, up;
    public Collidable collide;

    protected PVector velocity;
    public int ground = GROUND;

    private static final float jumpSpeed = 6;
    private static final float walkSpeed = 3;

    public Player(UUID id)
    {
        type = Type.PLAYER;
        Random r = new Random();
        collide = null;
        this.id = id;
        int i = r.nextInt(SPAWN.length);
        pos = new PVector(SPAWN[i].x, SPAWN[i].y - PLAYER_SZ);
        rect = new Rectangle((int) pos.x, (int) pos.y, PLAYER_SZ, PLAYER_SZ);
        dir = 1;
        velocity = new PVector(0, 0);
        color = new Color(40 + r.nextInt(10) * 12);
    }

    public Player()
    {
        id = UUID.randomUUID();
        type = Type.PLAYER;
        collide = null;
        pos = new PVector(0, PLAYER_SZ);
        rect = new Rectangle((int) pos.x, (int) pos.y, PLAYER_SZ, PLAYER_SZ);
        dir = 1;
        velocity = new PVector(0,0);
        color = new Color(0);
    }

    /**
     * Updates the player's current state
     * <p>
     * Adapted from
     * https://www.openprocessing.org/sketch/92234
     */
    public void update(float cycle)
    {
        if (pos.y < ground) {
            velocity.y += GRAVITY / cycle;
        } else
            velocity.y = 0;

        if (pos.y >= ground && up != 0)
            velocity.y = -jumpSpeed;

        velocity.x = walkSpeed / cycle * (left + right);

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

    @Override
    public void display(PApplet p, float cycle)
    {
        update(cycle);
        p.fill(color.getRGB());
        p.noStroke();
        p.rect(pos.x, pos.y, PLAYER_SZ, PLAYER_SZ);
    }

    @Override
    public String toString()
    {
        return "Player-" + id.toString().substring(0, 8);
    }

    public void setColor(int r, int g, int b) {
        color = new Color(r,g,b);
    }
}
