package com.basis.anhangda37.exception.payment;

import org.springframework.security.web.server.authentication.ServerWebExchangeDelegatingReactiveAuthenticationManagerResolver;

public class SignNotVerifyException extends Exception{
    public SignNotVerifyException() {super();}
    public SignNotVerifyException(String message) {super(message);};
}
