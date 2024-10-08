package com.ahitech.storage.repositories;

import com.ahitech.storage.entities.RefreshTokenEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.sql.Ref;
import java.util.Optional;

public interface TokenRepository extends JpaRepository<RefreshTokenEntity, Long> {
    Optional<RefreshTokenEntity> findById(Long id);
}
