package com.energytop.energytop_backend.auth.services;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.energytop.energytop_backend.auth.dto.CreateUserDto;
import com.energytop.energytop_backend.auth.dto.TokenValidationRequestDto;
import com.energytop.energytop_backend.auth.dto.UpdateUserDto;
import com.energytop.energytop_backend.auth.dto.UserDto;
import com.energytop.energytop_backend.auth.dto.mapper.UserDtoMapper;
import com.energytop.energytop_backend.auth.entities.User;
import com.energytop.energytop_backend.auth.repository.RoleRepository;
import com.energytop.energytop_backend.auth.repository.UserRepository;
import com.energytop.energytop_backend.common.config.auth.TokenJwtConfig;
import com.energytop.energytop_backend.common.dto.SearchDto;
import com.energytop.energytop_backend.common.helpers.StringUtils;

import io.jsonwebtoken.Jwts;
import jakarta.persistence.EntityNotFoundException;

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
    return users.stream()
        .filter(user -> {

          boolean isAdmin = user.getRoles().stream()
              .anyMatch(role -> "ROLE_ADMIN".equals(role.getName()));
          return !isAdmin;
        })
        .map(user -> UserDtoMapper.builder().setUser(user).build())
        .collect(Collectors.toList());
  }

  @Override
  @Transactional(readOnly = true)
  public Optional<UserDto> findByIdOrEmail(String identifier) {
    if (identifier.matches("\\d+")) {
      Long id = Long.valueOf(identifier);

      Optional<UserDto> user = userRepository.findById(id)
          .map(userEntity -> UserDtoMapper.builder().setUser(userEntity).build());

      if (!user.isPresent()) {
        throw new EntityNotFoundException("No existe el usuario con el identificador: " + identifier);
      }
      return user;
    } else {
      Optional<UserDto> user = userRepository.findByEmail(identifier)
          .map(userEntity -> UserDtoMapper.builder().setUser(userEntity).build());
      if (!user.isPresent()) {
        throw new EntityNotFoundException("No existe el usuario con email: " + identifier);
      }
      return user;
    }
  }

  @Override
  @Transactional
  public UserDto create(CreateUserDto createUserDto) {

    String emailNormalized = createUserDto.getEmail().trim().toLowerCase();

    Optional<User> userDb = userRepository.findByEmail(emailNormalized);
    if (userDb.isPresent()) {
      throw new IllegalArgumentException("Ya existe un perfil con ese email");
    }

    User user = new User();
    user.setFirstName(createUserDto.getFirstName().trim());
    user.setLastName(createUserDto.getLastName().trim());
    user.setEmail(emailNormalized); // Guardar el correo en minúsculas
    user.setPassword(passwordEncoder.encode(createUserDto.getPassword()));

    roleRepository.findByName("ROLE_USER").ifPresent(role -> user.setRoles(List.of(role)));

    User savedUser = userRepository.save(user);

    return UserDtoMapper.builder().setUser(savedUser).build();
  }

  @Override
  @Transactional
  public String update(Long id, UpdateUserDto updateUserDto) {
    Optional<User> userToUpdate = userRepository.findById(id);

    if (userToUpdate.isEmpty()) {
      throw new EntityNotFoundException("No existe el usuario con el identificador: " + id);
    }

    User userDbToEdit = userToUpdate.get();

    if (updateUserDto.getFirstName() != null) {
      userDbToEdit.setFirstName(updateUserDto.getFirstName().trim());
    }
    if (updateUserDto.getLastName() != null) {
      userDbToEdit.setLastName(updateUserDto.getLastName().trim());
    }
    if (updateUserDto.getEmail() != null) {
      String emailNormalized = updateUserDto.getEmail().trim().toLowerCase();
      userDbToEdit.setEmail(emailNormalized);
    }
    if (updateUserDto.getPassword() != null) {
      userDbToEdit.setPassword(passwordEncoder.encode(updateUserDto.getPassword()));
    }

    userRepository.save(userDbToEdit);
    return "El usuario se actualizado correctamente";
  }

  @Override
  @Transactional
  public void remove(Long id) {
    Optional<User> userToDelete = userRepository.findById(id);
    if (!userToDelete.isPresent()) {
      throw new EntityNotFoundException("No existe el usuario con ID: " + id);
    }
    boolean isAdmin = userToDelete.get().getRoles().stream()
        .anyMatch(role -> "ROLE_ADMIN".equals(role.getName()));

    if (isAdmin) {
      throw new UnsupportedOperationException("No se puede eliminar un usuario con rol de ADMIN");
    }
    userRepository.deleteById(id);
  }

  @Override
  @Transactional
  public boolean isTokenValid(TokenValidationRequestDto tokenValidationRequestDto) {
    try {
      Jwts.parser().verifyWith(TokenJwtConfig.SECRET_KEY).build()
          .parseSignedClaims(tokenValidationRequestDto.getToken()).getPayload();
      return true;
    } catch (Exception e) {
      System.out.println(e);
    }
    return false;
  }

  @Override
  @Transactional
  public String updateProfile(String email, UpdateUserDto updateUserDto) {
    Optional<User> userToUpdate = userRepository.findByEmail(email);

    if (userToUpdate.isEmpty()) {
      throw new EntityNotFoundException("No existe el usuario con el identificador: " + email);
    }

    User userDbToEdit = userToUpdate.get();

    if (updateUserDto.getFirstName() != null) {
      userDbToEdit.setFirstName(updateUserDto.getFirstName().trim());
    }
    if (updateUserDto.getLastName() != null) {
      userDbToEdit.setLastName(updateUserDto.getLastName().trim());
    }
    if (updateUserDto.getEmail() != null) {
      String emailNormalized = updateUserDto.getEmail().trim().toLowerCase();
      userDbToEdit.setEmail(emailNormalized);
    }
    if (updateUserDto.getPassword() != null) {
      userDbToEdit.setPassword(passwordEncoder.encode(updateUserDto.getPassword()));
    }

    userRepository.save(userDbToEdit);
    return "El usuario se actualizado correctamente";

  }

  @Override
  @Transactional(readOnly = true)
  public List<UserDto> searchUsers(SearchDto searchDto) {

    String searchTerm = StringUtils.removeAccents(searchDto.getSearchTerm()).toLowerCase();
    String searchBy = searchDto.getSearchBy();

    if ("email".equalsIgnoreCase(searchBy)) {
      List<User> usersDb = userRepository.findByEmailStartingWithIgnoreCase(searchTerm);

      return usersDb.stream().map(user -> UserDtoMapper.builder().setUser(user).build()).collect(Collectors.toList());
    }
    throw new IllegalArgumentException("El campo de búsqueda '" + searchBy + "' no es válido.");
  }

}
