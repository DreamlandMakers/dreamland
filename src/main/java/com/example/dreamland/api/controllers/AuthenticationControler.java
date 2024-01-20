package com.example.dreamland.api.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.dreamland.api.model.CurrentUser;
import com.example.dreamland.api.model.User;
import com.example.dreamland.services.UserService;

import jakarta.servlet.http.HttpSession;

@Controller
public class AuthenticationControler {
    
    private UserService userService;

    @Autowired
    public AuthenticationControler(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/signup")
    public String signUp() {
        return "signup";
    }

    @GetMapping("/login")
    public String logIn() {
        return "login";
    }


    @PostMapping("/signup")
    public String signUp(User user, 
                     @RequestParam("day") String day,
                     @RequestParam("month") String month,
                     @RequestParam("year") String year
                     ) {
    // Combine day, month, year into birthDate
    user.setBirthDate(year + "-" + month + "-" + day);
    userService.newUserSignUp(user);
    return "result";
    }


    @PostMapping("/login")
    public String logIn(@RequestParam("userName") String userName, @RequestParam("password") String password, HttpSession session) {


        userService.logIn(userName, password);

        User currentUser = CurrentUser.getCurrentUser();

            // Add the current user to the model
        session.setAttribute("currentUser", currentUser);
    
        return "profile"; // This is the name of your HTML file (without the .html extension) 
    }
}

