package com.basis.anhangda37.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.basis.anhangda37.domain.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    User save(User user);
    Optional<User> findById(long id);
}  
