package com.exe01.backend.service.impl;

import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.Message;
import org.springframework.stereotype.Service;

@Service
public class FirebaseService {

    public String sendNotification(String token, String title, String body) {
        Message message = Message.builder()
                .setToken(token)
                .putData("title", title)
                .putData("body", body)
                .build();

        try {
            String response = FirebaseMessaging.getInstance().send(message);
            return response;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
