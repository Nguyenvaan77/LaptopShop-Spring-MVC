package com.basis.anhangda37.service.validator;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

@Constraint(validatedBy = StrongClassValidator.class)
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface StrongPassword {
    String message() default "Mật khẩu không đúng định dạng";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
