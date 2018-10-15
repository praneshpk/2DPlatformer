package core.objects;

import processing.core.PVector;

import java.awt.*;

public interface Collidable {

    void display();
    void update();
    Rectangle getRect();
    PVector getPos();
}
