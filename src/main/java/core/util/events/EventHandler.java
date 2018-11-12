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
                            .getDeclaredMethod("handleCollision", LinkedList.class)
                            .invoke(inst, args.get(Event.Obj.COLLIDABLES));
                    break;
                case DEATH:
                    inst.getClass()
                            .getDeclaredMethod("handleDeath")
                            .invoke(inst);
                    break;
                case SPAWN:
                    inst.getClass()
                            .getDeclaredMethod("handleSpawn", UUID.class)
                            .invoke(inst, (UUID) args.get(Event.Obj.ID));
                    break;
                case INPUT:
                    inst.getClass()
                            .getDeclaredMethod("handleInput", Object.class)
                            .invoke(inst, args.get(Event.Obj.PLAYER));
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(1);
        }
    }

    public Event.Type getType() { return type; }
}