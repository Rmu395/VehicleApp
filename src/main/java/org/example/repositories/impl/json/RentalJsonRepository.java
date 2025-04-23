package org.example.repositories.impl.json;

import com.google.gson.reflect.TypeToken;
import org.example.models.Rental;
import org.example.models.dbRental;
import org.example.repositories.DbRentalRepository;
import org.example.repositories.RentalRepository;
import org.example.utils.JsonFileStorage;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class RentalJsonRepository implements DbRentalRepository {
    private final JsonFileStorage<dbRental> storage = new JsonFileStorage<>("rentals.json", new TypeToken<List<dbRental>>(){}.getType());
    private final List<dbRental> rentals;

    public RentalJsonRepository() {
        this.rentals = new ArrayList<>(storage.load());
    }

    @Override
    public List<dbRental> findAll() {
        return new ArrayList<>(rentals);
    }

    @Override
    public Optional<dbRental> findById(String id) {
        return rentals.stream().filter(r -> r.getId().equals(id)).findFirst();
    }

    @Override
    public Optional<dbRental> findByVehicleId(String vehicleId) {
        return rentals.stream().filter(r -> r.getVehicleId().equals(vehicleId)).findFirst();
    }

    @Override
    public Optional<dbRental> findByUserId(String userId) {
        return rentals.stream().filter(r -> r.getUserId().equals(userId)).findFirst();
    }

    @Override
    public dbRental save(dbRental rental) {
        if (rental.getId() == null || rental.getId().isBlank()) {
            rental.setId(UUID.randomUUID().toString());
        } else {
            deleteById(rental.getId());
        }
        rentals.add(rental);
        storage.save(rentals);
        return rental;
    }

    @Override
    public void deleteById(String id) {
        rentals.removeIf(r -> r.getId().equals(id));
        storage.save(rentals);
    }

    @Override
    public Optional<dbRental> findByVehicleIdAndReturnDateIsNull(String vehicleId) {
        return rentals.stream()
                .filter(r -> r.getVehicleId().equals(vehicleId))
                .filter(r -> r.getReturnDate().isBlank())
                .findFirst();
    }

    @Override
    public Optional<dbRental> findByUserIdAndReturnDateIsNull(String userId) {
        return rentals.stream()
                .filter(r -> r.getUserId().equals(userId))
                .filter(r -> r.getReturnDate().isBlank())
                .findFirst();
    }
}
