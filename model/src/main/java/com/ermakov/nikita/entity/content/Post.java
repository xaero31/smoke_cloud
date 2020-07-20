package com.ermakov.nikita.entity.content;

import com.ermakov.nikita.entity.content.media.Image;
import com.ermakov.nikita.entity.profile.Profile;
import lombok.Data;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

/**
 * created by Nikita_Ermakov at 7/20/20
 */
@Data
@Entity
@Table(name = "post")
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", unique = true, nullable = false, updatable = false)
    private long id;

    @Column(name = "title", nullable = false)
    private String title;

    @Lob
    @Column(name = "text")
    private String text;

    @Column(name = "date", nullable = false, updatable = false)
    private Date date;

    @OneToMany(mappedBy = "post", cascade = CascadeType.REMOVE, fetch = FetchType.LAZY, orphanRemoval = true)
    private List<Image> images;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "profile_id")
    private Profile profile;
}
