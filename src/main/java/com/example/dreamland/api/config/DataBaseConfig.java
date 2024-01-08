package com.example.dreamland.api.config;

import java.sql.Connection;
import java.sql.DriverManager;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DataBaseConfig {
    
    @Bean
    public Connection dataBaseConnection() {
        try {
            Class.forName("com.mysql.jdbc.Driver");
            String url = "jdbc:mysql://localhost:3306/dreamland";
            String username = "root";
            String password = "erzer641";

            return DriverManager.getConnection(url, username, password);

        } catch (Exception e) {
            
            System.out.println(e);
            return null;
        }
    }
}
