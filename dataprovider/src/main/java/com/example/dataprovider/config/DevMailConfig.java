// dataprovider/src/main/java/com/example/dataprovider/config/DevMailConfig.java
package com.example.dataprovider.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

import jakarta.mail.internet.MimeMessage;

@Configuration
@Profile("dev")
public class DevMailConfig {

    @Bean
    public JavaMailSender javaMailSender() {
        return new JavaMailSender() {
            @Override public MimeMessage createMimeMessage() { return new MimeMessage((jakarta.mail.Session) null); }
            @Override public MimeMessage createMimeMessage(java.io.InputStream contentStream) { return createMimeMessage(); }
            @Override public void send(MimeMessage mimeMessage) { /* no-op */ }
            @Override public void send(MimeMessage... mimeMessages) { /* no-op */ }
            @Override public void send(org.springframework.mail.javamail.MimeMessagePreparator mimeMessagePreparator) { /* no-op */ }
            @Override public void send(org.springframework.mail.javamail.MimeMessagePreparator... mimeMessagePreparators) { /* no-op */ }
            @Override public void send(SimpleMailMessage simpleMessage) { /* no-op */ }
            @Override public void send(SimpleMailMessage... simpleMessages) { /* no-op */ }
        };
    }
}
