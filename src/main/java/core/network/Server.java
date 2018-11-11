package core.network;


import core.objects.Collidable;
import core.objects.Player;
import core.util.events.Event;
import core.util.events.EventHandler;
import core.util.events.EventManager;
import core.util.time.GlobalTime;

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

    protected static volatile Collidable[] platforms;
    protected static volatile Hashtable<UUID, Player> users;
    protected static volatile EventManager em;
    protected static volatile GlobalTime time;
    protected static volatile UUID id;
    protected Player player;
    protected Socket s;

    private ServerSocket server;
    private ObjectInputStream input;
    private ObjectOutputStream output;
    private Event.type event_type;
    private Event.obj event_obj;
    private EventHandler[] handlers;

    public Server(Socket s)
    {
        this.s = s;
        try {
            output = new ObjectOutputStream(s.getOutputStream());
            input = new ObjectInputStream(s.getInputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
        handlers = new EventHandler[event_type.values().length];
        for(int i = 0; i < handlers.length; i++ ) {
            handlers[i] = new EventHandler(this, event_type.values()[i]);
            em.register(handlers[i]);
        }
    }

    public Server()
    {
        users = new Hashtable<>();
        time = new GlobalTime(TIC);
        id = UUID.randomUUID();
        em = new EventManager();
    }

    public void handleSpawn() throws IOException
    {
        System.out.println("Handling new player...");
        HashMap<Event.obj, Object> args = new HashMap<>();
        args.put(event_obj.TIME, time);
        Event send;

        // Check if server is full
        if (users.size() == MAX_USERS) {
            args.put(event_obj.MSG, "Server is full");
            send = new Event(event_type.ERROR, args);
        }
        else {
            player = new Player();
            synchronized (users) {
                users.put(player.id, player);
            }
            System.out.println(player.toString() + s.getLocalAddress() + " joined.");

            args.put(event_obj.PLAYER, player);
            args.put(event_obj.USERS, users);
            args.put(event_obj.PLATFORMS, platforms);
            send = new Event(event_type.SPAWN, args);
        }
        output.reset();
        output.writeObject(send);
        System.err.println("Sent " + send + " from thread " + Thread.currentThread().getId() + s.getLocalAddress());
    }

    public void handleDeath()
    {

    }

    public void handleInput(Object data) throws IOException
    {
        HashMap<Event.obj, Object> args = new HashMap<>();
        args.put(event_obj.TIME, time);
        player = (Player) data;

        // Update player in user list
        synchronized (users) {
            users.replace(player.id, player);
        }
        args.put(event_obj.PLAYER, player);
        Event send = new Event(event_type.INPUT, args);

        output.reset();
        output.writeObject(send);
        System.err.println("Sent " + send + " from thread " + Thread.currentThread().getId() + s.getLocalAddress());
    }

    public void handleCollision(Object data)
    {

    }

    public void mainLoop()
    {
        Event receive;
        while (true) {
            try {
                // Receive event data
                receive = (Event) input.readObject();
                System.err.println("Received " + receive + " from thread " + Thread.currentThread().getId() + s.getLocalAddress());
            } catch (IOException e) {
                e.printStackTrace();
                break;
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
                break;
            }
            try {
                em.handle(receive);
            } catch (Exception e) {
                break;
            }
        }
    }

    public void run()
    {
        mainLoop();
        try {
            System.out.println(player.toString() + s.getLocalAddress() + " left.");
            for(EventHandler h: handlers)
                em.unRegister(h);
            users.remove(player.id);
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
