package core.objects;

import processing.core.PApplet;
import processing.core.PVector;

import java.awt.*;

public interface Collidable {

    void display(PApplet p);
    void update();
    Rectangle getRect();
    PVector getPos();
}
