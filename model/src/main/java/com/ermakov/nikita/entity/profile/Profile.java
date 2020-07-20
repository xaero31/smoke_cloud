package com.ermakov.nikita.entity.profile;

import com.ermakov.nikita.entity.content.Post;
import com.ermakov.nikita.entity.content.media.Image;
import com.ermakov.nikita.entity.security.User;
import lombok.Data;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

/**
 * created by Nikita_Ermakov at 2/16/2020
 */
@Data
@Entity
@Table(name = "profile")
public class Profile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", unique = true, nullable = false, updatable = false)
    private long id;

    @OneToOne(cascade = CascadeType.REMOVE, orphanRemoval = true, fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", unique = true, nullable = false, updatable = false)
    private User user;

    @Column(name = "first_name", nullable = false)
    private String firstName;
    @Column(name = "last_name", nullable = false)
    private String lastName;
    @Column(name = "middle_name")
    private String middleName;
    @Column(name = "city")
    private String city;
    @Column(name = "hookah_model")
    private String hookahModel;
    @Column(name = "favorite_hookah")
    private String favoriteHookah;
    @Column(name = "favorite_tobacco")
    private String favoriteTobacco;
    @Column(name = "favorite_tobacco_flavor")
    private String favoriteTobaccoFlavor;
    @Column(name = "favorite_coal")
    private String favoriteCoal;
    @Column(name = "favorite_hookah_bar")
    private String favoriteHookahBar;

    @Column(name = "is_city_public", nullable = false)
    private boolean cityPublic;
    @Column(name = "is_birth_date_public", nullable = false)
    private boolean birthDatePublic;

    @Column(name = "birth_date", nullable = false)
    private Date birthDate;

    @OneToOne(cascade = CascadeType.REMOVE, orphanRemoval = true)
    @JoinColumn(name = "avatar_image_id", unique = true)
    private Image avatar;

    @OneToOne(cascade = CascadeType.REMOVE, orphanRemoval = true)
    @JoinColumn(name = "hookah_image_id", unique = true)
    private Image hookah;

    @OneToMany(mappedBy = "profile", cascade = CascadeType.REMOVE, fetch = FetchType.LAZY, orphanRemoval = true)
    private List<Post> posts;
}
