import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

public class CreateUserSerFile {
    public static void main(String[] args) {
        List<User> users = new ArrayList<>();
        try {
            users.add(new User("user1", hashPassword("password1"), "John", "Doe", "Engineering", "john.doe@example.com", "Male", "1234567890"));
            users.add(new User("user2", hashPassword("password2"), "Jane", "Doe", "Science", "jane.doe@example.com", "Female", "0987654321"));

            try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("users.ser"))) {
                oos.writeObject(users);
                System.out.println("users.ser file created successfully.");
            }
        } catch (NoSuchAlgorithmException | IOException e) {
            e.printStackTrace();
        }
    }

    private static String hashPassword(String password) throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance("MD5");
        md.update(password.getBytes());
        byte[] digest = md.digest();
        StringBuilder sb = new StringBuilder();
        for (byte b : digest) {
            sb.append(String.format("%02x", b));
        }
        return sb.toString();
    }
}
