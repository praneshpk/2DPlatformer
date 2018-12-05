package core.util;

import processing.core.PVector;

public interface Constants
{

    int WIDTH = 800;
    int HEIGHT = 400;
    int PLAYER_SZ = 50;
    int GROUND = HEIGHT - PLAYER_SZ - 1;
    float GRAVITY = .5f;
    int COLLIDABLES = 20;
    int[] MV_PLATORM = {50, 5};
    int TIC = 1;
}
