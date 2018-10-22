package core.network;

import core.objects.Player;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ConnectException;
import java.net.Socket;
import java.util.Scanner;

public class Client <E> {
    protected static String host;
    protected static int port;
    protected ObjectInputStream input;
    protected ObjectOutputStream output;
    protected static Object lock = new Object();


    public E getData() {
        return data;
    }

    protected E data;

    public void setData(E data) { this.data = data; }

    protected Scanner in;

    public Client(String host, int port)
    {
        this.host = host;
        this.port = port;
    }

    protected void initialize(Socket s) throws Exception
    {
        output = new ObjectOutputStream(s.getOutputStream());
        input = new ObjectInputStream(s.getInputStream());

        data = null;
        in = new Scanner(System.in);

        // Entering a username to data object
        System.out.print("Enter a username: ");
        ((Player) data).setUsername(in.nextLine());
        output.writeObject(data);

        // Receive player object with id
        data = (E) input.readObject();

        // Return false if server is full
        if(data.equals(null))
            throw new Exception();

        // Receive platform information
        // platforms = (Collidable[]) input.readObject();
    }

    protected void IO() throws IOException, ClassNotFoundException
    {
        // Interaction will likely be changed
        while(true) {
            System.out.println(data + " read");
            System.out.println("Type ':q' to leave the server, otherwise hit the return key to continue...");
            System.out.print(((Player) data).getUsername() + "> ");
            String txt_input = in.nextLine();
            if (txt_input.equals(":q")) {
                output.writeObject(data);
                break;
            } else {
                System.out.println(data + " sent");
                output.writeObject(data);
                data = (E) input.readObject();
            }
        }
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
