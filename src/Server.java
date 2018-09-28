import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
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

    private ArrayList<String> users = new ArrayList<>();
    private static String log = "";
    private static Object lock = new Object();

    /**
     Class to allow multithreading across users
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

    public void handleClient(Socket s) throws IOException {
        String client = "";
        InetAddress ip = s.getLocalAddress();
        DataOutputStream output = new DataOutputStream(s.getOutputStream());
        DataInputStream input = new DataInputStream(s.getInputStream());
        boolean fin = true;
        try {
            client = input.readUTF();
            if(users.size() == MAX_USERS)
                throw new IllegalStateException();
            while(users.contains(client)) {
                output.writeUTF("err");
                client = input.readUTF();
            }
            users.add(client);
            System.out.println(client + ip + " joined.");
            log += client + " has joined the server.\n";
            output.writeUTF(log);
            String request = input.readUTF();
            while(true) {
                synchronized ( lock ) {
                    log += request + "\n";
                    lock.notifyAll();
                }
                output.writeUTF(log);
                request = input.readUTF();
            }
        } catch (IllegalStateException e) {
            output.writeUTF("Error: Server is full!");
            s.close();
            fin = false;
        } finally {
            if(fin) {
                System.out.println(client + ip + " left.");
                users.remove(client);
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
