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
public class FosterFamilyService {

    private final Connection databaseConnection;
    private OwnerService ownerService;
    @Autowired
    public FosterFamilyService(Connection databaseConnection) {
        this.databaseConnection = databaseConnection;
    }

    public String fosterPet(int userId, int petId) {
        if (!isFoster(userId)) {
            return "You cannot foster this pet, you are not a registered foster";
        }
        if(!canFoster(userId,petId)){
            return "You cannot foster this pet. Possible reasons: not older than 18, not having financial stability for this pet...";
        }
            String query = "update pet set foster_id = ? , foster_from_date = ? where pet_id = ?";
            try (PreparedStatement preparedStatement = databaseConnection.prepareStatement(query)) {
                preparedStatement.setInt(1, userId);
                preparedStatement.setString(2, java.time.LocalDate.now().toString());
                preparedStatement.setInt(3, petId);

                int rowsAffected = preparedStatement.executeUpdate();

                if (rowsAffected > 0) {
                    return "Pet is forwarded to the foster family";
                } else {
                    return "Failed to adopt pet";
                }
            } catch (Exception e) {
                return e.toString();
            }
    }

    public String newFosterRegister(int userId, double salary, String type) {
        if (!isFoster(userId)) {
            String query = "INSERT INTO foster(id, salary, type) values (?, ?, ?)";
            try (PreparedStatement preparedStatement = databaseConnection.prepareStatement(query)) {
                preparedStatement.setInt(1, userId);
                preparedStatement.setDouble(2, salary);
                preparedStatement.setString(3, type);

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
        String query = "select pet_id, age, type, cost, breed, name, year_ownership from pet where adopter_id is null and foster_id is null and owner_id != ? and type in (Select type from foster where id=?) ";
        try (PreparedStatement preparedStatement = databaseConnection.prepareStatement(query)) {
            preparedStatement.setInt(1, userId);
            preparedStatement.setInt(2, userId);
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
    public List<Pet> getFosteredPets(int userId) {
        String query = "select pet_id, age, type, cost, breed, name, year_ownership from pet where foster_id=?";
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
    private boolean canFoster(int userId, int petId){
        String query = "SELECT id\r\n" + //
                "FROM foster as f\r\n" + //
                "WHERE f.id = ?\r\n" + //
                "    AND f.salary - (SELECT cost FROM pet WHERE pet_id = ?) > (\r\n" + //
                "        SELECT COALESCE(SUM(cost), 0)\r\n" + //
                "        FROM pet\r\n" + //
                "        WHERE (\r\n" + //
                "            owner_id = f.id AND adopter_id IS NULL AND foster_id IS NULL\r\n" + //
                "        ) OR adopter_id = f.id OR foster_id = f.id\r\n" + //
                "    )\r\n" + //
                "    AND f.id in (select id from user where TIMESTAMPDIFF(YEAR, birth_date, CURDATE()) > 18 );\r\n" + //
                "";
        try (PreparedStatement preparedStatement = databaseConnection.prepareStatement(query)) {
            preparedStatement.setInt(1, userId);
            preparedStatement.setInt(2, petId);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    return true;
                }
                return false;
            }

        } catch (Exception e) {
            return false;
        }
    }
    public double fosterSalary(int userId) {
        String query = "select salary from foster where id = ? ";
        try (PreparedStatement preparedStatement = databaseConnection.prepareStatement(query)) {
            preparedStatement.setInt(1, userId);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getDouble("salary");
                } else {
                    return -1;
                }
            }
        } catch (Exception e) {
            e.setStackTrace(null);
            return -1;
        }

    }
}
