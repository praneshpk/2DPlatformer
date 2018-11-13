package core.util.events;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.UUID;

public class EventHandler
{
    private Event.Type type;
    private Object inst;

    public EventHandler(Object inst, Event.Type type)
    {
        this.inst  = inst;
        this.type = type;
    }

    public void handle(HashMap args)
    {
        try {
            switch (type) {
                case COLLISION:
                    inst.getClass()
                            .getDeclaredMethod("handleCollision", HashMap.class)
                            .invoke(inst, args);
                    break;
                case DEATH:
                    inst.getClass()
                            .getDeclaredMethod("handleDeath", HashMap.class)
                            .invoke(inst, args);
                    break;
                case JOIN:
                    inst.getClass()
                            .getDeclaredMethod("handleJoin", HashMap.class)
                            .invoke(inst, args);
                    break;
                case SPAWN:
                    inst.getClass()
                            .getDeclaredMethod("handleSpawn", HashMap.class)
                            .invoke(inst, args);
                    break;
                case INPUT:
                    inst.getClass()
                            .getDeclaredMethod("handleInput", HashMap.class)
                            .invoke(inst, args);
                    break;
                case LEAVE:
                    inst.getClass()
                            .getDeclaredMethod("handleLeave", HashMap.class)
                            .invoke(inst, args);
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(1);
        }
    }

    public Event.Type getType() { return type; }
}