package core.objects;

import processing.core.PApplet;
import processing.core.PVector;

import java.awt.*;
import java.io.Serializable;

public interface Collidable extends Serializable {

    void display(PApplet p);
    void update();
    Rectangle getRect();
    PVector getPos();
}
