package com.example.demo;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.example.demo.model.UserEntity;
import com.example.demo.service.UserService;

@SpringBootApplication
public class DemoApplication {

    @Autowired
    private UserService emailService;
    
	public static void main(String[] args) {
		SpringApplication.run(DemoApplication.class, args);
	}

}

