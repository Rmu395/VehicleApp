package org.example.repositories.impl.hibernate;

import org.example.models.Rental;
import org.example.repositories.RentalRepository;
import org.hibernate.Session;
import org.hibernate.query.Query;

import java.util.List;
import java.util.Optional;

public class RentalHibernateRepository implements RentalRepository {
    private Session session;

    public void setSession(Session session) {
        this.session = session;
    }

    @Override
    public List<Rental> findAll() {
        return session.createQuery("FROM Rental", Rental.class).list();
    }

    @Override
    public Optional<Rental> findById(String id) {
        return Optional.ofNullable(session.get(Rental.class, id));
    }

    @Override
    public Optional<Rental> findByVehicleId(String vehicleId) {
        return Optional.ofNullable(session.get(Rental.class, vehicleId));
    }

    @Override
    public Optional<Rental> findByUserId(String userId) {
        return Optional.ofNullable(session.get(Rental.class, userId));
    }

    @Override
    public Rental save(Rental rental) {
        return session.merge(rental);
    }

    @Override
    public void deleteById(String id) {
        Rental rental = session.get(Rental.class, id);
        if (rental != null) {
            session.remove(rental);
        }
    }

    @Override
    public Optional<Rental> findByVehicleIdAndReturnDateIsNull
            (String vehicleId) {
        Query<Rental> query = session.createQuery("""
                FROM Rental r
                WHERE r.vehicle.id = :vehicleId
                AND r.returnDate = ''
                """, Rental.class);
        query.setParameter("vehicleId", vehicleId);
        return query.uniqueResultOptional();
    }


    @Override
    public Optional<Rental> findByUserIdAndReturnDateIsNull
            (String userId) {
        Query<Rental> query = session.createQuery("""
                FROM Rental r
                WHERE r.user.id = :userId
                AND r.returnDate = ''
                """, Rental.class);
        query.setParameter("userId", userId);
        return query.uniqueResultOptional();
    }
}