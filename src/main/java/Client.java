import java.io.*;
import java.net.ConnectException;
import java.net.Socket;
import java.util.Scanner;


/**
 * Class to connect to Server object
 */
public class Client {

    private static ObjectInputStream input;
    private static ObjectOutputStream output;

    /**
     * Starts the client program
     * @param args commandline arguments
     * @throws IOException if socket is not successfully closed
     */
    public static void main(String[] args) throws IOException {

        // Socket for client to connect to
        Socket s = null;

        // Player object that will be sent back and forth
        Player data = new Player();

        try {
            s = new Socket(Server.HOSTNAME, Server.PORT);
            output = new ObjectOutputStream(s.getOutputStream());
            input = new ObjectInputStream(s.getInputStream());

            // Initializing for user input (likely will be replaced)
            Scanner in = new Scanner(System.in);
            String txt_input;

            // Entering a username to data object
            System.out.print("Enter a username: ");
            data.username = in.nextLine();
            output.writeObject(data);
            data = (Player) input.readObject();

            // Throw an exception if server is full
            if(data.equals(null))
                throw new Exception();

            // Main loop for sending / receiving player data
            while(true) {
                // Interaction will likely be changed
                System.out.println(data + " read");
                System.out.println("Type ':q' to leave the server, otherwise hit the return key to continue...");
                System.out.print(data.username + "> ");
                txt_input = in.nextLine();
                if(txt_input.equals(":q")) {
                    output.writeObject(data);
                    break;
                }
                else {
                    System.out.println(data + " sent");
                    output.writeObject(data);
                    data = (Player) input.readObject();
                }
            }

        } catch(ConnectException e) {
            System.out.println("Error: Server has not been started!");
            System.exit(1);
        } catch (Exception e) {
            System.out.println("Error: Server is full!");
            System.exit(1);
        } finally {
            System.out.println("You have left the server.");
            s.close();
        }
    }
}

