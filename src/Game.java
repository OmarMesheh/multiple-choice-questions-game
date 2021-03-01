import java.io.*;
import java.net.Socket;
import java.util.ArrayList;

public class Game extends Thread{
        Socket player1;
        Socket player2;
        ArrayList<Question> questionsBank;
        static int sum1 = 0;
        static int sum2 = 0;
        static String name1;
        static String name2;

        public Game (Socket player1, Socket player2, ArrayList<Question> questionsBank){
            this.player1= player1;
            this.player2= player2;
            this.questionsBank = questionsBank;
        }

        @Override
        public void run() {
            try {
                // Preparing I/O streams
                BufferedReader reader1 = new BufferedReader( new InputStreamReader( player1.getInputStream() ));
                BufferedReader reader2 = new BufferedReader( new InputStreamReader( player2.getInputStream() ));
                PrintStream pout1 = new PrintStream(player1.getOutputStream());
                PrintStream pout2 = new PrintStream(player2.getOutputStream());

                // get the first player's name
                name1 = reader1.readLine();
                // get the second player's name
                name2 = reader2.readLine();

                ObjectOutputStream oos1 = new ObjectOutputStream(player1.getOutputStream());
                ObjectOutputStream oos2 = new ObjectOutputStream(player2.getOutputStream());

                // the actual starting point of the game
                for (int i=0 ; i<5 ; i++){
                    delay(2);

                    // sending the question to the players as a Question object
                    Question question = generateQuestion(questionsBank);
                    oos1.writeObject(question);
                    oos2.writeObject(question);

                    // Reading player1's answer
                    String response1 = reader1.readLine();
                    String answer1 = response1.split(":")[0];
                    long time1 = Long.parseLong(response1.split(":")[1]);

                    // Reading player2's answer
                    String response2 = reader2.readLine();
                    String answer2 = response2.split(":")[0];
                    long time2 = Long.parseLong(response2.split(":")[1]);

                    delay(1);

                    String questionResult = findQuestionResult(question,answer1,answer2,time1,time2);

                    // sending the current question result
                    pout1.println(questionResult);
                    pout2.println(questionResult);
                    delay(1);
                }

                // Sending the Final result to both players based on their collected points
                delay(2);
                if (sum1 > sum2){
                    pout1.println(name1 + " is the winner with " + sum1 + " points.");
                    pout1.println(name2 + " is the loser with " + sum2 + " points.");
                    pout2.println(name1 + " is the winner with " + sum1 + " points.");
                    pout2.println(name2 + " is the loser with " + sum2 + " points.");
                }
                else if (sum2 > sum1){
                    pout1.println(name2 + " is the winner with " + sum2 + " points.");
                    pout1.println(name1 + " is the loser with " + sum1 + " points.");
                    pout2.println(name2 + " is the winner with " + sum2 + " points.");
                    pout2.println(name1 + " is the loser with " + sum1 + " points.");
                }
                else {
                    pout1.println("DRAW, Both players got " + sum1 + " points.");
                    pout2.println("DRAW, Both players got " + sum1 + " points.");
                    pout1.println(""); pout2.println("");
                }

                // Flush unsent bytes
                pout1.flush();
                pout2.flush();
                // Close stream
                pout1.close();
                pout2.close();
            }
            catch (IOException ioe){
                System.err.println("I/O Error :" + ioe);
            }
        }

        // This method is responsible of generating a random question from a given bank of questions.
        public static Question generateQuestion (ArrayList<Question> bank){
            Question question;
            // generate random index using Math.random method
            int randomIndex = (int) (Math.random() * bank.size());
            // choosing a random question from the questions bank
            question = bank.get(randomIndex);
            // removing the chosen question to make sure that no question is selected twice
            bank.remove(randomIndex);
            return question;
        }

        // this method is responsible of generating a time delay (by seconds)
        public static void delay(long seconds){
            try {
                Game.sleep(seconds * 1000);
            }
            catch (InterruptedException ie){
                System.err.println("Interrupted Exception (from sleep) " + ie);
            }
        }

        // this method is responsible of finding the specific question result
        // based on the players choices & their response time
        public static String findQuestionResult (Question q, String s1, String s2, long t1, long t2){
            if (t1<t2){
                if (Integer.parseInt(s1) == q.getCorrectAnswer()){
                    // if we reached this point, this means that player1 got the correct answer before player2
                    sum1 += q.getPoints();
                    return q.getChoices()[q.getCorrectAnswer()-1] + " is the correct Answer, " +
                            q.getPoints() + " points goes to " + name1;

                }
                else if (Integer.parseInt(s2) == q.getCorrectAnswer()){
                    // if we reached this point, this means that player2 got the correct answer and player1 is faster but wrong
                    sum2 += q.getPoints();
                    return q.getChoices()[q.getCorrectAnswer()-1] + " is the correct Answer, " +
                            q.getPoints() + " points goes to " + name2;

                }
                else {
                    // if we reached this point, this means that neither player1 nor player2 is correct
                    return  "both players provided wrong answers, No points are provided. " +
                            "The correct answer is : " + q.getChoices()[q.getCorrectAnswer()-1];
                }
            }
            else if(t2<t1){
                if (Integer.parseInt(s2) == q.getCorrectAnswer()){
                    // if we reached this point, this means that player2 got the correct answer before player1
                    sum2 += q.getPoints();
                    return q.getChoices()[q.getCorrectAnswer()-1] + " is the correct Answer, " +
                            q.getPoints() + " points goes to " + name2;

                }
                else if (Integer.parseInt(s1) == q.getCorrectAnswer()){
                    // if we reached this point, this means that player1 got the correct answer and player2 is faster but wrong
                    sum1 += q.getPoints();
                    return q.getChoices()[q.getCorrectAnswer()-1] + " is the correct Answer, " +
                            q.getPoints() + " points goes to " + name1;

                }
                else {
                    // if we reached this point, this means that neither player1 nor player2 is correct
                    return  "both players provided wrong answers, No points are provided. " +
                            "The correct answer is : " + q.getChoices()[q.getCorrectAnswer()-1];
                }
            }
            return null;
        }

}
