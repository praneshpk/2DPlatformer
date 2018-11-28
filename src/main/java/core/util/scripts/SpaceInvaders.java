package core.util.scripts;

import core.objects.Collidable;

import java.util.LinkedList;

public class SpaceInvaders
{
    static core.Main Applet;
    static core.Server server;
    public static void main(String[] args) {
        server = new core.Server();
//        LinkedList<Collidable> platforms = new LinkedList<>();

//        platforms.add(new Static)

        new Thread(() -> {
            server.listen();
        }).start();
        //server.listen();
        Applet.main(args);
        while(true) {
			ScriptManager.loadScript("scripts/space-invaders/init.js");
//			ScriptManager.bindArgument("player", new core.objects.Player());
//			ScriptManager.bindArgument("networkClient", new core.network.Client(core.network.Server.HOSTNAME, core.network.Server.PORT));
//			ScriptManager.bindArgument("networkServer", new core.network.Server());
            ScriptManager.executeScript();


            try { System.in.read(); }
            catch(Exception e) { }
        }

    }

}
