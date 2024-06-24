import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Survey implements Serializable {
    private String surveyId;
    private String surveyTitle;
    private String creatorName;
    private List<Question> questions;

    public Survey(String surveyId, String surveyTitle, String creatorName) {
        this.surveyId = surveyId;
        this.surveyTitle = surveyTitle;
        this.creatorName = creatorName;
        this.questions = new ArrayList<>();
    }

    public String getSurveyId() {
        return surveyId;
    }

    public String getSurveyTitle() {
        return surveyTitle;
    }

    public void setSurveyTitle(String surveyTitle) {
        this.surveyTitle = surveyTitle;
    }

    public String getCreatorName() {
        return creatorName;
    }

    public void setCreatorName(String creatorName) {
        this.creatorName = creatorName;
    }

    public List<Question> getQuestions() {
        return questions;
    }

    public void addQuestion(Question question) {
        this.questions.add(question);
    }

    @Override
    public String toString() {
        return "Survey{" +
                "surveyId='" + surveyId + '\'' +
                ", surveyTitle='" + surveyTitle + '\'' +
                ", creatorName='" + creatorName + '\'' +
                ", questions=" + questions +
                '}';
    }
}
