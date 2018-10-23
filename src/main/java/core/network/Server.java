package core.network;


import core.objects.Collidable;
import core.objects.Player;
import core.util.Event;
import core.util.event_type;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Hashtable;
import java.util.UUID;

public abstract class Server {
    public static int PORT = 4096;
    public static String HOSTNAME = "127.0.0.1";
    public static int MAX_USERS = 3;

    protected volatile Collidable platforms[];
    protected static volatile Hashtable<UUID, Player> users;

    /**
     * Class to allow multithreading across users
     */
    protected class NetworkThread extends Thread
    {
        private Socket s;
        private Player player;
        private ObjectInputStream input;
        private ObjectOutputStream output;


        public NetworkThread(Socket s) {
            this.s = s;
            try {
                output = new ObjectOutputStream(s.getOutputStream());
                input = new ObjectInputStream(s.getInputStream());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        public void mainLoop() {
            System.out.println("New thread " +Thread.currentThread().getId());
            Event event;
            while(true) {
                synchronized(input) {
                    try {
                        event = (Event) input.readObject();
                        // Receive event data
                        System.out.println("Received " + event + " from thread " + Thread.currentThread().getId());
                    } catch (IOException e) {
                        break;
                    } catch (ClassNotFoundException e) {
                        e.printStackTrace();
                        break;
                    }
                }
                // Platforms request
                if (event.type == event_type.REQUEST) {
                    if(event.data.equals("platforms".hashCode()))
                        event = new Event(event.type, platforms);
                    if(event.data.equals("users".hashCode()))
                        event = new Event(event.type, users);
                }

                // Create new player
                if (event.type == event_type.CREATE) {
                    // Check if server is full
                    if (users.size() == MAX_USERS)
                        event = new Event(event_type.ERROR, null);
                    else {
                        player = (Player) event.data;
                        users.put(player.id, player);
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

                    event = new Event(event.type, users);
                }
                synchronized (output) {
                    try {
                        output.writeObject(event);
                    } catch (IOException e) {
                        break;
                    }
                }
                //}
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
    }

    public abstract void run();
}
