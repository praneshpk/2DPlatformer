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

    public void handleJoin(HashMap args) throws IOException
    {
        System.out.println(args + " from thread " + Thread.currentThread().getId() + s.getLocalAddress());
        // Check if server is full
        if (users.size() == MAX_USERS) {
            args = new HashMap();
            args.put(event_obj.TIME, time);
            args.put(event_obj.MSG, "Server is full");
            send(new Event(event_type.ERROR, args), true);
        }
        else {
            UUID e_id = (UUID) args.get(event_obj.ID);
            // Block gets executed once
            if(!users.containsKey(e_id)) {
                synchronized (users) {
                    users.put(e_id, new Player(e_id));
                }
                System.out.println(users.get(e_id).toString() + s.getLocalAddress() + " joined.");
            }
            args = new HashMap();
            args.put(event_obj.TIME, time);
            if(cid.equals(e_id)) {
                args.put(event_obj.COLLIDABLES, platforms);
                args.put(event_obj.USERS, users);
            }
            else
                args.put(event_obj.PLAYER, users.get(e_id));

            send(new Event(event_type.SPAWN, args), true);
        }
    }

    public void handleLeave(HashMap args) throws IOException
    {
        if(users.containsKey(args.get(event_obj.ID))) {
            System.out.println(users.get(args.get(event_obj.ID)).toString() + " left.");
            synchronized (users) {
                users.remove(args.get(event_obj.ID));
            }
        }
        args.replace(event_obj.TIME, time);
        if(!cid.equals(args.get(event_obj.ID)))
            send(new Event(event_type.LEAVE, args), true);
    }

    public void handleDeath()
    {

    }

    public void handleInput(HashMap args) throws IOException
    {
        // Defaults to sending back an input type
        event_type = event_type.INPUT;

        // Check if there are collisions
        Player p = (Player) args.get(event_obj.PLAYER);
//        LinkedList<Collidable> objects = collision(p.getRect());

        // Update player in user list, if necessary
        if(!users.get(p.id).equals(p)) {
            synchronized (users) {
                users = (Hashtable) args.get(event_obj.USERS);
                users.replace(p.id, p);
            }
        }

        args = new HashMap();

//        // Send back as collision event if found
//        if (!objects.isEmpty()) {
//            if(objects.peek() instanceof DeathZone)
//                event_type = event_type.DEATH;
//            else
//                event_type = event_type.COLLISION;
//            args.put(event_obj.ID, p.id);
//            args.put(event_obj.COLLIDABLES, objects);
//
//        }

        // Add standard args
        args.put(event_obj.TIME, time);
        args.put(event_obj.USERS, users);
        send(new Event(event_type, args), true);
    }

    public LinkedList<Collidable> collision(Rectangle pRect)
    {
        LinkedList<Collidable> ret = new LinkedList<>();
        for (Collidable p : platforms) {
            if(p != null) {
                p.update(time.getTime());
                if(pRect.intersects(p.getRect())) {
                    if(p instanceof DeathZone) {
                        ret.clear();
                        ret.add(p);
                        return ret;
                    }
                    ret.add(p);
                }
            }
        }
        return ret;

    }

    public void run()
    {
        Event receive;
        while (true) {
            try {

                // Receive event data
                receive = (Event) input.readObject();
                System.err.println("Received " + receive + " from thread " + Thread.currentThread().getId() + s.getLocalAddress());
                em.handle(receive);
            } catch (Exception e) {
                //s.close();
                for(EventHandler h: handlers)
                    em.unRegister(h);
                //e.printStackTrace();
                break;
            }
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
