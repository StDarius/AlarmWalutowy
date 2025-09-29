package com.example.dataprovider.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.Instant;

@Entity
@Table(name = "rate_ticks", indexes = {
        @Index(name = "idx_pair_ts", columnList = "baseCurrency,quoteCurrency,timestamp")
})
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class RateTick {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String baseCurrency;
    private String quoteCurrency;
    private double rate;
    private Instant timestamp;
}
