package com.energytop.energytop_backend.service;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.energytop.energytop_backend.persistence.entity.user.UserEntity;
import com.energytop.energytop_backend.persistence.repository.user.UserRepository;

@Service
public class AuthService {

  private List<UserEntity> users;

  @Autowired
  UserRepository userRepository;
  private PasswordEncoder passwordEncoder;

  public String createUser(UserEntity user) {
    loadUsers();
    // Verificar si el email ya está registrado
    for (UserEntity userr : users) {
      if (userr.getEmail().equals(user.getEmail())) {
        return "El email ya está en uso.";
      }
    }

    // Hashing de la contraseña

    // Hashing de la contraseña
    // String encodedPassword = passwordEncoder.encode(user.getPassword());
    // user.setPassword(encodedPassword);

    // Guardar el usuario
    userRepository.save(user);
    return "Usuario Creado Correctamente";
  }

  private void loadUsers() {
    this.users = this.userRepository.findAll();
  }

  public String login() {
    return "login";
  }

}
