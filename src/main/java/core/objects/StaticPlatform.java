package core.objects;

import processing.core.PApplet;
import processing.core.PVector;

import java.awt.*;


public class StaticPlatform implements Collidable {

    protected Rectangle rect;
    protected PApplet parent;
    protected PVector pos;
    protected Color color;

    public StaticPlatform(PApplet p, PVector pos, float w, float h, Color c)
    {
        color = c;
        parent = p;
        this.pos = pos;
        rect = new Rectangle((int) pos.x, (int) pos.y, (int) w, (int) h);
    }

    public Rectangle getRect() { return rect; }

    public PVector getPos() { return pos; }

    public void display()
    {
        update();
        parent.fill(color.getRGB());
        parent.noStroke();
        parent.rect(pos.x, pos.y, rect.width, rect.height);
    }

    public void update() {

    }
}
