package com.aravena.msusers.controllers;

import com.aravena.msusers.dto.RolDTO;
import com.aravena.msusers.dto.UserDTO;
import com.aravena.msusers.services.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.*;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

@WebMvcTest(UserController.class)
class UserControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private UserService userService;
    private UserDTO mockUser;

    @BeforeEach
    void setUp() {
        List<RolDTO> roles = List.of(new RolDTO(1L, "User"));
        mockUser = new UserDTO(1L, "nico", "12345", "nico@gmail.com",'M', true, LocalDateTime.now(), roles);
    }

    @TestConfiguration
    static class TestConfig {
        @Bean
        public UserService userService() {
            return mock(UserService.class);
        }
    }

    @Test
    void getUserById() throws Exception {
        when(userService.getUserById(anyLong())).thenReturn(Optional.of(mockUser));

        mockMvc.perform(get("/users/find-by-id/1").contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.name", is("nico")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.password", is("12345")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.email", is("nico@gmail.com")));
    }

    @Test
    void getUserByName() throws Exception {
        when(userService.getUserByName(anyString())).thenReturn(Optional.of(mockUser));

        mockMvc.perform(get("/users/find-by-user/nico@gmail.com").contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.name", is("nico")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.password", is("12345")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.email", is("nico@gmail.com")));
    }
}