package org.example.app;

import org.apache.commons.codec.digest.DigestUtils;
import org.example.model.Vehicle;
import org.example.repositories.IVehicleRepository;
import org.example.repositories.ListOfVehicles;

import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        IVehicleRepository repository = new ListOfVehicles();
        repository.save();

        ArrayList<String> passwords = new ArrayList<>();
        passwords.add("a");
        passwords.add("b");
        passwords.add("a");

        for (String s : passwords) {
            String sha256hex = DigestUtils.sha256Hex(s);
            System.out.println(s);
            System.out.println(sha256hex);
        }

//        List<Vehicle> testList;
//        testList = repository.getVehicles();
//
//        System.out.println(repository.getHash(0));
//        System.out.println(testList.get(0).hashCode());
//        System.out.println(repository.getHash(1));
//        System.out.println(testList.get(1).hashCode());
//
//        System.out.println(repository.getVehicle(0).equals(testList.get(0)));   // should be true
//        System.out.println(repository.getVehicle(0).equals(testList.get(1)));   // should be true
//        repository.returnVehicle(0);
//        System.out.println(repository.getVehicle(0).equals(testList.get(0)));   // should be false
//        repository.rentVehicle(0);

//        System.out.println(repository);
//        System.out.println("----------------------------");
//        for (Vehicle vehicle : testList) {
//            System.out.println(vehicle);
//        }
//
//        repository.rentVehicle(0);
//        repository.rentVehicle(1);
//        repository.rentVehicle(2);
//        repository.rentVehicle(3);
//        repository.rentVehicle(4);
//        repository.rentVehicle(5);
//        repository.rentVehicle(6);
//        repository.rentVehicle(7);
//        repository.rentVehicle(8);
//
//        System.out.println("OOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOO");
//        System.out.println(repository);
//        System.out.println("----------------------------");
//        for (Vehicle vehicle : testList) {
//            System.out.println(vehicle);
//        }
//
//        for (Vehicle vehicle : testList) {
//            vehicle.setModel("CHANGE");
//        }
//
//        System.out.println("OOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOO");
//        System.out.println(repository);
//        System.out.println("----------------------------");
//        for (Vehicle vehicle : testList) {
//            System.out.println(vehicle);
//        }
    }
}