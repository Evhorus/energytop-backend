package com.energytop.energytop_backend.auth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.energytop.energytop_backend.auth.dto.CreateUserDto;
import com.energytop.energytop_backend.auth.dto.LoginDto;

import jakarta.validation.Valid;

@RestController
@RequestMapping(path = "/api/auth")
@Validated
public class AuthController {

  @Autowired
  AuthService authService;

  @PostMapping("/register")
  public ResponseEntity<String> createUser(@Valid @RequestBody CreateUserDto createUserDto) {
    return authService.createUser(createUserDto);
  }

  @PostMapping("/login")
  public ResponseEntity<String> loginUser(@Valid @RequestBody LoginDto loginDto) {
    return authService.login(loginDto);
  }

}
