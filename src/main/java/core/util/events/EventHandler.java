package core.util.events;

import java.util.HashMap;

public class EventHandler
{
    private Event.type type;
    private Object inst;

    public EventHandler(Object inst, Event.type type)
    {
        this.inst  = inst;
        this.type = type;
    }

    public void handle(HashMap args)
    {
        Object obj = args.get(Event.obj.DATA);
        try {
            switch (type) {
                case COLLISION:
                    inst.getClass()
                            .getDeclaredMethod("handleCollision", Object.class)
                            .invoke(inst, obj);
                    break;
                case DEATH:
                    inst.getClass()
                            .getDeclaredMethod("handleDeath")
                            .invoke(inst);
                    break;
                case SPAWN:
                    inst.getClass()
                            .getDeclaredMethod("handleSpawn")
                            .invoke(inst);
                    break;
                case INPUT:
                    inst.getClass()
                            .getDeclaredMethod("handleInput", Object.class)
                            .invoke(inst, obj);
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(1);
        }
    }

    public Event.type getType() { return type; }
}