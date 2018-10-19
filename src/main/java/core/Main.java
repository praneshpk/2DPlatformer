package core;

import core.network.Server;
import core.objects.Collidable;
import core.objects.Player;
import processing.core.PApplet;

import java.io.IOException;
import java.net.Socket;


/**
 * Class to connect to Server object
 */
public class Main extends PApplet implements GameConstants {

    private Player player;
    private static GameClient client;

    private void renderObjects()
    {
        Collidable [] platforms = client.getPlatforms();
        for(int i = platforms.length - 1; i >= 0; i--)
            platforms[i].display(this);
        player.display(this);
    }

    public void settings()
    {
        size(WIDTH, HEIGHT);
    }
    public void setup()
    {
        // Socket for client to connect to
        Socket s = null;

        // Player object
        player = new Player();

        // Processing window settings
        smooth();
        noStroke();
        fill(0,255,0);

        // Initialize client
        client = new GameClient(this, Server.HOSTNAME, Server.PORT);
        client.start();

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
    public void draw()
    {
        background(255);
        renderObjects();
        player.update();
        System.out.println(player.getPos());
        // Sends player state to network
        try {
            player = client.send(player);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}

