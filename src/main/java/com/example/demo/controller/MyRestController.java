package com.example.demo.controller;

import com.example.demo.model.UserEntity;
import com.example.demo.service.UserService;

import freemarker.template.TemplateException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import java.io.IOException;
import java.util.List;

@RestController
@ComponentScan(basePackages = "com.example.demo")
public class MyRestController {

    @Autowired
    private UserService userService;


    @RequestMapping(path = "/users", method = RequestMethod.POST)
    public ResponseEntity addUsers(@Valid @RequestBody UserEntity user) {
        userService.addUsers(user);
        return ResponseEntity.ok(user);
    }

    @RequestMapping(path = "/users", method = RequestMethod.PUT)
    public ResponseEntity updateUser(@Valid @RequestBody UserEntity user) {

        userService.updateUser(user);
        return ResponseEntity.ok(user);
    }


    @RequestMapping(path = "/users", method = RequestMethod.GET)
    public ResponseEntity listUsers() {
        List<UserEntity> voList = userService.listUsers();
        return ResponseEntity.ok(voList);

    }

    @RequestMapping(path = "/sendEmail", method = RequestMethod.GET)
    public ResponseEntity sendEmail(@RequestHeader("emailTo") String emailTo, @RequestHeader("userName") String userName) throws IOException, TemplateException {
        userService.sendSimpleMessage(emailTo, userName);
        return ResponseEntity.ok().build();
    }

}
