package core;

import core.network.Client;
import core.network.Server;
import core.objects.Collidable;
import core.objects.DeathZone;
import core.objects.Player;
import core.util.Constants;
import core.util.events.Event;
import processing.core.PApplet;
import java.util.*;

/**
 * Class to connect to Server object
 */
public class Main extends PApplet implements Constants
{
    private Event event;
    private Event.Type event_type;
    private Event.Obj event_obj;
    public static Client client;
    private boolean pause = false, mem = false;
    private long wait, offset = 0;
    private Event start;
    private static int bg = 255;

    private void renderObjects()
    {
        if(client.recording)
            background(255,245,245);
        else if(client.replay)
            background(245);
        else
            background(bg);
        for (Collidable p : client.platforms())
            p.display(this, client.time().getTime() - offset);
        for(Player p: client.users.values()) {
            p.display(this, client.time().getTic());
        }
    }

    private void updateObjects()
    {
        if(!pause)
            for(Player p: client.users.values()) {
                collision(p);
                client.users.replace(p.id, p);
            }
    }
    public void settings()
    {
        size(WIDTH, HEIGHT);
    }

    public void setup()
    {
        // Initialize variables
        client = new Client(Server.HOSTNAME, Server.PORT);
        client.start();

        // Processing window settings
        smooth();
        noStroke();
        fill(0, bg, 0);
    }

    /**
     * Run the client program
     */
    public static void main(String[] args)
    {
        if(args.length > 0)
            bg = parseInt(args[0]);
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
        Player player = client.users.get(client.id());
        if(!client.replay) {
            switch (keyCode) {
                case LEFT:
                    player.left = -1;
                    player.dir = 1;
                    client.send(event_type.INPUT, true, player, client.users);
                    break;
                case RIGHT:
                    player.right = 1;
                    player.dir = -1;
                    client.send(event_type.INPUT, true, player, client.users);
                    break;
                case 32:
                    player.up = -1;
                    client.send(event_type.INPUT, true, player, client.users);
                    break;
                case 'R':
                    mem = true;
                    if(!client.recording) {
                        client.send(event_type.START_REC, true, player, client.users);
                    } else {
                        client.send(event_type.STOP_REC, true, player, client.users);
                        while (true) {
                            Event e = client.receive();
                            renderObjects();
                            handleEvent(e);
                            updateObjects();
                            if (e != null && e.type() == event_type.STOP_REC) {
                                break;
                            }
                        }
                    }
                    client.record();

                    break;
            }
        } else {
            switch (keyCode) {
                case 32:
                    pause = !pause;
                    client.pause(pause);
                    break;
                case 'Q':
                    client.setReplay(false, 0);
                    offset = 0;
                    Event e = client.receive();
                    while (e != null) {
                        background(255);
                        renderObjects();
                        handleEvent(e);
                        updateObjects();
                        e = client.receive();
                    }
                    break;

            }
        }
        switch(keyCode) {
            case 49:
                client.setReplay(mem, 1);
                break;
            case 50:
                client.setReplay(mem, .5f);
                break;
            case 51:
                client.setReplay(mem, 2);
                break;
        }
        if(keyCode >= 49 && keyCode <=51) {
            start = client.receive();
            wait = (long) start.data().get(event_obj.TIMESTAMP);
            offset = client.time().getTime() - wait;
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
        if(!client.replay) {
            Player player = client.users.get(client.id());
            switch (keyCode) {
                case LEFT:
                    player.left = 0;
                    client.send(event_type.INPUT, true, player, client.users);
                    break;
                case RIGHT:
                    player.right = 0;
                    client.send(event_type.INPUT, true, player, client.users);
                    break;
                case 32:
                    player.up = 0;
                    client.send(event_type.INPUT, true, player, client.users);
                    break;
            }
        }
    }

    public void draw()
    {
        renderObjects();
        if(client.replay) {
            if(client.time().getTime() - offset >=
                    (long) start.data().get(event_obj.TIMESTAMP)) {
                handleEvent(start);
                start = client.receive();
            }
        } else {
            handleEvent(client.receive());
        }
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
                case START_REC:
                case STOP_REC:
                case INPUT:
                    client.users = (Hashtable) e.data().get(event_obj.USERS);
                    break;
                case SPAWN:
                    p = (Player) e.data().get(event_obj.PLAYER);
                    client.users.put(p.id, p);
                    break;
                case LEAVE:
                    client.users.remove(e.data().get(event_obj.ID));
                    break;
            }
        }
    }

    public void collision(Player player)
    {
        PriorityQueue<Collidable> objects = new PriorityQueue<>();
        for (Collidable p : client.platforms()) {
            if(p != null) {
                if(player.getRect().intersects(p.getRect())) {
                    if(p instanceof DeathZone) {
                        p.handle(player);
                        client.send(event_type.DEATH, false, player, client.users);
                        return;
                    }
                    objects.add(p);
                }
            }
        }
        if(!objects.isEmpty()) {
            Collidable c = objects.poll();
            c.handle(player);
            if(!c.equals(player.collide)) {
                player.collide = c;
                client.send(event_type.COLLISION, true, player, client.users);
            }
        } else {
            player.ground = GROUND;
        }

    }
}

