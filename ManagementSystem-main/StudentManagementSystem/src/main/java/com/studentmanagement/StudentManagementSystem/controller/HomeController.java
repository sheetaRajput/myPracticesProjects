package com.studentmanagement.StudentManagementSystem.controller;

import com.studentmanagement.StudentManagementSystem.entity.User;
import com.studentmanagement.StudentManagementSystem.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import javax.servlet.http.HttpSession;

@Controller
public class HomeController {
    @Autowired
    private UserService userService;

    @GetMapping("/")
    public String index() {
        return "index";
    }

    @GetMapping("/signin")
    public String login() {
        return "login";
    }

    @GetMapping("/register")
    public String register() {
        return "register";
    }

    @PostMapping("/createUser")
    public String saveUser(@ModelAttribute User user, HttpSession session) {
        // System.out.println(user);
        boolean us = userService.checkEmail(user.getUsername());
        if (us) {
            session.setAttribute("msg", "Email Id alreday exists");
        } else {
            User userDtls = userService.saveUser(user);
            if (userDtls != null) {
                session.setAttribute("msg", "Register Sucessfully");
            } else {
                session.setAttribute("msg", "Something wrong on server");
            }
        }
        return "redirect:/register";
    }
}
