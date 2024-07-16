package com.exe01.backend.controller;


import com.exe01.backend.service.impl.FirebaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/notifications")
public class NotificationController {

    @Autowired
    private FirebaseService firebaseService;

    @PostMapping("/send")
    public String sendNotification(@RequestBody Map<String, String> request) {
        String token = request.get("token");
        String title = request.get("title");
        String body = request.get("body");

        return firebaseService.sendNotification(token, title, body);
    }
}

