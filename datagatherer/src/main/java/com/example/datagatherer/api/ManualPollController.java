package com.example.datagatherer.api;

import com.example.datagatherer.service.RatePollingService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/status")
public class ManualPollController {

    private static final Logger log = LoggerFactory.getLogger(ManualPollController.class);
    private final RatePollingService pollingService;

    public ManualPollController(RatePollingService pollingService) {
        this.pollingService = pollingService;
    }

    @PostMapping("/poll")
    public ResponseEntity<?> pollNow() {
        try {
            pollingService.poll();
            return ResponseEntity.ok(Map.of("polled", true));
        } catch (Exception e) {
            log.error("Manual poll failed", e);
            return ResponseEntity.internalServerError().body(Map.of("polled", false, "error", e.getMessage()));
        }
    }
}
