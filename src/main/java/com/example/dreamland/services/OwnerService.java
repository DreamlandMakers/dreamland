package com.example.dreamland.services;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class OwnerService {

    private final Connection databaseConnection;

    @Autowired
    public OwnerService(Connection databaseConnection) {
        this.databaseConnection = databaseConnection;
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

    private boolean isOwner(int userId) {
        String query = "select * from owner where id = ?";
        try (PreparedStatement preparedStatement = databaseConnection.prepareStatement(query)) {
            preparedStatement.setInt(1, userId);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    return true;
                } else {
                    System.out.println("user name available");
                    return false;
                }
            }
        } catch (Exception e) {
            e.setStackTrace(null);
            return false;
        }

    }
}
