package com.aravena.msusers.services;

import com.aravena.msusers.dto.UserDTO;
import com.aravena.msusers.entities.User;
import com.aravena.msusers.repositories.RolRepository;
import com.aravena.msusers.repositories.UserRepository;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserServiceImpl implements UserService{
    private final UserRepository userRepository;
    private final RolRepository rolRepository;

    private final ModelMapper modelMapper;

    public UserServiceImpl(UserRepository userRepository, RolRepository rolRepository, ModelMapper modelMapper) {
        this.userRepository = userRepository;
        this.rolRepository = rolRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public Optional<UserDTO> getUserById(Long id) {
        return userRepository.findById(id).map(user -> modelMapper.map(user, UserDTO.class));
    }

    @Override
    public Optional<UserDTO> getUserByName(String name) {
        return userRepository.findByName(name).map(user -> modelMapper.map(user, UserDTO.class));
    }
}
