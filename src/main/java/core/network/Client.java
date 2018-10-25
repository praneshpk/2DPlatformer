package core.network;

import core.util.Constants;
import core.objects.Player;
import core.util.Event;
import core.util.event_type;
import processing.core.PApplet;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ConnectException;
import java.net.Socket;

public class Client implements Constants {

    private PApplet p;
    private Socket s;

    private static String host;
    private static int port;
    private ObjectInputStream input;
    private ObjectOutputStream output;



    private Player player;

    public Client(String host, int port)
    {
        this.host = host;
        this.port = port;
    }

    protected void initialize(Socket s) throws Exception
    {
        // Initialize IO streams
        output = new ObjectOutputStream(s.getOutputStream());
        input = new ObjectInputStream(s.getInputStream());

        // Create a new player object
        Event e = send(new Event(event_type.CREATE, new Player()), false);

        // Throw exception if server is full
        if(e.type == event_type.ERROR)
            throw new IllegalStateException(e.data.toString());

        player = (Player) e.data;
    }

    public void start()
    {
        try {
            s = new Socket(host, port);
            initialize(s);
        } catch(ConnectException e) {
            System.out.println("Error: Server has not been started!");
            System.exit(1);
        } catch (IllegalStateException e) {
            System.out.println("Error: Server is full!");
            System.exit(1);
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(1);
        }
    }
    public long getServerTime() {
        Event e = send(new Event(event_type.REQUEST, "time".hashCode()), false);
        return (long) e.data;
    }

    public Player getPlayer() {
        return player;
    }

    public synchronized Event send(Event event, boolean uncached) {
        try {
            if(uncached)
                output.reset();
            output.writeObject(event);
            return (Event) input.readObject();
        } catch (Exception e) {
            System.out.println("Error: Server has been stopped");
            System.exit(1);
        }
        return new Event(event_type.ERROR, null);
    }
}
