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
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
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

    protected void initialize(Socket s) throws IOException, ClassNotFoundException
    {
        output = new ObjectOutputStream(s.getOutputStream());
        input = new ObjectInputStream(s.getInputStream());
    }

    protected void IO(Socket s) throws IOException, ClassNotFoundException {
        Event event;
        Player p;
        while(true) {
            // Receive stream data
            System.out.println("Receiving stream data from " + Thread.currentThread().getId());
            event = (Event) input.readObject();

            if(event.type == event_type.REQUEST) {
                // Send back platforms
                output.writeObject(new Event(event.type, platforms));
            }
            if(event.type == event_type.CREATE) {
                // Check if server is full
                if(users.size() == MAX_USERS)
                    output.writeObject(new Event(event_type.ERROR, null));
                else {
                    p = (Player) event.data;

                    // Assign id based on location in list
                    p.id = users.size();
                    users.add(p);
                    System.out.println(p.toString() + s.getLocalAddress() + " joined.");

                    // Send back player object with id
                    output.writeObject(new Event(event.type, p));
                }

            }

            if(event.type == event_type.SEND) {
                p = (Player) event.data;

                // Update player in user list
                synchronized (lock) {
                    users.set(p.id, p);
                    lock.notifyAll();
                }
                // Send back player data
                output.writeObject(event);
            }



        }
    }


    public static void main(String[] args)
    {
        GameServer server = new GameServer();
        server.run();
    }


}
