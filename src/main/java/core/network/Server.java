package core.network;


import core.objects.Collidable;
import core.objects.DeathZone;
import core.objects.Player;
import core.util.events.Event;
import core.util.events.EventHandler;
import core.util.events.EventManager;
import core.util.time.GlobalTime;

import java.awt.*;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;

public class Server extends Thread
{
    public static int PORT = 4096;
    public static String HOSTNAME = "127.0.0.1";
    public static int MAX_USERS = 6;
    public static final int TIC = 1;

    protected static volatile LinkedList<Collidable> platforms;
    protected static volatile Hashtable<UUID, Player> users;
    protected static volatile EventManager em;
    protected static volatile GlobalTime time;
    protected static volatile UUID id;
    protected Socket s;

    private ServerSocket server;
    private ObjectInputStream input;
    private ObjectOutputStream output;
    private Event.Type event_type;
    private Event.Obj event_obj;
    private UUID cid;
    private LinkedList<EventHandler> handlers;

    public Server(Socket s)
    {
        this.s = s;

        try {
            output = new ObjectOutputStream(s.getOutputStream());
            input = new ObjectInputStream(s.getInputStream());

            // Set client id
            cid = (UUID) input.readObject();
        } catch (Exception e) {
            e.printStackTrace();
        }
        handlers = new LinkedList<>();
        for(Event.Type t : event_type.values()) {
            handlers.add(new EventHandler(this, t));
            em.register(handlers.getLast());
        }
    }

    public Server()
    {
        users = new Hashtable<>();
        time = new GlobalTime(TIC);
        id = UUID.randomUUID();
        em = new EventManager();
    }

    private synchronized void send(Object data, boolean uncached) throws IOException
    {
        if(uncached)
            output.reset();
        output.writeObject(data);
        System.err.println("Sent " + data + " from thread " + Thread.currentThread().getId() + s.getLocalAddress());
    }

    public void handleSpawn(UUID id) throws IOException
    {
        HashMap<Event.Obj, Object> args = new HashMap<>();
        args.put(event_obj.TIME, time);
        Event send;

        // Check if server is full
        if (users.size() == MAX_USERS) {
            args.put(event_obj.MSG, "Server is full");
            send = new Event(event_type.ERROR, args);
        }
        else {
            // Block gets executed once
            if(!users.containsKey(id)) {
                synchronized (users) {
                    users.put(id, new Player(id));
                }
                System.out.println(users.get(id).toString() + s.getLocalAddress() + " joined.");
            }

            if(cid.equals(id)) {
                args.put(event_obj.PLAYER, users.get(id));
                args.put(event_obj.COLLIDABLES, platforms);
            }

            args.put(event_obj.USERS, users);
            send = new Event(event_type.SPAWN, args);
        }
        send(send, true);
    }

    public void handleDeath()
    {

    }

    public void handleInput(Object data) throws IOException
    {
        HashMap<Event.Obj, Object> args = new HashMap<>();
        args.put(event_obj.TIME, time);
        event_type = event_type.INPUT;

        Player p = (Player) data;

        LinkedList<Collidable> objects = collision(p.getRect());
        if(!objects.isEmpty()) {
            //Collidable c = objects.pop();
            for(Collidable c : objects) {
                c.handle(p);
                if(!(c instanceof DeathZone))
                    p.update(1);
            }
            event_type = event_type.COLLISION;
        }

        // Update player in user list
        synchronized (users) {
            users.replace(cid, p);
        }
        args.put(event_obj.PLAYER, p);
        send(new Event(event_type, args), true);
    }

    public LinkedList<Collidable> collision(Rectangle pRect)
    {
        LinkedList<Collidable> ret = new LinkedList<>();
        for (Collidable p : platforms) {
            if (p != null && pRect.intersects(p.getRect()))
                ret.add(p);
        }
        return ret;

    }

    public void handleCollision(LinkedList<Collidable> data) throws IOException
    {
        HashMap<Event.Obj, Object> args = new HashMap<>();
        args.put(event_obj.TIME, time);

        Player p = (Player) data.pollLast();
        for(Collidable c: data) {
            c.handle(p);
        }
        p.update(1);
        args.put(event_obj.PLAYER, p);
        send(new Event(event_type.COLLISION, args), true);

    }

    public void mainLoop()
    {
        Event receive;
        while (true) {
            try {
                // Receive event data
                receive = (Event) input.readObject();
                System.err.println("Received " + receive + " from thread " + Thread.currentThread().getId() + s.getLocalAddress());
            } catch (Exception e) {
                //e.printStackTrace();
                break;
            }

            try {
                em.handle(receive);
            } catch (Exception e) {
                e.printStackTrace();
                break;
            }
        }
    }

    public void run()
    {
        mainLoop();
        try {
            System.out.println(users.get(cid).toString() + " left.");
            for(EventHandler h: handlers)
                em.unRegister(h);
            users.remove(cid);
            s.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void listen()
    {
        time.start();
        try {
            server = new ServerSocket(PORT);
        } catch (Exception e) {
            System.err.println("Can't initialize server: " + e);
            System.exit(1);
        }
        System.out.println("Server started on " + server.getLocalSocketAddress());

        try {
            while (true) {
                Socket s = server.accept();
                Server t = new Server(s);
                t.start();
            }
        } catch (Exception e) {
            System.err.println("Error accepting client " + e);
        } finally {

            try {
                server.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
