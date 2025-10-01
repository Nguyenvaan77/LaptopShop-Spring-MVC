package com.basis.anhangda37.service.validator;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

@Constraint(validatedBy = EmailValidator.class)
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface CustomEmail{
    String message() default "Hãy nhập đúng định dạng Email";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
    String suffix(); 
}