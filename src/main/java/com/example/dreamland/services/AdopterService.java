package com.example.dreamland.services;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.dreamland.api.model.jsonconverters.PetIdConverter;

@Service
public class AdopterService {

    private final Connection databaseConnection;

    @Autowired
    public AdopterService(Connection databaseConnection) {
        this.databaseConnection = databaseConnection;
    }

    public String adoptPet(int userId, PetIdConverter petId) {
        String query = "update pet set adopter_id = ? where pet_id = ?";
        try (PreparedStatement preparedStatement = databaseConnection.prepareStatement(query)) {
                preparedStatement.setInt(1, userId);
                preparedStatement.setInt(2, petId.getPetId());

                int rowsAffected = preparedStatement.executeUpdate();

                if (rowsAffected > 0) {
                    return "Pet adopted";
                } else {
                    return "Failed to adopt pet";
                }
            } catch (Exception e) {
                return e.toString();
            }
    }

    public String newAdopterRegister(int userId, double salary) {
        if (!isAdopter(userId)) {
            String query = "INSERT INTO adopter(id, salary) values (?, ?)";
            try (PreparedStatement preparedStatement = databaseConnection.prepareStatement(query)) {
                preparedStatement.setInt(1, userId);
                preparedStatement.setDouble(2, salary);

                int rowsAffected = preparedStatement.executeUpdate();

                if (rowsAffected > 0) {
                    return "New Adopter created";
                } else {
                    return "Failed to create new adopter";
                }
            } catch (Exception e) {
                return e.toString();
            }
        } else {
            return "already adopter";
        }
    }

    public boolean isAdopter(int userId) {
        String query = "select id from user where id = ? AND id in (select id from adopter)";
        try (PreparedStatement preparedStatement = databaseConnection.prepareStatement(query)) {
            preparedStatement.setInt(1, userId);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    return true;
                } else {
                    return false;
                }
            }
        } catch (Exception e) {
            e.setStackTrace(null);
            return false;
        }

    }
}
