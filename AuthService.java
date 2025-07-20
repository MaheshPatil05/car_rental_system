import java.sql.SQLException;
import java.util.Objects;
// In a real application, you'd import a hashing library like org.mindrot.jbcrypt.BCrypt
// For this example, we'll simulate hashing.

public class AuthService {
    private UserDAO userDAO;
    private User loggedInUser; // To keep track of the currently logged-in user

    public AuthService(UserDAO userDAO) {
        this.userDAO = userDAO;
    }

    public boolean register(String username, String password, String name, String contactNumber, String email, String address) {
        // In a real app: Hash the password before creating the User object
        // String hashedPassword = BCrypt.hashpw(password, BCrypt.gensalt());
        String hashedPassword = password; // SIMULATED HASHING: DO NOT USE IN PRODUCTION

        User newUser = new User(username, hashedPassword, name, contactNumber, email, address);
        boolean success = userDAO.registerUser(newUser);
        if (success) {
            System.out.println("Registration successful for user: " + username);
        } else {
            System.out.println("Registration failed. Username might already exist or a database error occurred.");
        }
        return success;
    }

    public User login(String username, String password) {
        User user = userDAO.findUserByUsername(username);
        if (user != null) {
            // In a real app: Compare the provided password (after hashing it) with the stored hashed password
            // if (BCrypt.checkpw(password, user.getPassword())) {
            if (Objects.equals(password, user.getPassword())) { // SIMULATED HASHING CHECK: DO NOT USE IN PRODUCTION
                this.loggedInUser = user;
                System.out.println("Login successful! Welcome, " + user.getName());
                return user;
            }
        }
        System.out.println("Login failed. Invalid username or password.");
        this.loggedInUser = null;
        return null;
    }

    public User getLoggedInUser() {
        return loggedInUser;
    }

    public void logout() {
        this.loggedInUser = null;
        System.out.println("Logged out successfully.");
    }
}