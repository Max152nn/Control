package com.example.demo.controller;

import com.example.demo.UserRepo;
import com.example.demo.domain.Role;
import com.example.demo.domain.UserLog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.annotation.security.RolesAllowed;
import java.util.Arrays;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/user")
@PreAuthorize("hasAuthority('ADMIN')")
public class UserController {
    @Autowired
    private UserRepo userRepo;

    @GetMapping
    public String userList(Model model){
        model.addAttribute("users",userRepo.findAll());
        return "userList";
    }

    @GetMapping("{userLog}")
    public String userEditForm(@PathVariable UserLog userLog, Model model){
        model.addAttribute("user",userLog);
        model.addAttribute("roles", Role.values());
        return "userEdit";
    }

    @PostMapping
    public String usrSave(
            @RequestParam String username,
            @RequestParam Map<String,String> form,
            @RequestParam("userId") UserLog userLog){
        userLog.setUsername(username);
        Set<String> roles = Arrays.stream(Role.values())
                .map(Role::name)
                .collect(Collectors.toSet());
        userLog.getRoles().clear();
        for(String key:form.keySet()){
            if(roles.contains(key)){
                userLog.getRoles().add(Role.valueOf(key));
            }
        }
        userRepo.save(userLog);

        return "redirect:/user";
    }
}
