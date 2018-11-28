package core.util.scripts;

import core.network.Client;
import core.objects.*;
import processing.core.PVector;

import java.awt.*;
import java.util.LinkedList;

public class SpaceInvaders
{
    static core.Server server;
    static core.Main Applet;

    public static void main(String[] args)
    {
        LinkedList<Collidable> platforms = new LinkedList<>();
        platforms.add(new StaticPlatform(
                new PVector(0,0), 50, 50, new Color(255,255,255)));
        server = new core.Server(platforms);

        Applet.main(new String[]{"0"});


//        platforms.add(new Static)

        new Thread(() -> {
            server.listen();
        }).start();
        //server.listen();




        while(true) {
			ScriptManager.loadScript("scripts/space-invaders/init.js");
			ScriptManager.bindArgument("networkClient", Applet.client);
//			ScriptManager.bindArgument("networkClient", new core.network.Client(core.network.Server.HOSTNAME, core.network.Server.PORT));
//			ScriptManager.bindArgument("networkServer", new core.network.Server());
            ScriptManager.executeScript();


            try { System.in.read(); }
            catch(Exception e) { }
        }

    }

}
