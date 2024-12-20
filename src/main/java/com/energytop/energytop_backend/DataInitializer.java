package com.energytop.energytop_backend;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import com.energytop.energytop_backend.auth.entities.Role;
import com.energytop.energytop_backend.auth.entities.User;
import com.energytop.energytop_backend.auth.repository.RoleRepository;
import com.energytop.energytop_backend.auth.repository.UserRepository;
import java.util.List;

@Component
public class DataInitializer implements CommandLineRunner {

  @Autowired
  private UserRepository userRepository;

  @Autowired
  private RoleRepository roleRepository;

  @Autowired
  private PasswordEncoder passwordEncoder;

  @Override
  public void run(String... args) throws Exception {
    Optional<Role> roleOptional = roleRepository.findByName("ROLE_ADMIN");
    if (roleOptional.isEmpty()) {
      Role adminRole = new Role("ROLE_ADMIN");
      roleRepository.save(adminRole);
      System.out.println("Rol de administrador creado.");
    }
    if (userRepository.countByEmail("admin@admin.com") == 0) {

      User user = new User();
      user.setFirstName("admin");
      user.setLastName("admin");
      user.setEmail("admin@admin.com");
      user.setPassword(passwordEncoder.encode("admin_password")); // Cambia la contraseña por defecto
  
      // Asignar el rol de administrador al usuario
      Role adminRole = roleRepository.findByName("ROLE_ADMIN").orElseThrow();
      user.setRoles(List.of(adminRole));

      userRepository.save(user);
      System.out.println("Usuario administrador creado.");
    } else {
      System.out.println("Ya existe un usuario administrador en la base de datos.");
    }
  }
}