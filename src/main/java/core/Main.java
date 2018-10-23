package core;

import core.network.Client;
import core.network.Server;
import core.objects.Collidable;
import core.objects.Player;
import core.util.Constants;
import core.util.Event;
import core.util.event_type;
import processing.core.PApplet;

import java.awt.*;
import java.net.Socket;
import java.util.*;


/**
 * Class to connect to Server object
 */
public class Main extends PApplet implements Constants {

    private Client client;
    private static Collidable[] platforms;
    private Player player;
    private Event event;
    private ArrayList<Player> users;

    private void renderObjects()
    {

        for(int i = platforms.length - 1; i >= 0; i--)
            platforms[i].display(this, client.getServerTime());
        for(Player p : users)
            p.display(this, 0);
    }

    public void settings()
    {
        size(WIDTH, HEIGHT);
    }
    public void setup()
    {
        // Socket for client to connect to
        Socket s = null;

        // Processing window settings
        smooth();
        noStroke();
        fill(0,255,0);

        // Initialize client
        client = new Client(Server.HOSTNAME, Server.PORT);
        client.start();

        // Get player from client
        player = client.getPlayer();

        // Get platforms from client
        event = client.send(new Event(event_type.REQUEST, "platforms".hashCode()), false);
        if(event.type == event_type.ERROR)
            System.exit(1);
        platforms = (Collidable[]) event.data;

        // Get user list from client
        event = client.send(new Event(event_type.REQUEST, "users".hashCode()), false);
        if(event.type == event_type.ERROR)
            System.exit(1);
        users = (ArrayList) event.data;
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
     *
     * Adapted from
     * https://www.openprocessing.org/sketch/92234
     */
    public void keyPressed()
    {
        if(keyCode == LEFT) {
            player.left = -1;
            player.dir = 1;
        }
        if(keyCode == RIGHT) {
            player.right = 1;
            player.dir = -1;
        }
        if(key == ' ')
            player.up = -1;
    }

    /**
     * Updates player state based on keystrokes
     *
     * Adapted from
     * https://www.openprocessing.org/sketch/92234
     */
    public void keyReleased()
    {
        if(keyCode == LEFT)
            player.left = 0;
        if(keyCode == RIGHT)
            player.right = 0;
        if(key == ' ')
            player.up = 0;
    }
    public void draw() {
        background(255);
        renderObjects();
        player.update(0);
        event = client.send(new Event(event_type.SEND, player), true);
        if(event.type == event_type.SEND)
            users = (ArrayList) event.data;
    }
    public static Collidable collision(Rectangle pRect)
    {
        for(Collidable p: platforms) {
            if (p != null && pRect.intersects(p.getRect()))
                return p;
        }
        return null;

    }
    public static Collidable collision(Rectangle pRect, Collidable[] platforms)
    {
        for(Collidable p: platforms) {
            if (p != null && pRect.intersects(p.getRect()))
                return p;
        }
        return null;

    }
}

