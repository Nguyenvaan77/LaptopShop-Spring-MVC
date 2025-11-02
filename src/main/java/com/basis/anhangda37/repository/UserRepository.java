package com.basis.anhangda37.repository;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.basis.anhangda37.domain.User;
import java.util.List;


@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    User save(User user);
    Optional<User> findById(long id);
    void deleteById(Long id);
    boolean existsByEmail(String email);
    User findByEmail(String email);
    long countBy();
    Page<User> findAll(Pageable pageable);
}  
