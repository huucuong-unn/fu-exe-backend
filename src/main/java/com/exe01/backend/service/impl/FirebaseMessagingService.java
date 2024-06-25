//package com.exe01.backend.service.impl;
//
//import com.exe01.backend.dto.response.NotificationMessage;
//import com.google.firebase.messaging.FirebaseMessaging;
//import com.google.firebase.messaging.Message;
//import com.google.firebase.messaging.Notification;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//
//@Service
//public class FirebaseMessagingService {
//
//    @Autowired
//    private FirebaseMessaging firebaseMessaging;
//
//    public String sendNotificationByToken(NotificationMessage notificationMessage) {
//        Notification notification = Notification.builder()
//                .setTitle(notificationMessage.getTitle())
//                .setBody(notificationMessage.getBody())
//                .setImage(notificationMessage.getImage())
//                .build();
//        Message message = Message.builder()
//                .setToken(notificationMessage.getRecipientToken())
//                .setNotification(notification)
//                .putAllData(notificationMessage.getData())
//                .build();
//
//        try{
//            String response = firebaseMessaging.send(message);
//            return response;
//        } catch (Exception e) {
//            e.printStackTrace();
//return "Error sending notification";
//        }
//    }
//
//}
