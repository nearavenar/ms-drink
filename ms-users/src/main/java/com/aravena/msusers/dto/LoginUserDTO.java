package com.aravena.msusers.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoginUserDTO {
    @NotBlank(message = "The user cannot be blank.")
    private String user;
    @NotBlank(message = "The password cannot be blank.")
    private String password;
}
