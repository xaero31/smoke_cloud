package com.ermakov.nikita.repository;

import com.ermakov.nikita.entity.profile.VerificationToken;
import org.springframework.data.repository.Repository;

/**
 * created by Nikita_Ermakov at 2/25/2020
 */
public interface VerificationTokenRepository extends Repository<VerificationToken, Long> {

    VerificationToken findByToken(String token);

    VerificationToken save(VerificationToken token);

    void delete(VerificationToken token);
}
