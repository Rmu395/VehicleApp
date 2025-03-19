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
    public void rentVehicle(int vehicleId) {
        for (Vehicle vehicle : this.list)
        {
            if (vehicle.getVehicleId() == vehicleId) {
                if (!vehicle.isRented()) vehicle.setRented(true);
                else System.out.println("!!!This vehicle is already rented!!!");
                this.save();
                break;
            }
        }
    }

    @Override
    public void returnVehicle(int vehicleId) {
        for (Vehicle vehicle : this.list)
        {
            if (vehicle.getVehicleId() == vehicleId) {
                if (vehicle.isRented()) vehicle.setRented(false);
                else System.out.println("!!!This vehicle was not rented!!!");
                this.save();
                break;
            }
        }
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
    public String toString() {
        StringBuilder toReturn = new StringBuilder("ListOfVehicles{\n");

        for (Vehicle vehicle : this.list) {
            toReturn.append("\t").append(vehicle.toString()).append("\n");
        }

        toReturn.append("}\n");

        return toReturn.toString();
    }


    // for the time being (i need this for testing)
    public int getHash(int number_on_list){//int vehicleId) {
//        // this can be changed for simply returning a number off of the list if needed
//        // (for now it looks for the vehicle id so if there are 2 entries with the same id, it will only see the first one)
//        int result = -1;
//        for (Vehicle vehicle : this.list)
//        {
//            if (vehicle.vehicleId == vehicleId) {
//                result = vehicle.hashCode();
//                break;
//            }
//        }
//        return result;

        // the other implementation
        return list.get(number_on_list).hashCode();
    }
    public Vehicle getVehicle(int number_on_list){//int vehicleId) {
//        // this can be changed for simply returning a number off of the list if needed
//        // (for now it looks for the vehicle id so if there are 2 entries with the same id, it will only see the first one)
//        int result = -1;
//        for (Vehicle vehicle : this.list)
//        {
//            if (vehicle.vehicleId == vehicleId) {
//                result = vehicle.hashCode();
//                break;
//            }
//        }
//        return result;

        // the other implementation
        return list.get(number_on_list);
    }

}
