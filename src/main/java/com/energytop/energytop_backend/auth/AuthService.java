package com.energytop.energytop_backend.auth;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.energytop.energytop_backend.auth.dto.CreateUserDto;
import com.energytop.energytop_backend.auth.dto.LoginDto;
import com.energytop.energytop_backend.auth.entities.UserEntity;
import com.energytop.energytop_backend.auth.repository.UserRepository;

@Service
public class AuthService {

  private final UserRepository userRepository;
  private final PasswordEncoder passwordEncoder;

  public AuthService(UserRepository userRepository, PasswordEncoder passwordEncoder,
      AuthenticationManager authenticationManager) {
    this.userRepository = userRepository;
    this.passwordEncoder = passwordEncoder;
  }

  public ResponseEntity<String> createUser(CreateUserDto createUserDto) {

    // Validar si el email existe
    UserEntity user = this.userRepository.findByEmail(createUserDto.getEmail());
    if (user != null)
      return ResponseEntity.status(HttpStatus.CONFLICT).body("El email ya existe");

    UserEntity userEntity = new UserEntity();
    userEntity.setFirstName(createUserDto.getFirstName());
    userEntity.setLastName(createUserDto.getLastName());
    userEntity.setEmail(createUserDto.getEmail());

    // Encriptar password
    userEntity.setPassword(this.passwordEncoder.encode(createUserDto.getPassword()));

    // Guardar el usuario
    userRepository.save(userEntity);
    return ResponseEntity.status(HttpStatus.CREATED).body("Usuario Creado Correctamente");
  }

  public ResponseEntity<String> login(LoginDto loginDto) {

     // Validar si el email existe
    UserEntity user = this.userRepository.findByEmail(loginDto.getEmail());
    if (user == null)
      return ResponseEntity.status(HttpStatus.CONFLICT).body("El email no existe");

    if (!this.passwordEncoder.matches(loginDto.getPassword(), user.getPassword()))
      return ResponseEntity.status(HttpStatus.CONFLICT).body("Contraseña incorrecta");

    return ResponseEntity.ok("Bienvenido");
  }

}
