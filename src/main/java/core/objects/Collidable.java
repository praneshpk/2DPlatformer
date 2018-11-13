package core.objects;

import processing.core.PApplet;
import processing.core.PVector;

import java.awt.*;
import java.io.Serializable;

public interface Collidable extends Serializable, Comparable<Collidable>
{
    enum Type
    {
        DEATH_ZONE, MOVING_PLATFORM, STATIC_PLATFORM, PLAYER
    }
    Type type();

    void display(PApplet p, long cycle);

    void update(long cycle);

    void handle(Collidable p);

    Rectangle getRect();
    PVector getPos();
}
