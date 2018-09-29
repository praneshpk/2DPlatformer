import org.w3c.dom.css.Rect;
import processing.core.PApplet;
import processing.core.PShape;
import processing.core.PVector;

import java.awt.*;

public class MainClass extends PApplet {

    private static final int SHAPES = 20;
    private static final int WIDTH = 600;
    private static final int HEIGHT = 300;
    private static final int FR = 6;
    private static final int PLAYER_SZ = 50;
    private static final int GROUND = HEIGHT - (int)(PLAYER_SZ*1.1);
    private static final double GRAVITY = .5;
    private Player player = new Player();

    private float w = WIDTH/(SHAPES/2);
    private float h = random(HEIGHT/10, HEIGHT/5);

//    private int xPos = 0;
//    private int yPos = HEIGHT - (int)w - PLAYER;
    private float left, right, up;


    private PShape shapes[] = new PShape[SHAPES];
    private Rectangle rect[] = new Rectangle[SHAPES];

    public static void main(String[] args)
    {
        PApplet.main("MainClass", args);
    }

    /**
     * Sets up a list of randomly positioned and colored
     * shapes around the window
     */
    private void setupObjects()
    {
        // random squares
        for(int i = 0; i < SHAPES / 2; i++) {
            int x = (int)(w + random(0, WIDTH-w));
            int y = (int)(HEIGHT-w - random(0, HEIGHT-h));
            shapes[i] = createShape(RECT, x, y, w, w);
            shapes[i].setFill(color(random(255),random(255),random(255)));
            rect[i] = new Rectangle(x, y,(int)w,(int)w);
            shape(shapes[i]);
        }
        // random rectangles
        for(int i = SHAPES/2; i < SHAPES; i++ ) {
            int x = (int)(w + random(0, WIDTH-w));
            int y = (int)(HEIGHT-w - random(0, HEIGHT-h));
            shapes[i] = createShape(RECT, x, y, h, w/2);
            shapes[i].setFill(color(random(255),random(255),random(255)));
            rect[i] = new Rectangle(x, y,(int)h,(int)w/2);
            shape(shapes[i]);
        }
    }
    private void renderObjects()
    {
        for(PShape s: shapes)
            shape(s);
        shape(player.sprite, player.pos.x, player.pos.y);
    }

    private boolean collision(Rectangle pRect)
    {
        for(int i = 0; i < rect.length; i++ )
            if (pRect.intersects(rect[i]))
                return true;
        return false;
    }
    public void settings()
    {
        size(WIDTH, HEIGHT);
    }
    public void setup()
    {
        player.pos = new PVector(0, 0);
        player.sprite = createShape(RECT, player.pos.x, player.pos.y, PLAYER_SZ, PLAYER_SZ);
        player.sprite.setFill(color(120));
        player.sprite.setStroke(false);
        player.rect = new Rectangle((int)player.pos.x, (int)player.pos.y, PLAYER_SZ, PLAYER_SZ);
        player.dir = 1;
        player.velocity = new PVector(0, 0);
        player.jumpSpeed = 6;
        player.walkSpeed = 3;

        smooth();
        noStroke();
        fill(0,255,0);
        setupObjects();
    }

    /**
     * Updates the player's current state
     *
     * Adapted from
     * https://www.openprocessing.org/sketch/92234
     */
    private void updatePlayer()
    {
        if(player.pos.y < GROUND)
            player.velocity.y += GRAVITY;
        else
            player.velocity.y = 0;

        if(player.pos.y >= GROUND && up != 0)
            player.velocity.y = -player.jumpSpeed;

        player.velocity.x = player.walkSpeed * (left + right);

        PVector nextPos = new PVector(player.pos.x, player.pos.y);
        nextPos.add(player.velocity);

        float offset = player.rect.width;

        if(collision(new Rectangle((int) nextPos.x, (int) nextPos.y, PLAYER_SZ, PLAYER_SZ))) {
            println("Collision detected");
        }
        if (nextPos.x > 0 && nextPos.x < (width - offset))
            player.pos.x = nextPos.x;
        if (nextPos.y > 0 && nextPos.y < (height - offset))
            player.pos.y = nextPos.y;
        player.rect.x = (int) player.pos.x;
        player.rect.y = (int) player.pos.y;

    }

    /**
     * Updates player state based on keystrokes
     *
     * Adapted from
     * https://www.openprocessing.org/sketch/92234
     */
    public void keyPressed()
    {
        if(keyCode == LEFT) {
            left = -1;
            player.dir = 1;
        }
        if(keyCode == RIGHT) {
            right = 1;
            player.dir = -1;
        }
        if(key == ' ')
            up = -1;

    }

    /**
     * Updates player state based on keystrokes
     *
     * Adapted from
     * https://www.openprocessing.org/sketch/92234
     */
    public void keyReleased()
    {
        if(keyCode == LEFT)
            left = 0;
        if(keyCode == RIGHT)
            right = 0;
        if(key == ' ')
            up = 0;
    }
    public void draw()
    {
        background(255);
        renderObjects();
        updatePlayer();

//        if (collision()) {
//            if(up)
//                yPos+=FR;
//            if(keyCode == LEFT)
//                xPos += FR;
//            if(keyCode == RIGHT)
//                xPos-=FR;
//
//        }
    }
}
