package com.energytop.energytop_backend.auth.service;

import java.util.List;
import java.util.Optional;

import com.energytop.energytop_backend.auth.dto.TokenValidationRequestDto;
import com.energytop.energytop_backend.auth.dto.UserDto;
import com.energytop.energytop_backend.auth.entities.User;

public interface AuthService {
  List<UserDto> findAll();

  Optional<UserDto> findById(Long id);

  UserDto save(User user);

  Optional<UserDto> update(User user, Long id);

  boolean isTokenValid(TokenValidationRequestDto token);

  void remove(Long id);
}
