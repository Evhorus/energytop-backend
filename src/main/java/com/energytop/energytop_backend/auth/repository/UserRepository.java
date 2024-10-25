package com.energytop.energytop_backend.auth.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.energytop.energytop_backend.auth.entities.UserEntity;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long> {
  UserEntity findByEmail(String email);
}
