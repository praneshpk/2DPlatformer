package core;

import core.network.Client;
import core.objects.Collidable;
import core.objects.Player;
import processing.core.PApplet;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ConnectException;
import java.net.Socket;
import java.util.Scanner;

public class GameClient extends Client<Player> {


    private static Collidable[] platforms;
    private PApplet p;
    private Socket s;

    public GameClient(PApplet p, String host, int port)
    {
        super(host, port);
        this.p = p;
    }

    public static Collidable[] getPlatforms() { return platforms; }

    protected void initialize(Socket s) throws Exception
    {
        output = new ObjectOutputStream(s.getOutputStream());
        input = new ObjectInputStream(s.getInputStream());

        // Sending a new player object
        data = new Player();
        output.writeObject(data);

        // Receive player object with id
        data = (Player) input.readObject();

        // Throw exception if server is full
        if(data.equals(null))
            throw new Exception();

        // Receive platform information
        platforms = (Collidable[]) input.readObject();
    }
    protected void IO() throws IOException, ClassNotFoundException
    {
        // Interaction will likely be changed
        //while(true);
    }
    public void start()
    {
        try {
            s = new Socket(host, port);
            initialize(s);
        } catch(ConnectException e) {
            System.out.println("Error: Server has not been started!");
            System.exit(1);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error: Server is full!");
            System.exit(1);
        }
    }
    public Player send(Player data) throws IOException, ClassNotFoundException
    {
        output.writeObject(data);
        return (Player) input.readObject();
    }
    public void close() throws IOException {
        s.close();
    }
}
