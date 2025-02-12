package com.example.Intern_api.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.Intern_api.model.User;

import java.util.*;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    public Optional<User> findByUserName(String userName);

    public Optional<User> findByEmail(String email);
}
