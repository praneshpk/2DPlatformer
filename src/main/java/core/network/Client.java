package core.network;

import core.util.Constants;
import core.objects.Player;
import core.util.events.Event;

import core.util.events.EventHandler;
import core.util.events.EventManager;
import core.util.time.GlobalTime;
import core.util.time.LocalTime;
import processing.core.PApplet;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ConnectException;
import java.net.Socket;
import java.util.HashMap;
import java.util.UUID;

public class Client implements Constants
{
    private PApplet p;
    private Socket s;

    private static String host;
    private static int port;
    private ObjectInputStream input;
    private ObjectOutputStream output;
    private LocalTime time;
    private EventManager em;
    protected Event.type event_type;
    protected Event.obj event_obj;
    private EventHandler[] handlers;


    public Client(String host, int port)
    {
        this.host = host;
        this.port = port;
        em = new EventManager();
        handlers = new EventHandler[event_type.values().length];
        for(int i = 0; i < handlers.length; i++ ) {
            handlers[i] = new EventHandler(this, event_type.values()[i]);
            em.register(handlers[i]);
        }
    }

    public Event start()
    {
        Event e = null;
        try {
            s = new Socket(host, port);

            // Initialize IO streams
            output = new ObjectOutputStream(s.getOutputStream());
            input = new ObjectInputStream(s.getInputStream());

            // Create a new player object
            send(event_type.SPAWN, null, false);

            // Wait until an appropriate response is received
            while(true) {
                e = (Event) input.readObject();
                if(e.type() == event_type.ERROR) {
                    System.err.println(e.data().get(event_obj.MSG));
                    System.exit(1);
                }
                if(e.type() == event_type.SPAWN)
                    break;
            }

            time = new LocalTime((GlobalTime) e.data().get(event_obj.TIME), 1);
        } catch (Exception ex) {
            ex.printStackTrace();
            System.exit(1);
        }

        return e;
    }

    public synchronized void send(Event.type type, Object data, boolean uncached)
    {
        HashMap<Event.obj, Object> args = new HashMap<>();
        args.put(Event.obj.DATA, data);
        args.put(Event.obj.TIME, time);
        Event event = new Event(type, args);
        try {
            if (uncached)
                output.reset();
            output.writeObject(event);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error: Server has been stopped");
            System.exit(1);
        }
    }

    public synchronized Event receive()
    {
        Event e = null;
        try {
            e = (Event) input.readObject();
        } catch (Exception ex) {
            ex.printStackTrace();
            System.exit(1);
        }
        return e;
    }
}
