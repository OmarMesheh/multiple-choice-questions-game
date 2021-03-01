import java.net.*;
import java.io.*;
import java.util.concurrent.TimeUnit;

public class Player {
    public static final int SERVICE_PORT = 3000;
    public static String name;

    public static void main(String[] args){

        try{
            Socket player = new Socket("127.0.0.1", SERVICE_PORT);
            System.out.println("Connected to the game server!");

            // reading the player's name (from the console) before sending it to the server
            System.out.println("Enter Your username: ");
            BufferedReader console = new BufferedReader(new InputStreamReader(System.in));
            name = console.readLine();

            // preparing I/O streams
            BufferedReader reader = new BufferedReader( new InputStreamReader((player.getInputStream())));
            PrintStream pout = new PrintStream(player.getOutputStream());

            // notifying the player whether the game is ready or not
            int num = Integer.parseInt(reader.readLine());
            if (num == 1){
                // if we are here, this means that one player only is inside the server
                System.out.println("Waiting for another player to connect");
                num = Integer.parseInt(reader.readLine());
            }
            if (num == 2){
                // if we are here, this means that two players entered the server so a new game will start
                System.out.println("\n*************************************************");
                System.out.println("************* The Game has started **************");
                System.out.println("*************************************************");
                delay(1);
            }

            // sending the player's name to the server
            pout.println(name);

            // receiving the question, reading the answer from the console and sending it to the server side
            ObjectInputStream ois = new ObjectInputStream(player.getInputStream());
            for (int i=0 ; i<5; i++){
                System.out.println("\n********* Prepare for question number " + (i+1) + " *********");
                delay(2);
                // displaying the question
                System.out.println(ois.readObject());
                // reading & sending the player's answer
                System.out.print("Enter the choice number ONLY: ");
                try {
                    pout.println(Integer.parseInt(console.readLine()) + ":" + System.currentTimeMillis());
                }
                catch (NumberFormatException nfe){
                    // if we are here, this means that the player has inserted a string
                    // (not an integer that represent a choice)
                    pout.println(0 + ":" + System.currentTimeMillis());
                }

                // displaying the question result that is provided by the server
                System.out.println(reader.readLine());
                delay(2);
            }

            // displaying the final result that is provided by the server.
            System.out.println("\n*********************************************");
            System.out.println("*************** The Result is ***************");
            System.out.println("*********************************************");

            delay(2);
            System.out.println(reader.readLine());
            System.out.println(reader.readLine());

            // Close the connection
            player.close();
        }
        catch (ClassNotFoundException cnfe){
            System.out.println("could not load class");
        }
        catch (IOException ioe){
            System.err.println("IOError " + ioe);
        }
    }

    // this method is responsible of generating a time delay (by seconds)
    public static void delay(long seconds){
        try {
            TimeUnit.SECONDS.sleep(seconds);
        }
        catch (InterruptedException ie){
            System.err.println("Interrupted Exception (from sleep) " + ie);
        }
    }

}
