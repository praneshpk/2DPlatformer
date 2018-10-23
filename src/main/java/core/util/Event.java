package core.util;

import java.io.Serializable;

public class Event implements Serializable {

    public event_type type;
    public Object data;

    public Event(event_type type, Object data) {
        this.type = type;
        this.data = data;
    }

    @Override
    public String toString() {
        return "Event[type:" + type + ", data:" + data + "]";
    }
}
