import java.io.Serializable;

public class Question implements Serializable {
    private static final long serialVersionUID = 1L;

    private String questionId;
    private String questionPosition;
    private String questionText;
    private String surveyId;

    public Question(String questionId, String questionPosition, String questionText, String surveyId) {
        this.questionId = questionId;
        this.questionPosition = questionPosition;
        this.questionText = questionText;
        this.surveyId = surveyId;
    }

    @Override
    public String toString() {
        return "Question{" +
                "questionId='" + questionId + '\'' +
                ", questionPosition='" + questionPosition + '\'' +
                ", questionText='" + questionText + '\'' +
                ", surveyId='" + surveyId + '\'' +
                '}';
    }
}
