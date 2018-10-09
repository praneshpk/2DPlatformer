package core;

public interface GameConstants {
    int SHAPES = 20;
    int WIDTH = 600;
    int HEIGHT = 300;
    int PLAYER_SZ = 50;
    int GROUND = HEIGHT - (int)(PLAYER_SZ*1.1);
    float GRAVITY = .5f;
}
