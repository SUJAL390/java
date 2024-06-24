import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

import java.io.Serializable;
public class LoginApp extends JFrame {
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JComboBox<String> userTypeComboBox;

    private static final String ADMIN_USERNAME = "admin";
    private static final String ADMIN_PASSWORD_HASH = "21232f297a57a5a743894a0e4a801fc3"; // Hash for "admin"

    private JFrame currentFrame; // Track current visible frame

    public LoginApp() {
        setTitle("Login");
        setSize(500, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(4, 2));

        JLabel usernameLabel = new JLabel("Username:");
        usernameField = new JTextField();
        JLabel passwordLabel = new JLabel("Password:");
        passwordField = new JPasswordField();

        JLabel userTypeLabel = new JLabel("User Type:");
        String[] userTypes = {"Admin", "Survey Creator","user"};
        userTypeComboBox = new JComboBox<>(userTypes);

        panel.add(usernameLabel);
        panel.add(usernameField);
        panel.add(passwordLabel);
        panel.add(passwordField);
        panel.add(userTypeLabel);
        panel.add(userTypeComboBox);

        JButton loginButton = new JButton("Login");
        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String username = usernameField.getText();
                char[] passwordChars = passwordField.getPassword();
                String password = new String(passwordChars);
                String userType = (String) userTypeComboBox.getSelectedItem();

                if (userType.equals("Admin")) {
                    if (isValidAdminLogin(username, password)) {
                        JOptionPane.showMessageDialog(LoginApp.this, "Admin login successful!");
                        openAdminDashboard();
                    } else {
                        JOptionPane.showMessageDialog(LoginApp.this, "Admin login failed. Try again.");
                        passwordField.setText("");
                    }
                } else if (userType.equals("Survey Creator")) {
                    if (isFirstTimeSurveyCreator(username)) {
                        int option = JOptionPane.showConfirmDialog(LoginApp.this,
                                "It seems you are a new Survey Creator. Would you like to register?",
                                "Survey Creator Registration", JOptionPane.YES_NO_OPTION);
                        if (option == JOptionPane.YES_OPTION) {
                            openSurveyCreatorRegistration();
                        }
                    } else {
                        if (isValidSurveyCreatorLogin(username, password)) {
                            JOptionPane.showMessageDialog(LoginApp.this, "Survey Creator login successful!");
                            openSurveyCreatorInterface();
                        } else {
                            JOptionPane.showMessageDialog(LoginApp.this, "Survey Creator login failed. Try again.");
                            passwordField.setText("");
                        }
                    }
                }
                else if (userType.equals("user")) {
                    if (isFirstTimeUser(username)) {
                        int option = JOptionPane.showConfirmDialog(LoginApp.this,
                                "It seems you are a new user. Would you like to register?",
                                "User Registration", JOptionPane.YES_NO_OPTION);
                        if (option == JOptionPane.YES_OPTION) {
                            openUserRegistration();
                        }
                    } else {
                        if (isValidUserLogin(username, password)) {
                            JOptionPane.showMessageDialog(LoginApp.this, "User login successful!");
                            openUserInterface();
                        } else {
                            JOptionPane.showMessageDialog(LoginApp.this, "User login failed. Try again.");
                            passwordField.setText("");
                        }
                    }
                }

            }
        });

        panel.add(loginButton);

        add(panel);
        setVisible(true);
    }

    private boolean isValidAdminLogin(String username, String password) {
        try {
            return username.equals(ADMIN_USERNAME) && hashPassword(password).equals(ADMIN_PASSWORD_HASH);
        } catch (NoSuchAlgorithmException e) {
            JOptionPane.showMessageDialog(this, "Error hashing password: " + e.getMessage());
            return false;
        }
    }

    private boolean isFirstTimeSurveyCreator(String username) {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream("survey_creators.ser"))) {
            List<SurveyCreator> surveyCreators = (List<SurveyCreator>) ois.readObject();
            for (SurveyCreator sc : surveyCreators) {
                if (sc.getUsername().equals(username)) {
                    return false;
                }
            }
        } catch (IOException | ClassNotFoundException e) {
            JOptionPane.showMessageDialog(this, "Error reading survey creator details: " + e.getMessage());
        }
        return true;
    }

    private void openSurveyCreatorRegistration() {
        JFrame registrationFrame = new JFrame("Survey Creator Registration");
        registrationFrame.setSize(400, 300);
        registrationFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(9, 2));

        JLabel usernameLabel = new JLabel("Username:");
        JTextField usernameField = new JTextField();
        JLabel passwordLabel = new JLabel("Password:");
        JPasswordField passwordField = new JPasswordField();
        JLabel firstNameLabel = new JLabel("First Name:");
        JTextField firstNameField = new JTextField();
        JLabel lastNameLabel = new JLabel("Last Name:");
        JTextField lastNameField = new JTextField();
        JLabel facultyLabel = new JLabel("Faculty:");
        JTextField facultyField = new JTextField();
        JLabel emailLabel = new JLabel("Email Address:");
        JTextField emailField = new JTextField();
        JLabel genderLabel = new JLabel("Gender:");
        JTextField genderField = new JTextField();
        JLabel phoneNoLabel = new JLabel("Phone Number:");
        JTextField phoneNoField = new JTextField();

        panel.add(usernameLabel);
        panel.add(usernameField);
        panel.add(passwordLabel);
        panel.add(passwordField);
        panel.add(firstNameLabel);
        panel.add(firstNameField);
        panel.add(lastNameLabel);
        panel.add(lastNameField);
        panel.add(facultyLabel);
        panel.add(facultyField);
        panel.add(emailLabel);
        panel.add(emailField);
        panel.add(genderLabel);
        panel.add(genderField);
        panel.add(phoneNoLabel);
        panel.add(phoneNoField);

        JButton registerButton = new JButton("Register");
        registerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String username = usernameField.getText();
                String password = new String(passwordField.getPassword());
                String firstName = firstNameField.getText();
                String lastName = lastNameField.getText();
                String faculty = facultyField.getText();
                String email = emailField.getText();
                String gender = genderField.getText();
                String phoneNo = phoneNoField.getText();

                try {
                    password = hashPassword(password);
                    SurveyCreator sc = new SurveyCreator(username, password, firstName, lastName, faculty, email, gender, phoneNo);
                    saveSurveyCreator(sc);
                    JOptionPane.showMessageDialog(registrationFrame, "Survey Creator registered successfully.");
                    registrationFrame.dispose();
                    openFrame(currentFrame); // Go back to login frame
                } catch (NoSuchAlgorithmException | IOException ex) {
                    JOptionPane.showMessageDialog(registrationFrame, "Error registering survey creator: " + ex.getMessage());
                }
            }
        });

        JButton backButton = new JButton("Back");
        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                registrationFrame.dispose();
//                loginApp(); // Go back to login frame
            }
        });

        panel.add(registerButton);
        panel.add(backButton);

        registrationFrame.add(panel);
        registrationFrame.setVisible(true);
        currentFrame = registrationFrame; // Set current frame to this registration frame
    }

    private boolean isValidSurveyCreatorLogin(String username, String password) {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream("survey_creators.ser"))) {
            List<SurveyCreator> surveyCreators = (List<SurveyCreator>) ois.readObject();
            for (SurveyCreator sc : surveyCreators) {
                if (sc.getUsername().equals(username) && sc.getPassword().equals(hashPassword(password))) {
                    return true;
                }
            }
        } catch (IOException | ClassNotFoundException | NoSuchAlgorithmException e) {
            JOptionPane.showMessageDialog(this, "Error reading survey creator details: " + e.getMessage());
        }
        return false;
    }

    private void openAdminDashboard() {
        JFrame adminDashboard = new JFrame("Admin Dashboard");
        adminDashboard.setSize(600, 400);
        adminDashboard.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel panel = new JPanel();
        panel.setLayout(new FlowLayout());

        JButton manageSettingsButton = new JButton("Manage System Settings");
        manageSettingsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(adminDashboard, "Opening System Settings Management...");
            }
        });

        JButton manageDataButton = new JButton("Manage Data");
        manageDataButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                openDataManagement();
            }
        });

        JButton backButton = new JButton("Back");
        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                adminDashboard.dispose();

            }
        });

        panel.add(manageSettingsButton);
        panel.add(manageDataButton);
        panel.add(backButton);

        adminDashboard.add(panel);
        adminDashboard.setVisible(true);
        currentFrame = adminDashboard; // Set current frame to this admin dashboard frame
    }

    private void openDataManagement() {
        JFrame dataManagementFrame = new JFrame("Data Management");
        dataManagementFrame.setSize(600, 400);
        dataManagementFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());

        JTextArea dataTextArea = new JTextArea();
        JScrollPane scrollPane = new JScrollPane(dataTextArea);
        panel.add(scrollPane, BorderLayout.CENTER);

        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream("survey_creators.ser"))) {
            List<SurveyCreator> surveyCreators = (List<SurveyCreator>) ois.readObject();
            for (SurveyCreator sc : surveyCreators) {
                dataTextArea.append(sc.toString() + "\n");
            }
        } catch (IOException | ClassNotFoundException e) {
            dataTextArea.append("Error reading survey creator details: " + e.getMessage() + "\n");
        }

        JButton closeButton = new JButton("Close");
        closeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dataManagementFrame.dispose();
                 // Go back to previous frame
            }
        });

        JButton backButton = new JButton("Back");
        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dataManagementFrame.dispose();
                openAdminDashboard(); // Go back to admin dashboard
            }
        });

        panel.add(closeButton, BorderLayout.SOUTH);
        panel.add(backButton, BorderLayout.NORTH);

        dataManagementFrame.add(panel);
        dataManagementFrame.setVisible(true);
        currentFrame = dataManagementFrame; // Set current frame to this data management frame
    }

    private void openSurveyCreatorInterface() {
        JFrame surveyCreatorInterface = new JFrame("Survey Creator Interface");
        surveyCreatorInterface.setSize(600, 400);
        surveyCreatorInterface.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel panel = new JPanel();
        panel.setLayout(new FlowLayout());

        JButton createSurveyButton = new JButton("Create Survey");
        createSurveyButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                openSurveyForm();
            }
        });

        JButton backButton = new JButton("Back");
        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                surveyCreatorInterface.dispose();
                 // Go back to login frame
            }
        });

        panel.add(createSurveyButton);
        panel.add(backButton);

        surveyCreatorInterface.add(panel);
        surveyCreatorInterface.setVisible(true);
        currentFrame = surveyCreatorInterface; // Set current frame to this survey creator interface frame
    }

    private void openSurveyForm() {
        JFrame surveyForm = new JFrame("Survey Form");
        surveyForm.setSize(600, 400);
        surveyForm.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(6, 1));

        JLabel surveyIdLabel = new JLabel("Survey ID:");
        JTextField surveyIdField = new JTextField();
        JLabel surveyTitleLabel = new JLabel("Survey Title:");
        JTextField surveyTitleField = new JTextField();
        JLabel scIdLabel = new JLabel("SC ID:");
        JTextField scIdField = new JTextField();
        JLabel creatorNameLabel = new JLabel("Creator Name:");
        JTextField creatorNameField = new JTextField();

        panel.add(surveyIdLabel);
        panel.add(surveyIdField);
        panel.add(surveyTitleLabel);
        panel.add(surveyTitleField);
        panel.add(scIdLabel);
        panel.add(scIdField);
        panel.add(creatorNameLabel);
        panel.add(creatorNameField);

        JLabel questionTypeLabel = new JLabel("Question Type:");
        String[] questionTypes = {"MCQ", "Open-ended", "Another Type"};
        JComboBox<String> questionTypeComboBox = new JComboBox<>(questionTypes);
        panel.add(questionTypeLabel);
        panel.add(questionTypeComboBox);

        JButton addQuestionButton = new JButton("Add Question");
        addQuestionButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String selectedType = (String) questionTypeComboBox.getSelectedItem();
                openQuestionForm(selectedType);
            }
        });
        panel.add(addQuestionButton);

        JButton submitButton = new JButton("Submit Survey");
        submitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                surveyForm.dispose();
                JOptionPane.showMessageDialog(null, "Survey submitted successfully!");
                openFrame(currentFrame); // Go back to previous frame
            }
        });
        panel.add(submitButton);

        JButton backButton = new JButton("Back");
        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                surveyForm.dispose();
                openSurveyCreatorInterface(); // Go back to survey creator interface
            }
        });

        panel.add(backButton);

        surveyForm.add(panel);
        surveyForm.setVisible(true);
        currentFrame = surveyForm; // Set current frame to this survey form frame
    }
//User
private void openUserRegistration() {
    JFrame registrationFrame = new JFrame("User Registration");
    registrationFrame.setSize(400, 300);
    registrationFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

    JPanel panel = new JPanel();
    panel.setLayout(new GridLayout(9, 2));

    JLabel usernameLabel = new JLabel("Username:");
    JTextField usernameField = new JTextField();
    JLabel passwordLabel = new JLabel("Password:");
    JPasswordField passwordField = new JPasswordField();
    JLabel firstNameLabel = new JLabel("First Name:");
    JTextField firstNameField = new JTextField();
    JLabel lastNameLabel = new JLabel("Last Name:");
    JTextField lastNameField = new JTextField();
    JLabel facultyLabel = new JLabel("Faculty:");
    JTextField facultyField = new JTextField();
    JLabel emailLabel = new JLabel("Email Address:");
    JTextField emailField = new JTextField();
    JLabel genderLabel = new JLabel("Gender:");
    JTextField genderField = new JTextField();
    JLabel phoneNoLabel = new JLabel("Phone Number:");
    JTextField phoneNoField = new JTextField();

    panel.add(usernameLabel);
    panel.add(usernameField);
    panel.add(passwordLabel);
    panel.add(passwordField);
    panel.add(firstNameLabel);
    panel.add(firstNameField);
    panel.add(lastNameLabel);
    panel.add(lastNameField);
    panel.add(facultyLabel);
    panel.add(facultyField);
    panel.add(emailLabel);
    panel.add(emailField);
    panel.add(genderLabel);
    panel.add(genderField);
    panel.add(phoneNoLabel);
    panel.add(phoneNoField);

    JButton registerButton = new JButton("Register");
    registerButton.addActionListener(new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            String username = usernameField.getText();
            String password = new String(passwordField.getPassword());
            String firstName = firstNameField.getText();
            String lastName = lastNameField.getText();
            String faculty = facultyField.getText();
            String email = emailField.getText();
            String gender = genderField.getText();
            String phoneNo = phoneNoField.getText();

            try {
                password = hashPassword(password);
                SurveyCreator sc = new SurveyCreator(username, password, firstName, lastName, faculty, email, gender, phoneNo);
                saveSurveyCreator(sc);
                JOptionPane.showMessageDialog(registrationFrame, "Survey Creator registered successfully.");
                registrationFrame.dispose();
                openFrame(currentFrame); // Go back to login frame
            } catch (NoSuchAlgorithmException | IOException ex) {
                JOptionPane.showMessageDialog(registrationFrame, "Error registering survey creator: " + ex.getMessage());
            }
        }
    });

    JButton backButton = new JButton("Back");
    backButton.addActionListener(new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            registrationFrame.dispose();
//                loginApp(); // Go back to login frame
        }
    });

    panel.add(registerButton);
    panel.add(backButton);

    registrationFrame.add(panel);
    registrationFrame.setVisible(true);
    currentFrame = registrationFrame; // Set current frame to this registration frame
}
    private void openUserInterface() {
        JFrame userInterface = new JFrame("User Interface");
        userInterface.setSize(600, 400);
        userInterface.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel panel = new JPanel();
        panel.setLayout(new FlowLayout());

        JButton createSurveyButton = new JButton("Create Survey");
        createSurveyButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                openSurveyForm();
            }
        });

        JButton backButton = new JButton("Back");
        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                userInterface.dispose();
                // Go back to login frame
            }
        });

        panel.add(createSurveyButton);
        panel.add(backButton);

        userInterface.add(panel);
        userInterface.setVisible(true);
        currentFrame = userInterface; // Set current frame to this survey creator interface frame
    }

    private boolean isFirstTimeUser(String username) {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream("users.ser"))) {
            List<User> users = (List<User>) ois.readObject();
            for (User user : users) {
                if (user.getUsername().equals(username)) {
                    return false;
                }
            }
        } catch (IOException | ClassNotFoundException e) {
            JOptionPane.showMessageDialog(null, "Error reading user details: " + e.getMessage());
        }
        return true;
    }
    private boolean isValidUserLogin(String username, String password) {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream("users.ser"))) {
            List<User> users = (List<User>) ois.readObject();
            for (User user : users) {
                if (user.getUsername().equals(username) && user.getPassword().equals(hashPassword(password))) {
                    return true;
                }
            }
        } catch (IOException | ClassNotFoundException | NoSuchAlgorithmException e) {
            JOptionPane.showMessageDialog(this, "Error reading survey creator details: " + e.getMessage());
        }
        return false;
    }






    private void openQuestionForm(String questionType) {
        JFrame questionForm = new JFrame("Question Form - " + questionType);
        questionForm.setSize(500, 300);
        questionForm.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(6, 2));

        JLabel questionIdLabel = new JLabel("Question ID:");
        JTextField questionIdField = new JTextField();
        JLabel questionPositionLabel = new JLabel("Question Position:");
        JTextField questionPositionField = new JTextField();
        JLabel questionTextLabel = new JLabel("Question Text:");
        JTextField questionTextField = new JTextField();
        JLabel surveyIdLabel = new JLabel("Survey ID:");
        JTextField surveyIdField = new JTextField();

        panel.add(questionIdLabel);
        panel.add(questionIdField);
        panel.add(questionPositionLabel);
        panel.add(questionPositionField);
        panel.add(questionTextLabel);
        panel.add(questionTextField);
        panel.add(surveyIdLabel);
        panel.add(surveyIdField);

        JButton saveQuestionButton = new JButton("Save Question");
        saveQuestionButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String questionId = questionIdField.getText();
                String questionPosition = questionPositionField.getText();
                String questionText = questionTextField.getText();
                String surveyId = surveyIdField.getText();

                saveQuestionData(questionId, questionPosition, questionText, surveyId);

                questionIdField.setText("");
                questionPositionField.setText("");
                questionTextField.setText("");
                surveyIdField.setText("");

                JOptionPane.showMessageDialog(questionForm, "Question saved successfully!");
            }
        });
        panel.add(saveQuestionButton);

        JButton backButton = new JButton("Back");
        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                questionForm.dispose();
                openSurveyForm(); // Go back to survey form
            }
        });

        panel.add(backButton);

        questionForm.add(panel);
        questionForm.setVisible(true);
        currentFrame = questionForm; // Set current frame to this question form frame
    }

    private void saveQuestionData(String questionId, String questionPosition, String questionText, String surveyId) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("questions.ser", true))) {
            Question question = new Question(questionId, questionPosition, questionText, surveyId);
            oos.writeObject(question);
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(null, "Error saving question: " + ex.getMessage());
        }
    }

    private void saveSurveyCreator(SurveyCreator sc) throws IOException {
        List<SurveyCreator> surveyCreators = new ArrayList<>();
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream("survey_creators.ser"))) {
            surveyCreators = (List<SurveyCreator>) ois.readObject();
        } catch (FileNotFoundException | ClassNotFoundException e) {
            // File not found is okay, it means this is the first entry
        } catch (IOException e) {
            throw new IOException("Error reading survey creators file: " + e.getMessage());
        }

        surveyCreators.add(sc);

        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("survey_creators.ser"))) {
            oos.writeObject(surveyCreators);
        }
    }

    private String hashPassword(String password) throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance("MD5");
        md.update(password.getBytes());
        byte[] digest = md.digest();
        StringBuilder sb = new StringBuilder();
        for (byte b : digest) {
            sb.append(String.format("%02x", b));
        }
        return sb.toString();
    }

    private void openFrame(JFrame frame) {
        if (currentFrame != null) {
            currentFrame.dispose(); // Close current frame if not null
        }
        currentFrame = frame; // Set current frame to the new frame
        currentFrame.setVisible(true); // Make new frame visible
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new LoginApp();
            }
        });
    }
}
