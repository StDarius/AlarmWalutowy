
package com.example.dataprovider.repo;

import com.example.dataprovider.model.RateHistory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RateHistoryRepository extends JpaRepository<RateHistory, Long> {
    List<RateHistory> findTop50ByBaseCurrencyAndQuoteCurrencyOrderByTimestampDesc(String base, String quote);
}
