package core;

import core.objects.*;
import core.util.Constants;
import processing.core.PVector;

import java.awt.*;
import java.util.LinkedList;
import java.util.Random;

public class Server extends core.network.Server implements Constants
{

    /**
     * Sets up a list of randomly positioned and colored
     * shapes around the window
     */
    public Server()
    {
        super();
        platforms = new LinkedList<>();
        PVector pos;

        // Death zones
        for(PVector p : DEATH_ZONES)
            platforms.add(new DeathZone(p, PLAYER_SZ, PLAYER_SZ));

        // Spawn-friendly platforms
        for (int i = 0; i < 4; i++)
            platforms.add(new StaticPlatform(SPAWN[i],
                    100, 8, new Color(120)));

        // Random static platforms
        for (int i = 0; i < PLATFORMS - 4; i++) {
            Collidable c;
            Random random = new Random();
            do {
                int r = random.nextInt(30) + 20;
                pos = new PVector(random.nextInt(WIDTH), random.nextInt(HEIGHT));
                c = new StaticPlatform(pos, r, r,
                        new Color((int) (Math.random() * 0x1000000)));
            } while (Main.collision(c.getRect(), platforms) != null);
            platforms.add(c);
        }

        platforms.add(new MovingPlatform(new PVector(WIDTH / 2, HEIGHT / 3),
                new PVector(-35, 0)));
        platforms.add(new MovingPlatform(new PVector(MV_PLATORM[0] * 4, HEIGHT / 2),
                new PVector(35, 0)));
        platforms.add(new MovingPlatform(new PVector(MV_PLATORM[0], HEIGHT - 50),
                new PVector(0, 50)));
        platforms.add(new MovingPlatform(new PVector(WIDTH - 200, HEIGHT - 50),
                new PVector(0, 50)));


    }



    public static void main(String[] args)
    {
        Server server = new Server();
        server.listen();
    }


}
