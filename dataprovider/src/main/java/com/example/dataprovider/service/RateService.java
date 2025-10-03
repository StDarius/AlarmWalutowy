package com.example.dataprovider.service;

import com.example.dataprovider.model.RateHistory;
import com.example.dataprovider.model.Subscription;
import com.example.dataprovider.model.User;
import com.example.dataprovider.repo.RateHistoryRepository;
import com.example.dataprovider.repo.SubscriptionRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Instant;
import java.util.List;
import java.util.Optional;

@Service
public class RateService {

    private static final int PCT_SCALE = 4;
    private static final int DIV_SCALE = 8;

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

    public void saveTick(String base, String quote, BigDecimal rate, Instant ts) {
        saveTick(base, quote, rate, ts, null);
    }


    public void saveTick(String base,
                         String quote,
                         BigDecimal rate,
                         Instant ts,
                         BigDecimal changePct) {

        // Look up previous rate once (used for optional calc and duplicate guard)
        Optional<BigDecimal> prevOpt = findPreviousRate(base, quote);

        // If the new value is identical to the previous, skip writing a duplicate row
        if (prevOpt.isPresent() && rate.compareTo(prevOpt.get()) == 0) {
            return;
        }

        BigDecimal effectiveChange = changePct;

        if (effectiveChange == null) {
            if (prevOpt.isPresent() && prevOpt.get().signum() != 0) {
                BigDecimal prev = prevOpt.get();
                effectiveChange = rate.subtract(prev)
                        .divide(prev, DIV_SCALE, RoundingMode.HALF_UP)
                        .multiply(BigDecimal.valueOf(100))
                        .abs()
                        .setScale(PCT_SCALE, RoundingMode.HALF_UP);
            } else {

                effectiveChange = null;
            }
        } else {

            effectiveChange = effectiveChange.setScale(PCT_SCALE, RoundingMode.HALF_UP);
        }

        record(base, quote, rate, effectiveChange, ts);
    }

    public void record(String base,
                       String quote,
                       BigDecimal rate,
                       BigDecimal changePercent,
                       Instant ts) {

        RateHistory rh = new RateHistory();
        rh.setBaseCurrency(base);
        rh.setQuoteCurrency(quote);
        rh.setRate(rate);
        rh.setChangePercent(changePercent);
        rh.setTimestamp(java.time.Instant.now());
        rateRepo.save(rh);


        if (changePercent != null) {
            List<Subscription> subs = subRepo.findByCurrencyIgnoreCase(quote);
            for (Subscription s : subs) {
                BigDecimal threshold = s.getThresholdPercent();
                if (threshold == null) continue;
                if (changePercent.compareTo(threshold) >= 0) {
                    User user = s.getUser();
                    String subject = "Currency Alert: " + base + "/" + quote;
                    String body = "Rate changed by " + changePercent + "%, new rate = "
                            + rate + " at " + ts;
                    notificationService.notifyByEmail(user, subject, body);
                }
            }
        }
    }


    public Optional<BigDecimal> findPreviousRate(String base, String quote) {
        return rateRepo.findTopByBaseCurrencyAndQuoteCurrencyOrderByTimestampDesc(base, quote)
                .map(RateHistory::getRate);
    }

    public List<RateHistory> last50(String base, String quote) {
        return rateRepo.findTop50ByBaseCurrencyAndQuoteCurrencyOrderByTimestampDesc(base, quote);
    }
}
