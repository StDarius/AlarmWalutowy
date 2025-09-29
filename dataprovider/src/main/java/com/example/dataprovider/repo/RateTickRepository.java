package com.example.dataprovider.repo;

import com.example.dataprovider.model.RateTick;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RateTickRepository extends JpaRepository<RateTick, Long> {
    Optional<RateTick> findTopByBaseCurrencyAndQuoteCurrencyOrderByTimestampDesc(String base, String quote);
}
