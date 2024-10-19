package com.ahitech.storage.repositories;

import com.ahitech.storage.entities.AccountEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AccountRepository extends JpaRepository<AccountEntity, Long> {
    Optional<AccountEntity> findByNickname(String nickname);
    Optional<AccountEntity> findByUserId(Long userId);
}
