package com.energytop.energytop_backend.auth.controller;

import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.energytop.energytop_backend.auth.dto.CreateUserDto;
import com.energytop.energytop_backend.auth.dto.TokenValidationRequestDto;
import com.energytop.energytop_backend.auth.dto.UpdateUserDto;
import com.energytop.energytop_backend.auth.dto.UserDto;
import com.energytop.energytop_backend.auth.services.AuthService;
import com.energytop.energytop_backend.common.dto.SearchDto;

import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

@CrossOrigin(originPatterns = "*")
@RestController
@RequestMapping(path = "/users")
public class AuthController {

  @Autowired
  AuthService authService;

  @GetMapping()
  public ResponseEntity<List<UserDto>> findAll() {
    List<UserDto> users = authService.findAll();
    return ResponseEntity.status(HttpStatus.OK).body(users);
  }

  @GetMapping("/{identifier}")
  public ResponseEntity<Optional<UserDto>> findByIdOrEmail(@PathVariable String identifier) {
    Optional<UserDto> user = authService.findByIdOrEmail(identifier);
    return ResponseEntity.status(HttpStatus.OK).body(user);
  }

  @GetMapping("/search")
  public List<UserDto> searchUsers(@RequestParam String searchTerm, @RequestParam String searchBy) {
    SearchDto searchDto = new SearchDto();
    searchDto.setSearchTerm(searchTerm);
    searchDto.setSearchBy(searchBy);
    return authService.searchUsers(searchDto);
  }

  @PostMapping()
  public ResponseEntity<String> create(@Valid @RequestBody CreateUserDto createUserDto) {
    authService.create(createUserDto);
    return ResponseEntity.status(HttpStatus.CREATED).body("Usuario creado correctamente");
  }

  @PatchMapping("/{id}")
  public ResponseEntity<String> update(@PathVariable Long id, @RequestBody UpdateUserDto updateUserDto) {
    authService.update(id, updateUserDto);
    return ResponseEntity.status(HttpStatus.OK).body("Usuario actualizado correctamente");
  }

  @PatchMapping("/profile/{email}")
  public ResponseEntity<String> updateProfile(@PathVariable String email, @RequestBody UpdateUserDto updateUserDto) {
    System.out.println(email);
    authService.updateProfile(email, updateUserDto);
    return ResponseEntity.status(HttpStatus.OK).body("Usuario actualizado correctamente");
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<?> remove(@PathVariable Long id) {
    authService.remove(id);
    return ResponseEntity.status(HttpStatus.OK).body("Usuario eliminado");
  }

  @PostMapping("/validate-token")
  public ResponseEntity<?> validateToken(@RequestBody TokenValidationRequestDto tokenValidationRequestDto) {
    boolean isValid = authService.isTokenValid(tokenValidationRequestDto);
    if (isValid) {
      return ResponseEntity.ok("Token válido.");
    } else {
      return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("invalid token.");
    }
  }

}
