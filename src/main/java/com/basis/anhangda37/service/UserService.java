package com.basis.anhangda37.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.basis.anhangda37.domain.User;
import com.basis.anhangda37.repository.UserRepository;

@Service
public class UserService {
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User saveUser(User user) {
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
