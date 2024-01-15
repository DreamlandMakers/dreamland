package com.example.dreamland.api.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AuthenticationScreenController {
 
    @GetMapping("/login")
    public String logIn() {
        return "login";
    }
}
