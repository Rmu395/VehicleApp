package org.example.services.hibernate;

import org.example.db.HibernateConfig;
import org.example.models.Rental;
import org.example.models.User;
import org.example.models.Vehicle;
import org.example.repositories.impl.hibernate.RentalHibernateRepository;
import org.example.repositories.impl.hibernate.UserHibernateRepository;
import org.example.repositories.impl.hibernate.VehicleHibernateRepository;
import org.example.services.interfaces.AuthService;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.mindrot.jbcrypt.BCrypt;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

public class AuthHibernateService implements AuthService {
    private final UserHibernateRepository userRepo;

    public AuthHibernateService(UserHibernateRepository userRepo) {
        this.userRepo = userRepo;
    }

    @Override
    public boolean register(String login, String rawPassword, String role) {
        Transaction tx = null;
        try (Session session = HibernateConfig.getSessionFactory().openSession()) {
            tx = session.beginTransaction();
            userRepo.setSession(session);

            String hashed = BCrypt.hashpw(rawPassword, BCrypt.gensalt());

            if (userRepo.findByLogin(login).isPresent()) {
                return false;   // or should it throw an exception?
            }

            User user = User.builder()
                    .id(UUID.randomUUID().toString())
                    .login(login)
                    .password(hashed)
                    .role(role)
                    .build();

            userRepo.save(user);
            tx.commit();
            return true;
        } catch (Exception e) {
            if (tx != null && tx.isActive()) {
                tx.rollback();
            }
            throw e;
        }
    }

    @Override
    public Optional<User> login(String login, String rawPassword) {
        try (Session session = HibernateConfig.getSessionFactory().openSession()) {
            userRepo.setSession(session);

            return userRepo.findByLogin(login)
                    .filter(user -> BCrypt.checkpw(rawPassword, user.getPassword()));
        }
    }
}
