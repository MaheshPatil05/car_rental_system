import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Scanner;

public class Main {

    private static Scanner scanner = new Scanner(System.in);
    private static AuthService authService;
    private static CarService carService;
    private static RentalService rentalService;
    private static CarDAO carDAO;

    public static void main(String[] args) {
        // Initialize DAOs
        UserDAO userDAO = new UserDAO();
        carDAO = new CarDAO();
        RentalDAO rentalDAO = new RentalDAO();

        // Initialize Services
        authService = new AuthService(userDAO);
        carService = new CarService(carDAO);
        rentalService = new RentalService(rentalDAO, carDAO);

        System.out.println("------------------------------------");
        System.out.println("  Welcome to the Car Rental System! ");
        System.out.println("------------------------------------");

        runAuthenticationMenu();

        // If a user is logged in, show the main menu
        if (authService.getLoggedInUser() != null) {
            runMainMenu();
        }

        System.out.println("\nThank you for using the Car Rental System. Bye!");
        scanner.close(); // Close the scanner when done
    }

    private static void runAuthenticationMenu() {
        int choice;
        do {
            System.out.println("\n--- Authentication Menu ---");
            System.out.println("1. Login");
            System.out.println("2. Register");
            System.out.println("3. Exit");
            System.out.print("Enter your choice: ");
            choice = getIntInput();

            switch (choice) {
                case 1:
                    handleLogin();
                    break;
                case 2:
                    handleRegister();
                    break;
                case 3:
                    System.out.println("Exiting application.");
                    return; // Exit the method to terminate
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
            // Continue loop until login is successful or user exits
        } while (authService.getLoggedInUser() == null && choice != 3);
    }

    private static void handleLogin() {
        System.out.print("Enter username: ");
        String username = scanner.nextLine();
        System.out.print("Enter password: ");
        String password = scanner.nextLine(); // In a real app, read securely (e.g., char array)

        authService.login(username, password);
    }

    private static void handleRegister() {
        System.out.print("Enter desired username: ");
        String username = scanner.nextLine();
        System.out.print("Enter password: ");
        String password = scanner.nextLine();
        System.out.print("Enter your full name: ");
        String name = scanner.nextLine();
        System.out.print("Enter contact number: ");
        String contactNumber = scanner.nextLine();
        System.out.print("Enter email address: ");
        String email = scanner.nextLine();
        System.out.print("Enter address (optional): ");
        String address = scanner.nextLine();

        authService.register(username, password, name, contactNumber, email, address);
    }

    private static void runMainMenu() {
        int choice;
        do {
            System.out.println("\n--- Main Menu ---");
            System.out.println("Logged in as: " + authService.getLoggedInUser().getName());
            System.out.println("1. View Available Cars");
            System.out.println("2. Rent a Car");
            System.out.println("3. Return a Car");
            System.out.println("4. View My Rentals");
            System.out.println("5. Add New Car (Admin Only)"); // Example for future admin features
            System.out.println("6. Logout");
            System.out.println("7. Exit Application");
            System.out.print("Enter your choice: ");
            choice = getIntInput();

            switch (choice) {
                case 1:
                    viewAvailableCars();
                    break;
                case 2:
                    rentCar();
                    break;
                case 3:
                    returnCar();
                    break;
                case 4:
                    viewMyRentals();
                    break;
                case 5:
                    // This would ideally be restricted to admin users
                    addNewCar();
                    break;
                case 6:
                    authService.logout();
                    runAuthenticationMenu(); // Go back to auth menu after logout
                    if (authService.getLoggedInUser() == null) { // If still logged out
                        // This ensures that after logging out and then choosing to exit the auth menu,
                        // the main menu loop also terminates.
                        return;
                    }
                    break;
                case 7:
                    return; // Exit the main menu loop
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        } while (true);
    }

    private static void viewAvailableCars() {
        List<Car> cars = carService.getAvailableCars();
        if (!cars.isEmpty()) {
            System.out.println("\n--- Available Cars ---");
            System.out.printf("%-5s %-15s %-10s %-20s%n", "ID", "Car Number", "Type", "Name");
            System.out.println("-------------------------------------------------------");
            for (Car car : cars) {
                System.out.printf("%-5d %-15s %-10s %-20s%n", car.getId(), car.getCarNum(), car.getCarType(), car.getCarName());
            }
        }
    }

    private static void rentCar() {
        System.out.print("Enter car number of the car you want to rent: ");
        String carNum = scanner.nextLine();

        LocalDate startDate = getDateInput("Enter start date (YYYY-MM-DD): ");
        if (startDate == null) return;
        LocalDate endDate = getDateInput("Enter end date (YYYY-MM-DD): ");
        if (endDate == null) return;

        rentalService.rentCar(authService.getLoggedInUser(), carNum, startDate, endDate);
    }

    private static void returnCar() {
        System.out.print("Enter car number of the car you are returning: ");
        String carNum = scanner.nextLine();
        rentalService.returnCar(carNum);
    }

    private static void viewMyRentals() {
        List<Rental> rentals = rentalService.getRentalsForUser(authService.getLoggedInUser());
        if (!rentals.isEmpty()) {
            System.out.println("\n--- Your Active Rentals ---");
            System.out.printf("%-5s %-15s %-15s %-12s %-12s%n", "ID", "Car Number", "Car Name", "Start Date", "End Date");
            System.out.println("------------------------------------------------------------------");
            for (Rental rental : rentals) {
                // To get car details for the rental, you'd typically fetch the car
                // within the RentalService or here by calling carService.getCarById(rental.getCarId())
                // For simplicity here, we'll just show the car ID, or you could do a quick lookup.
                Car rentedCar = carService.getCarByCarNumber(carDAO.findCarById(rental.getCarId()).getCarNum()); // findCarById in CarDAO
                if(rentedCar != null) {
                    System.out.printf("%-5d %-15s %-15s %-12s %-12s%n",
                            rental.getId(),
                            rentedCar.getCarNum(),
                            rentedCar.getCarName(),
                            rental.getStartDate(),
                            rental.getEndDate());
                } else {
                    System.out.printf("%-5d %-15s %-15s %-12s %-12s%n",
                            rental.getId(),
                            "N/A (Car ID: " + rental.getCarId() + ")",
                            "N/A",
                            rental.getStartDate(),
                            rental.getEndDate());
                }
            }
        }
    }

    private static void addNewCar() {
        // Basic check: in a real app, verify user role (e.g., authService.getLoggedInUser().getRole() == Role.ADMIN)
        System.out.println("\n--- Add New Car ---");
        System.out.print("Enter car number: ");
        String carNum = scanner.nextLine();
        System.out.print("Enter car type (SUV, Sedan, Mini): ");
        String carType = scanner.nextLine();
        System.out.print("Enter car name/model: ");
        String carName = scanner.nextLine();

        carService.addCar(carNum, carType, carName);
    }

    // --- Helper methods for input validation ---
    private static int getIntInput() {
        while (!scanner.hasNextInt()) {
            System.out.println("Invalid input. Please enter a number.");
            scanner.next(); // consume the invalid input
            System.out.print("Enter your choice: ");
        }
        int input = scanner.nextInt();
        scanner.nextLine(); // Consume newline left-over
        return input;
    }

    private static LocalDate getDateInput(String prompt) {
        while (true) {
            System.out.print(prompt);
            String dateString = scanner.nextLine();
            try {
                return LocalDate.parse(dateString);
            } catch (DateTimeParseException e) {
                System.out.println("Invalid date format. Please use YYYY-MM-DD.");
            }
        }
    }
}