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
        User nullUser = new User();
            nullUser.setId(-1L);
            nullUser.setFullName("N/A");
            nullUser.setEmail("N/A");
            nullUser.setAddress("N/A");
            nullUser.setPhoneNumber("N/A");
            nullUser.setPassword("N/A");
            if (id == null) {
                
                return nullUser;
            }
        return userRepository.findById(id).orElse(nullUser);
    }
}
