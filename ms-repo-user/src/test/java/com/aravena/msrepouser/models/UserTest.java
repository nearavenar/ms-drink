package com.aravena.msrepouser.models;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class UserTest {

    private User user;
    @BeforeEach
    void setUp() {
        user = new User();
        user.setId(1L);
        user.setName("nicolas");
        user.setPassword("nico1234");
        user.setEmail("nico@gmail.com");
        user.setDateCreated(LocalDateTime.now());
        user.setGender('M');
        user.setStatus(true);
        List<Rol> roles = List.of(new Rol(1L, "USER"));
        user.setRoles(roles);
    }

    @Test
    void user() {
        assertEquals(1L, user.getId());
        assertEquals("nicolas", user.getName());
        assertEquals("nico1234", user.getPassword());
        assertEquals("nico@gmail.com", user.getEmail());
        assertEquals('M', user.getGender());
        assertTrue(user.isStatus());
        assertFalse(user.getRoles().isEmpty());
        assertNotNull(user.getDateCreated());
        assertEquals("USER", user.getRoles().get(0).getName());
        assertEquals(1L, user.getRoles().get(0).getId());
    }

    @Test
    void rol() {
        Rol rol  = new Rol();
        rol.setId(1L);
        rol.setName("USER");
        List<Rol> roles = List.of(rol);
        User user = new User(1L,"nicolas","nico1234","nico@gmail.com",'M',true, LocalDateTime.now(), roles);
        assertNotNull(user);
    }
}