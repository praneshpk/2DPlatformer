package core;

import core.network.Client;
import core.network.Server;
import core.objects.Collidable;
import core.objects.Player;
import core.util.Constants;
import core.util.events.Event;
import processing.core.PApplet;

import java.awt.*;
import java.net.Socket;
import java.util.*;

/**
 * Class to connect to Server object
 */
public class Main extends PApplet implements Constants
{
    private static LinkedList<Collidable> platforms;
    //private Player player;
    private Hashtable<UUID, Player> users;
    private Event event;
    protected Event.Type event_type;
    protected Event.Obj event_obj;
    private Client client;
    private int collision = 0;

    private void renderObjects()
    {
        for (Collidable p : platforms)
            p.display(this, client.getTime());

        LinkedList objects;
        for(Player p: users.values()) {
            p.display(this, 0);
        }
    }

    private void updateObjects()
    {
        for(Player p: users.values()) {
            p.update(collision);
            users.replace(p.id, p);
        }
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

        //player = (Player) event.data().get(event_obj.PLAYER);
        users = (Hashtable) event.data().get(event_obj.USERS);
        platforms = (LinkedList) event.data().get(event_obj.COLLIDABLES);

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
        Player player = users.get(client.id());
        switch(keyCode) {
            case LEFT:
                player.left = -1;
                player.dir = 1;
                client.send(event_type.INPUT, player, true);
                break;
            case RIGHT:
                player.right = 1;
                player.dir = -1;
                client.send(event_type.INPUT, player, true);
                break;
            case 32:
                player.up = -1;
                client.send(event_type.INPUT, player, true);
                break;
        }
    }

    /**
     * Updates player state based on keystrokes
     * <p>
     * Adapted from
     * https://www.openprocessing.org/sketch/92234
     */
    public void keyReleased()
    {
        Player player = users.get(client.id());
        switch(keyCode) {
            case LEFT:
                player.left = 0;
                client.send(event_type.INPUT, player, true);
                break;
            case RIGHT:
                player.right = 0;
                client.send(event_type.INPUT, player, true);
                break;
            case 32:
                player.up = 0;
                client.send(event_type.INPUT, player, true);
                break;
        }
    }

    public void draw()
    {
        background(255);
        renderObjects();
        handleEvent(client.receive());
        updateObjects();
    }

    public void handleEvent(Event e)
    {
        if(e != null) {
            collision = 0;
            switch (e.type()) {
                case SPAWN:
                    users = (Hashtable) e.data().get(event_obj.USERS);
                    break;
                case COLLISION:
                    collision = 1;
                case DEATH:
                case INPUT:
                    Player p = (Player) e.data().get(event_obj.PLAYER);
                    users.replace(p.id, p);
                    break;
            }
        }
        //time = (Time) e.data().get(event_obj.TIME);
    }

    public LinkedList<Collidable> collision(Rectangle pRect)
    {
        LinkedList<Collidable> ret = new LinkedList<>();
        for (Collidable p : platforms) {
            if (p != null && pRect.intersects(p.getRect()))
                ret.add(p);
        }
        return ret;

    }

    public static Collidable collision(Rectangle pRect, LinkedList<Collidable> platforms)
    {
        for (Collidable p : platforms) {
            if (p != null && pRect.intersects(p.getRect()))
                return p;
        }
        return null;

    }
}

