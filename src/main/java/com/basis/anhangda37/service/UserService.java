package com.basis.anhangda37.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.basis.anhangda37.domain.Role;
import com.basis.anhangda37.domain.User;
import com.basis.anhangda37.repository.RoleRepository;
import com.basis.anhangda37.repository.UserRepository;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

    public UserService(UserRepository userRepository, RoleRepository roleRepository) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
    }

    public User saveUser(User user) {
        Role role = null;
        role = roleRepository.findByName(user.getRole().getName()).orElse(null);
        user.setRole(role);
        User savedUSer = userRepository.save(user);
        return savedUSer;
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public User getUserById(Long id) {
        return userRepository.findById(id).orElse(null);
    }

    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }
}
