package com.project.jobportal.controller;

import com.project.jobportal.entity.Users;
import com.project.jobportal.entity.UsersType;
import com.project.jobportal.services.UsersService;
import com.project.jobportal.services.UsersTypeService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

@Controller
public class UserController {
    private UsersTypeService usersTypeService;
    private UsersService usersService;

    public UserController(UsersTypeService usersTypeService, UsersService usersService) {
        this.usersTypeService = usersTypeService;
        this.usersService = usersService;
    }

    @GetMapping("/register")
    public String register(Model model){
        List<UsersType> allTypes=usersTypeService.getAll();
        model.addAttribute("getAllTypes",allTypes);
        model.addAttribute("user", new Users());
        return "register";
    }
    @PostMapping("/register/new")
    public String userRegister(@Valid Users user){
        usersService.addNewUser(user);
        return "dashboard";
    }
}