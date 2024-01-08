package com.example.dreamland.services;

import java.sql.Connection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DataBaseService {
    
    private final Connection databaseConnection;

    @Autowired
    public DataBaseService(Connection databaseConnection) {
        this.databaseConnection = databaseConnection;
    }
}
