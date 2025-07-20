import java.time.LocalDate;

public class Rental {
    private int id;
    private int userId; // Foreign key to the User table
    private int carId;  // Foreign key to the Car table
    private LocalDate startDate;
    private LocalDate endDate;

    // Constructor for new rentals (before ID is assigned by DB)
    public Rental(int userId, int carId, LocalDate startDate, LocalDate endDate) {
        this.userId = userId;
        this.carId = carId;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    // Constructor for existing rentals (retrieved from DB)
    public Rental(int id, int userId, int carId, LocalDate startDate, LocalDate endDate) {
        this.id = id;
        this.userId = userId;
        this.carId = carId;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    // Getters
    public int getId() { return id; }
    public int getUserId() { return userId; }
    public int getCarId() { return carId; }
    public LocalDate getStartDate() { return startDate; }
    public LocalDate getEndDate() { return endDate; }

    // Setters (if needed, e.g., to extend rental)
    public void setEndDate(LocalDate endDate) { this.endDate = endDate; }
    public void setUserId(int userId) { this.userId = userId; }
    public void setCarId(int carId) { this.carId = carId; }
    public void setStartDate(LocalDate startDate) { this.startDate = startDate; }
}