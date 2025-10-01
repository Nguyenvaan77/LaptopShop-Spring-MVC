package com.basis.anhangda37.domain.domain;

import com.basis.anhangda37.service.validator.CustomEmail;
import com.basis.anhangda37.service.validator.RegisterChecked;
import com.basis.anhangda37.service.validator.StrongPassword;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@RegisterChecked
public class RegisterDto {
    private String firstName;
    private String lastName;

    @Email(message = "")
    @CustomEmail(suffix = "@gmail.com")
    private String email;

    @StrongPassword
    private String password;
    private String confirmPassword;

    public RegisterDto() {
        
    }

    public RegisterDto(String firstName, String lastName, String email, String password, String confirmPassword) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
        this.confirmPassword = confirmPassword;
    }

    
    public String getEmail() {
        return email;
    }


    public void setEmail(String email) {
        this.email = email;
    }


    public String getConfirmPassword() {
        return confirmPassword;
    }


    public void setConfirmPassword(String confirmPassword) {
        this.confirmPassword = confirmPassword;
    }


    public String getFirstName() {
        return firstName;
    }
    public void setFirstName(String fisrtName) {
        this.firstName = fisrtName;
    }
    public String getLastName() {
        return lastName;
    }
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }
    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }

    
}
