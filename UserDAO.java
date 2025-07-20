import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserDAO {

    // Method to register a new user (signup)
    public boolean registerUser(User user) {
        String sql = "INSERT INTO users (username, password_hash, name, contact_number, email, address) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseManager.getConnection(); // Get connection from your DatabaseManager
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, user.getUsername());
            pstmt.setString(2, user.getPassword()); // In a real app, hash this password!
            pstmt.setString(3, user.getName());
            pstmt.setString(4, user.getContactNumber());
            pstmt.setString(5, user.getEmail());
            pstmt.setString(6, user.getAddress());

            int affectedRows = pstmt.executeUpdate();
            return affectedRows > 0; // Returns true if a row was inserted
        } catch (SQLException e) {
            // Handle specific SQL exceptions, e.g., unique constraint violation for username
            if (e.getSQLState().startsWith("23")) { // SQLState 23xxx for integrity constraint violation
                System.err.println("Error: Username '" + user.getUsername() + "' already exists.");
            } else {
                System.err.println("Database error during user registration: " + e.getMessage());
            }
            return false;
        }
    }

    // Method to find a user by username (for login verification)
    public User findUserByUsername(String username) {
        String sql = "SELECT id, username, password_hash, name, contact_number, email, address FROM users WHERE username = ?";
        User user = null;
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, username);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                user = new User(
                        rs.getInt("id"),
                        rs.getString("username"),
                        rs.getString("password_hash"), // Remember to compare with hashed password
                        rs.getString("name"),
                        rs.getString("contact_number"),
                        rs.getString("email"),
                        rs.getString("address")
                );
            }
        } catch (SQLException e) {
            System.err.println("Database error during user lookup: " + e.getMessage());
        }
        return user;
    }
}