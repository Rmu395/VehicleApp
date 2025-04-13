package org.example.services;

import org.example.models.Rental;
import org.example.models.Vehicle;
import org.example.repositories.RentalRepository;
import org.example.repositories.UserRepository;

import java.time.LocalDateTime;
import java.util.Optional;

public class RentalService {

    private final RentalRepository rentalRepo;

    public RentalService(RentalRepository rentalRepo) {
        this.rentalRepo = rentalRepo;
    }

    public void showUserRentedCar(String userId) {
        Optional<Rental> rental =  rentalRepo.findByUserIdAndReturnDateIsNull(userId);
        if (rental.isPresent()) {
            System.out.println(rental);
        }
        else {
            System.out.println("Nothing");
        }
    }

    public void rentVehicleByVehicleId(String vehicleId, String userId) {
        if (rentalRepo.findByUserIdAndReturnDateIsNull(userId).isEmpty()) {
            if (rentalRepo.findByVehicleIdAndReturnDateIsNull(vehicleId).isEmpty()) {
                Rental rental = Rental.builder()
                        .vehicleId(vehicleId)
                        .userId(userId)
                        .rentDate(LocalDateTime.now().toString())
                        .returnDate("")
                        .build();
                rentalRepo.save(rental);
                System.out.println("Renting successful");
            }
            else {
                System.out.println("Vehicle already rented");
            }
        }
        else {
            System.out.println("You are already renting a vehicle");
        }
    }

    public void returnVehicleByVehicleId(String vehicleId, String userId) {

        Optional<Rental> returningRental = rentalRepo.findByVehicleIdAndReturnDateIsNull(vehicleId);

        if (returningRental.isPresent()) {
            if (returningRental.get().getUserId().equals(userId)) {
                returningRental.get().setReturnDate(LocalDateTime.now().toString());
                rentalRepo.save(returningRental.get());
                System.out.println("Returning successful");
            }
            else {
                System.out.println("You have not rented this vehicle!");
            }
        }
        else {
            System.out.println("The vehicle is not rented");
        }
    }
}
