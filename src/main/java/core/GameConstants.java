package core;

public interface GameConstants {
    int PLATFORMS = 10;
    int WIDTH = 600;
    int HEIGHT = 300;
    int PLAYER_SZ = 50;
    int GROUND = HEIGHT - (int)(PLAYER_SZ*1.1);
    float GRAVITY = .5f;
    int[] MV_PLATORM = {50, 5};
}
