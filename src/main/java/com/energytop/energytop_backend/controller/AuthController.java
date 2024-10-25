package com.energytop.energytop_backend.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.energytop.energytop_backend.persistence.entity.user.UserEntity;
import com.energytop.energytop_backend.service.AuthService;

@RestController
@RequestMapping(path = "/api/auth")
public class AuthController {

  @Autowired
  AuthService authService;


  @PostMapping("/register")
  public String createUser(@RequestBody UserEntity user) {
    return authService.createUser(user);
  }

  @GetMapping("/login")

  public String loginUser() {
    return authService.login();
  }

}
