package core.network;


import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class Server <E> {

    protected static Object lock = new Object();

    public static int PORT = 4096;
    public static String HOSTNAME = "127.0.0.1";
    public static int MAX_USERS = 3;

    private ServerSocket server;
    protected ArrayList<E> users;
    protected E data;
    protected ObjectOutputStream output;
    protected ObjectInputStream input;

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

    public Server(int max, ArrayList<E> users)
    {
        MAX_USERS = max;
        this.users = users;

    }

    protected void initialize(Socket s) throws IOException, ClassNotFoundException {
        output = new ObjectOutputStream(s.getOutputStream());
        input = new ObjectInputStream(s.getInputStream());

        data = (E) input.readObject();
        if(users.size() == MAX_USERS)
            throw new IllegalStateException();

        // Assign id based on location in list
        users.add(data);
        System.out.println(data.toString() + s.getLocalAddress() + " joined.");

        data = (E) input.readObject();
    }

    protected void IO(Socket s) throws IOException, ClassNotFoundException {
        while(true) {
            // Update server user list
            synchronized ( lock ) {
                // boiler plate for modifying user list
                lock.notifyAll();
            }
            // Perform something. Sending back data object for now
            output.writeObject(data);
            data = (E) input.readObject();
        }
    }

    /**
     *
     * @param s the socket to be connected to
     * @throws IOException if socket is not successfully closed
     */
    public void handleClient(Socket s) throws IOException {
        boolean fin = true;

        try {
            initialize(s);
            IO(s);
        } catch (IllegalStateException e) {
            s.close();
            fin = false;
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } finally {
            if(fin) {
                System.out.println(data.toString() + s.getLocalAddress() + " left.");
                users.remove(data);
                s.close();
            }

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
