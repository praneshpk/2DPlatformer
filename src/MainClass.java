import processing.core.PApplet;
import processing.core.PShape;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.PathIterator;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

public class MainClass extends PApplet {
    private static final int SHAPES = 20;
    private static final int WIDTH = 600;
    private static final int HEIGHT = 300;

    private float w = WIDTH/(SHAPES/2);
    private float h = random(HEIGHT/10, HEIGHT/5);

    private Rectangle player;
    private int xPos = 0;
    private int yPos = HEIGHT - (int)w*2;
    private int dir = 0;

    private PShape shapes[] = new PShape[SHAPES];
    private Rectangle rect[] = new Rectangle[SHAPES];

    public static void main(String[] args)
    {
        PApplet.main("MainClass", args);
    }
    public void settings()
    {
        size(WIDTH,HEIGHT);
    }

    private void setupObjects()
    {
        // random squares
        for(int i = 0; i < SHAPES / 2; i++) {
            shapes[i] = createShape(RECT, (WIDTH/(SHAPES/2))*i, HEIGHT-w, w, w);
            shapes[i].setFill(color(random(255),random(255),random(255)));
            rect[i] = new Rectangle((WIDTH/(SHAPES/2))*i,(int)(HEIGHT-w),(int)w,(int)w);
            shape(shapes[i]);
        }
        // random rectangles
        for(int i = SHAPES/2; i < SHAPES; i++ ) {
            shapes[i] = createShape(RECT, w + random(0, WIDTH-w), HEIGHT-w - random(0, HEIGHT-h), h, w/2);
            shapes[i].setFill(color(random(255),random(255),random(255)));
            rect[i] = new Rectangle((int)(w + random(0, WIDTH-w)),(int)(HEIGHT-w - random(0, HEIGHT-h)),(int)h,(int)w/2);
            shape(shapes[i]);
        }
        for(Rectangle r : rect)
            System.out.println(r);
    }
    private void renderObjects()
    {
        for(PShape s: shapes)
            shape(s);
        //player square
        fill(120);
        rect(xPos, yPos, 50, 50);
        player = new Rectangle(xPos, yPos, 50, 50);
    }
    private void jump()
    {
        System.out.println("JUMP");
    }
    private int collision()
    {
        for(int i = 0; i < rect.length; i++ ) {
            if (player.intersects(rect[i]))
                return i;
        }
        return -1;
    }
    public void setup()
    {
        size(WIDTH, HEIGHT);
        smooth();
        noStroke();
        fill(0,255,0);
        setupObjects();
    }

    public void draw()
    {
        if(keyPressed) {
            switch(keyCode) {
                case LEFT:
                    xPos --;
                    //yPos = dir*yPos;
                    break;
                case RIGHT:
                    xPos ++;
                    //yPos = dir*yPos;
                    break;
                default:
                    if(key == 32) {
                        dir = 1;
                    }
                    else
                        System.out.println((int)key + " pressed");
                    break;
            }
        }
        background(255);
        renderObjects();

        int s = collision();
        if(s != -1) {
            System.out.println("collision b/w player and " + shapes[s]);
        }
    }
}
