package com.chat.chatAppln.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.http.HttpMethod;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    	http
        .csrf(csrf -> csrf.disable())
        .authorizeHttpRequests(auth -> auth
        	.requestMatchers("/chat-ws/**", "/chat-ws/info/**").permitAll()
        	.requestMatchers("/api/**", "/error").permitAll()
        	.requestMatchers("/api/auth/**").permitAll()
        	.requestMatchers("/api/messages/**").permitAll()
            .requestMatchers("/api/groups/**").permitAll()
            .requestMatchers("/api/group-messages/**").permitAll()
            .requestMatchers(HttpMethod.POST, "/api/auth/login").permitAll()
            .requestMatchers(HttpMethod.POST, "/api/messages/personal-chat").permitAll()
            .requestMatchers(HttpMethod.POST, "/api/messages/chat").permitAll()
            .requestMatchers(HttpMethod.POST, "/api/groups/create").permitAll()
            .requestMatchers(HttpMethod.POST, "/api/group-messages/send").permitAll()
            
//            .requestMatchers("/error").permitAll()
//            .requestMatchers("/chat-ws/**").permitAll()
            .anyRequest().authenticated()
        )
        .formLogin(form -> form.disable())
        .httpBasic(basic -> basic.disable())
        .cors(cors -> {});


    	// ✅ completely ignore WebSocket paths from security filter chain
//        http.securityMatcher(new AntPathRequestMatcher("/chat-ws/**"));
        return http.build();
    }
    
    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return (web) -> web.ignoring().requestMatchers("/chat-ws/**", "/chat-ws/info/**");
    }
}
