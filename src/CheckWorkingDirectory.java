public class CheckWorkingDirectory {
    public static void main(String[] args) {
        String workingDir = System.getProperty("user.dir");
        System.out.println("Current working directory: " + workingDir);
    }
}
