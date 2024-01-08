package com.example.dreamland.api.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.dreamland.services.UserService;

@RestController
public class UserController {
    
    private UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }
    
    @PostMapping("/signup")
    public void signUp(@RequestParam String userName, String password) {
        String responseMessage = userService.newUserSignUp(userName, password);
        System.out.println(responseMessage);
    }

    @GetMapping("/login")
    public void logIn(@RequestParam String userName, String password) {
        userService.checkCrediantials(userName, password);
    }
}
