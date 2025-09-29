
package com.example.dataprovider.service;

import com.example.dataprovider.model.Subscription;
import com.example.dataprovider.model.User;
import com.example.dataprovider.repo.SubscriptionRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
public class SubscriptionService {

    private final SubscriptionRepository repo;

    public SubscriptionService(SubscriptionRepository repo) {
        this.repo = repo;
    }

    public Subscription create(User user, String currency, BigDecimal thresholdPercent) {
        Subscription s = new Subscription();
        s.setUser(user);
        s.setCurrency(currency);
        s.setThresholdPercent(thresholdPercent);
        return repo.save(s);
    }

    public Subscription save(Subscription sub) {
        return repo.save(sub);
    }

    public Optional<Subscription> findById(Long id) {
        return repo.findById(id);
    }

    public List<Subscription> findByCurrency(String currency) {
        return repo.findByCurrencyIgnoreCase(currency);
    }

    public List<Subscription> findAll() {
        return repo.findAll();
    }

    public void delete(Long id) {
        repo.deleteById(id);
    }

    public List<Subscription> listForUsername(String username) {
        return repo.findByUserUsername(username);
    }




}
