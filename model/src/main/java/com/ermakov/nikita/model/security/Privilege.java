package com.ermakov.nikita.model.security;

import lombok.Data;

import javax.persistence.*;
import java.util.Collection;

/**
 * created by Nikita_Ermakov at 2/17/2020
 */
@Data
@Entity
@Table(name = "privileges")
public class Privilege {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", unique = true, nullable = false, updatable = false)
    private long id;

    @Column(name = "name", unique = true, nullable = false)
    private String name;

    @ManyToMany(mappedBy = "privileges")
    private Collection<Role> roles;
}
