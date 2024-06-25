//package com.exe01.backend.controller;
//
//import com.exe01.backend.constant.ConstAPI;
//import com.exe01.backend.dto.response.NotificationMessage;
//import com.exe01.backend.service.impl.FirebaseMessagingService;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestBody;
//import org.springframework.web.bind.annotation.RestController;
//
//@RestController
//public class NotificationController {
//
//    @Autowired
//    FirebaseMessagingService firebaseMessagingService;
//
//    @PostMapping(value = ConstAPI.NotificationAPI.SEND_NOTIFITION)
//    public String sendNotificationByToken(@RequestBody NotificationMessage notificationMessage)
//    {
//        return firebaseMessagingService.sendNotificationByToken(notificationMessage);
//    }
//
//}
