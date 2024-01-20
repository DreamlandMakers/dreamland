package com.example.dreamland.api.config;

import java.sql.Connection;
import java.sql.DriverManager;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

@Configuration
public class DataBaseConfig {
    
    @Bean
    @Scope("singleton")
    public Connection dataBaseConnection() {
        try {
            Class.forName("com.mysql.jdbc.Driver");
            String url = "jdbc:mysql://localhost:3306/dreamland";
            String username = "root";
            String password = "1234";

            return DriverManager.getConnection(url, username, password);

        } catch (Exception e) {

            System.out.println(e);
            return null;
        }
    }
}
