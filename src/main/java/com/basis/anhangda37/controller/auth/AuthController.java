package com.basis.anhangda37.controller.auth;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import com.basis.anhangda37.domain.User;
import com.basis.anhangda37.domain.dto.LoginDto;
import com.basis.anhangda37.domain.dto.RegisterDto;
import com.basis.anhangda37.repository.RoleRepository;
import com.basis.anhangda37.service.UserService;
import com.basis.anhangda37.util.AppConstants;

import jakarta.validation.Valid;

/**
 * Controller for handling authentication operations (login and registration).
 * Manages user authentication flow without any business logic.
 * Follows MVC pattern with proper separation of concerns.
 */
@Controller
public class AuthController {

    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);

    private final PasswordEncoder passwordEncoder;
    private final UserService userService;
    private final RoleRepository roleRepository;

    /**
     * Constructs an AuthController with required dependencies.
     * Uses constructor injection for dependency management.
     */
    public AuthController(PasswordEncoder passwordEncoder, UserService userService, RoleRepository roleRepository) {
        this.passwordEncoder = passwordEncoder;
        this.userService = userService;
        this.roleRepository = roleRepository;
    }

    /**
     * Displays the user registration page.
     * GET endpoint: /register
     * @param model The model to add attributes
     * @return The registration view name
     */
    @GetMapping("/register")
    public String getRegisterPage(Model model) {
        logger.debug("Displaying registration page");
        model.addAttribute("registerDto", new RegisterDto());
        return "/client/auth/register";
    }

    /**
     * Handles user registration.
     * POST endpoint: /register
     * @param registerDto The registration data transfer object
     * @param bindingResult The binding result for validation errors
     * @return Redirect to homepage on success, back to registration page on validation error
     */
    @PostMapping("/register")
    public String postRegisterPage(
            @ModelAttribute("registerDto") @Valid RegisterDto registerDto,
            BindingResult bindingResult) {

        // Early return if validation fails
        if (bindingResult.hasErrors()) {
            List<FieldError> errors = bindingResult.getFieldErrors();
            logger.warn("Registration validation failed with {} errors", errors.size());
            errors.forEach(error -> 
                logger.debug("Field: {}, Message: {}", error.getField(), error.getDefaultMessage())
            );
            return "/client/auth/register";
        }

        logger.info("Processing user registration for email: {}", registerDto.getEmail());

        try {
            // Convert DTO to entity
            User user = userService.convertRegisterDtoToUser(registerDto);

            // Encode password
            user.setPassword(passwordEncoder.encode(registerDto.getPassword()));

            // Set default role
            user.setRole(roleRepository.findByName(AppConstants.ROLE_USER)
                    .orElseThrow(() -> new IllegalStateException("Default role not found")));

            // Save user
            userService.saveUser(user);
            logger.info("User registered successfully: {}", registerDto.getEmail());

            return "redirect:/";
        } catch (Exception ex) {
            logger.error("Error during user registration: {}", ex.getMessage(), ex);
            bindingResult.reject("registration.error", "An error occurred during registration");
            return "/client/auth/register";
        }
    }

    /**
     * Displays the user login page.
     * GET endpoint: /login
     * @param model The model to add attributes
     * @return The login view name
     */
    @GetMapping("/login")
    public String getLoginPage(Model model) {
        logger.debug("Displaying login page");
        model.addAttribute("loginDto", new LoginDto());
        return "/client/auth/login";
    }

    /**
     * Displays a test hello page.
     * GET endpoint: /hello
     * @return The hello view name
     */
    @GetMapping("/hello")
    public String getHelloPage() {
        logger.debug("Displaying hello page");
        return "/hello";
    }
}
