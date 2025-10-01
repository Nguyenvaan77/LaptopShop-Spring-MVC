package com.basis.anhangda37.domain.domain;

import com.basis.anhangda37.service.validator.CustomEmail;

import jakarta.validation.constraints.Email;

public class LoginDto {
    @Email
    @CustomEmail(suffix = "@gmail.com")
    private String email;
    private String password;

    public LoginDto() {
        
    }

    public LoginDto(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }
}
