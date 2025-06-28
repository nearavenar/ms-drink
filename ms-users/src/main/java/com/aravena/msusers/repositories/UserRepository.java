package com.aravena.msusers.repositories;

import com.aravena.msusers.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
    Optional<User> findByNameAndPassword(String name, String password);
    Optional<User> findByNameAndEmail(String name, String email);
}
