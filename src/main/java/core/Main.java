package core;

import core.network.Client;
import core.network.Server;
import core.objects.Collidable;
import core.objects.DeathZone;
import core.objects.Player;
import core.util.Constants;
import core.util.events.Event;
import processing.core.PApplet;
import java.net.Socket;
import java.util.*;

/**
 * Class to connect to Server object
 */
public class Main extends PApplet implements Constants
{
    private static LinkedList<Collidable> platforms;
    private Hashtable<UUID, Player> users;
    private Event event;
    private Event.Type event_type;
    private Event.Obj event_obj;
    private Client client;
    private boolean pause;

    private void renderObjects()
    {
        for (Collidable p : platforms)
            p.display(this, client.getTime());
        for(Player p: users.values()) {
            p.display(this, 0);
        }
    }

    private void updateObjects()
    {
        for(Player p: users.values()) {
            collision(p);
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
        pause = false;
        client = new Client(Server.HOSTNAME, Server.PORT);
        event = client.start();

        if(event.type() == event_type.ERROR) {
            System.err.println(event.data().get(event_obj.MSG));
            System.exit(1);
        }

        platforms = (LinkedList) event.data().get(event_obj.COLLIDABLES);
        users = (Hashtable) event.data().get(event_obj.USERS);

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
                client.send(event_type.INPUT, true, player, users);
                break;
            case RIGHT:
                player.right = 1;
                player.dir = -1;
                client.send(event_type.INPUT, true, player, users);
                break;
            case 32:
                player.up = -1;
                client.send(event_type.INPUT, true, player, users);
                break;
            case 'P':
                pause = !pause;
                client.pause(pause);
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
                client.send(event_type.INPUT, true, player, users);
                break;
            case RIGHT:
                player.right = 0;
                client.send(event_type.INPUT, true, player, users);
                break;
            case 32:
                player.up = 0;
                client.send(event_type.INPUT, true, player, users);
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

    public void exit()
    {
        client.close();
        super.exit();
    }

    public void handleEvent(Event e)
    {
        if(e != null) {
            Player p;
            switch (e.type()) {
                case COLLISION:
                case DEATH:
                    break;
                case INPUT:
                    users = (Hashtable) e.data().get(event_obj.USERS);
                    break;
                case SPAWN:
                    p = (Player) e.data().get(event_obj.PLAYER);
                    users.put(p.id, p);
                    break;
                case LEAVE:
                    users.remove(e.data().get(event_obj.ID));
                    break;
            }
        }
        //time = (Time) e.data().get(event_obj.TIME);
    }

    public void collision(Player player)
    {
        PriorityQueue<Collidable> objects = new PriorityQueue<>();
        for (Collidable p : platforms) {
            if(p != null) {
                if(player.getRect().intersects(p.getRect())) {
                    if(p instanceof DeathZone) {
                        p.handle(player);
                        client.send(event_type.DEATH, false, player, users);
                        return;
                    }
                    objects.add(p);
                }
            }
        }

        if(!objects.isEmpty()) {
            Collidable c = objects.poll();
            c.handle(player);
            player.update(1);
            if(!c.equals(player.collide)) {
                player.collide = c;
                client.send(event_type.COLLISION, true, player, users);
            }
        } else {
            player.update(0);
        }

    }
}

