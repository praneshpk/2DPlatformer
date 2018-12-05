package core.scripts;

import core.objects.Collidable;
import core.util.events.Event;
import processing.core.PApplet;

public class ScriptingExample extends core.Main
{
    @Override
    public void draw() {
        ScriptManager.loadScript("scripts/modify_object.js");
        ScriptManager.executeScript();
        super.draw();
        ScriptManager.loadScript("scripts/handle_event.js");
        ScriptManager.bindArgument("client", client);
        ScriptManager.bindArgument("event", event);
        ScriptManager.executeScript();
        //client.send(event);
    }

    @Override
    public void setup() {
        super.setup();

    }

    public static void main(String[] args) {
        final core.Server server = new core.Server();

        new Thread(() -> {
            try {
                server.listen();
            } catch (Exception e) {
            }
        }).start();
        PApplet.main("core.scripts.ScriptingExample", args);
        ScriptManager.bindArgument("client", client);
        ScriptManager.bindArgument("platforms", client.platforms());
        for(Event.Type e: event_type.values())
            ScriptManager.bindArgument(e.name(), e);
        for(Collidable.Type e: Collidable.Type.values())
            ScriptManager.bindArgument(e.name(), e);
        for(Event.Obj e: event_obj.values())
            ScriptManager.bindArgument(e.name(), e);
    }
}