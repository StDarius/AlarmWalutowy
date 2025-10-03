package com.example.dataprovider.config;

import com.example.dataprovider.security.JwtAuthFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.core.env.Profiles;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
public class SecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http,
                                           JwtAuthFilter jwtAuthFilter,
                                           Environment env) throws Exception {

        boolean h2Enabled = env.getProperty("spring.h2.console.enabled", Boolean.class, false);
        boolean isDev = env.acceptsProfiles(Profiles.of("dev"));
        AntPathRequestMatcher h2Matcher = new AntPathRequestMatcher("/h2-console/**");

        http
                .csrf(csrf -> csrf.disable())
                .sessionManagement(sm -> sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> {
                    if (h2Enabled) {
                        auth.requestMatchers(h2Matcher).permitAll();
                    }
                    if (isDev) {
                        // Allow your helper endpoint only on the dev profile
                        auth.requestMatchers("/dev/**").permitAll();
                    }
                    auth
                            .requestMatchers(
                                    "/swagger-ui.html",
                                    "/swagger-ui/**",
                                    "/v3/api-docs/**",
                                    "/swagger-resources/**",
                                    "/webjars/**",
                                    "/api/auth/**",
                                    "/api/status",
                                    "/error"
                            ).permitAll()
                            .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll() // (optional) CORS preflight
                            .requestMatchers(HttpMethod.GET, "/api/public/**").permitAll()
                            .requestMatchers(HttpMethod.GET, "/api/users/me").authenticated()
                            .anyRequest().authenticated();
                });

        if (h2Enabled) {
            // Needed for H2 console frames
            http.headers(h -> h.frameOptions(f -> f.sameOrigin()));
        }

        http.addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }
}
