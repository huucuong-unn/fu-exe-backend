package com.exe01.backend.service.impl;

import com.exe01.backend.entity.Account;
import com.exe01.backend.entity.EmailDetailsEntity;
import com.exe01.backend.repository.AccountRepository;
import com.exe01.backend.service.IEmailService;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;
import java.io.File;


@Service
public class EmailService implements IEmailService {

    @Autowired
    JavaMailSender javaMailSender;

    @Autowired
    private SpringTemplateEngine templateEngine;



    @Value("${spring.mail.username}")
    private String sender;
    @Autowired
    private AccountRepository accountRepository;


    private String processThymeleafTemplate(String email, String message, String type) {

        Context context = new Context();
        Account account  = accountRepository.findByEmail(email).get();
        context.setVariable("customerName", account.getUsername());
        context.setVariable("message", message);

        if(type.equals("ACCOUNT"))
        {
            return templateEngine.process("email-template", context);

        } else if(type.equals("PASSWORD"))
        {
            return templateEngine.process("email-template", context);
        } else if(type.equals("APPLICATION"))
        {
            return templateEngine.process("application-template", context);
        }
        return null;
    }



    //Method 1
    //To send a simple mail
    @Override
    public String sendSimpleMail(EmailDetailsEntity details) {
        try {
            //Creating a simple mail message
            MimeMessage mimeMessage = javaMailSender.createMimeMessage();
            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, true, "UTF-8");
            mimeMessageHelper.setFrom(sender);
            mimeMessageHelper.setTo(details.getRecipient());
            String emailContent=null;

            emailContent = processThymeleafTemplate(details.getRecipient(), details.getMsgBody(), details.getType());
            mimeMessageHelper.setSubject(details.getSubject());
            mimeMessageHelper.setText(emailContent, true); // Set the content type to HTML

            // Sending the mail
            javaMailSender.send(mimeMessage);
            return "Mail Sent Successfully...";
        } catch (Exception e) {
            e.printStackTrace();
            return "Error while Sending Mail";
        }

    }

    // Method 2
    // To send an email with attachment
    @Override
    public String sendMailWithAttachment(EmailDetailsEntity details) {
        // Creating a mime message
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper mimeMessageHelper;

        try {

            mimeMessageHelper = new MimeMessageHelper(mimeMessage, true);
            mimeMessageHelper.setFrom(sender);
            mimeMessageHelper.setTo(details.getRecipient());

            mimeMessageHelper.setSubject(details.getSubject());
//
//            String htmlContent = processThymeleafTemplate(details.getOrderId());
//            mimeMessageHelper.setText(htmlContent, true);


            // Adding the attachment
            FileSystemResource file = new FileSystemResource(new File(details.getAttachment()));

            mimeMessageHelper.addAttachment(file.getFilename(), file);

            // Sending the mail
            javaMailSender.send(mimeMessage);
            return "Mail sent Successfully";
        }

        // Catch block to handle MessagingException
        catch (MessagingException e) {

            // Display message when exception occurred
            return "Error while sending mail!!!";
        }
    }



    public void createEmailDetailForRestPassword(String email){

        EmailDetailsEntity emailDetailsEntity = new EmailDetailsEntity();
        emailDetailsEntity.setRecipient(email);
        emailDetailsEntity.setSubject("[11-Twell Parrot Shop] - Reset Password");
        emailDetailsEntity.setCheck("password");
        emailDetailsEntity.setMsgBody("Test send mail Parrot Farm Shop project \n\nThis is a Simple Email \n\nThanks");
        sendSimpleMail(emailDetailsEntity);

    }
}
