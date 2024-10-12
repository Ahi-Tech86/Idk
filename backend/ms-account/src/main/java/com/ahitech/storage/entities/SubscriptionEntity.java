package com.ahitech.storage.entities;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "subs",
        indexes = {
            @Index(name = "idx_follower_id", columnList = "follower_id"),
            @Index(name = "idx_followed_id", columnList = "followed_id")
        }
)
public class SubscriptionEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @Column(name = "follower_id", nullable = false)
    private Long followerId;

    @ManyToOne
    @Column(name = "followed_id", nullable = false)
    private Long followedId;
}
