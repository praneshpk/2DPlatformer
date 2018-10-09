package core.objects;

import core.Main;
import processing.core.PApplet;
import processing.core.PVector;

import java.awt.*;
import java.io.Serializable;

import static core.GameConstants.*;

/**
 * Player class responsible for movement
 *
 * Adapted from:
 * https://www.openprocessing.org/sketch/92234
 */
public class Player implements Serializable, Collidable {
    public int id;
    public float dir, left, right, up;

    private PApplet parent;
    private String username;
    private Rectangle rect;
    private PVector pos;
    private PVector velocity;
    private static float jumpSpeed = 6;
    private static float walkSpeed = 3;

    public Player(PApplet p)
    {
        parent = p;
        pos = new PVector(0, 0);
        rect = new Rectangle((int)pos.x, (int)pos.y, PLAYER_SZ, PLAYER_SZ);
        dir = 1;
        velocity = new PVector(0, 0);
    }

    public Player()
    {
    }

    /**
     * Updates the player's current state
     *
     * Adapted from
     * https://www.openprocessing.org/sketch/92234
     */
    public void update()
    {
        if(pos.y < GROUND)
            velocity.y += GRAVITY;
        else
            velocity.y = 0;

        if(pos.y >= GROUND && up != 0)
            velocity.y = -jumpSpeed;

        velocity.x = walkSpeed * (left + right);

        PVector nextPos = new PVector(pos.x, pos.y);
        nextPos.add(velocity);

        float offset = rect.width;

        if(Main.collision(new Rectangle((int) nextPos.x, (int) nextPos.y, PLAYER_SZ, PLAYER_SZ))) {
            System.out.println("Collision detected");
        }
        if (nextPos.x > 0 && nextPos.x < (WIDTH - offset))
            pos.x = nextPos.x;
        if (nextPos.y > 0 && nextPos.y < (HEIGHT - offset))
            pos.y = nextPos.y;
        rect.x = (int) pos.x;
        rect.y = (int) pos.y;

    }

    public Rectangle getRect() { return rect; }

    public void display()
    {
        parent.fill(parent.color(id * 100 % 255));
        parent.noStroke();
        parent.rect(pos.x, pos.y, PLAYER_SZ, PLAYER_SZ);
    }

    public String getUsername() { return username; }

    public void setUsername(String username) { this.username = username; }
}
