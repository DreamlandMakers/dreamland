package com.example.dreamland.services;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.dreamland.api.model.Pet;
import com.example.dreamland.api.model.jsonconverters.PetIdConverter;

@Service
public class AdopterService {

    private final Connection databaseConnection;
    private FosterFamilyService fosterFamilyService;

    @Autowired
    public AdopterService(Connection databaseConnection) {
        this.databaseConnection = databaseConnection;
    }

    public String adoptPet(int userId, PetIdConverter petId) {
        if (isAdopter(userId)) {
            String query = "update pet set adopter_id = ? , adoption_date = ? where pet_id = ?";
            try (PreparedStatement preparedStatement = databaseConnection.prepareStatement(query)) {
                preparedStatement.setInt(1, userId);
                preparedStatement.setString(2, java.time.LocalDate.now().toString());
                preparedStatement.setInt(3, petId.getPetId());

                int rowsAffected = preparedStatement.executeUpdate();

                if (rowsAffected > 0) {
                    return "Pet adopted";
                } else {
                    return "Failed to adopt pet";
                }
            } catch (Exception e) {
                return e.toString();
            }
        } else {
            return "Not an adopter";
        }
    }

    public List<Pet> getAdoptablePetList() {
        String query = "select pet_id, age, type, cost, breed, name, year_ownership from pet where adopter_id is null";
        try (PreparedStatement preparedStatement = databaseConnection.prepareStatement(query)) {
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                List<Pet> petList = new ArrayList<>();
                while (resultSet.next()) {
                    Pet pet = new Pet();
                    pet.setId(resultSet.getInt("pet_id"));
                    pet.setAge(resultSet.getInt("age"));
                    pet.setType(resultSet.getString("type"));
                    pet.setAverageExpense(resultSet.getDouble("cost"));
                    pet.setBreed(resultSet.getString("breed"));
                    pet.setName(resultSet.getString("name"));
                    pet.setYearOfOwnership(resultSet.getInt("year_ownership"));

                    petList.add(pet);
                }
                return petList;
            }

        } catch (Exception e) {
            e.setStackTrace(null);
            return Collections.emptyList();
        }

    }

    public List<Pet> getFilteredAdoptablePetList() {
        return Collections.emptyList();
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
