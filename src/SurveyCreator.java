import java.io.Serializable;

public class SurveyCreator implements Serializable {
    private static final long serialVersionUID = 1L;

    private String username;
    private String password;
    private String firstName;
    private String lastName;
    private String faculty;
    private String email;
    private String gender;
    private String phoneNo;

    public SurveyCreator(String username, String password, String firstName, String lastName, String faculty, String email, String gender, String phoneNo) {
        this.username = username;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
        this.faculty = faculty;
        this.email = email;
        this.gender = gender;
        this.phoneNo = phoneNo;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    @Override
    public String toString() {
        return "SurveyCreator{" +
                "username='" + username + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", faculty='" + faculty + '\'' +
                ", email='" + email + '\'' +
                ", gender='" + gender + '\'' +
                ", phoneNo='" + phoneNo + '\'' +
                '}';
    }
}
