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

    public  List<Pet> getGivenPets(int userId) {
        List<Pet> givenPetList = new ArrayList<>();
        if(!isOwner(userId)){
            return givenPetList;
        }
        String query = "select pet_id, age, type, cost, breed, name, year_ownership from pet where adopter_id is null and foster_id is null and owner_id is not null and owner_id=?";
        try (PreparedStatement preparedStatement = databaseConnection.prepareStatement(query)) {
            preparedStatement.setInt(1, userId);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    Pet pet = new Pet();
                    pet.setId(resultSet.getInt("pet_id"));
                    pet.setAge(resultSet.getInt("age"));
                    pet.setType(resultSet.getString("type"));
                    pet.setAverageExpense(resultSet.getDouble("cost"));
                    pet.setBreed(resultSet.getString("breed"));
                    pet.setName(resultSet.getString("name"));
                    pet.setYearOfOwnership(resultSet.getInt("year_ownership"));
                    givenPetList.add(pet);
                }
                return givenPetList;
            }
        } catch (Exception e) {
            e.setStackTrace(null);
            return Collections.emptyList();
        }
    }

    public  Pet getPet(int petId) {
        String query = "select pet_id, age, type, cost, breed, name, year_ownership from pet where pet_id=?";
        try (PreparedStatement preparedStatement = databaseConnection.prepareStatement(query)) {
            preparedStatement.setInt(1, petId);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    Pet pet = new Pet();
                    pet.setId(resultSet.getInt("pet_id"));
                    pet.setAge(resultSet.getInt("age"));
                    pet.setType(resultSet.getString("type"));
                    pet.setAverageExpense(resultSet.getDouble("cost"));
                    pet.setBreed(resultSet.getString("breed"));
                    pet.setName(resultSet.getString("name"));
                    pet.setYearOfOwnership(resultSet.getInt("year_ownership"));
                    return pet;
                }
                return null;
            }
        } catch (Exception e) {
            e.setStackTrace(null);
            return null;
        }
    }
    public  String updatePet(Pet pet) {
        String query = "update pet set name = ? , age = ?, type = ? , averageExpense = ?, breed = ? , yearOfOwnership = ?  where pet_id = ?";
            try (PreparedStatement preparedStatement = databaseConnection.prepareStatement(query)) {
                preparedStatement.setString(1, pet.getName());
                preparedStatement.setInt(2, pet.getAge());
                preparedStatement.setString(3, pet.getType());
                preparedStatement.setDouble(4, pet.getAverageExpense());
                preparedStatement.setString(5, pet.getBreed());
                preparedStatement.setInt(6, pet.getYearOfOwnership());

                int rowsAffected = preparedStatement.executeUpdate();

                if (rowsAffected > 0) {
                    return "Pet updated";
                } else {
                    return "Failed to update pet";
                }
            } catch (Exception e) {
                return e.toString();
            }
    }
}
