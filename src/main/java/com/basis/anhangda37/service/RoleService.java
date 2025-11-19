package com.basis.anhangda37.service;

import javax.management.relation.RelationService;

import org.springframework.stereotype.Service;

import com.basis.anhangda37.domain.Role;
import com.basis.anhangda37.repository.RoleRepository;

@Service
public class RoleService {
    private final RoleRepository roleRepository;

    public RoleService(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    public Role findByName(String name) {
        return roleRepository.findByName(name).get();
    }
}
