package com.basis.anhangda37.service.validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class StrongClassValidator implements ConstraintValidator<StrongPassword, String>{

    @Override
    public boolean isValid(String passwordString, ConstraintValidatorContext arg1) {
        return ((passwordString.length() >= 8 && passwordString != null) ? true : false);
    }
    
}
