package com.exe01.backend.controller;

import com.exe01.backend.entity.EmailDetailsEntity;
import com.exe01.backend.service.IEmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin
public class EmailController {
    @Autowired
    private IEmailService emailService;


    // Sending a simple Email
    @PostMapping("/api/send-mail")
    public String sendMail(@RequestBody EmailDetailsEntity details)
    {
        String status = emailService.sendSimpleMail(details);
        return status;
    }

    @PostMapping("/api/send-mail/forgot-password")
    public String sendMailForgotPassword(@RequestBody EmailDetailsEntity details)
    {
        String status = emailService.sendSimpleMail(details);
        return status;
    }
    //ko xai cai nay



    //======================================
    // Sending email with attachment
    @PostMapping("/api/send-mail-with-attachment")
    public String sendMailWithAttachment(@RequestBody EmailDetailsEntity details)
    {
        String status = emailService.sendMailWithAttachment(details);
        return status;
    }
}
