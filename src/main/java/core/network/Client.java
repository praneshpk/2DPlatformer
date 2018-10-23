package core.network;

import core.objects.Player;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
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

    public abstract void start();
}
