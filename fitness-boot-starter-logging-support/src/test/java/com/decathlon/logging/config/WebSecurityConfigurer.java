/* Decathlon (C)2023 */
package com.decathlon.logging.config;

import static org.springframework.security.config.Customizer.withDefaults;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.servlet.util.matcher.MvcRequestMatcher;
import org.springframework.web.servlet.handler.HandlerMappingIntrospector;

@Configuration
@EnableWebSecurity
public class WebSecurityConfigurer {

    @Bean
    InMemoryUserDetailsManager userDetailsService() {
        @SuppressWarnings("deprecation")
        UserDetails user =
                User.withDefaultPasswordEncoder()
                        .username("Harold Da Costa")
                        .password("secret")
                        .roles("USER")
                        .build();

        return new InMemoryUserDetailsManager(user);
    }

    @Bean
    MvcRequestMatcher.Builder mvc(HandlerMappingIntrospector introspector) {
        return new MvcRequestMatcher.Builder(introspector);
    }

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http, MvcRequestMatcher.Builder mvc)
            throws Exception {
        return http.csrf(e -> e.disable())
                .authorizeHttpRequests(
                        e -> e.requestMatchers(mvc.pattern("/api/v1/**")).permitAll())
                .httpBasic(withDefaults())
                .build();
    }
}
