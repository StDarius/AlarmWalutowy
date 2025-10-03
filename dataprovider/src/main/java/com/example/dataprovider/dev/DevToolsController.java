package com.example.dataprovider.dev;

import com.example.common.RateUpdateMessage;
import com.example.dataprovider.messaging.RateUpdateListener;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.Map;

@Profile("dev")
@RestController
@RequestMapping("/dev")
@RequiredArgsConstructor
public class DevToolsController {

    private final RateUpdateListener rateUpdateListener;

    @PostMapping("/emit-rate")
    public ResponseEntity<Void> emit(@RequestBody DevEmitRateRequest body) {
        RateUpdateMessage msg = new RateUpdateMessage();
        msg.setBase(body.base());
        msg.setQuote(body.quote());
        msg.setRate(body.rate());

        rateUpdateListener.onMessage(msg);
        return ResponseEntity.accepted().build();
    }
}
