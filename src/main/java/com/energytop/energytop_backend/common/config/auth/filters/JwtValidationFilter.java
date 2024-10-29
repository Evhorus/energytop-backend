package com.energytop.energytop_backend.common.config.auth.filters;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import com.energytop.energytop_backend.common.config.auth.TokenJwtConfig;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class JwtValidationFilter extends BasicAuthenticationFilter {

  public JwtValidationFilter(AuthenticationManager authenticationManager) {
    super(authenticationManager);
  }

  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
      throws IOException, ServletException {
    String header = request.getHeader(TokenJwtConfig.HEADER_AUTHORIZATION);

    if (header == null || !header.startsWith(TokenJwtConfig.PREFIX_TOKEN)) {
      chain.doFilter(request, response);
      return;
    }

    String token = header.replace(TokenJwtConfig.PREFIX_TOKEN, "");
    

    try {
      Claims claims = Jwts.parser().verifyWith(TokenJwtConfig.SECRET_KEY).build().parseSignedClaims(token).getPayload();

      // Comprobar si authoritiesClaims es una lista y convertir
      Object authoritiesClaims = claims.get("authorities");
      List<String> authorities = new ArrayList<>();
      if (authoritiesClaims instanceof List<?>) {
        for (Object authority : (List<?>) authoritiesClaims) {
          if (authority instanceof String) {
            authorities.add((String) authority);
          }
        }
      }

      // Convertir a GrantedAuthority
      Collection<GrantedAuthority> grantedAuthorities = authorities.stream()
          .map(SimpleGrantedAuthority::new)
          .collect(Collectors.toList());

      String username = claims.getSubject();

      UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
          username, null, grantedAuthorities);

      SecurityContextHolder.getContext().setAuthentication(authentication);
      chain.doFilter(request, response);

    } catch (Exception e) {
      Map<String, String> body = new HashMap<>();
      body.put("error", e.getMessage());
      response.getWriter().write(new ObjectMapper().writeValueAsString(body));
      response.setStatus(403);
      response.setContentType("application/json");
    }

  }

}
