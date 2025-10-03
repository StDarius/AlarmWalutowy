
package com.example.dataprovider.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.Instant;

@Setter
@Getter
@Entity
@Table(name = "rate_history")
public class RateHistory {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name="base_currency", nullable=false, length=3)
    private String baseCurrency;

    @Column(name="quote_currency", nullable=false, length=3)
    private String quoteCurrency;

    @Column(name="rate", nullable=false, precision=18, scale=6)
    private BigDecimal rate;

    @Column(name="change_percent", precision=18, scale=6)
    private BigDecimal changePercent;          // <-- add this

    @Column(name="timestamp", nullable=false)
    private Instant timestamp;
}
