package com.ermakov.nikita.model.security;

import lombok.Data;

import javax.persistence.*;

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
}
