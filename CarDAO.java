import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CarDAO {

    // Method to add a new car to the database
    public boolean addCar(Car car) {
        String sql = "INSERT INTO cars (car_num, car_type, car_name, status) VALUES (?, ?, ?, ?)";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, car.getCarNum());
            pstmt.setString(2, car.getCarType());
            pstmt.setString(3, car.getCarName());
            pstmt.setString(4, car.getStatus()); // e.g., "available", "rented", "maintenance"

            int affectedRows = pstmt.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException e) {
            if (e.getSQLState().startsWith("23")) { // Unique constraint violation for car_num
                System.err.println("Error: Car number '" + car.getCarNum() + "' already exists.");
            } else {
                System.err.println("Database error during car addition: " + e.getMessage());
            }
            return false;
        }
    }

    // Method to find a car by its car number
    public Car findCarByCarNum(String carNum) {
        String sql = "SELECT id, car_num, car_type, car_name, status FROM cars WHERE car_num = ?";
        Car car = null;
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, carNum);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                car = new Car(
                        rs.getInt("id"),
                        rs.getString("car_num"),
                        rs.getString("car_type"),
                        rs.getString("car_name"),
                        rs.getString("status")
                );
            }
        } catch (SQLException e) {
            System.err.println("Database error during car lookup: " + e.getMessage());
        }
        return car;
    }

    // Method to get all available cars
    public List<Car> getAvailableCars() {
        List<Car> availableCars = new ArrayList<>();
        String sql = "SELECT id, car_num, car_type, car_name, status FROM cars WHERE status = 'available'";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                availableCars.add(new Car(
                        rs.getInt("id"),
                        rs.getString("car_num"),
                        rs.getString("car_type"),
                        rs.getString("car_name"),
                        rs.getString("status")
                ));
            }
        } catch (SQLException e) {
            System.err.println("Database error retrieving available cars: " + e.getMessage());
        }
        return availableCars;
    }

    // Method to update a car's status (e.g., to 'rented', 'available', 'maintenance')
    public boolean updateCarStatus(int carId, String newStatus) {
        String sql = "UPDATE cars SET status = ? WHERE id = ?";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, newStatus);
            pstmt.setInt(2, carId);

            int affectedRows = pstmt.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException e) {
            System.err.println("Database error updating car status: " + e.getMessage());
            return false;
        }
    }

    public Car findCarById(int id) {
        String sql = "SELECT id, car_num, car_type, car_name, status FROM cars WHERE id = ?";
        Car car = null;
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                car = new Car(
                        rs.getInt("id"),
                        rs.getString("car_num"),
                        rs.getString("car_type"),
                        rs.getString("car_name"),
                        rs.getString("status")
                );
            }
        } catch (SQLException e) {
            System.err.println("Database error during car lookup by ID: " + e.getMessage());
        }
        return car;
    }
}