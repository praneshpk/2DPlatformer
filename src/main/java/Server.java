import java.io.*;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Scanner;


public class Server {

    public static int PORT = 4096;
    public static String HOSTNAME = "127.0.0.1";
    public static int MAX_USERS = 3;

    private ServerSocket server;

    private ArrayList<Player> users = new ArrayList<>(MAX_USERS);
    private static Object lock = new Object();

    /**
     * Class to allow multithreading across users
     */
    private class UserThread extends Thread {
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

    /**
     *
     * @param s the socket to be connected to
     * @throws IOException if socket is not successfully closed
     */
    public void handleClient(Socket s) throws IOException {
        Player data = new Player();
        InetAddress ip = s.getLocalAddress();
        ObjectOutputStream output = new ObjectOutputStream(s.getOutputStream());
        ObjectInputStream input = new ObjectInputStream(s.getInputStream());
        boolean fin = true;
        try {
            data = (Player) input.readObject();
            if(users.size() == MAX_USERS)
                throw new IllegalStateException();

            // Assign id based on location in list
            data.id = users.size();
            users.add(data);
            System.out.println(data.username + ip + " joined.");

            output.writeObject(data);
            data = (Player) input.readObject();
            while(true) {
                // Update server user list
                synchronized ( lock ) {
                    users.set(data.id, data);
                    lock.notifyAll();
                }
                // Perform something. Sending back data object for now
                output.writeObject(data);
                data = (Player) input.readObject();
            }
        } catch (IllegalStateException e) {
            s.close();
            fin = false;
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } finally {
            if(fin) {
                System.out.println(data.username + ip + " left.");
                users.remove(data);
                s.close();
            }

        }

    }

    private void run()
    {
        try {
            server = new ServerSocket(PORT);
        } catch( Exception e ) {
            System.err.println("Can't initialize server: " + e);
            System.exit(1);
        }
        Scanner in = new Scanner(System.in);
        String input = "";
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
    public static void main(String[] args)
    {
        Server server = new Server();
        server.run();
    }

}
