package org.example.testhosting;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@RequiredArgsConstructor
public class SecurityConfig {

    @Autowired
    private BlockIpFilter blockIpFilter;

//    private final ExtensionBannerFilter extensionBannerFilter;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
//                .addFilterBefore(blockIpFilter, UsernamePasswordAuthenticationFilter.class)
//                // ✅ الفلتر بتاع البانر هيشتغل بس على HTML pages
//                .addFilterAfter(extensionBannerFilter, UsernamePasswordAuthenticationFilter.class)
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/extension/**").permitAll()
                        .anyRequest().permitAll()
                );

        return http.build();
    }
}