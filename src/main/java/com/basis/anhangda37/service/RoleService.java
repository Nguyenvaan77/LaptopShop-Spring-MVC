package com.basis.anhangda37.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.basis.anhangda37.domain.Role;
import com.basis.anhangda37.repository.RoleRepository;
import com.basis.anhangda37.exception.UserNotFoundException;

/**
 * Service class for Role-related operations.
 * Handles role management and retrieval with proper logging and exception handling.
 */
@Service
public class RoleService {

    private static final Logger logger = LoggerFactory.getLogger(RoleService.class);

    private final RoleRepository roleRepository;

    /**
     * Constructs a RoleService with required repository.
     */
    public RoleService(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    /**
     * Finds a role by name.
     * @param name The role name
     * @return The role if found
     * @throws UserNotFoundException if the role doesn't exist
     */
    public Role findByName(String name) {
        logger.debug("Fetching role with name: {}", name);
        return roleRepository.findByName(name)
                .orElseThrow(() -> {
                    logger.error("Role not found with name: {}", name);
                    return new UserNotFoundException("Role not found: " + name);
                });
    }
}
