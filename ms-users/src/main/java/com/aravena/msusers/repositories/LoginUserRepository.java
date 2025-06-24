package com.aravena.msusers.repositories;

import com.aravena.msusers.entities.LoginUser;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LoginUserRepository extends JpaRepository<LoginUser, Long> {
}