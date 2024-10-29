package com.energytop.energytop_backend.common.config.auth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import com.energytop.energytop_backend.common.config.auth.filters.JwtAuthenticationFilter;
import com.energytop.energytop_backend.common.config.auth.filters.JwtValidationFilter;

import io.jsonwebtoken.lang.Arrays;

@Configuration
@EnableWebSecurity
public class SpringSecurityConfig {

  @Autowired
  private AuthenticationConfiguration authenticationConfiguration;

  @Bean
  PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }

  @Bean
  AuthenticationManager authenticationManager() throws Exception {
    return authenticationConfiguration.getAuthenticationManager();
  }

  @Bean
  SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {

    return httpSecurity

        .authorizeHttpRequests(http -> {
          http.requestMatchers(HttpMethod.POST, "/users/validate-token").permitAll()
              .requestMatchers(HttpMethod.GET, "/users").permitAll()
              .requestMatchers("/users/**").hasRole("ADMIN")
              .anyRequest().authenticated();
        })
        .addFilter(new JwtAuthenticationFilter(authenticationManager()))
        .addFilter(new JwtValidationFilter(authenticationManager()))
        .csrf(csrf -> csrf.disable())
        .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
        .cors(cors -> cors.configurationSource(corsConfigurationSource()))
        .build();
  }

  @Bean
  CorsConfigurationSource corsConfigurationSource() {
    CorsConfiguration configuration = new CorsConfiguration();
    configuration.setAllowedOrigins(Arrays.asList(new String[] { "http://localhost:5173" }));
    configuration.setAllowedMethods(Arrays.asList(new String[] { "GET", "POST", "PATCH", "DELETE" }));
    configuration.setAllowedHeaders(Arrays.asList(new String[] { "Authorization", "Content-type" }));
    configuration.setAllowCredentials(true);
    UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();

    source.registerCorsConfiguration("/**", configuration);

    return source;

  }

  @Bean
  FilterRegistrationBean<CorsFilter> corsFilterRegistrationBean() {

    FilterRegistrationBean<CorsFilter> bean = new FilterRegistrationBean<>(
        new CorsFilter(corsConfigurationSource()));
    bean.setOrder(Ordered.HIGHEST_PRECEDENCE);
    return bean;

  }
}
