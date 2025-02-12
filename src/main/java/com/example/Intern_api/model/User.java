package com.example.Intern_api.model;

import java.util.Date;
import java.util.Set;

import jakarta.persistence.*;
import lombok.Data;
@Data
@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long userId;

    @Column(name = "user_name")
    private String userName;

    @Column(name = "password")
    private String password;

    @Column(name = "email")
    private String email;

    @Column(name = "status")
    private int status;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(updatable = false, name = "created_at")
    private Date created_at = new Date();

    @Column(name = "updated_at")
    private Date updated_at;
    
    @OneToMany(mappedBy = "user", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private Set<UserAndRole> userAndRoles;

    @PreUpdate
    protected void onUpdate() {
        updated_at = new Date();
    }
}
