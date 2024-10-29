package com.energytop.energytop_backend.auth.dto.mapper;

import com.energytop.energytop_backend.auth.dto.UserDto;
import com.energytop.energytop_backend.auth.entities.User;

public class UserDtoMapper {

  private User user;

  private UserDtoMapper() {
  }

  public static UserDtoMapper builder() {
    return new UserDtoMapper();
  }

  public UserDtoMapper setUser(User user) {
    this.user = user;
    return this;
  }

  public UserDto build() {
    if (user == null) {
      throw new RuntimeException("Debe pasar el entity user!");
    }
    return new UserDto(this.user.getId(), user.getFirstName(), user.getLastName(), user.getEmail());
  }
}
