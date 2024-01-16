package com.example.dreamland.api.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;


import com.example.dreamland.api.model.User;
import com.example.dreamland.api.model.jsonconverters.CredentialConverter;
import com.example.dreamland.services.UserService;

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
}
