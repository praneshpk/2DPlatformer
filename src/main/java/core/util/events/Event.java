package core.util.events;

import core.objects.Player;
import core.util.time.Time;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.LinkedList;
import java.util.UUID;

public class Event implements Serializable, Comparable<Event>
{
    private Type type;
    private HashMap data;

    public Event(Type type, HashMap data)
    {
        this.type = type;
        this.data = data;
    }

    @Override
    public String toString()
    {
        return "Event[Type:" + type + ", data:" + data + "]";
    }

    public Type type() { return type; }

    public HashMap data() { return data; }

    @Override
    public int compareTo(Event o)
    {
        Time t1 = (Time) data.get(Obj.TIME);
        Time t2 = (Time) o.data().get(Obj.TIME);

        if(t1 == null || t2 == null)
            return 0;
        if(t1.getTime() > t2.getTime())
            return 1;
        else if(t1.getTime() == t2.getTime())
            return 0;
        else
            return -1;
    }


    public enum Obj
    {
        ID(UUID.class),
        TIME(Time.class),
        COLLIDABLES(LinkedList.class),
        USERS(Hashtable.class),
        PLAYER(Player.class),
        MSG(String.class),
        DATA(HashMap.class);

        private final Class<?> type;
        Obj(Class<?> type)
        {
            this.type = type;
        }
        public Class<?> type() { return type; }

    }

    public enum Type
    {
        COLLISION, DEATH, SPAWN, INPUT, ERROR
    }
}
