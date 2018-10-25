package core.util;

import processing.core.PVector;

public interface Constants {

    int WIDTH = 800;
    int HEIGHT = 400;
    int PLAYER_SZ = 50;
    int GROUND = HEIGHT - PLAYER_SZ - 1;
    float GRAVITY = .5f;

    PVector[] DEATH_ZONES = {
            new PVector(WIDTH/2, GROUND)
    };
    PVector[] SPAWN = {
            new PVector(PLAYER_SZ, PLAYER_SZ*2),
            new PVector(PLAYER_SZ, GROUND),
            new PVector(WIDTH - PLAYER_SZ * 3, PLAYER_SZ*2),
            new PVector(WIDTH - PLAYER_SZ * 3, GROUND)
    };
    int PLATFORMS = 20;
    int COLLIDABLES = PLATFORMS + DEATH_ZONES.length + SPAWN.length;

    int[] MV_PLATORM = {50, 5};


}
