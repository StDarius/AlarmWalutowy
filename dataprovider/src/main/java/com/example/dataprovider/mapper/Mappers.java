package com.example.dataprovider.mapper;

import com.example.dataprovider.dto.*;
import com.example.dataprovider.model.*;

import java.math.BigDecimal;

public final class Mappers {
    private Mappers() {}

    public static UserDTO toDTO(User u) {
        return new UserDTO(u.getId(), u.getUsername(), u.getEmail());
    }

    public static SubscriptionDTO toDTO(Subscription s) {
        return new SubscriptionDTO(s.getId(), s.getCurrency(), s.getThresholdPercent());
    }

    public static RateTickDTO toDTO(RateTick t) {
        return new RateTickDTO(
                t.getBaseCurrency(),
                t.getQuoteCurrency(),
                BigDecimal.valueOf(t.getRate()),
                t.getTimestamp()
        );
    }
}

