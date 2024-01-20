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
import com.example.dreamland.api.model.jsonconverters.FosterConverter;
import com.example.dreamland.api.model.jsonconverters.PetIdConverter;

@Service
public class FosterFamilyService {

    private final Connection databaseConnection;

    @Autowired
    public FosterFamilyService(Connection databaseConnection) {
        this.databaseConnection = databaseConnection;
    }

    public String fosterPet(int userId, PetIdConverter petId) {
        if (isFoster(userId)) {
            String query = "update pet set foster_id = ? , foster_from_date = ? where pet_id = ?";
            try (PreparedStatement preparedStatement = databaseConnection.prepareStatement(query)) {
                preparedStatement.setInt(1, userId);
                preparedStatement.setString(2, java.time.LocalDate.now().toString());
                preparedStatement.setInt(3, petId.getPetId());

                int rowsAffected = preparedStatement.executeUpdate();

                if (rowsAffected > 0) {
                    return "Pet is forwarded to the foster family";
                } else {
                    return "Failed to adopt pet";
                }
            } catch (Exception e) {
                return e.toString();
            }
        } else {
            return "Not an foster family member";
        }
    }

    public String newFosterRegister(int userId, FosterConverter fosterConverter) {
        if (!isFoster(userId)) {
            String query = "INSERT INTO foster(id, salary, type) values (?, ?, ?)";
            try (PreparedStatement preparedStatement = databaseConnection.prepareStatement(query)) {
                preparedStatement.setInt(1, userId);
                preparedStatement.setDouble(2, fosterConverter.getSalary());
                preparedStatement.setString(3, fosterConverter.getType());

                int rowsAffected = preparedStatement.executeUpdate();

                if (rowsAffected > 0) {
                    return "New Foster created";
                } else {
                    return "Failed to create new foster";
                }
            } catch (Exception e) {
                return e.toString();
            }
        } else {
            return "already foster";
        }
    }

    public List<Pet> getFosterPetList(int userId) {
        String query = "select pet_id, age, type, cost, breed, name from pet" 
            + " where type in (select type from foster where id = ?)";
        try (PreparedStatement preparedStatement = databaseConnection.prepareStatement(query)) {
            preparedStatement.setInt(1, userId);
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
    
                    petList.add(pet);
                }
                return petList;
            }
        } catch (Exception e) {
            e.setStackTrace(null);
            return Collections.emptyList();
        }
    }

    public boolean isFoster(int userId) {
        String query = "select id from user where id = ? AND id in (select id from foster)";
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
