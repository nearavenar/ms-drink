package com.aravena.msusers.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "login_user")
public class LoginUser {
    @Id
    private Long idUser;
    @Column(nullable = false, unique = true)
    private String username;
    private int attempts;
    private boolean blocked;
    private LocalDateTime dateAttempt;
}
