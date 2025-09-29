
package com.example.datagatherer.service;

import com.example.common.RateUpdateMessage;

public interface RatePublisher {
    void publish(RateUpdateMessage msg);
}
