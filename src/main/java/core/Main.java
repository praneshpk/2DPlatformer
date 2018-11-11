package core;

import core.network.Client;
import core.network.Server;
import core.objects.Collidable;
import core.objects.Player;
import core.util.Constants;
import core.util.events.Event;
import core.util.events.EventHandler;
import core.util.events.EventManager;
import core.util.time.LocalTime;
import processing.core.PApplet;

import java.awt.*;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ConnectException;
import java.net.Socket;
import java.util.*;

/**
 * Class to connect to Server object
 */
public class Main extends PApplet implements Constants
{
    private static Collidable[] platforms;
    private Player player;
    private Hashtable<UUID, Player> users;
    private Event event;
    protected Event.type event_type;
    protected Event.obj event_obj;
    private Client client;
    private long time;

    private void renderObjects()
    {
        for (Collidable p : platforms)
            p.display(this, time);
        for (Player p : users.values())
            p.display(this, 0);
    }
    public void settings()
    {
        size(WIDTH, HEIGHT);
    }

    public void setup()
    {
        // Initialize variables
        Socket s = null;
        client = new Client(Server.HOSTNAME, Server.PORT);
        event = client.start();

        if(event.type() == event_type.ERROR) {
            System.err.println(event.data().get(event_obj.MSG));
            System.exit(1);
        }

        player = (Player) event.data().get(event_obj.PLAYER);
        users = (Hashtable) event.data().get(event_obj.USERS);
        platforms = (Collidable[]) event.data().get(event_obj.PLATFORMS);

        // Processing window settings
        smooth();
        noStroke();
        fill(0, 255, 0);


    }

    /**
     * Run the client program
     */
    public static void main(String[] args)
    {
        // Initialize PApplet
        PApplet.main("core.Main", args);
    }

    /**
     * Updates player state based on keystrokes
     * <p>
     * Adapted from
     * https://www.openprocessing.org/sketch/92234
     */
    public void keyPressed()
    {
        if (keyCode == LEFT) {
            player.left = -1;
            player.dir = 1;
        }
        if (keyCode == RIGHT) {
            player.right = 1;
            player.dir = -1;
        }
        if (key == ' ')
            player.up = -1;

        client.send(Event.type.INPUT, player, true);
        event = client.receive();
        System.err.println("Received " + event + " from server ");
        handleEvent(event);
    }

    /**
     * Updates player state based on keystrokes
     * <p>
     * Adapted from
     * https://www.openprocessing.org/sketch/92234
     */
    public void keyReleased()
    {
        if (keyCode == LEFT)
            player.left = 0;
        if (keyCode == RIGHT)
            player.right = 0;
        if (key == ' ')
            player.up = 0;

        client.send(Event.type.INPUT, player, true);
        event = client.receive();
        System.err.println("Received " + event + " from server ");
        handleEvent(event);
    }

    public void draw()
    {
        background(255);
        renderObjects();
        player.update(0);



    }

    public void handleEvent(Event e)
    {
        switch(e.type()) {
            case COLLISION:
                break;
            case SPAWN:
                users = (Hashtable) e.data().get(event_obj.USERS);
                break;
            case DEATH:
            case INPUT:
                Player p = (Player) e.data().get(event_obj.PLAYER);
                users.replace(p.id, p);
                break;
        }
        //time = (Time) e.data().get(event_obj.TIME);
    }

    public static Collidable collision(Rectangle pRect)
    {
        for (Collidable p : platforms) {
            if (p != null && pRect.intersects(p.getRect()))
                return p;
        }
        return null;

    }

    public static Collidable collision(Rectangle pRect, Collidable[] platforms)
    {
        for (Collidable p : platforms) {
            if (p != null && pRect.intersects(p.getRect()))
                return p;
        }
        return null;

    }
}

