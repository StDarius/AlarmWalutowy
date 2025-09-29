
package com.example.dataprovider.service;

import com.example.dataprovider.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class NotificationService {
    private static final Logger log = LoggerFactory.getLogger(NotificationService.class);

    private final JavaMailSender mailSender;

    public NotificationService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    public void notifyByEmail(User user, String subject, String body) {
        try {
            if (user.getEmail() == null || user.getEmail().isBlank()) {
                log.info("Notification to {}: {}", user.getUsername(), body);
                return;
            }
            SimpleMailMessage msg = new SimpleMailMessage();
            msg.setTo(user.getEmail());
            msg.setSubject(subject);
            msg.setText(body);
            mailSender.send(msg);
        } catch (Exception ex) {
            log.info("Notification (fallback) to {}: {}", user.getUsername(), body);
        }
    }
}
