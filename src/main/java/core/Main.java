package core;

import core.network.Client;
import core.network.Server;
import core.objects.Collidable;
import core.objects.DeathZone;
import core.objects.Player;
import core.util.Constants;
import core.util.events.Event;
import processing.core.PApplet;
import processing.core.PShape;

import java.util.*;

/**
 * Class to connect to Server object
 */
public class Main extends PApplet implements Constants
{
    protected Event event;
    protected static Event.Type event_type;
    protected static Event.Obj event_obj;
    protected static Client client;
    protected boolean pause = false, mem = false;
    protected long wait, offset = 0;
    protected Event start;
    protected static int bg = 255;

    protected void renderObjects()
    {
        if(client.recording)
            background(255,245,245);
        else if(client.replay)
            background(245);
        else
            background(bg);
        for (Collidable p : client.platforms())
            p.display(this, client.time().getTime() - offset);
        for(Player p: client.users().values()) {
            p.display(this, client.time().getTic());
        }
    }

    protected void updateObjects()
    {
        if(!pause)
            for(Player p: client.users().values()) {
                collision(p);
                client.users().replace(p.getId(), p);
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



        //client

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
        Player player = client.users().get(client.id());
        if(!client.replay) {
            switch (keyCode) {
                case LEFT:
                    player.setLeft(-1);
                    player.setDir(1);
                    client.send(event_type.INPUT, true, player, client.users());
                    break;
                case RIGHT:
                    player.setRight(1);
                    player.setDir(-1);
                    client.send(event_type.INPUT, true, player, client.users());
                    break;
                case 32:
                    player.setUp(-1);
                    client.send(event_type.INPUT, true, player, client.users());
                    break;
                case 'R':
                    mem = true;
                    if(!client.recording) {
                        client.send(event_type.START_REC, true, player, client.users());
                    } else {
                        client.send(event_type.STOP_REC, true, player, client.users());
                        while (true) {
                            Event e = client.receive();
                            renderObjects();
                            client.handleEvent(e);
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
                        client.handleEvent(e);
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
            Player player = client.users().get(client.id());
            switch (keyCode) {
                case LEFT:
                    player.setLeft(0);
                    client.send(event_type.INPUT, true, player, client.users());
                    break;
                case RIGHT:
                    player.setRight(0);
                    client.send(event_type.INPUT, true, player, client.users());
                    break;
                case 32:
                    player.setUp(0);
                    client.send(event_type.INPUT, true, player, client.users());
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
                client.handleEvent(start);
                start = client.receive();
            }
        } else {
            event = client.receive();
            client.handleEvent(event);
        }
        updateObjects();
    }

    public void exit()
    {
        client.close();
        super.exit();
    }

    public void collision(Player player)
    {
        PriorityQueue<Collidable> objects = new PriorityQueue<>();
        for (Collidable p : client.platforms()) {
            if(p != null) {
                if(player.getRect().intersects(p.getRect())) {
                    if(p instanceof DeathZone) {
                        p.handle(player);
                        client.send(event_type.DEATH, false, player, client.users());
                        return;
                    }
                    objects.add(p);
                }
            }
        }
        if(!objects.isEmpty()) {
            Collidable c = objects.poll();
            c.handle(player);
            if(!c.equals(player.getCollide())) {
                player.setCollide(c);
                client.send(event_type.COLLISION, true, player, client.users());
            }
        } else {
            player.setGround(GROUND);
        }

    }
}

