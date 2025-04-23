package org.example.services;

import org.example.models.Vehicle;
import org.example.repositories.DbRentalRepository;
import org.example.repositories.VehicleRepository;

public class SimpleVehicleService {

    private final VehicleRepository vehicleRepo;
    private final DbRentalRepository rentalRepo;

    public SimpleVehicleService(VehicleRepository vehicleRepo, DbRentalRepository rentalRepo) {
        this.vehicleRepo = vehicleRepo;
        this.rentalRepo = rentalRepo;
    }

    public void addVehicle(Vehicle vehicle) {
        // there really should be something more here but nothing else makes sense to move in here (mainly because of the scanner we use)
        vehicleRepo.save(vehicle);
    }

    public void showAvailableVehicles() {
        for (Vehicle v : vehicleRepo.findAll()) {
            if (rentalRepo.findByVehicleIdAndReturnDateIsNull(v.getId()).isEmpty()) {
                System.out.println(v);
            }
        }
    }

    public void removeVehicle(String vehicleId) {
        if (vehicleRepo.findById(vehicleId).isPresent()) {
            vehicleRepo.deleteById(vehicleId);
            System.out.println("Vehicle successfully removed");
        }
        else {
            System.out.println("Vehicle id is wrong, vehicle not removed");
        }
    }

    public void showAllVehicles() {
        for (Vehicle v : vehicleRepo.findAll()) {
            System.out.println(v.toString());
        }
    }
}
