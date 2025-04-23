package org.example.repositories;

import org.example.models.dbRental;

import java.util.List;
import java.util.Optional;

public interface DbRentalRepository {
    List<dbRental> findAll();
    Optional<dbRental> findById(String id);
    Optional<dbRental> findByVehicleId(String vehicleId);
    Optional<dbRental> findByUserId(String userId);
    dbRental save(dbRental rental);
    void deleteById(String id);
    public Optional<dbRental> findByVehicleIdAndReturnDateIsNull(String vehicleId);
    public Optional<dbRental> findByUserIdAndReturnDateIsNull(String userId);
}
