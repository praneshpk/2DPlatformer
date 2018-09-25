import processing.core.PApplet;
import processing.core.PShape;

import java.awt.*;

public class MainClass extends PApplet {
    private static final int SHAPES = 16;
    private static final int WIDTH = 400;
    private static final int HEIGHT = 400;
    private static final float MARGIN = .15f;

    private PShape shapes[] = new PShape[SHAPES];

    public static void main(String[] args)
    {
        PApplet.main("MainClass", args);
    }
    public void settings()
    {
        size(WIDTH,HEIGHT);
    }

    public void setup()
    {
        // random squares
        for(int i = 0; i < SHAPES / 2; i++) {

            float r = random(width/10, width/6);
            shapes[i] = createShape(RECT, random(width*(1 - MARGIN)), random(height*(1 - MARGIN)), r, r);
            shapes[i].setFill(color(random(255),random(255),random(255)));
        }

        // random rectangles
        for(int i = SHAPES/2; i < SHAPES; i++ ) {
            shapes[i] = createShape(RECT, random(width*(1 - MARGIN)), random(height*(1 - MARGIN)), random(width/10, width/5), random(width/10, width/5));
            shapes[i].setFill(color(random(255),random(255),random(255)));
        }

    }

    public void draw()
    {
        for(int i = 0; i < SHAPES; i++)
            shape(shapes[i]);

    }
}
