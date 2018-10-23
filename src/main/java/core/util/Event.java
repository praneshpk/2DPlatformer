package core.util;

import java.io.Serializable;

public class Event implements Serializable {

    public event_type type;
    public Serializable data;

    public Event(event_type type, Serializable data) {
        this.type = type;
        this.data = data;
    }

    @Override
    public String toString() {
        return "Event[type:" + type + ", data:" + data + "]";
    }
}
