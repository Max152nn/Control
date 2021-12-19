package com.example.demo.controller;

import com.example.demo.UserRepo;
import com.example.demo.domain.Role;
import com.example.demo.domain.UserLog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.Collections;
import java.util.Map;

@Controller
@PreAuthorize(value = "hasAuthority('ADMIN')")
public class RegistrationController {
    @Autowired
    private UserRepo userRepo;

    @GetMapping("/registration")
    public String registration(){
        return "registration";
    }

    @PostMapping("/registration")
    public String addUser(UserLog userLog, Model model){
       UserLog userFromDb = userRepo.findByUsername(userLog.getUsername());

       if(userFromDb != null){
           model.addAttribute("message","User exists");
           return "registration";
       }

       userLog.setActive(true);
       userLog.setRoles(Collections.singleton(Role.USER));
       userRepo.save(userLog);
        return "redirect:/login";
    }
}
