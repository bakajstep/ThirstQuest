package edu.vutfit.ThirstQuest.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/status")
public class StatusController {

    @Value("${spring.application.name}")
    private String appName;

    @GetMapping
    public Map<String, String> getStatus() {
        Map<String, String> status = new HashMap<>();
        status.put("message", "The server is up and running!");
        status.put("name", appName);
        status.put("timestamp", String.valueOf(System.currentTimeMillis()));
        return status;
    }
}

