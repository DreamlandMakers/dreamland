package com.example.dreamland.services;

import org.springframework.stereotype.Service;

@Service
public class UserService {
    
    public String newUserSignUp(String userName, String password) {
        //This will be filled with the neccesseray functions
        saveNewUserToDataBase(userName, password);
        return "User Created";
    }

    public String saveNewUserToDataBase(String userName, String password) {
        //Add the user to data base
        return "";
    }

    public boolean checkUserNameAvailability(String userName) {
        //Check if username is suitable and if it is not existing in the database
        return false;
    }

    public boolean passwordRestrictionCheck() {
        //Check if password is suitable to be password
        return false;
    }

    public boolean checkCrediantials(String userName, String password) {
        //Fill the function
        return false;
    }
}
