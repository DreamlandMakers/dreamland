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
import com.example.dreamland.api.model.Report;

@Service
public class AdopterService {

    private final Connection databaseConnection;
    private FosterFamilyService fosterFamilyService;
    private OwnerService ownerService;
    @Autowired
    public AdopterService(Connection databaseConnection) {
        this.databaseConnection = databaseConnection;
    }

    public String adoptPet(int userId, int petId) {

        if (isAdopter(userId) && canAdopt(userId,petId)) {
            String query = "update pet set adopter_id = ? , adoption_date = ?, year_ownership = 0  where pet_id = ? and owner_id != ?";
            try (PreparedStatement preparedStatement = databaseConnection.prepareStatement(query)) {
                preparedStatement.setInt(1, userId);
                preparedStatement.setString(2, java.time.LocalDate.now().toString());
                preparedStatement.setInt(3, petId);
                preparedStatement.setInt(4, userId);
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

    private boolean canAdopt(int userId, int petId){
        String query = "SELECT id\r\n" + //
                "FROM adopter as a\r\n" + //
                "WHERE a.id = ?\r\n" + //
                "    AND a.salary - (SELECT cost FROM pet WHERE pet_id = ?) > (\r\n" + //
                "        SELECT COALESCE(SUM(cost), 0)\r\n" + //
                "        FROM pet\r\n" + //
                "        WHERE (\r\n" + //
                "            owner_id = a.id AND adopter_id IS NULL AND foster_id IS NULL\r\n" + //
                "        ) OR adopter_id = a.id OR foster_id = a.id\r\n" + //
                "    )\r\n" + //
                "    AND a.id in (select id from user where TIMESTAMPDIFF(YEAR, birth_date, CURDATE()) > 18 );\r\n" + //
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

    public List<Pet> getAdoptedPets(int userId) {
        String query = "select pet_id, age, type, cost, breed, name, year_ownership from pet where adopter_id = ?";
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

    

    public List<Report> getPetReports(int petId) {
        String query = "select report_id, type, description from report where pet_id = ?";
        try (PreparedStatement preparedStatement = databaseConnection.prepareStatement(query)) {
            preparedStatement.setInt(1, petId);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                List<Report> reports = new ArrayList<>();
                while (resultSet.next()) {
                    Report report = new Report();
                    report.setId(resultSet.getInt("report_id"));
                    report.setType(resultSet.getString("type"));
                    report.setDescription(resultSet.getString("description"));

                    reports.add(report);
                }
                return reports;
            }

        } catch (Exception e) {
            e.setStackTrace(null);
            return Collections.emptyList();
        }
    }
    public double adopterSalary(int userId) {
        String query = "select salary from adopter where id = ? ";
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
