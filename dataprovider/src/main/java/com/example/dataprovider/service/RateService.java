package com.example.dataprovider.service;

import com.example.dataprovider.model.RateHistory;
import com.example.dataprovider.model.Subscription;
import com.example.dataprovider.model.User;
import com.example.dataprovider.repo.RateHistoryRepository;
import com.example.dataprovider.repo.SubscriptionRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.Optional;

@Service
public class RateService {

    private final RateHistoryRepository rateRepo;
    private final SubscriptionRepository subRepo;
    private final NotificationService notificationService;

    public RateService(RateHistoryRepository rateRepo,
                       SubscriptionRepository subRepo,
                       NotificationService notificationService) {
        this.rateRepo = rateRepo;
        this.subRepo = subRepo;
        this.notificationService = notificationService;
    }

    /** Used by listeners when they only know base/quote/rate/timestamp */
    public void saveTick(String base, String quote, BigDecimal rate, Instant ts) {
        // no changePercent computed here; record with null
        record(base, quote, rate, null, ts);
    }

    /** Full record that can include a precomputed percent change */
    public void record(String base, String quote, BigDecimal rate, BigDecimal changePercent, Instant ts) {
        RateHistory rh = new RateHistory();
        rh.setBaseCurrency(base);
        rh.setQuoteCurrency(quote);
        rh.setRate(rate);
        rh.setChangePercent(changePercent); // can be null
        rh.setTimestamp(ts);
        rateRepo.save(rh);


        if (changePercent != null) {
            List<Subscription> subs = subRepo.findByCurrencyIgnoreCase(quote);
            for (Subscription s : subs) {
                if (changePercent.compareTo(s.getThresholdPercent()) >= 0) {
                    User user = s.getUser();
                    String subject = "Currency Alert: " + base + "/" + quote;
                    String body = "Rate changed by " + changePercent + "%, new rate = " + rate + " at " + ts;
                    notificationService.notifyByEmail(user, subject, body);
                }
            }
        }
    }

    /** Returns the latest stored rate for the pair (used as “previous” for the next tick). */
    public Optional<BigDecimal> findPreviousRate(String base, String quote) {
        return rateRepo.findTopByBaseCurrencyAndQuoteCurrencyOrderByTimestampDesc(base, quote)
                .map(RateHistory::getRate);
    }

    public List<RateHistory> last50(String base, String quote) {
        return rateRepo.findTop50ByBaseCurrencyAndQuoteCurrencyOrderByTimestampDesc(base, quote);
    }
}
