import processing.core.PShape;
import processing.core.PVector;

import java.awt.*;
import java.io.Serializable;

/**
 * Player class responsible for movement
 *
 * Adapted from:
 * https://www.openprocessing.org/sketch/92234
 */
public class Player implements Serializable {
    int id;
    String username;
    PShape sprite;
    Rectangle rect;
    PVector pos, velocity;
    float dir, jumpSpeed, walkSpeed;

}
