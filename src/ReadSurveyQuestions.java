import java.io.*;
import java.util.*;

public class ReadSurveyQuestions {
    public static void main(String[] args) {
        List<String> questions = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader("survey_questions.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                questions.add(line);
            }
        } catch (FileNotFoundException e) {
            System.err.println("Error: survey_questions.txt (The system cannot find the file specified)");
        } catch (IOException e) {
            e.printStackTrace();
        }

        for (String question : questions) {
            System.out.println(question);
        }
    }
}
