package com.ahitech.storage.entities;

import com.ahitech.storage.enums.Country;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "application_account")
public class AccountEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(unique = true, nullable = false)
    private String email;

    @Column(unique = true, nullable = false)
    private String nickname;

    @Column(length = 50, nullable = false)
    private String firstname;

    @Column(length = 50, nullable = false)
    private String lastname;

    @Column(length = 300)
    private String bio;

    @Column(name = "image_url")
    private String profileImageUrl;

    @Column(name = "create_at", nullable = false)
    private Date createAt;

    @Builder.Default
    @Column(nullable = false)
    private boolean isPrivate = false;

    @Builder.Default
    @Column(name = "followers_num", nullable = false)
    private Long followersCount = 0L;

    @Builder.Default
    @Column(name = "following_num", nullable = false)
    private Long followingCount = 0L;

    @Enumerated(EnumType.STRING)
    private Country country;

    @Column(length = 30)
    private String city;

    @Column(length = 75)
    private String website;

    @OneToMany(mappedBy = "follower", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<SubscriptionEntity> followers = new ArrayList<>();

    @OneToMany(mappedBy = "followed", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<SubscriptionEntity> following = new ArrayList<>();
}
