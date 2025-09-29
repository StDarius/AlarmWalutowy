
package com.example.dataprovider.api;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class StatusController {
    @GetMapping("/api/status")
    public Map<String, Object> status() {
        return Map.of("service", "dataprovider", "ok", true);
    }
}
