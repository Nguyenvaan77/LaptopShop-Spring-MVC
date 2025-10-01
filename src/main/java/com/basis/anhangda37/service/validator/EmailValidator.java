package com.basis.anhangda37.service.validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class EmailValidator implements ConstraintValidator<CustomEmail, String>{
    String suffix = null;

    @Override
    public void initialize(CustomEmail constraintAnnotation) {
        suffix = constraintAnnotation.suffix();
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(String arg0, ConstraintValidatorContext arg1) {
        return checkValidEmail(arg0);
    }
    
    private boolean checkValidEmail(String email) {
        return email.contains(suffix);
    }
}
