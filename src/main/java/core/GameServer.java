package core;

import core.network.Server;
import core.objects.Collidable;
import core.objects.MovingPlatform;
import core.objects.Player;
import core.objects.StaticPlatform;
import processing.core.PVector;

import java.awt.*;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Random;

public class GameServer extends Server<Player> implements GameConstants {

    private static Collidable platforms[] = new Collidable[PLATFORMS];

    /**
     * Sets up a list of randomly positioned and colored
     * shapes around the window
     */
    public GameServer()
    {
        super();
        PVector pos;

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
            } while(collision(c.getRect()) != null);
            platforms[i] = c;
        }
    }

    protected void initialize(Socket s) throws IOException, ClassNotFoundException
    {
        output = new ObjectOutputStream(s.getOutputStream());
        input = new ObjectInputStream(s.getInputStream());

        // Check if server is full and accept player object
        if(users.size() == MAX_USERS)
            throw new IllegalStateException();
        data = (Player) input.readObject();

        // Assign id based on location in list
        data.id = users.size();
        users.add(data);
        System.out.println(data.toString() + s.getLocalAddress() + " joined.");

        // Send back player object with id
        output.writeObject(data);

        // Send platform information
        output.writeObject(platforms);

    }

    protected void IO() throws IOException, ClassNotFoundException {
        while(true) {
            // Receive player data
            data = (Player) input.readObject();

            // Update server user list
            synchronized ( lock ) {
                users.set(data.id, data);
                lock.notifyAll();
            }
            // Perform something. Sending back data object for now
            output.writeObject(data);
        }
    }


    public static void main(String[] args)
    {
        GameServer server = new GameServer();
        server.run();
    }


    public static Collidable collision(Rectangle pRect)
    {
//        System.out.println(pRect.getBounds());
        for(Collidable p: platforms)
            if (p != null && pRect.intersects(p.getRect()))
                return p;
        return null;

    }


}
