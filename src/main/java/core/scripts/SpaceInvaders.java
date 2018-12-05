package core.scripts;

import core.Main;
import core.network.Client;
import core.objects.*;
import processing.core.PApplet;
import processing.core.PShape;
import processing.core.PVector;

import java.awt.*;
import java.util.Hashtable;
import java.util.LinkedList;
import java.util.PriorityQueue;

public class SpaceInvaders extends Main
{
    static core.Server server;
    static Hashtable<Collidable.Type, String> paths = new Hashtable<>();
    static Hashtable<String, PShape> images = new Hashtable<>();

    protected void renderObjects()
    {
        background(0);
        for (Collidable p : client.platforms()) {
            if(paths.containsKey(p.getType())) {
                p.update(client.time().getTime());
                shape(images.get(paths.get(p.getType())), p.getPos().x, p.getPos().y);
            } else {
                p.display(this, client.time().getTime());
            }
        }
        for(Player p: client.users().values()) {
            if(paths.containsKey(p.getType())) {
                p.display(this, images.get(paths.get(p.getType())), client.time().getTic());
            } else {
                p.display(this, client.time().getTic());
            }
        }
    }

    public void setup()
    {
        // Processing window settings
        smooth();
        noStroke();
        //fill(0, 0, 0);

        for(String path : paths.values())
            images.put(path, loadShape(path));
    }

    public void draw()
    {
        renderObjects();
        client.handleEvent(client.receive());
        updateObjects();
    }

    public void keyPressed()
    {
        Player player = client.users().get(client.id());
        switch (keyCode) {
            case LEFT:
                player.setLeft(-1);
                player.setDir(1);
                client.send(client.event_type.INPUT, true, player, client.users());
                break;
            case RIGHT:
                player.setRight(1);
                player.setDir(-1);
                client.send(client.event_type.INPUT, true, player, client.users());
                break;
            case 32:
                client.send(client.event_type.INPUT, true, player, client.users());
                break;
        }
    }

    public void keyReleased()
    {
        Player player = client.users().get(client.id());
        switch (keyCode) {
            case LEFT:
                player.setLeft(0);
                client.send(client.event_type.INPUT, true, player, client.users());
                break;
            case RIGHT:
                player.setRight(0);
                client.send(client.event_type.INPUT, true, player, client.users());
                break;
        }
    }

    public static void main(String[] args)
    {
        LinkedList<Collidable> collidables = new LinkedList<>();
        collidables.add(new StaticPlatform(
                new PVector(150, server.HEIGHT - 150),
                50, 50, new Color(0xFFFFFF)));
        collidables.add(new StaticPlatform(
                new PVector(350, server.HEIGHT - 150),
                50, 50, new Color(0xFFFFFF)));
        collidables.add(new StaticPlatform(
                new PVector(550, server.HEIGHT - 150),
                50, 50, new Color(0xFFFFFF)));
        server = new core.Server(collidables);


        new Thread(() -> {
            try {
                server.listen();
            } catch (Exception e) {
                server = null;
            }
        }).start();

        client = new Client(server.HOSTNAME, server.PORT);
        client.start();

        Collidable.Type ctype = Collidable.Type.PLAYER;
        ScriptManager.loadScript("scripts/space-invaders/init.js");
        ScriptManager.bindArgument("networkClient", client);
        ScriptManager.bindArgument("images", paths);
        for(Collidable.Type t : Collidable.Type.values())
            ScriptManager.bindArgument(t.name(), t);
        ScriptManager.bindArgument("rootpath", System.getProperty("user.dir"));
        ScriptManager.executeScript();

        PApplet.main("core.scripts.SpaceInvaders", args);
        /*
        while(true) {
//			ScriptManager.bindArgument("networkClient", new core.network.Client(core.network.Server.HOSTNAME, core.network.Server.PORT));
//			ScriptManager.bindArgument("networkServer", new core.network.Server());


            try { System.in.read(); }
            catch(Exception e) { }
        }
        */


    }

}
