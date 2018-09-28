import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;


public class Server {

    public static int PORT = 4096;
    public static String HOSTNAME = "127.0.0.1";

    private ServerSocket server;

    private static class User {
        String name;
        int id;
    }

    private User[] users;

    /**
     Class to allow multithreading across users
     */
    private class UserThread extends Thread {
        private Socket s;
        public UserThread(Socket s) {
            this.s = s;
        }
        public void run() {
            handleClient(s);
        }
    }

    public void handleClient(Socket s) {
        String client = null;
        try {
            DataOutputStream output = new DataOutputStream(s.getOutputStream());
            DataInputStream input = new DataInputStream(s.getInputStream());

            client = input.readUTF();
            System.out.println(client + "/" + s.getLocalAddress() + " joined.");
            output.writeUTF(client + " has joined the server.\n");

            String log = input.readUTF();
            while(!log.equals(":Q")) {
                output.writeUTF(log);
                log = input.readUTF();
            }
            // block
        } catch (Exception e) {
            // to-do
        } finally {
            System.out.println(client + "/" + s.getLocalAddress() + " left.");
            try {
                s.close();
            } catch(Exception e) {

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
