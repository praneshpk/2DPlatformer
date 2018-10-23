package core;

import core.network.Client;
import core.objects.Player;
import core.util.Event;
import core.util.event_type;
import processing.core.PApplet;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ConnectException;
import java.net.Socket;

public class GameClient extends Client implements GameConstants {

    private PApplet p;
    private Socket s;

    public GameClient(PApplet p, String host, int port)
    {
        super(host, port);
        this.p = p;
    }

    protected void initialize(Socket s) throws Exception
    {
        System.out.println("I/O streams initialized...");
        output = new ObjectOutputStream(s.getOutputStream());
        input = new ObjectInputStream(s.getInputStream());
        Event e;

        synchronized (this) {
            // Creating a new player object
            System.out.println("Creating a new player object...");
            output.writeObject(new Event(event_type.CREATE, new Player()));

            // Receiving back an event
            e = (Event) input.readObject();
        }

        // Throw exception if server is full
        if(e.type == event_type.ERROR)
            throw new Exception(e.data.toString());

        player = (Player) e.data;
    }
    protected void IO() {

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
    public synchronized Event send(Event event) {
        Event ret;
        try {
            output.writeObject(event);
            ret = (Event) input.readObject();
        } catch (Exception e) {
            e.printStackTrace();
            ret = new Event(event_type.ERROR, null);
        }
        return ret;
    }
    public void close() throws IOException {
        s.close();
    }
}
