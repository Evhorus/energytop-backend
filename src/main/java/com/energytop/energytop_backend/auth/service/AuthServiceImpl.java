package com.energytop.energytop_backend.auth.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.energytop.energytop_backend.auth.dto.TokenValidationRequestDto;
import com.energytop.energytop_backend.auth.dto.UserDto;
import com.energytop.energytop_backend.auth.dto.mapper.UserDtoMapper;
import com.energytop.energytop_backend.auth.entities.Role;
import com.energytop.energytop_backend.auth.entities.User;
import com.energytop.energytop_backend.auth.repository.RoleRepository;
import com.energytop.energytop_backend.auth.repository.UserRepository;
import com.energytop.energytop_backend.common.config.auth.TokenJwtConfig;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;

@Service
public class AuthServiceImpl implements AuthService {

  @Autowired
  private UserRepository userRepository;
  @Autowired
  private RoleRepository roleRepository;

  @Autowired
  private PasswordEncoder passwordEncoder;

  @Override
  @Transactional(readOnly = true)
  public List<UserDto> findAll() {
    List<User> users = (List<User>) userRepository.findAll();
    return users
        .stream().map(user -> UserDtoMapper.builder().setUser(user).build())
        .collect(Collectors.toList());
  }

  @Override
  @Transactional(readOnly = true)
  public Optional<UserDto> findById(Long id) {
    return userRepository.findById(id).map(user -> UserDtoMapper.builder().setUser(user).build());
  }

  @Override
  @Transactional()
  public UserDto save(User user) {
    user.setPassword(passwordEncoder.encode(user.getPassword()));
    Optional<Role> roleOptional = roleRepository.findByName("ROLE_USER");
    List<Role> roles = new ArrayList<>();

    if (roleOptional.isPresent()) {
      roles.add(roleOptional.orElseThrow());
    }
    user.setRoles(roles);
    return UserDtoMapper.builder().setUser(userRepository.save(user)).build();
  }

  @Override
  @Transactional
  public Optional<UserDto> update(User user, Long id) {
    Optional<User> userDb = userRepository.findById(id);

    if (userDb.isPresent()) {
      User userDbToEdit = userDb.orElseThrow();
      if (user.getFirstName() != null) {
        userDbToEdit.setFirstName(user.getFirstName());
      }
      if (user.getLastName() != null) {
        userDbToEdit.setLastName(user.getLastName());
      }
      if (user.getEmail() != null) {
        userDbToEdit.setEmail(user.getEmail());
      }
      if (user.getPassword() != null) {
        userDbToEdit.setPassword(passwordEncoder.encode(user.getPassword()));
      }

      User userOptional = userRepository.save(userDbToEdit);
      return Optional.ofNullable(UserDtoMapper.builder().setUser(userOptional).build());
    }

    return Optional.empty();
  }

  @Override
  @Transactional
  public void remove(Long userId) {
    User user = userRepository.findById(userId)
        .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

    // Verificar si el usuario tiene el rol de ADMIN
    boolean isAdmin = user.getRoles().stream()
        .anyMatch(role -> "ROLE_ADMIN".equals(role.getName()));

    if (isAdmin) {
      throw new UnsupportedOperationException("No se puede eliminar un usuario con rol de ADMIN");
    }

    userRepository.deleteById(userId);
  }

  @Override
  @Transactional
  public boolean isTokenValid(TokenValidationRequestDto tokenValidationRequestDto) {
    try {
      Jwts.parser().verifyWith(TokenJwtConfig.SECRET_KEY).build().parseSignedClaims(tokenValidationRequestDto.getToken()).getPayload();
      return true;
    } catch (Exception e) {
      System.out.println(e);
    }
    return false;
  }

}
