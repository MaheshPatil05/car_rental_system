import java.util.List;

public class CarService {
    private CarDAO carDAO;

    public CarService(CarDAO carDAO) {
        this.carDAO = carDAO;
    }

    public boolean addCar(String carNum, String carType, String carName) {
        Car newCar = new Car(carNum, carType, carName, "available"); // New cars are available by default
        boolean success = carDAO.addCar(newCar);
        if (success) {
            System.out.println("Car '" + carName + "' (" + carNum + ") added successfully.");
        } else {
            System.out.println("Failed to add car. Car number might already exist or a database error occurred.");
        }
        return success;
    }

    public List<Car> getAvailableCars() {
        List<Car> cars = carDAO.getAvailableCars();
        if (cars.isEmpty()) {
            System.out.println("No available cars at the moment.");
        }
        return cars;
    }

    public Car getCarByCarNumber(String carNum) {
        return carDAO.findCarByCarNum(carNum);
    }
}