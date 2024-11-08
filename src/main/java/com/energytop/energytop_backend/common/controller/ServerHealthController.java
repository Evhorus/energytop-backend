package com.energytop.energytop_backend.common.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/health")
public class ServerHealthController {

  @GetMapping
  public String healthCheck() {
    return "Server is running!";
} 

}
