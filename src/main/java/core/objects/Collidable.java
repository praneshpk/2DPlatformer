package core.objects;

import processing.core.PApplet;

import java.awt.*;
import java.io.Serializable;

public interface Collidable extends Serializable {

    void display(PApplet p, long cycle);
    void update(long cycle);
    void handle(Player p);
    Rectangle getRect();
}
