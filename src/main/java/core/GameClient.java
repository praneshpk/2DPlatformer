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

public class GameClient extends Client<Player> implements GameConstants {

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

        // Creating a new player object
        System.out.println("Creating a new player object...");
        output.writeObject(new Event(event_type.CREATE, new Player()));

        // Receiving back an event
        Event e = (Event) input.readObject();

        // Throw exception if server is full
        if(e.type == event_type.ERROR)
            throw new Exception(e.data.toString());

        data = (Player) e.data;
    }
    protected void IO() throws IOException, ClassNotFoundException {
        // Interaction will likely be changed
        while(true) {
            output.writeObject(data);
            data = (Player) input.readObject();
        }
    }
    public void start()
    {
        try {
            s = new Socket(host, port);
            initialize(s);
            //IO();
        } catch(ConnectException e) {
            System.out.println("Error: Server has not been started!");
            System.exit(1);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error: Server is full!");
            System.exit(1);
        }
    }
    public Event send(Event event) {
        try {
            output.writeObject(event);
            return (Event) input.readObject();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new Event(event_type.ERROR, null);
    }
    public void close() throws IOException {
        s.close();
    }
}
