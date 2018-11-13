package core.network;

import core.util.Constants;
import core.util.events.Event;

import core.util.events.EventHandler;
import core.util.events.EventManager;
import core.util.time.GlobalTime;
import core.util.time.LocalTime;
import processing.core.PApplet;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.HashMap;
import java.util.PriorityQueue;
import java.util.UUID;

public class Client implements Constants
{
    private PApplet p;
    private Socket s;
    private UUID id;
    private Thread t;

    private static String host;
    private static int port;
    private ObjectInputStream input;
    private ObjectOutputStream output;
    private LocalTime time;
    private PriorityQueue<Event> events;
    private EventListener listener;
    private Event.Type event_type;
    private Event.Obj event_obj;


    public Client(String host, int port)
    {
        this.host = host;
        this.port = port;
        id = UUID.randomUUID();
        events = new PriorityQueue<>();
    }

    public long getTime() { return time.getTime(); }

    public UUID id() { return id; }

    public Event receive()
    {
        Event e = null;
        synchronized (events) {
            e = events.poll();
        }
        return e;
    }

    public Event start()
    {
        Event e = null;
        try {
            s = new Socket(host, port);

            // Initialize IO streams
            output = new ObjectOutputStream(s.getOutputStream());
            input = new ObjectInputStream(s.getInputStream());

            // Send client id
            output.writeObject(id);

        } catch (Exception ex) {
            ex.printStackTrace();
            System.exit(1);
        }
        try {
            // Create new thread for receiving events
            t = new Thread(new EventListener());
            t.start();

            // Create a new player object
            send(event_type.JOIN, true, "");

            // Wait until an appropriate response is received
            while (true) {
                e = receive();
                if (e != null) {
                    if (e.type() == event_type.ERROR) {
                        System.err.println(e.data().get(event_obj.MSG));
                        System.exit(1);
                    }
                    if (e.type() == event_type.SPAWN)
                        break;
                }
            }

            time = new LocalTime((GlobalTime) e.data().get(event_obj.TIME), 1);

        } catch (Exception ex) {
            ex.printStackTrace();
            try {
                t.join();
            } catch (InterruptedException e1) {
                e1.printStackTrace();
            }
            System.exit(1);
        }
        return e;
    }

    public void close()
    {
        t.interrupt();
        send(event_type.LEAVE, false, id);
    }

    public synchronized void send(Event.Type type, boolean uncached, Object ...data)
    {
        HashMap<Event.Obj, Object> args = new HashMap<>();
        for(Object d: data) {
            if (d != null) {
                for (Event.Obj o : event_obj.values())
                    if (o.type().equals(d.getClass()))
                        args.put(o, d);
            }
        }
        args.put(Event.Obj.ID, id);
        args.put(Event.Obj.TIME, time);
        try {
            if (uncached)
                output.reset();
            output.writeObject(new Event(type, args));
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error: Server has been stopped");
            System.exit(1);
        }
    }

    private class EventListener implements Runnable
    {
        @Override
        public void run()
        {
            while(true) {
                try {
                    Object o = input.readObject();
                    if(o instanceof Event) {
                        synchronized (events) {
                            events.add((Event)o);
                        }
                        System.err.println("Received " + o );
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                    break;
                }
            }
        }
    }
}
