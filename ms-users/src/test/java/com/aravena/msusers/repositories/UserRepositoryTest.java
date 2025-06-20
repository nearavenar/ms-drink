package com.aravena.msusers.repositories;

import com.aravena.msusers.entities.Rol;
import com.aravena.msusers.entities.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class UserRepositoryTest {
    @Autowired
    private UserRepository userRepository;

    @Test
    void findByName() {
        String id = "nico";
        List<Rol> roles = List.of(new Rol(1L, "User"));
        User newUser = new User(1L, "nico", "12345", "nico@gmail.com", 'M',true, LocalDateTime.now(), roles);

        userRepository.save(newUser);
        Optional<User> user = userRepository.findByName(id);
        assertTrue(user.isPresent());
        assertEquals(1L, user.get().getId());
        assertEquals("nico", user.get().getName());
        assertEquals("nico@gmail.com", user.get().getEmail());
    }
}