package org.example.app;

import org.apache.commons.codec.digest.DigestUtils;
import org.example.accounts.User;
import org.example.model.Car;
import org.example.model.Motorcycle;
import org.example.model.Vehicle;
import org.example.repositories.IUserRepository;
import org.example.repositories.IVehicleRepository;
import org.example.repositories.ListOfVehicles;
import org.example.repositories.UserRepository;

import java.util.Objects;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        IVehicleRepository repository = new ListOfVehicles();
        repository.save();

        IUserRepository userRepository = new UserRepository();
        // the default logins are:
        // (user) login: karol, password: 123
        // (admin) login: marcin, password: qwerty
        userRepository.save();

        Scanner scanner = new Scanner(System.in);
        int option = 0;
        User user = null;
        boolean loop = true;
        boolean loggedIn = false;
        boolean isAdmin = false;

        while (loop) {
            if (loggedIn) {
                switch (option) {
                    case 0: // common loop, we choose what option to get
                        if (isAdmin) {  // should the not logged in people be able to rent???
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
                        }
                        else {
                            System.out.println("""
                            \nOptions:
                            1. Info
                            2. Renting a vehicle
                            3. Returning a vehicle
                            
                            9. Exit
                            """);
                        }
                        option = scanner.nextInt();
                        break;
                    case 1: // info about user
                        System.out.println("Login: " + user.getLogin() +
                                "\nPassword (hashed): " + user.getPassword() +
                                "\nRole: " + user.getRole() +
                                "\nVehicle rented: " + user.getRentedCar());
                        option = 0;
                        break;
                    case 2: // renting a vehicle
                        if (user.getRentedCar() == -1){
                            System.out.println("Id of the vehicle: ");
                            int toRentVehicleId = scanner.nextInt();
                            if (repository.rentVehicle(toRentVehicleId)) {
                                user.setRentedCar(toRentVehicleId);
                                userRepository.save();
                            }
                        }
                        else {
                            System.out.println("You are already renting a car!" +
                                    "\nYou have to return it before renting another one");
                        }

                        option = 0;
                        break;
                    case 3: // returning a vehicle
                        if (user.getRentedCar() == -1) {
                            System.out.println("You are not renting a car!" +
                                    "\nYou have to rent one to return it");
                        }
                        else {
                            System.out.println("Id of the vehicle: ");
                            int toReturnVehicleId = scanner.nextInt();
                            if (repository.returnVehicle(toReturnVehicleId)) {
                                user.setRentedCar(-1);
                                userRepository.save();
                            }
                        }

                        option = 0;
                        break;

                    // admin options
                    case 4: // adding a vehicle
                        if (isAdmin) {
                            System.out.println("Select a type of vehicle (C - car or M - motorcycle): ");
                            String vehicleAddType = scanner.next();

                            if (Objects.equals(vehicleAddType, "C")) {
                                System.out.println("Brand: ");
                                String vehicleAddBrand = scanner.next();
                                System.out.println("Model: ");
                                String vehicleAddModel = scanner.next();
                                System.out.println("Year: ");
                                int vehicleAddYear = scanner.nextInt();
                                System.out.println("Price: ");
                                float vehicleAddPrice = scanner.nextFloat();
                                boolean vehicleAddRented = false;
                                System.out.println("Vehicle Id: ");
                                int vehicleAddId = scanner.nextInt();

                                if (repository.addVehicle(new Car(
                                        vehicleAddBrand,
                                        vehicleAddModel,
                                        vehicleAddYear,
                                        vehicleAddPrice,
                                        vehicleAddRented,
                                        vehicleAddId
                                ))) {
                                    System.out.println("Vehicle successfully added");
                                }
                                else {
                                    System.out.println("Vehicle addition was unsuccessful");
                                }

                            } else if (Objects.equals(vehicleAddType, "M")) {
                                System.out.println("Brand: ");
                                String vehicleAddBrand = scanner.next();
                                System.out.println("Model: ");
                                String vehicleAddModel = scanner.next();
                                System.out.println("Year: ");
                                int vehicleAddYear = scanner.nextInt();
                                System.out.println("Price: ");
                                float vehicleAddPrice = scanner.nextFloat();
                                boolean vehicleAddRented = false;
                                System.out.println("Vehicle Id: ");
                                int vehicleAddId = scanner.nextInt();
                                System.out.println("Category: ");
                                String vehicleAddCategory = scanner.next();

                                if (repository.addVehicle(new Motorcycle(
                                        vehicleAddBrand,
                                        vehicleAddModel,
                                        vehicleAddYear,
                                        vehicleAddPrice,
                                        vehicleAddRented,
                                        vehicleAddId,
                                        vehicleAddCategory
                                ))) {
                                    System.out.println("Vehicle successfully added");
                                }
                                else {
                                    System.out.println("Vehicle addition was unsuccessful");
                                }
                            }
                        }
                        option = 0;
                        break;
                    case 5: // removing a vehicle
                        if (isAdmin) {
                            System.out.println("Id of the vehicle: ");
                            int toRemoveVehicleId = scanner.nextInt();
                            if (repository.removeVehicle(toRemoveVehicleId)) {
                                System.out.println("Vehicle successfully removed");
                            }
                            else {
                                System.out.println("Vehicle id is wrong, vehicle not removed");
                            }
                        }
                        option = 0;
                        break;
                    case 6: // list of vehicles
                        if (isAdmin) {
                            for (Vehicle listVehicle : repository.getVehicles()) {
                                System.out.println(listVehicle.toString());
                            }
                        }
                        option = 0;
                        break;
                    case 7: // list of users
                        if (isAdmin) {
                            for (User listUser : userRepository.getUsers()) {
                                System.out.println(listUser.toString());
                            }
                        }
                        option = 0;
                        break;

                    case 9: // exiting
                        loop = false;
                        break;

                    default:
                        System.out.println("THIS IS NOT HANDLED IN THE SWITCH STATEMENT!!!");
                        option = 0;
                }
            }
            else {
                System.out.println("Login: ");
                String login = scanner.next();
                System.out.println("Password: ");
                String password = scanner.next();
                // get the user if login returns true
                if (Authentication.loggingIn(userRepository, login, password)) {
                    user = userRepository.getUser(login);
                    loggedIn = true;
                    if (user.getRole().equals("admin")) isAdmin = true;
                }
            }

        }
        scanner.close();
    }
}