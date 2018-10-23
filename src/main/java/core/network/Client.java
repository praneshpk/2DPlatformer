package core.network;

import core.objects.Player;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ConnectException;
import java.net.Socket;
import java.util.Scanner;

public abstract class Client {
    protected static String host;
    protected static int port;
    protected ObjectInputStream input;
    protected ObjectOutputStream output;

    protected Player player;

    protected Scanner in;

    public Client(String host, int port)
    {
        this.host = host;
        this.port = port;
    }

    protected abstract void initialize(Socket s) throws Exception;

    protected abstract void IO();

    public Player getPlayer() {
        return player;
    }

    public void start()
    {
        // Socket for client to connect to
        Socket s = null;

        try {
            s = new Socket(host, port);
            initialize(s);
            IO();

        } catch(ConnectException e) {
            System.out.println("Error: Server has not been started!");
            System.exit(1);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error: Server is full!");
            System.exit(1);
        } finally {
            System.out.println("You have left the server.");
            try {
                s.close();
            } catch (IOException e) {
                System.out.println("An error occurred while closing the network socket");
            }
        }
    }
}
