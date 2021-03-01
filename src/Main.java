import java.io.*;
import java.util.ArrayList;

public class Main {

    public static void main(String[] args) throws IOException {
	// write your code here

        ArrayList<Question> questionArrayList = loadQuestions();


        ArrayList<Question> fiveQuestions = getFiveQuestions(questionArrayList);
        int result = 0;
        for(Question question : fiveQuestions) {
            System.out.println(question);
            System.out.print("Enter the choice number only:");
            BufferedReader console = new BufferedReader(new InputStreamReader(System.in));
            try {
                int answer = Integer.parseInt(console.readLine());
                if (answer == question.getCorrectAnswer()) {
                    result += question.getPoints();
                }
            }
            catch (NumberFormatException nfe){
                System.out.println("Wrong input - 0 points for this question");
            }
        }
        System.out.println("Your result is " + result);

//        BufferedReader console = new BufferedReader(new InputStreamReader(System.in));
//        System.out.println("Enter anything");
//        int answer = Integer.parseInt(console.readLine());
//        System.out.println(answer);


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

    // This method is responsible of generating five random questions from a given bank of questions.
    public static ArrayList<Question> getFiveQuestions (ArrayList<Question> questionBank){
        ArrayList<Question> fiveQuestions = new ArrayList<>();
        for (int i =0; i<5; i++){
            //generate random index using Math.random method:
            int randomIndex = (int) (Math.random() * questionBank.size());
            //adding the random question to the result list:
            fiveQuestions.add(questionBank.get( randomIndex ));
            //removing the chosen question to make sure that no question is selected twice
            questionBank.remove(randomIndex);
        }
        return fiveQuestions;
    }

}
