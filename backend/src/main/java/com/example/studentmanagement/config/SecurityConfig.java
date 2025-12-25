package com.example.studentmanagement.config;

import com.example.studentmanagement.service.UserDetailsServiceImpl;
import com.example.studentmanagement.service.LoginCacheService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import jakarta.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.http.HttpMethod;

import java.util.Arrays;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    // Use the application-provided UserDetailsService bean (via @Service) to avoid circular creation
    @Autowired
    private LoginCacheService loginCacheService;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth
                    .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                    .requestMatchers("/api/auth/login", "/api/auth/register", "/api/auth/logout").permitAll()
                    .requestMatchers("/api/auth/me", "/api/auth/cache/**").authenticated()
                    .requestMatchers("/api/students/**").authenticated()
                    .anyRequest().authenticated()
                )
                .formLogin(form -> form
                        .loginProcessingUrl("/api/auth/login")
                .successHandler((request, response, authentication) -> {
                    if (authentication != null) {
                        List<String> roles = authentication.getAuthorities().stream()
                                .map(a -> a.getAuthority())
                                .map(s -> s.startsWith("ROLE_") ? s.substring(5) : s)
                                .collect(Collectors.toList());
                        loginCacheService.cacheLogin(authentication.getName(), roles);
                    }
                    response.setStatus(HttpServletResponse.SC_OK);
                })
                .failureHandler((request, response, exception) -> {
                    response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
                })
                        .permitAll()
                )
                .logout(logout -> logout
                        .logoutUrl("/api/auth/logout")
                .logoutSuccessHandler((request, response, authentication) -> {
                    if (authentication != null) {
                        loginCacheService.evict(authentication.getName());
                    }
                    response.setStatus(HttpServletResponse.SC_OK);
                })
                        .permitAll()
                );
        return http.build();
    }

    // Authentication will be configured automatically using the available
    // `UserDetailsService` and `PasswordEncoder` beans. Avoid manual configureGlobal
    // to prevent circular bean dependencies in this configuration class.

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // The application defines UserDetailsServiceImpl as a @Service; no explicit @Bean needed here.

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(Arrays.asList("http://localhost:5173"));
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(Arrays.asList("*"));
        configuration.setAllowCredentials(true);
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}
