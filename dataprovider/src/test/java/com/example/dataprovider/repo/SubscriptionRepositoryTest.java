package com.example.dataprovider.repo;

import com.example.dataprovider.model.Subscription;
import com.example.dataprovider.model.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class SubscriptionRepositoryTest {

    @Autowired
    private SubscriptionRepository subscriptionRepository;

    @Autowired
    private UserRepository userRepository;

    @Test
    void shouldPersistAndFindByUser() {
        // given a user
        User u = new User();
        u.setUsername("repo_test_user");
        u.setPasswordHash("x");
        u.setEmail("repo@test.local");
        userRepository.save(u);

        // and a subscription
        Subscription s = new Subscription();
        s.setUser(u);
        s.setCurrency("USD");
        s.setThresholdPercent(BigDecimal.valueOf(1.5));
        subscriptionRepository.save(s);

        // when
        List<Subscription> subs = subscriptionRepository.findByUserId(u.getId());

        // then
        assertThat(subs).isNotEmpty();
        assertThat(subs.get(0).getCurrency()).isEqualTo("USD");
        assertThat(subs.get(0).getThresholdPercent()).isEqualByComparingTo("1.5");
        assertThat(subs.get(0).getUser().getId()).isEqualTo(u.getId());
    }
}
