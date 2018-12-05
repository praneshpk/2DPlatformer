package core.objects;

import core.util.Constants;
import processing.core.PApplet;
import processing.core.PShape;
import processing.core.PVector;

import java.awt.*;
import java.io.Serializable;
import java.util.UUID;

public abstract class Collidable
        implements Constants, Serializable, Comparable<Collidable>
{
    public enum Type
    {
        DEATH_ZONE, MOVING_PLATFORM, STATIC_PLATFORM, PLAYER
    }
    protected UUID id;
    protected Type type;
    protected Color color;
    protected Rectangle rect;
    protected PVector pos;
    protected String path;

    public UUID getId() { return id; }

    public Type getType() { return type; }

    public void setType(Type type) { this.type = type; }

    public Color getColor() { return color; }

    public void setColor(Color color) {
        this.color = color;
    }

    public void setColor(int r, int g, int b) {
        color = new Color(r,g,b);
    }

    public void setRect(Rectangle rect) { this.rect = rect; }

    public PVector getPos() { return pos; }

    public void setPos(PVector pos) { this.pos = pos; }

    public void display(PApplet sketch, float cycle)
    {
        update(cycle);
        if(color != null) {
            sketch.fill(color.getRGB());
            sketch.noStroke();
            sketch.rect(pos.x, pos.y, rect.width, rect.height);
        }
    }

    public void display(PApplet sketch, PShape s, float cycle) {
        update(cycle);
        if(color != null) {
            s.disableStyle();
            s.setFill(color.getRGB());
        }
        sketch.shape(s, pos.x, pos.y, rect.width, rect.height);
    }

    public abstract void update(float cycle);

    public abstract void handle(Collidable p);

    public Rectangle getRect() { return rect; }

    public int compareTo(Collidable o) { return type.compareTo(o.getType()); }

}
