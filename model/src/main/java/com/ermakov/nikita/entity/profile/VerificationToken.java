package com.ermakov.nikita.entity.profile;

import com.ermakov.nikita.entity.security.User;
import lombok.Data;

import javax.persistence.*;
import java.util.Date;

/**
 * created by Nikita_Ermakov at 2/25/2020
 */
@Data
@Entity
@Table(name = "verification_token")
public class VerificationToken {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", unique = true, nullable = false, updatable = false)
    private long id;

    @Column(name = "token", unique = true, nullable = false, updatable = false)
    private String token;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    @JoinColumn(name = "user_id", nullable = false, updatable = false)
    private User user;

    @Column(name = "expiration_date", nullable = false, updatable = false)
    private Date expirationDate;
}
