package core;

import core.network.Server;
import core.objects.Collidable;
import core.objects.MovingPlatform;
import core.objects.Player;
import core.objects.StaticPlatform;
import core.util.*;
import core.util.Event;
import processing.core.PVector;

import java.awt.*;
import java.io.IOException;
import java.net.Socket;
import java.util.Random;

public class GameServer extends Server<Player> implements GameConstants {

    private static volatile Collidable platforms[] = new Collidable[PLATFORMS];
    public static volatile long start;

    /**
     * Sets up a list of randomly positioned and colored
     * shapes around the window
     */
    public GameServer()
    {
        super();
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

    protected void IO(Socket s)
    {
        System.out.println("New thread " +Thread.currentThread().getId());
        Player p;
        Event event;
        while(true) {
            synchronized(input) {
                try {
                    event = (Event) input.readObject();
                    // Receive event data
                    System.out.println("Received " + event + " from thread " + Thread.currentThread().getId());
                } catch (Exception e) {
                    e.printStackTrace();
                    break;
                }
            }
            // Platforms request
            if (event.type == event_type.REQUEST) {
                event = new Event(event.type, platforms);
            }

            // Create new player
            if (event.type == event_type.CREATE) {
                // Check if server is full
                if (users.size() == MAX_USERS)
                    event = new Event(event_type.ERROR, null);
                else {
                    p = (Player) event.data;

                    // Assign id based on location in list
                    p.id = users.size();
                    users.add(p);
                    System.out.println(p.toString() + s.getLocalAddress() + " joined.");

                    // Send back player object with id
                    event = new Event(event.type, p);
                }
            }
            // Update player
            if (event.type == event_type.SEND) {
                p = (Player) event.data;

                // Update player in user list
                synchronized (users) {
                    users.set(p.id, p);
                    users.notifyAll();
                }
            }
            synchronized (output) {
                try {
                    output.writeObject(event);
                } catch (IOException e) {
                    e.printStackTrace();
                    break;
                }
            }
            //}
        }
    }


    public static void main(String[] args)
    {
        GameServer server = new GameServer();
        server.run();
    }


}
