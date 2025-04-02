package org.example.app;

import org.example.models.Rental;
import org.example.models.User;
import org.example.models.Vehicle;
import org.example.repositories.RentalRepository;
import org.example.repositories.UserRepository;
import org.example.repositories.VehicleRepository;
import org.example.repositories.impl.RentalJsonRepository;
import org.example.repositories.impl.UserJsonRepository;
import org.example.repositories.impl.VehicleJsonRepository;
import org.example.services.AuthService;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Optional;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        VehicleRepository vehicleRepository = new VehicleJsonRepository();
        UserRepository userRepository = new UserJsonRepository();
        RentalRepository rentalRepository = new RentalJsonRepository();

        Scanner scanner = new Scanner(System.in);
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
                        System.out.println("Login: " + user.get().getLogin() +
                                "\nPassword (hashed): " + user.get().getPassword() +
                                "\nRole: " + user.get().getRole() +
                                "\nUser Id: " + user.get().getId());
                        System.out.println("Rented vehicles: ");
                        for (Vehicle v : vehicleRepository.findAll()) {
                            boolean rented = false;
                            for (Rental r : rentalRepository.findAll()) {
                                if (r.getUserId().equals(user.get().getId()) &&
                                        r.getVehicleId().equals(v.getId()) &&
                                        r.getReturnDate().isEmpty()) {
                                    rented = true;
                                    break;
                                }
                            }
                            if (rented) {
                                System.out.println(v);
                            }
                        }
                        option = 0;
                        break;

                    case 2: // renting a vehicle
                        System.out.println("Id of the vehicle: ");
                        String toRentVehicleId = scanner.next();

                        for (Vehicle v : vehicleRepository.findAll()) {
                            boolean rented = false;
                            for (Rental r : rentalRepository.findAll()) {
                                if (r.getVehicleId().equals(toRentVehicleId) &&
                                        r.getVehicleId().equals(v.getId()) &&
                                        r.getReturnDate().isEmpty()) {
                                    rented = true;
                                    break;
                                }
                            }
                            if (v.getId().equals(toRentVehicleId) && !rented) {
                                Rental rental = Rental.builder()
                                        .vehicleId(v.getId())
                                        .userId(user.get().getId())
                                        .rentDate(LocalDateTime.now().toString())
                                        .returnDate("")
                                        .build();
                                rentalRepository.save(rental);
                            }
                        }
                        option = 0;
                        break;

                    case 3: // returning a vehicle
                        System.out.println("Id of the returning vehicle: ");
                        String toReturnVehicleId = scanner.next();
                        boolean found = false;

                        for (Rental r : rentalRepository.findAll()) {
                            if (r.getVehicleId().equals(toReturnVehicleId) &&
                                    r.getUserId().equals(user.get().getId()) &&
                                    r.getReturnDate().isEmpty()) {
                                found = true;
                                r.setReturnDate(LocalDateTime.now().toString());
                            }
                        }

                        if (found) {
                            System.out.println("Vehicle has been returned");
                        }
                        else {
                            System.out.println("The returning process was unsuccessful");
                        }

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

                            Vehicle vehicle = Vehicle.builder()
                                    .category(vehicleAddCategory)
                                    .brand(vehicleAddBrand)
                                    .model(vehicleAddModel)
                                    .year(vehicleAddYear)
                                    .plate(vehicleAddPlate)
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

                            vehicleRepository.save(vehicle);
                        }
                        else {  // list of available vehicles
                            for (Vehicle v : vehicleRepository.findAll()) {
                                // there HAS TO BE a better way of finding it but i cant think of it
                                // also a good idea would be doing this at the start (in like a constructor or something in UserRepository)
                                // (in user so that we know who rented what)
                                boolean notRented = true;
                                for (Rental r : rentalRepository.findAll()) {
                                    if (r.getVehicleId().equals(v.getId()) && r.getReturnDate().isEmpty()) {
                                        notRented = false;
                                        break;
                                    }
                                }
                                if (notRented) {
                                    System.out.println(v);
                                }
                            }
                        }
                        option = 0;
                        break;

                    case 5: // removing a vehicle
                        if (role.equals("ADMIN")) {
                            System.out.println("Id of the vehicle: ");
                            String toRemoveVehicleId = scanner.next();
                            if (vehicleRepository.findById(toRemoveVehicleId).isPresent()) {
                                vehicleRepository.deleteById(toRemoveVehicleId);
                                System.out.println("Vehicle successfully removed");
                            }
                            else {
                                System.out.println("Vehicle id is wrong, vehicle not removed");
                            }
                        }
                        option = 0;
                        break;

                    case 6: // list of vehicles
                        if (role.equals("ADMIN")) {
                            for (Vehicle listVehicle : vehicleRepository.findAll()) {
                                System.out.println(listVehicle.toString());
                            }
                        }
                        option = 0;
                        break;

                    case 7: // list of users
                        if (role.equals("ADMIN")) {
                            for (User listUser : userRepository.findAll()) {
                                System.out.println(listUser.toString());
                            }
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

                        user = AuthService.login(userRepository, login, password);
                        if (user.isPresent()) {
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

                        user = AuthService.register(userRepository, regLogin, regPassword);
                        if (user.isPresent()) {
                            role = user.get().getRole();
                            loggedIn = true;
                        } else {
                            System.out.println("Either account with that login already exits or there has been a problem during the registration");
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