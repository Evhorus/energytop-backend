package com.energytop.energytop_backend.auth.services;

import java.util.List;
import java.util.Optional;

import com.energytop.energytop_backend.auth.dto.CreateUserDto;
import com.energytop.energytop_backend.auth.dto.TokenValidationRequestDto;
import com.energytop.energytop_backend.auth.dto.UpdateUserDto;
import com.energytop.energytop_backend.auth.dto.UserDto;
import com.energytop.energytop_backend.common.dto.SearchDto;

public interface AuthService {
  List<UserDto> findAll();

  Optional<UserDto> findByIdOrEmail(String identifier);

  List<UserDto> searchUsers(SearchDto searchDto);

  UserDto create(CreateUserDto createUserDto);

  String update(Long id, UpdateUserDto updateUserDto);

  String updateProfile(String id, UpdateUserDto updateUserDto);

  void remove(Long id);

  boolean isTokenValid(TokenValidationRequestDto token);

}
