package core.network;

import core.objects.Collidable;
import core.objects.Player;
import core.util.Constants;
import core.util.events.Event;

import core.util.time.GlobalTime;
import core.util.time.LocalTime;
import core.util.time.Time;
import processing.core.PApplet;

import java.io.*;
import java.net.Socket;
import java.util.*;

public class Client implements Constants
{
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
//                        System.err.println("Received " + o );
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                    break;
                }
            }
        }
    }

    private PApplet p;
    private Socket s;
    private UUID id;
    private Thread t;
    private static LinkedList<Collidable> platforms;
    private Hashtable<UUID, Player> users;

    private static String host;
    private static int port;
    private ObjectInputStream input, log_input;
    private ObjectOutputStream output, log_output;
    private File log;
    private GlobalTime time;
    private LocalTime replayTime;
    private PriorityQueue<Event> events;
    private EventListener listener;
    public Event.Type event_type;
    public Event.Obj event_obj;
    public boolean recording, replay;
    private Event currEvent;

    public Client(String host, int port)
    {
        this.host = host;
        this.port = port;
        id = UUID.randomUUID();
        events = new PriorityQueue<>();
        time = new GlobalTime(TIC);
        log = new File("replay.log");
    }

    public Time time()
    {
        if(replay)
            return replayTime;
        else
            return time;
    }

    public UUID id() { return id; }

    public Hashtable<UUID, Player> users() { return users; }

    public static LinkedList<Collidable> platforms() { return platforms; }

    public void setReplay(boolean replay, float tic)
    {
        this.replay = replay;
        try {
            if (replay) {
                replayTime.setTic(tic);
                replayTime.reset();
                log_input = new ObjectInputStream(new FileInputStream(log));
            } else {
                log_input.close();
            }
        } catch (Exception e ) {
            e.printStackTrace();
        }
    }

    public void record()
    {
        recording = !recording;
        try {
            if (recording) {
                replayTime = new LocalTime(time, 1);
                replayTime.start();
                log.createNewFile();
                log.deleteOnExit();
                log_output = new ObjectOutputStream(new FileOutputStream(log, false));
            } else {
                log_output.close();
            }
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    public void start()
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
            send(event_type.JOIN, true, false);

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
            time = (GlobalTime) e.data().get(event_obj.TIME);
        } catch (Exception ex) {
            ex.printStackTrace();
            t.interrupt();
            System.exit(1);
        }

        platforms = (LinkedList) e.data().get(event_obj.LIST);
        users = (Hashtable) e.data().get(event_obj.USERS);
    }

    public void close()
    {
        t.interrupt();
        replay = false;
        send(event_type.LEAVE, false, id);
        log.delete();
    }

    public void handleEvent(Event e)
    {
        if(e != null) {
            Player p;
            switch (e.type()) {
                case COLLISION:
                case DEATH:
                    break;
                case START_REC:
                case STOP_REC:
                case INPUT:
                    users = (Hashtable) e.data().get(event_obj.USERS);
                    break;
                case SPAWN:
                    p = (Player) e.data().get(event_obj.PLAYER);
                    users.put(p.getId(), p);
                    break;
                case LEAVE:
                    users.remove(e.data().get(event_obj.ID));
                    break;
            }
        }
    }

    public synchronized void send(Event.Type type, boolean uncached, Object ...data)
    {
        if(replay)
            return;
        HashMap<Event.Obj, Object> args = new HashMap<>();

        for(Object d: data) {
            if(d instanceof Boolean)
                break;
            for (Event.Obj o : event_obj.values())
                if (o.type().equals(d.getClass()))
                    args.put(o, d);
        }
        args.put(Event.Obj.ID, id);
        args.put(Event.Obj.TIMESTAMP, time.getTime());
        try {
            Event send = new Event(type, args);
//            System.err.println("Sent " + send);
            if (uncached)
                output.reset();
            output.writeObject(new Event(type, args));
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error: Server has been stopped");
            System.exit(1);
        }
    }

    public Event receive()
    {
        Event e = null;
        if(replay) {
            try {
                do {
                    e = (Event) log_input.readObject();
                } while (e == null);
                return e;
            } catch (IOException e1) {
                setReplay(true, replayTime.getTic());
                return receive();
            } catch (ClassNotFoundException e1) {
                e1.printStackTrace();
            }
        } else {
            synchronized (events) {
                e = events.poll();
            }
            //System.err.println("Received " + e);
        }
        if(recording) {
            try {
                log_output.writeObject(e);
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        }

        return e;
    }

    public void pause(boolean pause)
    {
        if(pause)
            replayTime.pause();
        else
            replayTime.unPause();

    }



}
