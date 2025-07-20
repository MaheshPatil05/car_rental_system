import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

// User class will also act as the Customer class in this simplified design
public class User {
    private int id;
    private String username;
    private String password; // Should be hashed in a real application!
    private String name;
    private String contactNumber;
    private String email;
    private String address;

    // Constructor for new users (before ID is assigned by DB)
    public User(String username, String password, String name, String contactNumber, String email, String address) {
        this.username = username;
        this.password = password;
        this.name = name;
        this.contactNumber = contactNumber;
        this.email = email;
        this.address = address;
    }

    // Constructor for existing users (retrieved from DB)
    public User(int id, String username, String password, String name, String contactNumber, String email, String address) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.name = name;
        this.contactNumber = contactNumber;
        this.email = email;
        this.address = address;
    }

    // Getters
    public int getId() { return id; }
    public String getUsername() { return username; }
    public String getPassword() { return password; }
    public String getName() { return name; }
    public String getContactNumber() { return contactNumber; }
    public String getEmail() { return email; }
    public String getAddress() { return address; }

    // Setters (if needed, e.g., for updating profile)
    public void setPassword(String password) { this.password = password; } // Hash this when setting in a real app
    public void setUsername(String username) { this.username = username; }
    public void setName(String name) { this.name = name; }
    public void setContactNumber(String contactNumber) { this.contactNumber = contactNumber; }
    public void setEmail(String email) { this.email = email; }
    public void setAddress(String address) { this.address = address; }
}