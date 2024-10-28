package com.energytop.energytop_backend.auth.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.energytop.energytop_backend.auth.dto.UserDto;
import com.energytop.energytop_backend.auth.entities.User;
import com.energytop.energytop_backend.auth.service.AuthService;

import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;

@CrossOrigin(originPatterns = "*")
@RestController
@RequestMapping(path = "/users")
@Validated
public class AuthController {

  @Autowired
  AuthService authService;

  @GetMapping()
  public List<UserDto> findAll() {
    return authService.findAll();
  }

  @GetMapping("/{id}")
  public ResponseEntity<?> findById(@PathVariable() Long id) {
    Optional<UserDto> userOptional = authService.findById(id);

    if (!userOptional.isPresent())
      return ResponseEntity.notFound().build();

    return ResponseEntity.ok(userOptional.orElseThrow());
  }

  @PostMapping()
  public ResponseEntity<?> create(@Valid @RequestBody User user) {
    return ResponseEntity.status(HttpStatus.CREATED).body(authService.save(user));
  }

  @PutMapping("/{id}")
  public ResponseEntity<?> update(@PathVariable Long id, @RequestBody User user) {
    Optional<UserDto> userDb = authService.update(user, id);
    if (!userDb.isPresent())
      return ResponseEntity.notFound().build();
    return ResponseEntity.status(HttpStatus.CREATED).body(userDb.orElseThrow());
  }


  @DeleteMapping("/{id}")
  public ResponseEntity<?> remove(@PathVariable Long id) {
    Optional<UserDto> userDb = authService.findById(id);
    if (!userDb.isPresent())
      return ResponseEntity.notFound().build();

    authService.remove(id);
    return ResponseEntity.status(HttpStatus.OK).body("Usuario eliminado");
  }

  // @PostMapping("/login")
  // @ResponseStatus(HttpStatus.CREATED)
  // public ResponseEntity<String> loginUser(@Valid @RequestBody LoginDto
  // loginDto) {
  // return authService.login(loginDto);
  // }

}
