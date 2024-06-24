import java.io.*;

public class CreateSurveyQuestionsFile {
    public static void main(String[] args) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("survey_questions.txt"))) {
            writer.write("What is your favorite color?");
            writer.newLine();
            writer.write("How often do you exercise?");
            writer.newLine();
            writer.write("What is your preferred programming language?");
            writer.newLine();
            System.out.println("survey_questions.txt created successfully.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
