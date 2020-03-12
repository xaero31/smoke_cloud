package com.ermakov.nikita.repository;

import com.ermakov.nikita.entity.profile.Profile;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/**
 * created by Nikita_Ermakov at 2/19/2020
 */
@Repository("profileRepository")
public interface ProfileRepository extends CrudRepository<Profile, Long> {

    @Override
    <S extends Profile> S save(S entity);
}
