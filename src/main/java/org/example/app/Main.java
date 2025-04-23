package org.example.app;

import org.example.repositories.DbRentalRepository;
import org.example.repositories.RentalRepository;
import org.example.repositories.UserRepository;
import org.example.repositories.VehicleRepository;
import org.example.repositories.impl.hibernate.RentalHibernateRepository;
import org.example.repositories.impl.hibernate.UserHibernateRepository;
import org.example.repositories.impl.hibernate.VehicleHibernateRepository;
import org.example.repositories.impl.jdbc.RentalJdbcRepository;
import org.example.repositories.impl.jdbc.UserJdbcRepository;
import org.example.repositories.impl.jdbc.VehicleJdbcRepository;
import org.example.repositories.impl.json.RentalJsonRepository;
import org.example.repositories.impl.json.UserJsonRepository;
import org.example.repositories.impl.json.VehicleJsonRepository;
import org.example.services.SimpleAuthService;
import org.example.services.SimpleRentalService;
import org.example.services.SimpleVehicleService;
import org.example.services.hibernate.AuthHibernateService;
import org.example.services.hibernate.RentalHibernateService;
import org.example.services.hibernate.VehicleHibernateService;


public class Main {
    public static void main(String[] args) {
        String storageType = "hibernate";

        UserRepository userRepo;
        VehicleRepository vehicleRepo;
        DbRentalRepository rentalRepo;

        switch (storageType) {
            case "jdbc" -> {
                userRepo = new UserJdbcRepository();
                vehicleRepo = new VehicleJdbcRepository();
                rentalRepo = new RentalJdbcRepository();

                SimpleAuthService simpleAuthService = new SimpleAuthService(userRepo);
                SimpleVehicleService simpleVehicleService = new SimpleVehicleService(vehicleRepo, rentalRepo);
                SimpleRentalService simpleRentalService = new SimpleRentalService(rentalRepo);

                App app = new App(simpleAuthService, simpleVehicleService, simpleRentalService);
                app.run();
            }
            case "json" -> {
                userRepo = new UserJsonRepository();
                vehicleRepo = new VehicleJsonRepository();
                rentalRepo = new RentalJsonRepository();

                SimpleAuthService simpleAuthService = new SimpleAuthService(userRepo);
                SimpleVehicleService simpleVehicleService = new SimpleVehicleService(vehicleRepo, rentalRepo);
                SimpleRentalService simpleRentalService = new SimpleRentalService(rentalRepo);

                App app = new App(simpleAuthService, simpleVehicleService, simpleRentalService);
                app.run();
            }
            case "hibernate" -> {
                UserHibernateRepository hibernateUserRepo = new UserHibernateRepository();
                VehicleHibernateRepository hibernateVehicleRepo = new VehicleHibernateRepository();
                RentalHibernateRepository hibernateRentalRepo = new RentalHibernateRepository();

                RentalHibernateService rentalHibernateService = new RentalHibernateService(hibernateRentalRepo, hibernateVehicleRepo, hibernateUserRepo);
                AuthHibernateService authHibernateService = new AuthHibernateService(hibernateUserRepo);
                VehicleHibernateService vehicleHibernateService = new VehicleHibernateService(hibernateVehicleRepo, hibernateRentalRepo);

                HibernateApp hibernateApp = new HibernateApp(authHibernateService, vehicleHibernateService, rentalHibernateService);
                hibernateApp.run();
            }
            default -> throw new IllegalArgumentException("Unknown storage type: " + storageType);
        }
    }
}