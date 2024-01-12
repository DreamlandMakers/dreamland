package com.example.dreamland.services;

import java.sql.Connection;
import java.sql.PreparedStatement;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.dreamland.api.model.Pet;

@Service
public class PetAdoptationService {

    private final Connection databaseConnection;
    private final OwnerService ownerService;

    @Autowired
    public PetAdoptationService(Connection databaseConnection, OwnerService ownerService) {
        this.databaseConnection = databaseConnection;
        this.ownerService = ownerService;
    }

    public String newPetRegister(Pet pet) {

        ownerService.manageOwnerStatus(UserService.currentUserID); // Check if user is owner
        String query = "INSERT INTO pet(owner_id, age, type, cost, breed, name, year_ownership) "
                + "values (?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement preparedStatement = databaseConnection.prepareStatement(query)) {
            preparedStatement.setInt(1, UserService.currentUserID);
            preparedStatement.setInt(2, pet.getAge());
            preparedStatement.setString(3, pet.getType());
            preparedStatement.setDouble(4, pet.getAverageExpense());
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
}
