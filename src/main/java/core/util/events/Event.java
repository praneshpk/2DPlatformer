package core.util.events;

import core.objects.Collidable;
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
        if(type.compareTo(o.type) != 0)
            return type.compareTo(o.type);

        if(!data.containsKey(Obj.TIMESTAMP) || !o.data().containsKey(Obj.TIMESTAMP))
            return 0;
        long t1 = (long) data.get(Obj.TIMESTAMP);
        long t2 = (long) o.data().get(Obj.TIMESTAMP);

        if(t1 > t2)
            return 1;
        else if(t1 == t2)
            return 0;
        else
            return -1;
    }


    public enum Obj
    {
        ID(UUID.class),
        TIME(Time.class),
        TIMESTAMP(long.class),
        LIST(LinkedList.class),
        USERS(Hashtable.class),
        PLAYER(Player.class),
        MSG(String.class);

        private final Class<?> type;
        Obj(Class<?> type)
        {
            this.type = type;
        }
        public Class<?> type() { return type; }

    }

    public enum Type
    {
        JOIN, LEAVE, SPAWN, DEATH, INPUT, COLLISION, START_REC, STOP_REC, ERROR
    }
}
