package org.example.app;

import org.example.models.User;
import org.example.models.Vehicle;
import org.example.services.AuthService;
import org.example.services.RentalService;
import org.example.services.VehicleService;

import java.util.Optional;
import java.util.Scanner;

public class App {

    private final AuthService authService;
    private final VehicleService vehicleService;
    private final RentalService rentalService;
    private final Scanner scanner = new Scanner(System.in);

    public App(AuthService authService, VehicleService vehicleService, RentalService rentalService) {
        this.authService = authService;
        this.vehicleService = vehicleService;
        this.rentalService = rentalService;
    }

    public void run() {
        int option = 0;
        Optional<User> user = Optional.empty();
        boolean loop = true;
        boolean loggedIn = false;
        String role = "None";

        while (loop) {
            if (loggedIn) {
                switch (option) {
                    case 0: // common loop, we choose what option to get
                        switch (role) {
                            case "USER":
                                System.out.println("""
                                \nOptions:
                                1. Info
                                2. Renting a vehicle
                                3. Returning a vehicle
                                4. List of available vehicles
                                
                                9. Exit
                                """);
                                break;

                            case "ADMIN":
                                System.out.println("""
                                \n= You are an admin =
                                Options:
                                1. Info
                                2. Renting a vehicle
                                3. Returning a vehicle
                                4. Adding a vehicle
                                5. Removing a vehicle
                                6. List of vehicles
                                7. List of users
                                
                                9. Exit
                                """);
                                break;

                            default:
                                System.out.println("A role with value: " + role + " is not handled");
                                loggedIn = false;
                                break;
                        }
                        option = scanner.nextInt();
                        break;

                    case 1: // info about user
                        String userId = user.get().getId();

                        authService.showUserInfo(userId);
                        System.out.println("Rented vehicle: ");
                        rentalService.showUserRentedCar(userId);

                        option = 0;
                        break;

                    case 2: // renting a vehicle
                        System.out.println("Id of the vehicle: ");
                        String toRentVehicleId = scanner.next();

                        rentalService.rentVehicleByVehicleId(toRentVehicleId, user.get().getId());

                        option = 0;
                        break;

                    case 3: // returning a vehicle
                        System.out.println("Id of the returning vehicle: ");
                        String toReturnVehicleId = scanner.next();

                        rentalService.returnVehicleByVehicleId(toReturnVehicleId, user.get().getId());

                        option = 0;
                        break;

                    case 4: // adding a vehicle
                        if (role.equals("ADMIN")) {

                            System.out.println("Category: ");
                            String vehicleAddCategory = scanner.next();
                            System.out.println("Brand: ");
                            String vehicleAddBrand = scanner.next();
                            System.out.println("Model: ");
                            String vehicleAddModel = scanner.next();
                            System.out.println("Year: ");
                            int vehicleAddYear = scanner.nextInt();
                            System.out.println("Plate: ");
                            String vehicleAddPlate = scanner.next();
                            System.out.println("Price: ");
                            double vehicleAddPrice = scanner.nextDouble();

                            Vehicle vehicle = Vehicle.builder()
                                    .category(vehicleAddCategory)
                                    .brand(vehicleAddBrand)
                                    .model(vehicleAddModel)
                                    .year(vehicleAddYear)
                                    .plate(vehicleAddPlate)
                                    .price(vehicleAddPrice)
                                    .build();

                            System.out.println("Number of additional attributes: ");
                            int numberOfAttributes = scanner.nextInt();

                            for (int i = 0; i < numberOfAttributes; i++) {
                                System.out.println("Specify attributes key: ");
                                String attributesKey = scanner.next();
                                System.out.println("Specify attributes value: ");
                                Object attributesValue = scanner.next();
                                vehicle.addAttribute(attributesKey, attributesValue);
                            }

                            vehicleService.addVehicle(vehicle);
                        }
                        else {  // list of available vehicles
                            vehicleService.showAvailableVehicles();
                        }
                        option = 0;
                        break;

                    case 5: // removing a vehicle
                        if (role.equals("ADMIN")) {
                            System.out.println("Id of the vehicle: ");
                            String toRemoveVehicleId = scanner.next();

                            vehicleService.removeVehicle(toRemoveVehicleId);
                        }
                        option = 0;
                        break;

                    case 6: // list of vehicles
                        if (role.equals("ADMIN")) {
                            vehicleService.showAllVehicles();
                        }
                        option = 0;
                        break;

                    case 7: // list of users
                        if (role.equals("ADMIN")) {
                            authService.showAllUsers();
                        }
                        option = 0;
                        break;

                    case 9: // exiting
                        loop = false;
                        break;

                    default:
                        System.out.println("Incorrect option");
                        option = 0;
                }
            }
            else {
                switch (option) {
                    case 0:
                        System.out.println("Options:" +
                                "\n1. Login" +
                                "\n2. Register" +
                                "\n\n9. Quit");
                        option = scanner.nextInt();
                        break;

                    case 1:
                        System.out.println("Login: ");
                        String login = scanner.next();
                        System.out.println("Password: ");
                        String password = scanner.next();

                        user = authService.login(login, password);
                        if (user.isPresent()) {
                            System.out.println("Successfully logged in");
                            role = user.get().getRole();
                            loggedIn = true;
                        } else {
                            System.out.println("Wrong login / password");
                        }
                        option = 0;
                        break;

                    case 2:
                        System.out.println("New login: ");
                        String regLogin = scanner.next();
                        System.out.println("New password: ");
                        String regPassword = scanner.next();
                        System.out.println("New role: ");
                        String regRole = scanner.next();

                        if (!authService.register(regLogin, regPassword, regRole)) {
                            System.out.println("There has been a problem during the registration");
                        }
                        option = 0;
                        break;

                    case 9:
                        loop = false;
                        break;

                    default:
                        System.out.println("Incorrect option");
                        option = 0;
                }
            }
        }
        scanner.close();
    }
}
