package com.aravena.msusers.services;

import com.aravena.msusers.dto.UserDTO;
import com.aravena.msusers.entities.User;

import java.util.Optional;

public interface UserService {
    Optional<UserDTO> getUserById(Long id);
    Optional<UserDTO> getUserByName(String email);
    boolean loginUser(String email, String password);
    boolean sendEmailUser(String email, String password);
    User unlockUser(String email, String newPassword);
}
