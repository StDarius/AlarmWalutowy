package com.example.dataprovider.service;

import com.example.dataprovider.model.Subscription;
import com.example.dataprovider.model.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.lang.Nullable;          // <-- add this import
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class NotificationService {

    private final JavaMailSender mailSender;

    @Value("${app.notifications.enabled:true}")
    private boolean enabled;

    @Value("${app.notifications.from:no-reply@alarm-walutowy.local}")
    private String from;

    /** Generic mail helper some places already use */
    public void notifyByEmail(User user, String subject, String body) {
        if (!enabled) {
            log.info("Notifications disabled; skip email to {}", user.getEmail());
            return;
        }
        if (user.getEmail() == null || user.getEmail().isBlank()) {
            log.warn("User {} has no email; skip notification", user.getUsername());
            return;
        }

        SimpleMailMessage msg = new SimpleMailMessage();
        msg.setFrom(from);
        msg.setTo(user.getEmail());
        msg.setSubject(subject);
        msg.setText(body);

        mailSender.send(msg);
        log.info("Sent custom email to {}", user.getEmail());
    }

    /** Main notification used by the listener; now null-safe for 'sub'. */
    public void sendThresholdExceeded(
            User user,
            @Nullable Subscription sub,   // <-- allow null
            String base,
            String quote,
            double oldRate,
            double newRate,
            double changePercent
    ) {
        if (!enabled) {
            log.info("Notifications disabled; skip email to {}", user.getEmail());
            return;
        }
        if (user.getEmail() == null || user.getEmail().isBlank()) {
            log.warn("User {} has no email; skip notification", user.getUsername());
            return;
        }

        String subCurrency = (sub != null && sub.getCurrency() != null) ? sub.getCurrency() : quote;
        double threshold = (sub != null && sub.getThresholdPercent() != null)
                ? sub.getThresholdPercent().doubleValue()
                : changePercent; // for debug mails show current change as “threshold”

        String subject = String.format("Currency Alert: %s/%s changed by %.2f%%", base, quote, changePercent);
        String body = String.format(
                "Hi %s,%n%n" +
                        "Your %s for %s (threshold %.2f%%) was triggered.%n" +
                        "Pair: %s/%s%n" +
                        "Old rate: %.6f%n" +
                        "New rate: %.6f%n" +
                        "Change: %.2f%%%n%n" +
                        "-- Alarm Walutowy",
                user.getUsername(),
                (sub == null ? "DEBUG notification" : "subscription"),
                subCurrency, threshold,
                base, quote, oldRate, newRate, changePercent
        );

        SimpleMailMessage msg = new SimpleMailMessage();
        msg.setFrom(from);
        msg.setTo(user.getEmail());
        msg.setSubject(subject);
        msg.setText(body);

        mailSender.send(msg);
        log.info("Sent alert email to {}", user.getEmail());
    }

    /** Convenience used by /api/debug/mail */
    public void sendTestEmail(User user) {
        sendThresholdExceeded(user, null, "USD", "EUR", 3.80, 3.90, 2.63);
    }
}
