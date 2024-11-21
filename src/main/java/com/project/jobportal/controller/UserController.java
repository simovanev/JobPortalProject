package com.project.jobportal.controller;

import com.project.jobportal.entity.Users;
import com.project.jobportal.entity.UsersType;
import com.project.jobportal.services.UsersService;
import com.project.jobportal.services.UsersTypeService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import java.net.Authenticator;
import java.util.List;

@Controller
public class UserController {
    private final UsersTypeService usersTypeService;
    private final UsersService usersService;

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
    public String userRegister(@Valid Users user, Model model){
        if (usersService.ifEmailExist(user.getEmail()).isPresent()){
            model.addAttribute("error", "This email already exists");
            List<UsersType> allTypes=usersTypeService.getAll();
            model.addAttribute("getAllTypes",allTypes);
            model.addAttribute("user", new Users());
            return "register";
        }
        usersService.addNewUser(user);
        return "dashboard";
    }
    @GetMapping("/login")
    public String login(){
        return "login";
    }
@GetMapping("/logout")
    public String logout(HttpServletRequest request, HttpServletResponse response){
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    if (authentication!=null){
        new SecurityContextLogoutHandler().logout(request,response,authentication);
    }
    return "redirect:/";
}
}
