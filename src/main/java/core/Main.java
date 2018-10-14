package core;

import core.objects.Collidable;
import core.objects.MovingPlatform;
import core.objects.Player;
import core.objects.StaticPlatform;
import processing.core.PApplet;
import processing.core.PShape;
import processing.core.PVector;

import java.awt.*;

public class Main extends PApplet implements GameConstants {

    private Player player;

    private float w = WIDTH/(PLATFORMS/2);
    private float h = random(HEIGHT/10, HEIGHT/5);

    private static Collidable platforms[] = new Collidable[PLATFORMS];

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
        PVector pos;
        // Static platforms
        for(int i = 0; i < PLATFORMS - 4; i++) {
            pos = new PVector((int)(w + random(0, WIDTH-w)),
                    (int)(HEIGHT-w - random(0, HEIGHT-h)));
            platforms[i] = new StaticPlatform(this, pos,
                    w, w, new Color((int)(Math.random() * 0x1000000)));
        }
        platforms[PLATFORMS - 4] = new MovingPlatform(this,
                new PVector(WIDTH/2,HEIGHT/2),
                new PVector(-5,0));
        platforms[PLATFORMS - 3] = new MovingPlatform(this,
                new PVector(WIDTH/2,HEIGHT/2),
                new PVector(5,0));
        platforms[PLATFORMS - 2] = new MovingPlatform(this,
                new PVector(WIDTH/2,HEIGHT/2),
                new PVector(0,5));
        platforms[PLATFORMS - 1] = new MovingPlatform(this,
                new PVector(WIDTH/2,HEIGHT/2),
                new PVector(0,-5));
    }
    private void renderObjects()
    {
        for(Collidable p: platforms)
            p.display();
        player.display();
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

    public static Collidable collision(Rectangle pRect)
    {
//        System.out.println(pRect.getBounds());
        for(Collidable p: platforms)
            if (pRect.intersects(p.getRect()))
                return p;
        return null;

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
