// com.example.dataprovider.api.DebugController
package com.example.dataprovider.api;

import com.example.dataprovider.model.User;
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
        notificationService.sendTestEmail(user);
        return ResponseEntity.ok().build();
    }
}
