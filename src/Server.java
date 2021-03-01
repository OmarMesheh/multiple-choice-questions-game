import java.net.*;
import java.io.*;
import java.util.ArrayList;

public class Server {
    public static final int SERVICE_PORT = 3000;
    public static void main(String[] args){
        try {
            // Turning ON the server
            ServerSocket server = new ServerSocket(SERVICE_PORT);
            System.out.println("TCP server started");

            // loading the questions bank
            ArrayList<Question> questionsBank = loadQuestions();

            // Loop indefinitely, accept two players at a time then start a game
            while (true){
                // Get the first player
                Socket player1 = server.accept();
                // Display connections details
                System.out.println("A player has connected to the server!, Waiting for another player to start a game...");
                // send 1 to the player code to tell it that this player is the 1st player
                PrintStream pout1 = new PrintStream( player1.getOutputStream() );
                pout1.println(1);

                //Get the second player
                Socket player2 = server.accept();
                // Display connections details
                System.out.println("Another player has connected to the server!, Starting a new game!");
                // send 2 to the player code to tell it that this player is the 2nd player
                PrintStream pout2 = new PrintStream( player2.getOutputStream() );
                pout1.println(2);
                pout2.println(2);

                // Create a new thread (Game) and pass the created sockets (Players) to it
                // We pass a copy of the question bank so that the original bank is not affected inside the game
                Game nextThread = new Game (player1, player2, new ArrayList<Question>(questionsBank));
                // launch the thread (starting the game)
                nextThread.start();
            }
        }
        catch (BindException be){
            System.err.println("Service already running on port " + SERVICE_PORT);
        }
        catch (IOException ioe){
            System.err.println("I/O Error: " + ioe);
        }
    }

    // This method is responsible of loading the question bank. (it reads the questions.out file)
    public static ArrayList<Question> loadQuestions(){
        ArrayList<Question> questionArrayList;
        try
        {
            FileInputStream fis = new FileInputStream("questions.out");
            ObjectInputStream ois = new ObjectInputStream(fis);
            questionArrayList = (ArrayList) ois.readObject();
            ois.close();
            fis.close();
            return questionArrayList;
        }
        catch (IOException ioe)
        {
            ioe.printStackTrace();
            return null;
        }
        catch (ClassNotFoundException c)
        {
            System.out.println("Class not found");
            c.printStackTrace();
            return null;
        }
    }


}
