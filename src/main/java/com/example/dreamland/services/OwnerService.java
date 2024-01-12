package com.example.dreamland.services;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.dreamland.api.model.Pet;

@Service
public class OwnerService {

    private final Connection databaseConnection;

    @Autowired
    public OwnerService(Connection databaseConnection) {
        this.databaseConnection = databaseConnection;
    }

    public String newPetRegister(Pet pet) {

        manageOwnerStatus(UserService.currentUserID); // Check if user is owner
        String query = "INSERT INTO pet(owner_id, age, type, cost, breed, name, year_ownership) "
                + "values (?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement preparedStatement = databaseConnection.prepareStatement(query)) {
            preparedStatement.setInt(1, UserService.currentUserID);
            preparedStatement.setInt(2, pet.getAge());
            preparedStatement.setString(3, pet.getType());
            preparedStatement.setDouble(4, pet.getCost());
            preparedStatement.setString(5, pet.getBreed());
            preparedStatement.setString(6, pet.getName());
            preparedStatement.setInt(7, pet.getYearOfOwnership());

            int rowsAffected = preparedStatement.executeUpdate();

            if (rowsAffected > 0) {
                return "Pet is listed";
            } else {
                return "Failed to list the pet";
            }
        } catch (Exception e) {
            return e.toString();
        }
    }

    public String manageOwnerStatus(int userId) {
        if (!isOwner(userId)) {
            String query = "INSERT INTO owner(id) values (?)";
            try (PreparedStatement preparedStatement = databaseConnection.prepareStatement(query)) {
                preparedStatement.setInt(1, userId);

                int rowsAffected = preparedStatement.executeUpdate();

                if (rowsAffected > 0) {
                    return "New owner created";
                } else {
                    return "Failed to create new owner";
                }
            } catch (Exception e) {
                return e.toString();
            }
        } else {
            return "already owner";
        }
    }

    public boolean isOwner(int userId) {
        String query = "select id from user where id = ? AND id in (select id from owner)";
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
