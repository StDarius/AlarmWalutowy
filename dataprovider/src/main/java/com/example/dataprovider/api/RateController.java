package com.example.dataprovider.api;

import com.example.dataprovider.model.RateHistory;
import com.example.dataprovider.service.RateService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/rates")
public class RateController {

    private final RateService rateService;

    public RateController(RateService rateService) {
        this.rateService = rateService;
    }

    @Operation(summary = "Return last 50 points for the given currency pair")
    @GetMapping("/{base}/{quote}/last50")
    public ResponseEntity<List<RateHistory>> last50(
            @PathVariable("base") String base,
            @PathVariable("quote") String quote) {
        return ResponseEntity.ok(rateService.last50(base, quote));
    }
}
