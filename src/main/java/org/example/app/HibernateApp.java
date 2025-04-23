package org.example.app;

import org.example.models.User;
import org.example.models.Vehicle;
import org.example.services.hibernate.AuthHibernateService;
import org.example.services.hibernate.RentalHibernateService;
import org.example.services.hibernate.VehicleHibernateService;

import java.util.Optional;
import java.util.Scanner;
import java.util.UUID;

public class HibernateApp {

    private final AuthHibernateService authHibernateService;
    private final VehicleHibernateService vehicleHibernateService;
    private final RentalHibernateService rentalHibernateService;
    private final Scanner scanner = new Scanner(System.in);

    public HibernateApp(AuthHibernateService authHibernateService, VehicleHibernateService vehicleHibernateService, RentalHibernateService rentalHibernateService) {
        this.authHibernateService = authHibernateService;
        this.vehicleHibernateService = vehicleHibernateService;
        this.rentalHibernateService = rentalHibernateService;
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
                                
                                6. List of vehicles
                                
                                
                                9. Exit
                                """);
                                break;  //5. Removing a vehicle //7. List of users

                            default:
                                System.out.println("A role with value: " + role + " is not handled");
                                loggedIn = false;
                                break;
                        }
                        option = scanner.nextInt();
                        break;

                    case 1: // info about user
                        System.out.println("Login: " + user.get().getLogin() +
                                "\nPassword (hashed): " + user.get().getPassword() +
                                "\nRole: " + user.get().getRole() +
                                "\nUser Id: " + user.get().getId());

                        option = 0;
                        break;

                    case 2: // renting a vehicle
                        System.out.println("Id of the vehicle: ");
                        String toRentVehicleId = scanner.next();

                        rentalHibernateService.rent(toRentVehicleId, user.get().getId());

                        option = 0;
                        break;

                    case 3: // returning a vehicle
                        System.out.println("Id of the returning vehicle: ");
                        String toReturnVehicleId = scanner.next();

                        rentalHibernateService.returnRental(toReturnVehicleId, user.get().getId());

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
                                    .id(UUID.randomUUID().toString())
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

                            vehicleHibernateService.save(vehicle);
                        }
                        else {  // list of available vehicles
                            for (Vehicle v : vehicleHibernateService.findAvailableVehicles()) {
                                System.out.println(v);
                            }
                        }
                        option = 0;
                        break;

//                    case 5: // removing a vehicle
//                        if (role.equals("ADMIN")) {
//                            System.out.println("Id of the vehicle: ");
//                            String toRemoveVehicleId = scanner.next();
//
//                            vehicleHibernateService.
//                        }
//                        option = 0;
//                        break;

                    case 6: // list of vehicles
                        if (role.equals("ADMIN")) {
                            for (Vehicle v : vehicleHibernateService.findAll()) {
                                System.out.println(v);
                            }
                        }
                        option = 0;
                        break;

//                    case 7: // list of users
//                        if (role.equals("ADMIN")) {
//                            authHibernateService.showAllUsers();
//                        }
//                        option = 0;
//                        break;

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

                        user = authHibernateService.login(login, password);
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

                        if (!authHibernateService.register(regLogin, regPassword, regRole)) {
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
