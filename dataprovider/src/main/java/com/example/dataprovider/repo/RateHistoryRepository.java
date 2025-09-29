
package com.example.dataprovider.repo;

import com.example.dataprovider.model.RateHistory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface RateHistoryRepository extends JpaRepository<RateHistory, Long> {

    List<RateHistory> findTop50ByBaseCurrencyAndQuoteCurrencyOrderByTimestampDesc(String base, String quote);
    Optional<RateHistory> findTopByBaseCurrencyAndQuoteCurrencyOrderByTimestampDesc(String base, String quote);
}