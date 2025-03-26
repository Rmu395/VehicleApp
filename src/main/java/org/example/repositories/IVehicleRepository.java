package org.example.repositories;

import org.example.model.Vehicle;

import java.util.List;

public interface IVehicleRepository {
    public boolean rentVehicle(int vehicleId);
    public boolean returnVehicle(int vehicleId);
    public List<Vehicle> getVehicles();
    public void save();
    public boolean addVehicle(Vehicle vehicle); // returns vehicle id
    public boolean removeVehicle(int vehicleId);
    public Vehicle getVehicle(int vehicleId);
}
