package com.ahitech.storage.entities;

import jakarta.persistence.*;
import lombok.*;

import java.util.Date;

@Entity
@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "refresh_token")
public class RefreshTokenEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(optional = false)
    @JoinColumn(name = "user_id", referencedColumnName = "id", nullable = false)
    private UserEntity user;

    @Column(length = 1024, nullable = false)
    private String token;

    @Column(name = "create_at", nullable = false)
    private Date createAt;

    @Column(name = "expires_at", nullable = false)
    private Date expiresAt;
}
