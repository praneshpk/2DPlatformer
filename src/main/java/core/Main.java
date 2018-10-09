package core;

import core.objects.Player;
import processing.core.PApplet;
import processing.core.PShape;

import java.awt.*;

public class Main extends PApplet implements GameConstants {


    private Player player;

    private float w = WIDTH/(SHAPES/2);
    private float h = random(HEIGHT/10, HEIGHT/5);



    private static PShape shapes[] = new PShape[SHAPES];
    private static Rectangle rect[] = new Rectangle[SHAPES];

    public static void main(String[] args)
    {
        PApplet.main("core.Main", args);
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
        player.display();
    }

    public static boolean collision(Rectangle pRect)
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
        player = new Player(this);

        smooth();
        noStroke();
        fill(0,255,0);
        setupObjects();
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
            player.left = -1;
            player.dir = 1;
        }
        if(keyCode == RIGHT) {
            player.right = 1;
            player.dir = -1;
        }
        if(key == ' ')
            player.up = -1;

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
            player.left = 0;
        if(keyCode == RIGHT)
            player.right = 0;
        if(key == ' ')
            player.up = 0;
    }
    public void draw()
    {
        background(255);
        renderObjects();
        player.update();
    }
}
