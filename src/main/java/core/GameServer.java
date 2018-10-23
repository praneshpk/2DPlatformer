package core;

import core.network.Server;
import core.objects.Collidable;
import core.objects.MovingPlatform;
import core.objects.StaticPlatform;
import processing.core.PVector;

import java.awt.*;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Hashtable;
import java.util.Random;

public class GameServer extends Server implements GameConstants {

    private ServerSocket server;
    public static volatile long start;

    /**
     * Sets up a list of randomly positioned and colored
     * shapes around the window
     */
    public GameServer()
    {
        platforms = new Collidable[PLATFORMS];
        users = new Hashtable<>();
        PVector pos;
        start = System.currentTimeMillis();

        platforms[0] = new StaticPlatform(new PVector(100, HEIGHT - 35),
                200, 35, new Color(120));
        platforms[1] = new MovingPlatform(new PVector(WIDTH/2,HEIGHT/2),
                new PVector(-5,0));
        platforms[2] = new MovingPlatform(new PVector(MV_PLATORM[0] * 4,HEIGHT/2),
                new PVector(5,0));
        platforms[3] = new MovingPlatform(new PVector(MV_PLATORM[0],HEIGHT -50 ),
                new PVector(0,10));
        platforms[4] = new MovingPlatform(new PVector(WIDTH - 200,HEIGHT - 50),
                new PVector(0,10));

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

    public void run()
    {
        try {
            server = new ServerSocket(PORT);
        } catch( Exception e ) {
            System.err.println("Can't initialize server: " + e);
            System.exit(1);
        }
        System.out.println("Server started on " + server.getLocalSocketAddress());

        try {
            while(true) {
                Socket s = server.accept();
                NetworkThread t = new NetworkThread(s);
                t.start();
            }
        } catch ( Exception e ){
            System.err.println("Error accepting client " + e);
        } finally {

            try {
                server.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args)
    {
        GameServer server = new GameServer();
        server.run();
    }


}
