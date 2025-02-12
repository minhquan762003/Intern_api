package com.example.Intern_api.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.Intern_api.model.UserAndRole;
import org.springframework.stereotype.Repository;

@Repository
public interface UserAndRoleRepository extends JpaRepository<UserAndRole, Long>{
    
}
