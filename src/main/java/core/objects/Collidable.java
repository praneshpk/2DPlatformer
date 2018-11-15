package core.objects;

import core.util.Constants;
import processing.core.PApplet;
import processing.core.PVector;

import java.awt.*;
import java.io.Serializable;

public abstract class Collidable
        implements Constants, Serializable, Comparable<Collidable>
{
    protected enum Type
    {
        DEATH_ZONE, MOVING_PLATFORM, STATIC_PLATFORM, PLAYER
    }
    Type type;
    Color color;
    Rectangle rect;
    PVector pos;

    Type type() { return type; }

    public void display(PApplet p, float cycle)
    {
        update(cycle);
        p.fill(color.getRGB());
        p.noStroke();
        p.rect(pos.x, pos.y, rect.width, rect.height);
    }

    public abstract void update(float cycle);

    public abstract void handle(Collidable p);

    public Rectangle getRect() { return rect; }

    public int compareTo(Collidable o) { return type.compareTo(o.type()); }

}
