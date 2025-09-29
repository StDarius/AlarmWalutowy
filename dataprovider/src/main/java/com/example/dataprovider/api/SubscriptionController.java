
package com.example.dataprovider.api;

import com.example.dataprovider.model.Subscription;
import com.example.dataprovider.model.User;
import com.example.dataprovider.repo.UserRepository;
import com.example.dataprovider.service.SubscriptionService;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/subscriptions")
public class SubscriptionController {

    private final SubscriptionService service;
    private final UserRepository userRepo;

    public SubscriptionController(SubscriptionService service, UserRepository userRepo) {
        this.service = service;
        this.userRepo = userRepo;
    }

    public static class CreateRequest {
        @NotBlank public String currency;
        @DecimalMin("0.0")
        public BigDecimal thresholdPercent;
    }

    @PostMapping
    public ResponseEntity<Subscription> create(@AuthenticationPrincipal UserDetails principal,
                                               @RequestBody CreateRequest req) {
        User user = userRepo.findByUsername(principal.getUsername()).orElseThrow();
        Subscription s = service.create(user, req.currency, req.thresholdPercent);
        return ResponseEntity.ok(s);
    }

    @GetMapping
    public ResponseEntity<List<Subscription>> list(@AuthenticationPrincipal org.springframework.security.core.userdetails.UserDetails principal) {
        return ResponseEntity.ok(service.listForUsername(principal.getUsername()));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
