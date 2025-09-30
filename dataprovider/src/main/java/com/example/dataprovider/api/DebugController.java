// src/main/java/com/example/dataprovider/api/DebugController.java
package com.example.dataprovider.api;

import com.example.dataprovider.repo.UserRepository;
import com.example.dataprovider.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/debug")
@RequiredArgsConstructor
public class DebugController {

    private final NotificationService notificationService;
    private final UserRepository userRepo;

    @PostMapping("/mail")
    public ResponseEntity<Void> sendMail(@AuthenticationPrincipal UserDetails principal) {
        var user = userRepo.findByUsername(principal.getUsername()).orElseThrow();

        notificationService.sendThresholdExceeded(
                user, /* sub = */ null,
                "USD", "EUR",
                3.80, 3.90, 2.63
        );
        return ResponseEntity.ok().build();
    }
}
