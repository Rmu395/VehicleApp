package org.example.services.hibernate;
import org.example.db.HibernateConfig;
import org.example.models.Rental;
import org.example.models.User;
import org.example.models.Vehicle;
import org.example.repositories.impl.hibernate.RentalHibernateRepository;
import org.example.repositories.impl.hibernate.UserHibernateRepository;
import org.example.repositories.impl.hibernate.VehicleHibernateRepository;
import org.example.services.interfaces.RentalService;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class RentalHibernateService implements RentalService {
    private final RentalHibernateRepository rentalRepo;
    private final VehicleHibernateRepository vehicleRepo;
    private final UserHibernateRepository userRepo;

    public RentalHibernateService(RentalHibernateRepository rentalRepo, VehicleHibernateRepository vehicleRepo,
                                  UserHibernateRepository userRepo) {
        this.rentalRepo = rentalRepo;
        this.vehicleRepo = vehicleRepo;
        this.userRepo = userRepo;
    }

    @Override
    public boolean isVehicleRented(String vehicleId) {
        try (Session session = HibernateConfig.getSessionFactory().openSession()) {
            rentalRepo.setSession(session);
            return rentalRepo.findByVehicleIdAndReturnDateIsNull(vehicleId).isPresent();
        }
    }

    @Override
    public Rental rent(String vehicleId, String userId) {
        Transaction tx = null;
        try (Session session = HibernateConfig.getSessionFactory().openSession()) {
            tx = session.beginTransaction();
            rentalRepo.setSession(session);
            vehicleRepo.setSession(session);
            userRepo.setSession(session);
            if (rentalRepo.findByVehicleIdAndReturnDateIsNull(vehicleId).isPresent()) {
                throw new IllegalStateException("Vehicle is rented");
            }
            Vehicle vehicle = vehicleRepo.findById(vehicleId)
                    .orElseThrow(() -> new RuntimeException("Vehicle not found"));
            User user = userRepo.findById(userId)
                    .orElseThrow(() -> new RuntimeException("User not found"));
            Rental rental = Rental.builder()
                    .id(UUID.randomUUID().toString())
                    .vehicle(vehicle)
                    .user(user)
                    .rentDate(LocalDateTime.now().toString())
                    .build();
            rentalRepo.save(rental);
            tx.commit();
            return rental;
        } catch (Exception e) {
            if (tx != null && tx.isActive()) {
                tx.rollback();
            }
            throw e;
        }
    }

    @Override
    public boolean returnRental(String vehicleId, String userId) {
        Transaction tx = null;
        try (Session session = HibernateConfig.getSessionFactory().openSession()) {
            tx = session.beginTransaction();
            rentalRepo.setSession(session);
            Optional<Rental> rentalToReturn = rentalRepo.findByVehicleIdAndReturnDateIsNull(vehicleId);

            if (rentalToReturn.isPresent()) {
                if (rentalToReturn.get().getUser().getId().equals(userId)) {
                    rentalToReturn.get().setReturnDate(LocalDateTime.now().toString());

                    rentalRepo.save(rentalToReturn.get());
                    tx.commit();
                    return true;
                } else {
                    return false;
                }
            } else {
                return false;
            }

        } catch (Exception e) {
            if (tx != null && tx.isActive()) {
                tx.rollback();
            }
            throw e;
        }
    }

    @Override
    public List<Rental> findAll() {
        try (Session session = HibernateConfig.getSessionFactory().openSession()) {
            rentalRepo.setSession(session);
            return rentalRepo.findAll();
        }
    }
}