public class Car {
    private int id;
    private String carNum;
    private String carType; // e.g., "SUV", "Sedan", "Mini"
    private String carName;
    private String status; // e.g., "available", "rented", "maintenance"

    // Constructor for new cars (before ID is assigned by DB)
    public Car(String carNum, String carType, String carName, String status) {
        this.carNum = carNum;
        this.carType = carType;
        this.carName = carName;
        this.status = status;
    }

    // Constructor for existing cars (retrieved from DB)
    public Car(int id, String carNum, String carType, String carName, String status) {
        this.id = id;
        this.carNum = carNum;
        this.carType = carType;
        this.carName = carName;
        this.status = status;
    }

    // Getters
    public int getId() { return id; }
    public String getCarNum() { return carNum; }
    public String getCarType() { return carType; }
    public String getCarName() { return carName; }
    public String getStatus() { return status; }

    // Setters (if needed, e.g., to update status)
    public void setStatus(String status) { this.status = status; }
    public void setCarNum(String carNum) { this.carNum = carNum; }
    public void setCarType(String carType) { this.carType = carType; }
    public void setCarName(String carName) { this.carName = carName; }
}