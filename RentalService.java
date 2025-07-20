import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

public class RentalService {
    private RentalDAO rentalDAO;
    private CarDAO carDAO; // Need to interact with CarDAO to update car status

    public RentalService(RentalDAO rentalDAO, CarDAO carDAO) {
        this.rentalDAO = rentalDAO;
        this.carDAO = carDAO;
    }

    public boolean rentCar(User user, String carNum, LocalDate startDate, LocalDate endDate) {
        if (user == null) {
            System.out.println("Error: No user is logged in to rent a car.");
            return false;
        }
        if (startDate.isAfter(endDate) || startDate.isBefore(LocalDate.now())) {
            System.out.println("Error: Invalid rental dates. Start date cannot be after end date or in the past.");
            return false;
        }

        Car carToRent = carDAO.findCarByCarNum(carNum);
        if (carToRent == null) {
            System.out.println("Error: Car with number '" + carNum + "' not found.");
            return false;
        }
        if (!"available".equalsIgnoreCase(carToRent.getStatus())) {
            System.out.println("Error: Car '" + carNum + "' is not available for rent. Current status: " + carToRent.getStatus());
            return false;
        }

        // Proceed with rental
        Rental newRental = new Rental(user.getId(), carToRent.getId(), startDate, endDate);
        boolean rentalSuccess = rentalDAO.createRental(newRental);

        if (rentalSuccess) {
            // Update car status to 'rented'
            boolean statusUpdateSuccess = carDAO.updateCarStatus(carToRent.getId(), "rented");
            if (statusUpdateSuccess) {
                System.out.println("Car '" + carToRent.getCarName() + "' (" + carNum + ") rented successfully by " + user.getName() + ".");
                // Optionally display calculated cost here
                long days = calculateRentalDuration(startDate, endDate);
                double cost = calculateRentalCost(carToRent.getCarType(), days);
                System.out.printf("Estimated cost for %d days: ₹ %.2f%n", days, cost);
                return true;
            } else {
                System.err.println("Warning: Car rented but failed to update car status. Manual correction needed for car: " + carNum);
                return false; // Or throw an exception for critical failure
            }
        } else {
            System.out.println("Failed to create rental record in the database.");
            return false;
        }
    }

    public boolean returnCar(String carNum) {
        Car car = carDAO.findCarByCarNum(carNum);
        if (car == null) {
            System.out.println("Error: Car '" + carNum + "' not found.");
            return false;
        }
        if (!"rented".equalsIgnoreCase(car.getStatus())) {
            System.out.println("Error: Car '" + carNum + "' is not currently rented. Current status: " + car.getStatus());
            return false;
        }

        // For simplicity, we just update car status here.
        // In a more complex system, you'd find the active rental for this car, mark it as completed,
        // calculate final charges based on actual return date, etc.
        boolean statusUpdateSuccess = carDAO.updateCarStatus(car.getId(), "available");
        if (statusUpdateSuccess) {
            System.out.println("Car '" + car.getCarName() + "' (" + carNum + ") successfully returned and is now available.");
            return true;
        } else {
            System.out.println("Failed to update car status to 'available' upon return.");
            return false;
        }
    }

    public List<Rental> getRentalsForUser(User user) {
        if (user == null) {
            System.out.println("Error: No user is logged in to view rentals.");
            return List.of(); // Return empty list
        }
        List<Rental> rentals = rentalDAO.getRentalsByUserId(user.getId());
        if (rentals.isEmpty()) {
            System.out.println("You have no active rentals.");
        }
        return rentals;
    }

    // --- Rental Calculation Logic (could be its own service) ---
    public long calculateRentalDuration(LocalDate startDate, LocalDate endDate) {
        return ChronoUnit.DAYS.between(startDate, endDate) + 1; // +1 to include both start and end day
    }

    public double calculateRentalCost(String carType, long days) {
        double ratePerDay = 0.0;
        switch (carType.toLowerCase()) {
            case "suv":
                ratePerDay = 5000.0;
                break;
            case "sedan":
                ratePerDay = 4000.0;
                break;
            case "mini":
                ratePerDay = 3000.0;
                break;
            default:
                System.err.println("Unknown car type: " + carType + ". Defaulting to ₹0 rate.");
                ratePerDay = 0.0;
                break;
        }
        return ratePerDay * days;
    }
}