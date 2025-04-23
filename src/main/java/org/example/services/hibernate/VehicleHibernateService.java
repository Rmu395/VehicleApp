package org.example.services.hibernate;

import org.example.db.HibernateConfig;
import org.example.models.Vehicle;
import org.example.repositories.impl.hibernate.RentalHibernateRepository;
import org.example.repositories.impl.hibernate.VehicleHibernateRepository;
import org.example.services.interfaces.VehicleService;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class VehicleHibernateService implements VehicleService {
    private final VehicleHibernateRepository vehicleRepo;
    private final RentalHibernateRepository rentalRepo;

    public VehicleHibernateService(VehicleHibernateRepository vehicleRepo,
                                   RentalHibernateRepository rentalRepo) {
        this.vehicleRepo = vehicleRepo;
        this.rentalRepo = rentalRepo;
    }

    @Override
    public List<Vehicle> findAll() {
        try (Session session = HibernateConfig.getSessionFactory().openSession()) {
            vehicleRepo.setSession(session);
            return vehicleRepo.findAll();
        }
    }

    @Override
    public Optional<Vehicle> findById(String id) {
        try (Session session = HibernateConfig.getSessionFactory().openSession()) {
            vehicleRepo.setSession(session);
            return vehicleRepo.findById(id);
        }
    }

    @Override
    public Vehicle save(Vehicle vehicle) {
        Transaction tx = null;
        try (Session session = HibernateConfig.getSessionFactory().openSession()) {
            tx = session.beginTransaction();
            vehicleRepo.setSession(session);
            vehicleRepo.save(vehicle);
            tx.commit();
            return vehicle;
        } catch (Exception e) {
            if (tx != null && tx.isActive()) {
                tx.rollback();
            }
            throw e;
        }
    }

    @Override
    public List<Vehicle> findAvailableVehicles() {
        List<Vehicle> returnList = new ArrayList<>();
        try (Session session = HibernateConfig.getSessionFactory().openSession()) {
            vehicleRepo.setSession(session);
            rentalRepo.setSession(session);

            for (Vehicle v : vehicleRepo.findAll()) {
                if (rentalRepo.findByVehicleIdAndReturnDateIsNull(v.getId()).isEmpty()) {
                    returnList.add(v);
                }
            }

            return returnList;
        }
    }

    @Override
    public boolean isAvailable(String vehicleId) {
        try (Session session = HibernateConfig.getSessionFactory().openSession()) {
            vehicleRepo.setSession(session);
            return vehicleRepo.findById(vehicleId).isPresent();
        }
    }
}
