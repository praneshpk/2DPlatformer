package core.network;


import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.concurrent.CopyOnWriteArrayList;

public abstract class Server <E> {

    protected static Object lock = new Object();

    public static int PORT = 4096;
    public static String HOSTNAME = "127.0.0.1";
    public static int MAX_USERS = 3;

    private ServerSocket server;
    protected ArrayList<E> users;
    protected E data;
    protected static CopyOnWriteArrayList<ObjectOutputStream> output;
    protected static CopyOnWriteArrayList<ObjectInputStream> input;

    /**
     * Class to allow multithreading across users
     */
    private class UserThread extends Thread
    {
        private Socket s;
        public UserThread(Socket s) {
            this.s = s;
        }
        public void run() {
            try {
                handleClient(s);
            } catch (Exception e) {
            }
        }
    }

    public Server()
    {
        users = new ArrayList<>(MAX_USERS);
    }

    protected abstract void IO(Socket s) throws IOException, ClassNotFoundException;

    /**
     *
     * @param s the socket to be connected to
     * @throws IOException if socket is not successfully closed
     */
    public void handleClient(Socket s) throws IOException {
        try {
            synchronized(this)
            {
                output.add(new ObjectOutputStream(s.getOutputStream()));
                input.add(new ObjectInputStream(s.getInputStream()));
            }
            IO(s);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            System.out.println(data.toString() + s.getLocalAddress() + " left.");
            users.remove(data);
            s.close();
        }

    }

    public void run()
    {
        try {
            server = new ServerSocket(PORT);
        } catch( Exception e ) {
            System.err.println("Can't initialize server: " + e);
            System.exit(1);
        }
        System.out.println("Server started on " + server.getLocalSocketAddress());

        input = new CopyOnWriteArrayList<>();
        output = new CopyOnWriteArrayList<>();

        try {
            while(true) {
                Socket s = server.accept();
                UserThread t = new UserThread(s);
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
