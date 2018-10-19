package core.objects;

import core.GameServer;
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

    private String username;
    private Rectangle rect;
    private PVector pos;
    private PVector velocity;
    private static float jumpSpeed = 6;
    private static float walkSpeed = 3;
    private int ground = GROUND;

    public Player()
    {
        pos = new PVector(0, GROUND);
        rect = new Rectangle((int)pos.x, (int)pos.y, PLAYER_SZ, PLAYER_SZ);
        dir = 1;
        velocity = new PVector(0, 0);
    }

    /**
     * Updates the player's current state
     *
     * Adapted from
     * https://www.openprocessing.org/sketch/92234
     */
    public void update()
    {
        Collidable collision = GameServer.collision(new Rectangle((int) pos.x,
                (int) pos.y, PLAYER_SZ, PLAYER_SZ));
        if(collision != null) {
            Rectangle col = collision.getRect();

            if(pos.y + PLAYER_SZ < col.y + 10 ) {
                ground = (int)col.y - PLAYER_SZ;
                pos.y = col.y - PLAYER_SZ + 1;
                if(collision instanceof MovingPlatform) {
                    if(((MovingPlatform) collision).getDir().x != 0) {
//                        System.out.println("moving horiz...");
                    }

                }
            }
        }
        else
            ground = GROUND;

        if(pos.y < ground) {
            velocity.y += GRAVITY;
        }
        else
            velocity.y = 0;

        if(pos.y >= ground && up != 0)
            velocity.y = -jumpSpeed;

        velocity.x = walkSpeed * (left + right);

        PVector nextPos = new PVector(pos.x, pos.y);
        nextPos.add(velocity);

        float offset = rect.width;

        if (nextPos.x > 0 && nextPos.x < (WIDTH - PLAYER_SZ))
            pos.x = nextPos.x;
        //if (nextPos.y > 0 && nextPos.y < (HEIGHT - offset))
            pos.y = nextPos.y;
        rect.x = (int) pos.x;
        rect.y = (int) pos.y;

    }

    public Rectangle getRect() { return rect; }

    public PVector getPos() { return pos; }

    public void display(PApplet p)
    {
        p.fill(p.color(id * 100 % 255));
        p.noStroke();
        p.rect(pos.x, pos.y, PLAYER_SZ, PLAYER_SZ);
    }

    public String getUsername() { return username; }

    public void setUsername(String username) { this.username = username; }
}
