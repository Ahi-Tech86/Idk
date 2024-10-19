package com.ahitech.services;

import com.ahitech.exception.AppException;
import com.ahitech.services.interfaces.SubscriptionService;
import com.ahitech.storage.entities.AccountEntity;
import com.ahitech.storage.entities.SubscriptionEntity;
import com.ahitech.storage.repositories.AccountRepository;
import com.ahitech.storage.repositories.SubscriptionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class SubscriptionServiceImpl implements SubscriptionService {

    private final AccountRepository accountRepository;
    private final SubscriptionRepository subscriptionRepository;

    @Override
    @Transactional
    public void subscribe(Long userId, Long subscriberId) {
        AccountEntity account = isAccountExistsByUserId(userId);
        AccountEntity subscriberAccount = isAccountExistsByUserId(subscriberId);

        SubscriptionEntity subscription = SubscriptionEntity.builder()
                .follower(account)
                .followed(subscriberAccount)
                .build();

        subscriptionRepository.save(subscription);
        log.info("Successfully saved subscription entity");
    }

    @Override
    @Transactional
    public void unsubscribe(Long userId, Long unsubscriberId) {
        AccountEntity account = isAccountExistsByUserId(userId);
        AccountEntity unsubscriberAccount = isAccountExistsByUserId(unsubscriberId);

        subscriptionRepository.deleteByFollowerAndFollowed(account, unsubscriberAccount);
        log.info("Successfully unsubscribed operation for account {}", unsubscriberAccount.getNickname());
    }

    private AccountEntity isAccountExistsByUserId(Long userId) {
        Optional<AccountEntity> optionalAccount = accountRepository.findByUserId(userId);

        if (optionalAccount.isEmpty()) {
            log.error("An attempt to perform any actions with a non-existent nickname: {}", userId);
            throw new AppException(
                    String.format("Account with %s nickname doesn't exists", userId), HttpStatus.BAD_REQUEST
            );
        } else {
            return optionalAccount.get();
        }
    }
}
