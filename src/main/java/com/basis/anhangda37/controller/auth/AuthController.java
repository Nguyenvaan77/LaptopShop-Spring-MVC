package com.basis.anhangda37.controller.auth;

import java.util.List;

import org.springframework.boot.env.RandomValuePropertySourceEnvironmentPostProcessor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.basis.anhangda37.domain.User;
import com.basis.anhangda37.domain.dto.LoginDto;
import com.basis.anhangda37.domain.dto.RegisterDto;
import com.basis.anhangda37.service.UserService;

import jakarta.validation.Valid;

@Controller
public class AuthController {
    private final PasswordEncoder passwordEncoder;
    private final UserService userService;

    public AuthController(PasswordEncoder passwordEncoder, UserService userService) {
        this.passwordEncoder = passwordEncoder;
        this.userService = userService;
    }


    @GetMapping("/register")
    public String getRegisterPage(Model model) {
        model.addAttribute("registerDto", new RegisterDto());
        return "/client/auth/register";
    }
    
    @PostMapping("/register")
    public String postRegisterPage(@ModelAttribute("registerDto") @Valid RegisterDto registerDto,
                                    BindingResult registerBindingResult) {
        List<FieldError> errors = registerBindingResult.getFieldErrors();
        if(registerBindingResult.hasErrors()) {
            errors.forEach(e -> {
                System.out.println(e.getField() + " " + e.getDefaultMessage());
            });
            return "/client/auth/register";
        }
        User user = userService.registerDtoToUser(registerDto);
        user.setPassword(passwordEncoder.encode(registerDto.getPassword()));
        user.setRole(userService.getRoleByName("USER"));
        userService.saveUser(user);
        return "redirect:/";
    }

    @GetMapping("/login")
    public String getLoginPage(Model model) {
        model.addAttribute("loginDto", new LoginDto());
        return "/client/auth/login";
    }
    
    @GetMapping("/hello")
    public String getHelloPage() {
        return "/hello";
    }
}
