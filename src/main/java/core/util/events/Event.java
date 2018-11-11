package core.util.events;

import java.io.Serializable;
import java.util.HashMap;

public class Event implements Serializable
{
    private type type;
    private HashMap data;

    public Event(Event.type type, HashMap data)
    {
        this.type = type;
        this.data = data;
    }

    @Override
    public String toString()
    {
        return "Event[type:" + type + ", data:" + data + "]";
    }

//    @Override
//    public int compareTo(Event o)
//    {
//        if(ts < o.getTimestamp())
//            return -1;
//        else if(ts == o.getTimestamp())
//            return 0;
//        else
//            return 1;
//    }

    public Event.type type() { return type; }

    public HashMap data() { return data; }


    public enum obj
    {
        TIME, PLATFORMS, USERS, PLAYER, MSG, DATA
    }

    public enum type
    {
        COLLISION, DEATH, SPAWN, INPUT, ERROR
    }
}
