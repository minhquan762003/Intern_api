package com.example.Intern_api.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.Intern_api.model.Role;
import com.example.Intern_api.model.UserAndRole;
import org.springframework.stereotype.Repository;

@Repository
public interface UserAndRoleRepository extends JpaRepository<UserAndRole, Long>{
    @Query("SELECT ur.role FROM UserAndRole ur WHERE ur.user.userId = :userId")
    List<Role> findRolesByUserId(@Param("userId") Long userId);
}
