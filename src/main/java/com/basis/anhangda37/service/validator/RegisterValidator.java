package com.basis.anhangda37.service.validator;

import org.springframework.stereotype.Service;

import com.basis.anhangda37.domain.domain.RegisterDto;
import com.basis.anhangda37.repository.UserRepository;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

@Service
public class RegisterValidator implements ConstraintValidator<RegisterChecked, RegisterDto>  {

    private final UserRepository userRepository;

    public RegisterValidator(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public boolean isValid(RegisterDto user, ConstraintValidatorContext context) {
        boolean valid = true;

        if(!user.getPassword().equals(user.getConfirmPassword())) {
            context.buildConstraintViolationWithTemplate("Password not match")
                    .addPropertyNode("confirmPassword")
                    .addConstraintViolation()
                    .disableDefaultConstraintViolation();
            valid = false;
        }

        if(checkExistEmail(user.getEmail())) {
            context.buildConstraintViolationWithTemplate("Email already exist")
                    .addPropertyNode("email")
                    .addConstraintViolation()
                    .disableDefaultConstraintViolation();
            valid = false;
        }

        return valid;
    }

    private boolean checkExistEmail(String email) {
        return userRepository.existsByEmail(email);
    }
    
}
