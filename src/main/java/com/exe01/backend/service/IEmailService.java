package com.exe01.backend.service;


import com.exe01.backend.entity.EmailDetailsEntity;

public interface IEmailService {


    // Method
    // To send a simple email
    String sendSimpleMail(EmailDetailsEntity details);

    // Method
    // To send an email with attachment
    String sendMailWithAttachment(EmailDetailsEntity details);
}
