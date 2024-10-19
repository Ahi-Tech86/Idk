package com.ahitech.services.interfaces;

public interface SubscriptionService {
    void subscribe(Long userId, Long subscriberId);
    void unsubscribe(Long userId, Long unsubscriberId);
}
