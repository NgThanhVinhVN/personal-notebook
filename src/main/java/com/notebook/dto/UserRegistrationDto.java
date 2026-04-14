package com.notebook.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserRegistrationDto {
    @NotEmpty(message = "Username should not be empty")
    private String username;

    @NotEmpty(message = "Password should not be empty")
    private String password;

    @NotEmpty(message = "Full name should not be empty")
    private String fullName;

    @NotEmpty(message = "Email should not be empty")
    @Email(message = "Invalid email format")
    private String email;
}
