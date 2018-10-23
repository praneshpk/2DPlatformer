package core;

import core.objects.Collidable;
import core.objects.MovingPlatform;
import core.objects.StaticPlatform;
import core.util.Constants;
import processing.core.PVector;

import java.awt.*;
import java.util.Random;

public class Server extends core.network.Server implements Constants {

    /**
     * Sets up a list of randomly positioned and colored
     * shapes around the window
     */
    public Server()
    {
        super();
        platforms = new Collidable[PLATFORMS];
        PVector pos;

        platforms[0] = new StaticPlatform(new PVector(100, HEIGHT - 35),
                200, 35, new Color(120));
        platforms[1] = new MovingPlatform(new PVector(WIDTH/2,HEIGHT/2),
                new PVector(-35,0));
        platforms[2] = new MovingPlatform(new PVector(MV_PLATORM[0] * 4,HEIGHT/2),
                new PVector(35,0));
        platforms[3] = new MovingPlatform(new PVector(MV_PLATORM[0],HEIGHT -50 ),
                new PVector(0,50));
        platforms[4] = new MovingPlatform(new PVector(WIDTH - 200,HEIGHT - 50),
                new PVector(0,50));

        // Random static platforms
        for(int i = 5; i < PLATFORMS; i++) {
            Collidable c;
            Random random = new Random();
            do {
                int r = random.nextInt(30) + 20;
                pos = new PVector(random.nextInt(WIDTH), random.nextInt(HEIGHT));
                c = new StaticPlatform(pos, r, r,
                        new Color((int) (Math.random() * 0x1000000)));
            } while(Main.collision(c.getRect(), platforms) != null);
            platforms[i] = c;
        }
    }

    public static void main(String[] args)
    {
        Server server = new Server();
        server.listen();
    }


}
