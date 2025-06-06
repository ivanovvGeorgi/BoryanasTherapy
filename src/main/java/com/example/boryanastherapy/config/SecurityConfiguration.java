package com.example.boryanastherapy.config;

import jakarta.servlet.DispatcherType;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(auth -> auth
                        // Handle error dispatches first
                        .dispatcherTypeMatchers(DispatcherType.ERROR).permitAll()
                        // Static resources
                        .requestMatchers(PathRequest.toStaticResources().atCommonLocations()).permitAll()
                        .requestMatchers("/uploads/**").permitAll()
                        // Admin pages require ADMIN role
                        .requestMatchers("/admin/**").hasRole("ADMIN")
                        .requestMatchers("/admin/articles/**").hasRole("ADMIN")
                        // Error paths
                        .requestMatchers(
                                new AntPathRequestMatcher("/error/**"),
                                new AntPathRequestMatcher("/error")
                        ).permitAll()
                        // Public pages
                        .requestMatchers(
                                "/",
                                "/index",
                                "/css/**",
                                "/js/**",
                                "/images/**",
                                "/book/**",
                                "/confirmation",
                                "/book",
                                "/contact",
                                "/about",
                                "/privacy",
                                "/terms",
                                "/cookies",
                                "/send-message",
                                "/public/**",
                                "/services/**",
                                "/services",
                                "/blog/**"
                        ).permitAll()
                        // Keep this as authenticated for security
                        .anyRequest().authenticated()
                )
                .formLogin(login -> login
                        .loginPage("/admin/login")
                        .defaultSuccessUrl("/admin/control-panel", true)
                        .permitAll()
                )
                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/")
                )
                .csrf(AbstractHttpConfigurer::disable);

        return http.build();
    }

    @Bean
    public UserDetailsService userDetailsService() {
        InMemoryUserDetailsManager manager = new InMemoryUserDetailsManager();
        manager.createUser(User.withUsername("boryana")
                .password("{noop}zafirova")
                .roles("ADMIN")
                .build());
        return manager;
    }
}