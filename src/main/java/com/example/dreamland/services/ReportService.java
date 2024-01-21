package com.example.dreamland.services;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

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
        String query = "SELECT IFNULL(MAX(report_id), 0) + 1 FROM report GROUP BY pet_id";
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

    public Report getReport(int reportId,int petId)  {
        String query = "SELECT description, type FROM report Where report_id=? and pet_id=?";
        try (PreparedStatement preparedStatement = databaseConnection.prepareStatement(query)) {
            preparedStatement.setInt(1, reportId);
            preparedStatement.setInt(2, petId);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                Report report = new Report();
                if (resultSet.next()) {
                    report.setId(reportId);
                    report.setType(resultSet.getString("type"));
                    report.setDescription(resultSet.getString("description"));
                }
                return report;
            }

        } catch (Exception e) {
            e.setStackTrace(null);
            return null;
        }

    }
public String updateReport(Report report, int petId) {
    String query = "UPDATE report SET type = ?, description = ? WHERE report_id = ? and pet_id = ?";
    
    try (PreparedStatement preparedStatement = databaseConnection.prepareStatement(query)) {
        preparedStatement.setString(1, report.getType());
        preparedStatement.setString(2, report.getDescription());
        preparedStatement.setInt(3, report.getId());
        preparedStatement.setInt(4, petId);

        int rowsAffected = preparedStatement.executeUpdate();

        if (rowsAffected > 0) {
            return "Report updated";
        } else {
            return "Failed to update report";
        }
    } catch (SQLException e) {
        // Log the exception or print a more meaningful message
        e.printStackTrace();
        return "Failed to update report - SQL error";
    } catch (Exception e) {
        // Log the exception or print a more meaningful message
        e.printStackTrace();
        return "Failed to update report - General error";
    }
}
public String deleteReport(int reportId, int petId) {
    String query = "Delete from report WHERE report_id = ? and pet_id = ?";
    
    try (PreparedStatement preparedStatement = databaseConnection.prepareStatement(query)) {
        preparedStatement.setInt(1, reportId);
        preparedStatement.setInt(2, petId);

        int rowsAffected = preparedStatement.executeUpdate();

        if (rowsAffected > 0) {
            return "Report deleted";
        } else {
            return "Failed to delete report";
        }
    } catch (SQLException e) {
        // Log the exception or print a more meaningful message
        e.printStackTrace();
        return "Failed to delete report - SQL error";
    } catch (Exception e) {
        // Log the exception or print a more meaningful message
        e.printStackTrace();
        return "Failed to delete report - General error";
    }
}
    

}
