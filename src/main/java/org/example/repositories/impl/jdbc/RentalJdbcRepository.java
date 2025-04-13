package org.example.repositories.impl.jdbc;

import com.google.gson.Gson;
import org.example.db.JdbcConnectionManager;
import org.example.models.Rental;
import org.example.repositories.RentalRepository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

public class RentalJdbcRepository implements RentalRepository {

    private final Gson gson = new Gson();

    @Override
    public List<Rental> findAll() {
        List<Rental> list = new ArrayList<>();
        String sql = "SELECT * FROM rental";
        try (Connection connection = JdbcConnectionManager.getInstance().getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Rental rental = Rental.builder()
                        .id(rs.getString("id"))
                        .vehicleId(rs.getString("vehicle_id"))
                        .userId(rs.getString("user_id"))
                        .rentDate(rs.getString("rent_date"))
                        .returnDate(rs.getString("return_date"))
                        .build();
                list.add(rental);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error occurred while reading rentals", e);
        }
        return list;
    }

    @Override
    public Optional<Rental> findById(String id) {
        String sql = "SELECT * FROM rental WHERE id = ?";
        try (Connection connection = JdbcConnectionManager.getInstance().getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql)) {

            stmt.setString(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    Rental rental = Rental.builder()
                            .id(rs.getString("id"))
                            .vehicleId(rs.getString("vehicle_id"))
                            .userId(rs.getString("user_id"))
                            .rentDate(rs.getString("rent_date"))
                            .returnDate(rs.getString("return_date"))
                            .build();

                    return Optional.of(rental);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error occurred while reading rental", e);
        }
        return Optional.empty();
    }

    @Override
    public Optional<Rental> findByVehicleId(String vehicleId) {
        String sql = "SELECT * FROM rental WHERE vehicle_id = ?";
        try (Connection connection = JdbcConnectionManager.getInstance().getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql)) {

            stmt.setString(1, vehicleId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    Rental rental = Rental.builder()
                            .id(rs.getString("id"))
                            .vehicleId(rs.getString("vehicle_id"))
                            .userId(rs.getString("user_id"))
                            .rentDate(rs.getString("rent_date"))
                            .returnDate(rs.getString("return_date"))
                            .build();

                    return Optional.of(rental);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error occurred while reading rental", e);
        }
        return Optional.empty();
    }

    @Override
    public Optional<Rental> findByUserId(String userId) {

        String sql = "SELECT * FROM rental WHERE user_id = ?";
        try (Connection connection = JdbcConnectionManager.getInstance().getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql)) {

            stmt.setString(1, userId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    Rental rental = Rental.builder()
                            .id(rs.getString("id"))
                            .vehicleId(rs.getString("vehicle_id"))
                            .userId(rs.getString("user_id"))
                            .rentDate(rs.getString("rent_date"))
                            .returnDate(rs.getString("return_date"))
                            .build();

                    return Optional.of(rental);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error occurred while reading rental", e);
        }
        return Optional.empty();
    }

    @Override
    public Rental save(Rental rental) {
        if (rental.getId() == null || rental.getId().isBlank()) {
            rental.setId(UUID.randomUUID().toString());
        } else {
            deleteById(rental.getId());
        }

        String sql = "INSERT INTO rental (id, vehicle_id, user_id, rent_date, return_date) VALUES (?, ?, ?, ?, ?)";
        try (Connection connection = JdbcConnectionManager.getInstance().getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql)) {

            stmt.setString(1, rental.getId());
            stmt.setString(2, rental.getVehicleId());
            stmt.setString(3, rental.getUserId());
            stmt.setString(4, rental.getRentDate());
            stmt.setString(5, rental.getReturnDate());

            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error occurred while saving rental", e);
        }
        return rental;
    }

    @Override
    public void deleteById(String id) {
        String sql = "DELETE FROM rental WHERE id = ?";
        try (Connection connection = JdbcConnectionManager.getInstance().getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql)) {

            stmt.setString(1, id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error occurred while deleting rental", e);
        }
    }

    @Override
    public Optional<Rental> findByVehicleIdAndReturnDateIsNull(String vehicleId) {
        String sql = "SELECT * FROM rental WHERE vehicle_id = ? AND return_date = '' LIMIT 1";
        try (Connection connection = JdbcConnectionManager.getInstance().getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql)) {

            stmt.setString(1, vehicleId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    Rental rental = Rental.builder()
                            .id(rs.getString("id"))
                            .vehicleId(rs.getString("vehicle_id"))
                            .userId(rs.getString("user_id"))
                            .rentDate(rs.getString("rent_date"))
                            .returnDate(rs.getString("return_date"))
                            .build();

                    return Optional.of(rental);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error occurred while searching rental", e);
        }
        return Optional.empty();
    }

    @Override
    public Optional<Rental> findByUserIdAndReturnDateIsNull(String userId) {
        String sql = "SELECT * FROM rental WHERE user_id = ? AND return_date = '' LIMIT 1";
        try (Connection connection = JdbcConnectionManager.getInstance().getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql)) {

            stmt.setString(1, userId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    Rental rental = Rental.builder()
                            .id(rs.getString("id"))
                            .vehicleId(rs.getString("vehicle_id"))
                            .userId(rs.getString("user_id"))
                            .rentDate(rs.getString("rent_date"))
                            .returnDate(rs.getString("return_date"))
                            .build();

                    return Optional.of(rental);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error occurred while searching rental", e);
        }
        return Optional.empty();
    }
}
