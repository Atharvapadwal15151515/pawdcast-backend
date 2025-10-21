package com.pawdcast.pawdcast.application;

import com.pawdcast.pawdcast.application.service.PasswordMigrationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class PawdcastApplication implements CommandLineRunner {

    @Autowired
    private PasswordMigrationService passwordMigrationService;

    public static void main(String[] args) {
        SpringApplication.run(PawdcastApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        // Run this to migrate the new plain text passwords
        passwordMigrationService.migrateExistingPasswords();
    }
}