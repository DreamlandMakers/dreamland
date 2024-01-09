package com.example.dreamland.services;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.dreamland.api.model.User;

@Service
public class UserService {

    private final Connection databaseConnection;

    @Autowired
    public UserService(Connection databaseConnection, DataBaseService dataBaseService) {
        this.databaseConnection = databaseConnection;
    }

    public String newUserSignUp(User user) {
        // This will be filled with the neccesseray functions
        String checkUserNameAv = checkUserNameAvailability(user.getUserName());
        if (checkUserNameAv == "") {
            String query = "INSERT INTO user(username, password, name, surname, birth_date, sex, number_of_pets, email) "
                    + "values (?, ?, ?, ?, ?, ?, ?, ?)";
            try (PreparedStatement preparedStatement = databaseConnection.prepareStatement(query)) {
                preparedStatement.setString(1, user.getUserName());
                preparedStatement.setString(2, user.getPassword());
                preparedStatement.setString(3, user.getName());
                preparedStatement.setString(4, user.getSurName());
                preparedStatement.setString(5, user.getBirthDate());
                preparedStatement.setString(6, user.getSex());
                preparedStatement.setInt(7, user.getNumberOfPets());
                preparedStatement.setString(8, user.getEmail());

                int rowsAffected = preparedStatement.executeUpdate();

                if (rowsAffected > 0) {
                    return "Sign Up Successfull";
                } else {
                    return "Failed to Sign Up";
                }
            } catch (Exception e) {
                return e.toString();
            }
        } else {
            return checkUserNameAv;
        }
    }

    public String checkUserNameAvailability(String userName) {
        // Check if username is suitable and if it is not existing in the database
        if (userName.length() < 15) {
            if (userName.length() > 5) {
                if (userName.contains(" ")) {
                    return "Username cannot contain space";
                } else {
                    String query = "select * from user where username = ?";
                    try (PreparedStatement preparedStatement = databaseConnection.prepareStatement(query)) {
                        preparedStatement.setString(1, userName);
                        try (ResultSet resultSet = preparedStatement.executeQuery()) {
                            if (resultSet.next()) {
                                return "User name existing";
                            } else {
                                System.out.println("user name available");
                                return "";
                            }
                        }
                    } catch (Exception e) {
                        return e.toString();
                    }
                }
            } else {
                return "Username too short";
            }
        } else {
            return "Username too long";
        }
    }

    public boolean passwordRestrictionCheck() {
        // Check if password is suitable to be password
        return false;
    }

    public boolean checkCrediantials(String userName, String password) {
        // Fill the function
        return false;
    }
}
