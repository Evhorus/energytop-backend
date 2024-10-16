package com.energytop.energytop_backend.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.energytop.energytop_backend.repository.UserRepository;

@Service
public class AuthService {
  @Autowired
  UserRepository userRepository;

  public String createUser(){
    return "createUser";
  }

  public String login(){
    return "login";
  }

}
