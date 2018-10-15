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

        platforms[0] = new StaticPlatform(this,
                new PVector(100, HEIGHT - 35), 200, 35, new Color(120));
        platforms[1] = new MovingPlatform(this,
                new PVector(WIDTH/2,HEIGHT/2),
                new PVector(-5,0));
        platforms[2] = new MovingPlatform(this,
                new PVector(MV_PLATORM[0] * 4,HEIGHT/2),
                new PVector(5,0));
        platforms[3] = new MovingPlatform(this,
                new PVector(MV_PLATORM[0],HEIGHT -50 ),
                new PVector(0,10));
        platforms[4] = new MovingPlatform(this,
                new PVector(WIDTH - 200,HEIGHT - 50),
                new PVector(0,10));
        // Random static platforms
        for(int i = 5; i < PLATFORMS; i++) {
            Collidable c;
            do {
                int r = (int) (random(20, 50));
                pos = new PVector((int) random(0, WIDTH),
                        (int) (random(0, HEIGHT)));
                c = new StaticPlatform(this, pos,
                        r, r,
                        new Color((int) (Math.random() * 0x1000000)));
            } while(collision(c.getRect()) != null);
            platforms[i] = c;
        }
    }
    private void renderObjects()
    {
        for(int i = platforms.length - 1; i >= 0; i--)
            platforms[i].display();
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
            if (p != null && pRect.intersects(p.getRect()))
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
