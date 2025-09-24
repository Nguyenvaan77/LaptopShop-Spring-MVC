package com.basis.anhangda37.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.basis.anhangda37.domain.Role;
import java.util.Optional;


@Repository
public interface RoleRepository extends JpaRepository<Role, Long>{
    Optional<Role> findByName(String name);
}
