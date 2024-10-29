package com.energytop.energytop_backend.auth.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.energytop.energytop_backend.auth.repository.UserRepository;

@Service
@Transactional(readOnly = true)
public class JpaUserDetailsService implements UserDetailsService {

  @Autowired
  private UserRepository userRepository;

  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

    Optional<com.energytop.energytop_backend.auth.entities.User> userOptional = userRepository.findByEmail(username);
    if (!userOptional.isPresent()) {
      throw new UsernameNotFoundException(String.format("El email %s no existe en el sistema", username));
    }

    com.energytop.energytop_backend.auth.entities.User user = userOptional.orElseThrow();

    List<GrantedAuthority> authorities = user.getRoles()
        .stream().map(r -> new SimpleGrantedAuthority(r.getName())).collect(Collectors.toList());

    return new User(user.getEmail(), user.getPassword(), true, true, true, true,
        authorities);
  }

}
