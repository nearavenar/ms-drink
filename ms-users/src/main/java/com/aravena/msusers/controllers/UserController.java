package com.aravena.msusers.controllers;

import com.aravena.msusers.dto.LoginUserDTO;
import com.aravena.msusers.dto.UserDTO;
import com.aravena.msusers.entities.User;
import com.aravena.msusers.services.UserService;
import jakarta.validation.Valid;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
@Log4j2
public class UserController {
    private final UserService userService;
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/find-by-id/{id}")
    public ResponseEntity<UserDTO> getUserById(@PathVariable Long id){
        log.info("UserController.getUserById id: {}", id);
        return userService.getUserById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/find-by-user/{email}")
    public ResponseEntity<UserDTO> getUserByName(@PathVariable String email){
        log.info("UserController.getUserByName email: {}", email);
        return userService.getUserByName(email)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/login-user")
    public ResponseEntity<Boolean> loginUser(@RequestBody @Valid LoginUserDTO login){
        log.info("UserController.loginUser email: {} password: {}", login.getUser(), login.getPassword());
        boolean result = userService.loginUser(login.getUser(), login.getPassword());
        return ResponseEntity.ok(result);
    }

    @PostMapping("/send-email-user")
    public ResponseEntity<Boolean> sendEmailUser(@RequestBody @Valid LoginUserDTO login){
        log.info("UserController.sendEmailUser email: {} password: {}", login.getUser(), login.getPassword());
        boolean result =userService.sendEmailUser(login.getUser(), login.getPassword());
        return ResponseEntity.ok(result);
    }

    @PostMapping("/unlock-user")
    public ResponseEntity<Boolean> unlockUser(@RequestBody @Valid LoginUserDTO login){
        log.info("UserController.unlockUser email: {} password: {}", login.getUser(), login.getPassword());
        User result = userService.unlockUser(login.getUser(), login.getPassword());
        return ResponseEntity.ok(result.isStatus());
    }
}
