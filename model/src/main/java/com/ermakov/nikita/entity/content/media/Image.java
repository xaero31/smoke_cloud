package com.ermakov.nikita.entity.content.media;

import com.ermakov.nikita.entity.content.Post;
import lombok.Data;

import javax.persistence.*;

/**
 * created by Nikita_Ermakov at 7/20/20
 */
@Data
@Entity
@Table(name = "image")
public class Image {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", unique = true, nullable = false, updatable = false)
    private long id;

    @Column(name = "path", unique = true, nullable = false, updatable = false)
    private String path;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id")
    private Post post;
}
