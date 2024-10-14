package com.ahitech.storage.entities;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(
        name = "subs",
        indexes = {
            @Index(name = "idx_follower_id", columnList = "follower_id"),
            @Index(name = "idx_followed_id", columnList = "followed_id")
        }
)
public class SubscriptionEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(
            name = "follower_id", nullable = false,
            referencedColumnName = "id", foreignKey = @ForeignKey(name = "FK_follower")
    )
    private AccountEntity follower;

    @ManyToOne(optional = false)
    @JoinColumn(
            name = "followed_id", nullable = false,
            referencedColumnName = "id", foreignKey = @ForeignKey(name = "FK_followed")
    )
    private AccountEntity followed;
}
