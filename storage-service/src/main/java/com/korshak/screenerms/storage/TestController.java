package com.korshak.screenerms.storage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @GetMapping("/db-test")
    public String testDb() {
        try {
            String result = jdbcTemplate.queryForObject("SELECT 1", String.class);
            return "Database connection successful. Result: " + result;
        } catch (Exception e) {
            return "Database connection failed: " + e.getMessage();
        }
    }
}
