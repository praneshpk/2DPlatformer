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
    private final UUID id;
    protected float dir, left, right, up;

    protected PVector velocity;
    protected int ground = GROUND;

    protected static final float jumpSpeed = 6;
    protected static final float walkSpeed = 3;
    protected PVector[] spawnPts;


    public Player(UUID id)
    {
        type = Type.PLAYER;
        spawnPts = core.Server.getSpawnPoints();
        Random r = new Random();
        collide = null;
        this.id = id;
        if(spawnPts != null) {
            int i = r.nextInt(spawnPts.length);
            pos = new PVector(spawnPts[i].x, spawnPts[i].y - PLAYER_SZ);
        } else {
            pos = new PVector(0,HEIGHT - PLAYER_SZ);
        }
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

    public UUID getId() { return id; }

    public float getDir() { return dir; }

    public void setDir(float dir) { this.dir = dir; }

    public float getLeft() { return left; }

    public void setLeft(float left) { this.left = left; }

    public float getRight() { return right; }

    public void setRight(float right) { this.right = right; }

    public float getUp() { return up; }

    public void setUp(float up) { this.up = up; }

    public PVector getVelocity() { return velocity; }

    public void setVelocity(PVector velocity) { this.velocity = velocity; }

    public int getGround() { return ground; }

    public void setGround(int ground) { this.ground = ground; }

    public static float getJumpSpeed() { return jumpSpeed; }

    public static float getWalkSpeed() { return walkSpeed; }

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
    public String toString()
    {
        return "Player-" + id.toString().substring(0, 8);
    }

}
