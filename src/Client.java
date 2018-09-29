import java.io.*;
import java.net.ConnectException;
import java.net.Socket;
import java.net.SocketException;
import java.util.Scanner;



public class Client {
    private static ObjectInputStream input;
    private static ObjectOutputStream output;

    public static void main(String[] args) throws IOException {
        Socket s = null;
        Player data = new Player();
        try {
            s = new Socket(Server.HOSTNAME, Server.PORT);
            output = new ObjectOutputStream(s.getOutputStream());
            input = new ObjectInputStream(s.getInputStream());

            Scanner in = new Scanner(System.in);
            String txt_input;

            System.out.print("Enter a username: ");
            data.username = in.nextLine();
            output.writeObject(data);
            data = (Player) input.readObject();
            /* Deprecating same username check for now. */
//            while(response != null) {
//                System.out.print("Error: User already exists!\nEnter a username: ");
//                data.username = in.nextLine();
//                output.writeObject(data);
//                response = (Player) input.readObject();
//            }
            if(data.equals(null))
                throw new Exception();
            while(true) {
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

