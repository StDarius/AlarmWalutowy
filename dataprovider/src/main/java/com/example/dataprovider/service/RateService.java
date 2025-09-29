
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

@Service
public class RateService {

    private final RateHistoryRepository rateRepo;
    private final SubscriptionRepository subRepo;
    private final NotificationService notificationService;

    public RateService(RateHistoryRepository rateRepo, SubscriptionRepository subRepo, NotificationService notificationService) {
        this.rateRepo = rateRepo;
        this.subRepo = subRepo;
        this.notificationService = notificationService;
    }

    public void record(String base, String quote, BigDecimal rate, BigDecimal changePercent, Instant ts) {
        RateHistory rh = new RateHistory();
        rh.setBaseCurrency(base);
        rh.setQuoteCurrency(quote);
        rh.setRate(rate);
        rh.setChangePercent(changePercent);
        rh.setTimestamp(ts);
        rateRepo.save(rh);

        List<Subscription> subs = subRepo.findByCurrency(quote);
        for (Subscription s : subs) {
            if (changePercent == null) continue;
            if (changePercent.compareTo(s.getThresholdPercent()) >= 0) {
                User user = s.getUser();
                String subject = "Currency Alert: " + base + "/" + quote;
                String body = "Rate changed by " + changePercent + "%, new rate = " + rate + " at " + ts;
                notificationService.notifyByEmail(user, subject, body);
            }
        }
    }

    public List<RateHistory> last50(String base, String quote) {
        return rateRepo.findTop50ByBaseCurrencyAndQuoteCurrencyOrderByTimestampDesc(base, quote);
    }
}
