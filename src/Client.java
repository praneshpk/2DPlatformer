import java.io.*;
import java.net.ConnectException;
import java.net.Socket;
import java.net.SocketException;
import java.util.Scanner;



public class Client {
    private static DataInputStream input;
    private static DataOutputStream output;
    public static String name;

    public static void main(String[] args) throws IOException {
        Socket s = null;
        String response = "";
        try {
            s = new Socket(Server.HOSTNAME, Server.PORT);
            output = new DataOutputStream(s.getOutputStream());
            input = new DataInputStream(s.getInputStream());

            Scanner in = new Scanner(System.in);

            System.out.print("Enter a username: ");
            name = in.nextLine();
            output.writeUTF(name);
            response = input.readUTF();
            while(response.equals("err")) {
                System.out.print("Error: User already exists!\nEnter a username: ");
                name = in.nextLine();
                output.writeUTF(name);
                response = input.readUTF();
            }
            if(response.contains("Error"))
                throw new Exception();
            String data = "";
            while(true) {
                for(int i=0; i<10; i++)
                    System.out.println("\b");
                System.out.println(response);

                System.out.println("Type ':q' to leave the server");
                System.out.print(name + "> ");
                data = in.nextLine();
                if(data.equals(":q")) {
                    output.writeUTF(name + " has left the chat.");
                    break;
                }
                else {
                    output.writeUTF(name + ": " + data);
                    response = input.readUTF();
                }
            }

        } catch(ConnectException e) {
            System.out.println("Error: Server has not been started!");
            System.exit(1);
        } catch(SocketException e) {
            System.out.println("Error: Server has been suspended!");
            System.exit(1);
        } catch (Exception e) {
            System.out.println(response);
            System.exit(1);
        } finally {
            System.out.println("You have left the server.");
            s.close();
        }
    }
}

