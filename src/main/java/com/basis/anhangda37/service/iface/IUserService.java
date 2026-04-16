package com.basis.anhangda37.service.iface;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.basis.anhangda37.domain.User;
import com.basis.anhangda37.domain.dto.RegisterDto;

import java.util.List;

/**
 * Service interface for User-related operations.
 * Defines contracts for user management including authentication, registration, and CRUD operations.
 */
public interface IUserService {

    /**
     * Retrieves a user by ID.
     * @param id The user ID
     * @return The user, or null if not found
     * @throws com.basis.anhangda37.exception.UserNotFoundException if user doesn't exist
     */
    User getUserById(Long id);

    /**
     * Retrieves a user by email address.
     * @param email The user's email
     * @return The user, or null if not found
     */
    User getUserByEmail(String email);

    /**
     * Retrieves all users with pagination.
     * @param pageable The pagination information
     * @return A page of users
     */
    Page<User> getAllUsers(Pageable pageable);

    /**
     * Retrieves all users.
     * @return A list of all users
     */
    List<User> getAllUsers();

    /**
     * Saves or updates a user.
     * @param user The user to save
     * @return The saved user
     */
    User saveUser(User user);

    /**
     * Deletes a user by ID.
     * @param id The user ID
     */
    void deleteUserById(Long id);

    /**
     * Returns the total count of users.
     * @return The user count
     */
    long countUsers();

    /**
     * Converts a registration DTO to a User entity.
     * @param registerDto The registration DTO
     * @return The converted user entity
     */
    User convertRegisterDtoToUser(RegisterDto registerDto);
}
