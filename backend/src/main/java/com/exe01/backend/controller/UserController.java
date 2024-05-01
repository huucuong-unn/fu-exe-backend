package com.exe01.backend.controller;

import com.exe01.backend.constant.ConstAPI;
import com.exe01.backend.dto.UserDTO;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin
@RestController
public class UserController {

    @PostMapping(value = ConstAPI.AuthenticationAPI.LOGIN_WITH_PASSWORD_USERNAME)
    public String login(){
        return  "hello";
    }

    @GetMapping(value = ConstAPI.AuthenticationAPI.TEST_AWS_DEPLOY)
    public String testAwsDeploy(){
        return  "hello test aws deploy";
    }    @GetMapping(value = ConstAPI.AuthenticationAPI.TEST_AWS_DEPLOY2)
    public String testAwsDeploy2(){
        return  " /*_/*  \n" +
                "( o.o ) \n" +
                " > ^ <";
    }
}
