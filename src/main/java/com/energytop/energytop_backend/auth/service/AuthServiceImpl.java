package com.energytop.energytop_backend.auth.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.energytop.energytop_backend.auth.dto.UserDto;
import com.energytop.energytop_backend.auth.dto.mapper.UserDtoMapper;
import com.energytop.energytop_backend.auth.entities.Role;
import com.energytop.energytop_backend.auth.entities.User;
import com.energytop.energytop_backend.auth.repository.RoleRepository;
import com.energytop.energytop_backend.auth.repository.UserRepository;

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
  @Transactional()
  public Optional<UserDto> update(User user, Long id) {

    Optional<User> userDb = userRepository.findById(id);
    User userOptional = null;

    if (userDb.isPresent()) {
      User userDbToEdit = userDb.orElseThrow();
      userDbToEdit.setFirstName(user.getFirstName());
      userDbToEdit.setLastName(user.getLastName());
      userDbToEdit.setEmail(user.getEmail());
      userDbToEdit.setPassword(user.getPassword());
      userOptional = userRepository.save(userDbToEdit);
    }

    return Optional.ofNullable(UserDtoMapper.builder().setUser(userOptional).build());
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

}
