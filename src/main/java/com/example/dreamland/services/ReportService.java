package com.example.dreamland.services;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

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
    public int getLastId() {
        int nextReportId=-1;
        String query = "SELECT IFNULL(MAX(report_id), 0) + 1 FROM report";
        try (PreparedStatement preparedStatement = databaseConnection.prepareStatement(query);
             ResultSet resultSet = preparedStatement.executeQuery()) {
    
            if (resultSet.next()) {
                nextReportId = resultSet.getInt(1);
            }
            return nextReportId;
        } catch (Exception e) {
            // Handle exceptions appropriately
            e.printStackTrace();
            return -1; // or any other appropriate value
        }
    }
    

}
