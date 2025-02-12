package com.example.Intern_api.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.Intern_api.model.Role;

@Repository
public interface RoleRepository extends JpaRepository<Role,Long>{
    
}
