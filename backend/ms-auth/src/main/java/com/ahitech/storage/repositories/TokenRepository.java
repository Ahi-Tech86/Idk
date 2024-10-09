package com.ahitech.storage.repositories;

import com.ahitech.storage.entities.RefreshTokenEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.sql.Ref;
import java.util.Optional;

public interface TokenRepository extends JpaRepository<RefreshTokenEntity, Long> {
    Optional<RefreshTokenEntity> findById(Long id);

    @Query("SELECT rt FROM RefreshTokenEntity rt WHERE rt.user.email = :email")
    Optional<RefreshTokenEntity> findByEmail(@Param("email") String email);
}
