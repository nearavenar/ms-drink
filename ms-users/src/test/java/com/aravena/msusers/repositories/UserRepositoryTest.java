package com.aravena.msusers.repositories;

import com.aravena.msusers.entities.Rol;
import com.aravena.msusers.entities.User;
import jakarta.persistence.EntityManager;
import jakarta.persistence.metamodel.Metamodel;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

@DataJpaTest
@ActiveProfiles("test")
//@EntityScan(basePackages = "com.aravena.msusers.entities")
//@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class UserRepositoryTest {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RolRepository rolRepository;

    @Autowired
    private EntityManager entityManager;

    @Test
    void showEntities() {
        Metamodel metamodel = entityManager.getMetamodel();
        metamodel.getEntities().forEach(entityType -> {
            System.out.println("Entity: " + entityType.getName());
        });
    }

    @Test
    void findByName() {
        String id = "nico@gmail.com";

        Rol rol = new Rol();
        rol.setName("User");
        List<Rol> roles = List.of(rol);

        User newUser = new User();
        newUser.setEmail("nico@gmail.com");
        newUser.setName("nico");
        newUser.setPassword("12345");
        newUser.setGender('M');
        newUser.setStatus(true);
        newUser.setDateCreated(LocalDateTime.now());
        newUser.setTemporal(true);
        newUser.setTemporalDate(LocalDateTime.now());
        newUser.setRoles(roles);

        rolRepository.save(rol);
        userRepository.save(newUser);
        Optional<User> user = userRepository.findByEmail(id);
        assertTrue(user.isPresent());
        assertEquals(1L, user.get().getId());
        assertEquals("nico", user.get().getName());
        assertEquals("nico@gmail.com", user.get().getEmail());
    }
}