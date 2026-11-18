
package com.example.purchase.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;

@Configuration
@Profile("dev")
public class DevUsersConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

    @Bean
    public UserDetailsService userDetailsService(PasswordEncoder passwordEncoder) {

        UserDetails requester = User.builder()
                .username("requester")
                .password(passwordEncoder.encode("requester-dev"))
                .roles("REQUESTER")
                .build();

        UserDetails approver = User.builder()
                .username("approver")
                .password(passwordEncoder.encode("approver-dev"))
                .roles("APPROVER")
                .build();

        return new InMemoryUserDetailsManager(requester, approver);
    }
}
