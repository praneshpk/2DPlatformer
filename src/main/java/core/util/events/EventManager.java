package core.util.events;

import java.util.HashMap;
import java.util.LinkedList;

public class EventManager
{

    private static HashMap<Event.Type, LinkedList<EventHandler>> listeners;

    public EventManager()
    {
        listeners = new HashMap<>();
    }

    public synchronized void register(EventHandler handler)
    {
        if (!listeners.containsKey(handler.getType()))
            listeners.put(handler.getType(), new LinkedList<>());
        listeners.get(handler.getType()).add(handler);
    }

    public synchronized void unRegister(EventHandler handler)
    {
        listeners.get(handler.getType()).remove(handler);
    }

    public void handle(Event event)
    {
        for(EventHandler e: listeners.get(event.type()))
            e.handle(event.data());
    }

}
