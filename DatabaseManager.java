import java.sql.*;

public class DatabaseManager {
    private static final String JDBC_URL = "jdbc:postgresql://localhost:5432/car_rental_db";
    private static final String DB_USER = "username"; // Change this
    private static final String DB_PASSWORD = "your_password"; // Change this

    public static Connection getConnection() throws SQLException {
        // No need for Class.forName("org.postgresql.Driver"); due to JDBC 4.0+ automatic driver discovery
        return DriverManager.getConnection(JDBC_URL, DB_USER, DB_PASSWORD);
    }

    public static void closeResources(Connection conn, PreparedStatement pstmt, ResultSet rs) {
        try {
            if (rs != null) rs.close();
            if (pstmt != null) pstmt.close();
            if (conn != null) conn.close();
        } catch (SQLException e) {
            System.err.println("Error closing database resources: " + e.getMessage());
        }
    }
}
