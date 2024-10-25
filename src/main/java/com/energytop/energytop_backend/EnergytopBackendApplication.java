package com.energytop.energytop_backend;

import java.util.List;
import java.util.Set;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import com.energytop.energytop_backend.enums.RoleEnum;
import com.energytop.energytop_backend.persistence.entity.user.PermissionEntity;
import com.energytop.energytop_backend.persistence.entity.user.RoleEntity;
import com.energytop.energytop_backend.persistence.entity.user.UserEntity;
import com.energytop.energytop_backend.persistence.repository.user.UserRepository;

@SpringBootApplication
public class EnergytopBackendApplication {

	public static void main(String[] args) {
		SpringApplication.run(EnergytopBackendApplication.class, args);
	}

	// @Bean
	// CommandLineRunner init(UserRepository userRepository) {
	// 	return args -> {
	// 		/* CREATE PERMISSIONS */
	// 		PermissionEntity createPermission = PermissionEntity.builder()
	// 				.name("CREATE")
	// 				.build();
	// 		PermissionEntity readPermission = PermissionEntity.builder()
	// 				.name("READ")
	// 				.build();

	// 		/* Create ROLES */

	// 		RoleEntity roleAdmin = RoleEntity.builder().roleEnum(RoleEnum.ADMIN)
	// 				.permissionsList(Set.of(createPermission, readPermission)).build();
	// 		RoleEntity roleUser = RoleEntity.builder().roleEnum(RoleEnum.ADMIN)
	// 				.permissionsList(Set.of(readPermission)).build();

	// 		/* Create USER */

	// 		UserEntity userAdmin = UserEntity.builder()
	// 				.firstName("admin")
	// 				.lastName("admin")
	// 				.email("admin@admin.com")
	// 				.password("123")
	// 				.isEnabled(true)
	// 				.accountNoExpired(true)
	// 				.accountNoLocked(true)
	// 				.credentialNoExpired(true)
	// 				.roles(Set.of(roleAdmin))
	// 				.build();
	// 		userRepository.saveAll(List.of(userAdmin));
	// 	};
	// }

}
