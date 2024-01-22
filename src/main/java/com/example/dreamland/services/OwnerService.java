package com.example.dreamland.services;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.dreamland.api.model.Pet;
import com.example.dreamland.api.model.PetReport;
import com.example.dreamland.api.model.Report;

@Service
public class OwnerService {

    private final Connection databaseConnection;

    @Autowired
    public OwnerService(Connection databaseConnection) {
        this.databaseConnection = databaseConnection;
    }

    public int newPetRegister(Pet pet) {
        manageOwnerStatus(UserService.currentUserID); // Check if user is the owner
        String query = "INSERT INTO pet(owner_id, age, type, cost, breed, name, year_ownership) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement preparedStatement = databaseConnection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            preparedStatement.setInt(1, UserService.currentUserID);
            preparedStatement.setInt(2, pet.getAge());
            preparedStatement.setString(3, pet.getType());
            preparedStatement.setDouble(4, pet.getAverageExpense());
            preparedStatement.setString(5, pet.getBreed());
            preparedStatement.setString(6, pet.getName());
            preparedStatement.setInt(7, pet.getYearOfOwnership());
    
            int rowsAffected = preparedStatement.executeUpdate();
    
            if (rowsAffected > 0) {
                // Retrieve the generated keys
                try (ResultSet generatedKeys = preparedStatement.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        int generatedId = generatedKeys.getInt(1);
                        return generatedId;
                    } else {
                        // This should not happen, but handle it if it does
                        throw new SQLException("Failed to get the generated pet ID.");
                    }
                }
            } else {
                return -1; // Indicates failure
            }
        } catch (Exception e) {
            // Handle exceptions appropriately
            e.printStackTrace();
            return -1; // Indicates failure
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
    public String updatePet(Pet pet) {
    String query = "UPDATE pet SET name = ?, age = ?, type = ?, cost = ?, breed = ?, year_ownership = ?  WHERE pet_id = ?";
    
    try (PreparedStatement preparedStatement = databaseConnection.prepareStatement(query)) {
        preparedStatement.setString(1, pet.getName());
        preparedStatement.setInt(2, pet.getAge());
        preparedStatement.setString(3, pet.getType());
        preparedStatement.setDouble(4, pet.getAverageExpense());
        preparedStatement.setString(5, pet.getBreed());
        preparedStatement.setInt(6, pet.getYearOfOwnership());
        preparedStatement.setInt(7, pet.getId()); 

        int rowsAffected = preparedStatement.executeUpdate();

        if (rowsAffected > 0) {
            return "Pet updated";
        } else {
            return "Failed to update pet";
        }
    } catch (SQLException e) {
        // Log the exception or print a more meaningful message
        e.printStackTrace();
        return "Failed to update pet - SQL error";
    } catch (Exception e) {
        // Log the exception or print a more meaningful message
        e.printStackTrace();
        return "Failed to update pet - General error";
    }
}
public String existingPetAbondon(int petId) {
    String query = "UPDATE pet SET owner_id = adopter_id, adopter_id = NULL, adoption_date=NULL WHERE pet_id = ?";
    
    try (PreparedStatement preparedStatement = databaseConnection.prepareStatement(query)) {
        preparedStatement.setInt(1, petId);

        int rowsAffected = preparedStatement.executeUpdate();

        if (rowsAffected > 0) {
            return "Pet updated";
        } else {
            return "Failed to update pet";
        }
    } catch (SQLException e) {
        // Log the exception or print a more meaningful message
        e.printStackTrace();
        return "Failed to update pet - SQL error";
    } catch (Exception e) {
        // Log the exception or print a more meaningful message
        e.printStackTrace();
        return "Failed to update pet - General error";
    }
}
public String fosteredPetAbondon(int petId) {
    String query = "UPDATE pet SET owner_id = foster_id, foster_id = NULL, foster_from_date=NULL WHERE pet_id = ?";
    
    try (PreparedStatement preparedStatement = databaseConnection.prepareStatement(query)) {
        preparedStatement.setInt(1, petId);

        int rowsAffected = preparedStatement.executeUpdate();

        if (rowsAffected > 0) {
            return "Pet updated";
        } else {
            return "Failed to update pet";
        }
    } catch (SQLException e) {
        // Log the exception or print a more meaningful message
        e.printStackTrace();
        return "Failed to update pet - SQL error";
    } catch (Exception e) {
        // Log the exception or print a more meaningful message
        e.printStackTrace();
        return "Failed to update pet - General error";
    }
}
public List<PetReport> getGivenPetsWithReports(int userId) {
    List<PetReport> petsWithReports = new ArrayList<>();

    String query = "SELECT p.pet_id, p.age, p.type, p.cost, p.breed, p.name, p.year_ownership, r.report_id, r.type, r.description " +
                   "FROM pet AS p " +
                   "LEFT JOIN report AS r ON p.pet_id = r.pet_id " +
                   "where adopter_id is null and foster_id is null and owner_id is not null and owner_id=?";

    try (PreparedStatement preparedStatement = databaseConnection.prepareStatement(query)) {
        preparedStatement.setInt(1, userId);

        try (ResultSet resultSet = preparedStatement.executeQuery()) {
            while (resultSet.next()) {
                int petId = resultSet.getInt("p.pet_id");

                // Check if there is already a PetReport with the same petId
                PetReport existingPetReport = findPetReportById(petsWithReports, petId);

                if (existingPetReport == null) {
                    PetReport petReport = new PetReport();
                    Pet pet = new Pet();
                    Report report = new Report();
                    List<Report> reports = new ArrayList<>();

                    pet.setId(petId);
                    pet.setAge(resultSet.getInt("p.age"));
                    pet.setType(resultSet.getString("p.type"));
                    pet.setAverageExpense(resultSet.getDouble("p.cost"));
                    pet.setBreed(resultSet.getString("p.breed"));
                    pet.setName(resultSet.getString("p.name"));
                    pet.setYearOfOwnership(resultSet.getInt("p.year_ownership"));

                    report.setId(resultSet.getInt("r.report_id"));
                    report.setDescription(resultSet.getString("r.description"));
                    report.setType(resultSet.getString("r.type"));

                    reports.add(report);
                    petReport.setPet(pet);
                    petReport.setReports(reports);

                    petsWithReports.add(petReport);
                } else {
                    Report report = new Report();
                    report.setId(resultSet.getInt("r.report_id"));
                    report.setDescription(resultSet.getString("r.description"));
                    report.setType(resultSet.getString("r.type"));

                    existingPetReport.getReports().add(report);
                }
            }
            return petsWithReports;
        }
    } catch (Exception e) {
        e.printStackTrace();
        return null;
    }
}

private PetReport findPetReportById(List<PetReport> petReports, int petId) {
    for (PetReport petReport : petReports) {
        if (petReport.getPet().getId() == petId) {
            return petReport;
        }
    }
    return null;
}

}
