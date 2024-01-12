package com.example.dreamland.api.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.dreamland.api.model.User;
import com.example.dreamland.api.model.jsonconverters.CredentialConverter;
import com.example.dreamland.services.UserService;

@RestController
public class AuthenticationControler {
    
    private UserService userService;

    @Autowired
    public AuthenticationControler(UserService userService) {
        this.userService = userService;
    }
    
    @PostMapping("/signup")
    public String signUp(@RequestBody User newUser) {
        String responseMessage = userService.newUserSignUp(newUser);
        return responseMessage;
    }

    @PostMapping("/login")
    public String logIn(@RequestBody CredentialConverter credentials) {
        String responseMessage = userService.logIn(credentials.getUserName(), credentials.getPassword());
        return responseMessage;
    }

    @PostMapping("/test")
    public String test(@RequestParam String userName) {
        return "";
    }
}
