package org.example.repositories;

import org.example.model.Car;
import org.example.model.Motorcycle;
import org.example.model.Vehicle;

import java.io.*;
import java.util.*;

public class ListOfVehicles implements IVehicleRepository {
    private List<Vehicle> list = new ArrayList<>();

    public ListOfVehicles() {
        try {
            File file = new File("vehicles.csv");
            Scanner scanner = new Scanner(file);
            while (scanner.hasNextLine()){
                // check if the thing is a car or a motorcycle and add to the list
                String data = scanner.nextLine();
                String[] values = data.split(";");
                if (values[0].equals("C")) {
                    this.list.add(new Car(
                            values[1],
                            values[2],
                            Integer.parseInt(values[3]),
                            Float.parseFloat(values[4]),
                            Boolean.parseBoolean(values[5]),
                            Integer.parseInt(values[6])));
                } else if (values[0].equals("M")) {
                    this.list.add(new Motorcycle(
                            values[1],
                            values[2],
                            Integer.parseInt(values[3]),
                            Float.parseFloat(values[4]),
                            Boolean.parseBoolean(values[5]),
                            Integer.parseInt(values[6]),
                            values[7]));
                }
                else {
                    System.out.println("!!!Bad type in file!!!");
                }
            }
            scanner.close();
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean rentVehicle(int vehicleId) { // true -> success
        boolean success = false;
        for (Vehicle vehicle : this.list)
        {
            if (vehicle.getVehicleId() == vehicleId) {
                if (!vehicle.isRented()) {
                    vehicle.setRented(true);
                    success = true;
                }
                else {
                    System.out.println("!!!This vehicle is already rented!!!");
                    success = false;
                }
                this.save();
                break;
            }
        }
        return success;
    }

    @Override
    public boolean returnVehicle(int vehicleId) {
        boolean success = false;
        for (Vehicle vehicle : this.list)
        {
            if (vehicle.getVehicleId() == vehicleId) {
                if (vehicle.isRented()) {
                    vehicle.setRented(false);
                    success = true;
                }
                else {
                    System.out.println("!!!This vehicle was not rented!!!");
                    success = false;
                }
                this.save();
                break;
            }
        }
        return success;
    }

    @Override
    public List<Vehicle> getVehicles() {
        // this is supposed to return a copy of the list (THE POINT IS TO CREATE A NEW ONE AND SEND THAT ONE)
        List<Vehicle> copyOfList = new ArrayList<>();

        for (Vehicle vehicle : this.list) {
            if (vehicle instanceof Car) {
                copyOfList.add(new Car(
                        vehicle.getBrand(),
                        vehicle.getModel(),
                        vehicle.getYear(),
                        vehicle.getPrice(),
                        vehicle.isRented(),
                        vehicle.getVehicleId()));
            } else if (vehicle instanceof Motorcycle) {
                copyOfList.add(new Motorcycle(
                        vehicle.getBrand(),
                        vehicle.getModel(),
                        vehicle.getYear(),
                        vehicle.getPrice(),
                        vehicle.isRented(),
                        vehicle.getVehicleId(),
                        ((Motorcycle) vehicle).getCategory()));
            } else System.out.println("Bad type in ListOfVehicles");
        }
        return copyOfList;
    }

    @Override
    public void save() {
        try {
            FileWriter fileWriter = new FileWriter("vehicles.csv");
            PrintWriter printWriter = new PrintWriter(fileWriter);
            // I mean we dont need to write the whole thing but its easy this way
            for (Vehicle vehicle : this.list)
            {
                // check if the vehicle is a motorcycle?
                String type = "TypeNotInitialized";
                if (vehicle instanceof Car) type = "C;";
                else if (vehicle instanceof Motorcycle) type = "M;";
                printWriter.println(type + vehicle.toCSV());
            }
            printWriter.close();

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean addVehicle(Vehicle vehicle) {    // false = didnt succeed
        boolean isDouble = false;

        for (Vehicle vehicleCheck : this.list) {
            if (vehicleCheck.equals(vehicle)) {
                isDouble = true;
                break;
            }
        }

        if (!isDouble) {
            list.add(vehicle);
        }

        return !isDouble;
    }

    @Override
    public boolean removeVehicle(int vehicleId) {   // false = didnt succeed
        boolean found = false;
        for (Vehicle vehicleCheck : this.list) {
            if (vehicleCheck.getVehicleId() == vehicleId) {
                found = true;
                list.remove(vehicleCheck);
                break;
            }
        }
        this.save();
        return found;
    }

    @Override
    public Vehicle getVehicle(int vehicleId) {
        for (Vehicle vehicle : this.list) {
            if (vehicle.getVehicleId() == vehicleId) {
                return vehicle;
            }
        }
        System.out.println("getVehicle() didn't find a vehicle with specified id, returned the first vehicle from list");
        return this.list.get(0);    // no clue what should i return in this case
    }

    @Override
    public String toString() {
        StringBuilder toReturn = new StringBuilder("ListOfVehicles{\n");

        for (Vehicle vehicle : this.list) {
            toReturn.append("\t").append(vehicle.toString()).append("\n");
        }

        toReturn.append("}\n");

        return toReturn.toString();
    }

}
