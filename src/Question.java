
public class Question implements java.io.Serializable{
    private String question;
    private String [] choices;
    private int correctAnswer;
    private int points;
    
    public Question(){}

    public Question(String question, String[] choices, int correctAnswer, int points) {
        this.question = question;
        this.choices = choices;
        this.correctAnswer = correctAnswer;
        this.points = points;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String[] getChoices() {
        return choices;
    }

    public void setChoices(String[] choices) {
        this.choices = choices;
    }

    public int getCorrectAnswer() {
        return correctAnswer;
    }

    public void setCorrectAnswer(int correctAnswer) {
        this.correctAnswer = correctAnswer;
    }

    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }

    @Override
    public String toString() {
        String s= question + " ("+points+ " points) \n";
        for(int i=0; i<choices.length; i++)
            s += choices[i]+"\n";
        return s;
    }
}
