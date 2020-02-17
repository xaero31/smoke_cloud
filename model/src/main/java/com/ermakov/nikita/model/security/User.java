package com.ermakov.nikita.model.security;

import lombok.Data;

import javax.persistence.*;
import java.util.Collection;

/**
 * created by Nikita_Ermakov at 2/16/2020
 */
@Data
@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, updatable = false, unique = true)
    private long id;

    @Column(name = "username", unique = true, nullable = false, updatable = false)
    private String username;

    @Basic(fetch = FetchType.LAZY)
    @Column(name = "password", nullable = false)
    private String password;

    @ManyToMany
    @JoinTable(
            name = "users_roles",
            joinColumns = @JoinColumn(name = "user_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "role_id", referencedColumnName = "id")
    )
    private Collection<Role> roles;

    @Column(name = "non_expired", nullable = false)
    private boolean nonExpired;

    @Column(name = "non_locked", nullable = false)
    private boolean nonLocked;

    @Column(name = "credentials_non_expired", nullable = false)
    private boolean credentialsNonExpired;

    @Column(name = "enabled", nullable = false)
    private boolean enabled;
}
