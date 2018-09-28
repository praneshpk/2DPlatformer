import java.io.*;
import java.net.Socket;
import java.util.Scanner;


public class Client {
    private static DataInputStream input;
    private static DataOutputStream output;
    public static String name;

    public static void main(String[] args) throws IOException {
        Socket s = null;
        try {
            s = new Socket(Server.HOSTNAME, Server.PORT);
            output = new DataOutputStream(s.getOutputStream());
            input = new DataInputStream(s.getInputStream());

            Scanner in = new Scanner(System.in);
            System.out.print("Enter a username: ");
            name = in.nextLine();
            output.writeUTF(name);
            int iter = 0;
            while (true) {
                System.out.println(input.readUTF());
                output.writeUTF(name + ": " + in.nextLine());
            }
        } catch(Exception e) {
            e.printStackTrace();
        } finally {
            s.close();
        }
    }
}

