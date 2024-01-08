package com.example.dreamland.services;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DataBaseService {
    
    private final Connection databaseConnection;

    @Autowired
    public DataBaseService(Connection databaseConnection) {
        this.databaseConnection = databaseConnection;
    }

    public void insertQuery(String query) {
        try (PreparedStatement preparedStatement = databaseConnection.prepareStatement(query)) {
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    System.out.println(resultSet.getString("username"));
                }
            }
        } catch (Exception e) {
            System.out.println(e);
            return;
        }
    }
}
