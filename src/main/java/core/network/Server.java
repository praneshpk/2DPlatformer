package core.network;


import core.objects.Collidable;
import core.objects.Player;
import core.util.Event;
import core.util.event_type;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;

public class Server extends Thread {
    public static int PORT = 4096;
    public static String HOSTNAME = "127.0.0.1";
    public static int MAX_USERS = 3;

    protected static volatile Collidable platforms[];
    protected static volatile Hashtable<UUID, Player> users;
    protected static volatile long start;
    private Socket s;
    private ServerSocket server;
    private Player player;
    private ObjectInputStream input;
    private ObjectOutputStream output;
    private Event event;

    public Server(Socket s)
    {
        this.s = s;
        try {
            output = new ObjectOutputStream(s.getOutputStream());
            input = new ObjectInputStream(s.getInputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Server()
    {
        users = new Hashtable<>();
        start = System.currentTimeMillis();
    }

    public void mainLoop() {
        while(true) {
            try {
                event = (Event) input.readObject();
                // Receive event data
                System.err.println("Received " + event + " from thread " + Thread.currentThread().getId() + s.getLocalAddress());
            } catch (IOException e) {
                break;
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
                break;
            }
            // Requests
            if (event.type == event_type.REQUEST) {
                if(event.data.equals("platforms".hashCode()))
                    event = new Event(event.type, platforms);
                if(event.data.equals("users".hashCode()))
                    event = new Event(event.type, new ArrayList(users.values()));
                if(event.data.equals("time".hashCode()))
                    event = new Event(event.type, System.currentTimeMillis()-start);
            }

            // Create new player
            if (event.type == event_type.CREATE) {
                // Check if server is full
                if (users.size() == MAX_USERS)
                    event = new Event(event_type.ERROR, null);
                else {
                    player = (Player) event.data;
                    synchronized (users) {
                        users.put(player.id, player);
                    }
                    System.out.println(player.toString() + s.getLocalAddress() + " joined.");

                    // Send back player object with id
                    event = new Event(event.type, player);
                }
            }
            // Update player
            if (event.type == event_type.SEND) {
                player = (Player) event.data;

                // Update player in user list
                synchronized (users) {
                    users.replace(player.id, player);
                }

                event = new Event(event.type, new ArrayList(users.values()));
            }
            try {
                output.reset();
                output.writeObject(event);

                System.err.println("Sent " + event + " from thread " + Thread.currentThread().getId() + s.getLocalAddress());
            } catch (IOException e) {
                break;
            }
        }
    }

    public void run() {
        mainLoop();
        try {
            System.out.println(player.toString() + s.getLocalAddress() + " left.");
            users.remove(player.id);
            s.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void listen() {
        try {
            server = new ServerSocket(PORT);
        } catch( Exception e ) {
            System.err.println("Can't initialize server: " + e);
            System.exit(1);
        }
        System.out.println("Server started on " + server.getLocalSocketAddress());

        try {
            while(true) {
                Socket s = server.accept();
                Server t = new Server(s);
                t.start();
            }
        } catch ( Exception e ){
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
