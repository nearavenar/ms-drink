package com.aravena.msusers.services;

import com.aravena.msusers.dto.UserDTO;
import com.aravena.msusers.entities.Rol;
import com.aravena.msusers.entities.User;
import com.aravena.msusers.repositories.RolRepository;
import com.aravena.msusers.repositories.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {
    @InjectMocks
    private UserServiceImpl userService;
    @Mock
    private UserRepository userRepository;
    @Mock
    private RolRepository rolRepository;
    private User mockUser;

    @BeforeEach
    void setUp() {
        userService = new UserServiceImpl(userRepository, rolRepository, new ModelMapper());
        List<Rol> roles = List.of(new Rol(1L, "User"));
        mockUser = new User(1L, "nico", "12345", "nico@gmail.com", 'M',true, LocalDateTime.now(), roles);
    }

    @Test
    void getUserById() {
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(mockUser));
        Optional<UserDTO> result = userService.getUserById(1L);
        assertTrue(result.isPresent());
        assertEquals(mockUser.getId(), result.get().getId());
        assertEquals(mockUser.getRoles().get(0).getName(), result.get().getRoles().get(0).getName());
    }

    @Test
    void getUserByName() {
        when(userRepository.findByName(anyString())).thenReturn(Optional.of(mockUser));
        Optional<UserDTO> result = userService.getUserByName("nico");
        assertTrue(result.isPresent());
        assertEquals(mockUser.getName(), result.get().getName());
        assertEquals(mockUser.getRoles().get(0).getName(), result.get().getRoles().get(0).getName());
    }
}