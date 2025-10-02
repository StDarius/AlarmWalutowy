package com.example.dataprovider.repo;

import com.example.dataprovider.model.Subscription;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface SubscriptionRepository extends JpaRepository<Subscription, Long> {

    List<Subscription> findByUserUsername(String username);

    List<Subscription> findByCurrency(String currency);

    List<Subscription> findByUserId(Long id);

    List<Subscription> findByCurrencyIgnoreCase(String currency);
}