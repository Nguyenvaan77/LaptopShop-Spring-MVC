package com.basis.anhangda37.service.validator;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.net.Authenticator.RequestorType;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

@Constraint(validatedBy = RegisterValidator.class)
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface RegisterChecked {
    String message() default "Thông tin người dùng không hợp lệ";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
