package com.basis.anhangda37.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.basis.anhangda37.domain.User;
import com.basis.anhangda37.domain.dto.RegisterDto;
import com.basis.anhangda37.exception.UserNotFoundException;
import com.basis.anhangda37.repository.RoleRepository;
import com.basis.anhangda37.repository.UserRepository;
import com.basis.anhangda37.service.iface.IUserService;

/**
 * Service class for User-related operations.
 * Handles user management including registration, authentication, and CRUD operations.
 * Implements clean separation of concerns with proper logging and exception handling.
 */
@Service
public class UserService implements IUserService {

    private static final Logger logger = LoggerFactory.getLogger(UserService.class);

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

    /**
     * Constructs a UserService with required repositories.
     * Uses constructor injection for dependency management.
     */
    public UserService(UserRepository userRepository, RoleRepository roleRepository) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
    }

    /**
     * Retrieves a user by ID.
     * @param id The user ID
     * @return The user
     * @throws UserNotFoundException if the user doesn't exist
     */
    @Override
    public User getUserById(Long id) {
        logger.debug("Fetching user with id: {}", id);
        return userRepository.findById(id)
                .orElseThrow(() -> {
                    logger.error("User not found with id: {}", id);
                    return new UserNotFoundException(id);
                });
    }

    /**
     * Retrieves a user by email address.
     * @param email The user's email
     * @return The user, or null if not found
     */
    @Override
    public User getUserByEmail(String email) {
        logger.debug("Fetching user with email: {}", email);
        User user = userRepository.findByEmail(email);
        if (user == null) {
            logger.warn("User not found with email: {}", email);
        }
        return user;
    }

    /**
     * Retrieves all users.
     * @return A list of all users
     */
    @Override
    public List<User> getAllUsers() {
        logger.debug("Fetching all users");
        return userRepository.findAll();
    }

    /**
     * Retrieves all users with pagination.
     * @param pageable The pagination information
     * @return A page of users
     */
    @Override
    public Page<User> getAllUsers(Pageable pageable) {
        logger.debug("Fetching users with pagination: page={}, size={}", 
                pageable.getPageNumber(), pageable.getPageSize());
        return userRepository.findAll(pageable);
    }

    /**
     * Saves or updates a user.
     * @param user The user to save
     * @return The saved user
     */
    @Override
    public User saveUser(User user) {
        logger.info("Saving user with email: {}", user.getEmail());
        User savedUser = userRepository.save(user);
        logger.debug("User saved successfully with id: {}", savedUser.getId());
        return savedUser;
    }

    /**
     * Deletes a user by ID.
     * @param id The user ID
     */
    @Override
    public void deleteUserById(Long id) {
        logger.info("Deleting user with id: {}", id);
        if (!userRepository.existsById(id)) {
            logger.warn("Attempted to delete non-existent user with id: {}", id);
            throw new UserNotFoundException(id);
        }
        userRepository.deleteById(id);
        logger.debug("User deleted successfully with id: {}", id);
    }

    /**
     * Returns the total count of users.
     * @return The user count
     */
    @Override
    public long countUsers() {
        logger.debug("Counting total users");
        return userRepository.count();
    }

    /**
     * Converts a registration DTO to a User entity.
     * @param registerDto The registration DTO
     * @return The converted user entity
     */
    @Override
    public User convertRegisterDtoToUser(RegisterDto registerDto) {
        logger.debug("Converting RegisterDto to User for email: {}", registerDto.getEmail());
        User user = new User();
        user.setEmail(registerDto.getEmail());
        user.setFullName(registerDto.getFirstName() + " " + registerDto.getLastName());
        return user;
    }
}
