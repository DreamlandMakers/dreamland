package com.example.dreamland.services;

import java.sql.Connection;
import java.sql.PreparedStatement;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.dreamland.api.model.Report;

@Service
public class ReportService {
    

    private final Connection databaseConnection;

    @Autowired
    public ReportService(Connection databaseConnection) {
        this.databaseConnection = databaseConnection;
    }

    public String addReport(int petId, Report report) {

        String query = "INSERT INTO report(pet_id, report_id, description, type) VALUES (?, ?, ?, ?)";
        try (PreparedStatement preparedStatement = databaseConnection.prepareStatement(query)) {
            preparedStatement.setInt(1, petId);
            preparedStatement.setInt(2, report.getId());
            preparedStatement.setString(3, report.getDescription());
            preparedStatement.setString(4, report.getType());

            int rowsAffected = preparedStatement.executeUpdate();

            if (rowsAffected > 0) {
                return "Report Added";
            } else {
                return "Failed to add report";
            }
        } catch (Exception e) {
            return e.toString();
        }
    }

}
