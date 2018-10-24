package core;

import core.objects.Collidable;
import core.objects.DeathZone;
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
        platforms = new Collidable[COLLIDABLES];
        PVector pos;

        // Death zones
        for(int i = 0; i < DEATH_ZONES.length; i++ )
            platforms[i] = new DeathZone(DEATH_ZONES[i],
                    100, 100);

        // Spawn-friendly platforms
        for(int i = DEATH_ZONES.length; i < COLLIDABLES - PLATFORMS; i++ )
            platforms[i] = new StaticPlatform(SPAWN[i - DEATH_ZONES.length],
                    100, 8, new Color(120));

        // Random static platforms
        for(int i = COLLIDABLES - PLATFORMS; i < COLLIDABLES - 4; i++) {
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

        platforms[COLLIDABLES - 4] = new MovingPlatform(new PVector(WIDTH/2,HEIGHT/3),
                new PVector(-35,0));
        platforms[COLLIDABLES - 3] = new MovingPlatform(new PVector(MV_PLATORM[0] * 4,HEIGHT/2),
                new PVector(35,0));
        platforms[COLLIDABLES - 2] = new MovingPlatform(new PVector(MV_PLATORM[0],HEIGHT -50 ),
                new PVector(0,50));
        platforms[COLLIDABLES - 1] = new MovingPlatform(new PVector(WIDTH - 200,HEIGHT - 50),
                new PVector(0,50));




    }

    public static void main(String[] args)
    {
        Server server = new Server();
        server.listen();
    }


}
