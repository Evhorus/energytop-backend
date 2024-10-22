package com.energytop.energytop_backend.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.energytop.energytop_backend.entity.User;
import com.energytop.energytop_backend.service.AuthService;


@RestController
@RequestMapping(path = "/api/auth")
public class AuthController {

  private final AuthService authService;

  @Autowired
  public AuthController(AuthService authService) {
    this.authService = authService;
  }

  @PostMapping("/register")
  public String createUser(@RequestBody User user) {
    return authService.createUser(user);
  }

  @GetMapping("/login")
  public String loginUser() {
    return authService.login();
  }


}
