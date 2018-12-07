package core;

import core.objects.*;
import core.util.Constants;
import processing.core.PVector;

import java.awt.*;
import java.util.LinkedList;
import java.util.Random;

public class Server extends core.network.Server implements Constants
{

    private static PVector[] deathZones = null;
    private static PVector[] spawnPoints = null;

    /**
     * Sets up a list of randomly positioned and colored
     * shapes around the window
     */
    public Server()
    {
        super();

        deathZones = new PVector[]{
                new PVector(WIDTH / 2, GROUND)
        };
        spawnPoints = new PVector[]{
                new PVector(PLAYER_SZ, PLAYER_SZ * 2),
                new PVector(PLAYER_SZ, GROUND),
                new PVector(WIDTH - PLAYER_SZ * 3, PLAYER_SZ * 2),
                new PVector(WIDTH - PLAYER_SZ * 3, GROUND)
        };

        PVector pos;

        // Death zones
        for(PVector p : deathZones)
            platforms.add(new DeathZone(p, PLAYER_SZ, PLAYER_SZ));

        // Spawn-friendly platforms
        for (int i = 0; i < 4; i++)
            platforms.add(new StaticPlatform(spawnPoints[i],
                    100, 8, new Color(120)));

        // Random static platforms
        for (int i = 0; i < COLLIDABLES - 4; i++) {
            Collidable c;
            Random random = new Random();
            do {
                int r = random.nextInt(30) + 20;
                pos = new PVector(random.nextInt(WIDTH), random.nextInt(HEIGHT));
                c = new StaticPlatform(pos, r, r,
                        new Color((int) (Math.random() * 0x1000000)));
            } while (collision(c.getRect(), platforms));
            platforms.add(c);
        }

        platforms.add(new MovingPlatform(new PVector(WIDTH / 2, HEIGHT / 3),
                new PVector(-35 * TIC, 0)));
        platforms.add(new MovingPlatform(new PVector(MV_PLATORM[0] * 4, HEIGHT / 2),
                new PVector(35 * TIC, 0)));
        platforms.add(new MovingPlatform(new PVector(MV_PLATORM[0], HEIGHT - 50),
                new PVector(0, 50 * TIC)));
        platforms.add(new MovingPlatform(new PVector(WIDTH - 200, HEIGHT - 50),
                new PVector(0, 50 * TIC)));
    }

    public Server(LinkedList<Collidable> platforms)
    {
        super();
        this.platforms = platforms;
    }

    public boolean collision(Rectangle pRect, LinkedList<Collidable> platforms)
    {
        for (Collidable p : platforms)
            if(p != null && pRect.intersects(p.getRect()))
                return true;
        return false;
    }

    public static PVector[] getDeathZones() { return deathZones; }

    public static PVector[] getSpawnPoints() { return spawnPoints; }

    public static void main(String[] args)
    {
        Server server = new Server();
        try {
            server.listen();
        } catch(Exception e) {
            System.err.println("Can't initialize server: " + e);
        }
    }


}
