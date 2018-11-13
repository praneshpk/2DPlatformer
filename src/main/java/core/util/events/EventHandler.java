package core.util.events;

import java.util.HashMap;

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
                case JOIN:
                    inst.getClass()
                            .getDeclaredMethod("handleJoin", HashMap.class)
                            .invoke(inst, args);
                    break;
                case SPAWN:
                case COLLISION:
                case DEATH:
                case INPUT:
                    inst.getClass()
                            .getDeclaredMethod("handleUpdate", type.getClass(), HashMap.class)
                            .invoke(inst, type, args);
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