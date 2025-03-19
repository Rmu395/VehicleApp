package org.example.repositories;

import org.example.model.Vehicle;

import java.util.List;

public interface IVehicleRepository {
    public void rentVehicle(int vehicleId);
    public void returnVehicle(int vehicleId);
    public List<Vehicle> getVehicles();
    public void save();
    public int getHash(int vehicleId);
    public Vehicle getVehicle(int number_on_list);
}
