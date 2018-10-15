package core;

public interface GameConstants {
    int PLATFORMS = 20;
    int WIDTH = 600;
    int HEIGHT = 300;
    int PLAYER_SZ = 50;
    int GROUND = HEIGHT - PLAYER_SZ - 1;
    float GRAVITY = .5f;
    int[] MV_PLATORM = {50, 5};
}
