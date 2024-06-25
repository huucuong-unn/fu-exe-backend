package com.exe01.backend.controller;
import com.exe01.backend.constant.ConstAPI;
import com.exe01.backend.dto.request.PushNotificationRequest;
import com.exe01.backend.dto.response.PushNotificationResponse;
import com.exe01.backend.service.impl.PushNotificationService;
import org.springframework.http.HttpStatus;


import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class PushNotificationController {


    private PushNotificationService pushNotificationService;

    public PushNotificationController(PushNotificationService pushNotificationService) {
        this.pushNotificationService = pushNotificationService;
    }

    @PostMapping(value = ConstAPI.NotificationAPI.NOTIFITION_TOKEN)
    public ResponseEntity sendTokenNotification(@RequestBody PushNotificationRequest request) {
        pushNotificationService.sendPushNotificationToToken(request);
        System.out.println("princr");
        return new ResponseEntity<>(new PushNotificationResponse(HttpStatus.OK.value(), "Notification has been sent."), HttpStatus.OK);
    }

}