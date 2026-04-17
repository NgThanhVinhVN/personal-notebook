package com.notebook.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

public class UserRegistrationDto {
    @NotEmpty(message = "Username should not be empty")
    private String username;

    @NotEmpty(message = "Password should not be empty")
    @Size(min = 6, message = "Mật khẩu phải có ít nhất 6 ký tự")
    private String password;

    @NotEmpty(message = "Full name should not be empty")
    private String fullName;

    @NotEmpty(message = "Email should not be empty")
    @Email(message = "Invalid email format")
    private String email;

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
}
