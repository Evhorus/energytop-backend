package com.energytop.energytop_backend.common.config.auth.filters;

import java.io.IOException;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.energytop.energytop_backend.auth.entities.User;
import com.energytop.energytop_backend.common.config.auth.TokenJwtConfig;
import com.fasterxml.jackson.core.exc.StreamReadException;
import com.fasterxml.jackson.databind.DatabindException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Jwts;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

  private AuthenticationManager authenticationManager;

  public JwtAuthenticationFilter(AuthenticationManager authenticationManager) {
    this.authenticationManager = authenticationManager;
  }

  // para el login
  @Override
  public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
      throws AuthenticationException {
    User user = null;
    String email = null;
    String password = null;
    try {
      user = new ObjectMapper().readValue(request.getInputStream(), User.class);
      email = user.getEmail();
      password = user.getPassword();

    } catch (StreamReadException e) {

      e.printStackTrace();
    } catch (DatabindException e) {

      e.printStackTrace();
    } catch (IOException e) {

      e.printStackTrace();
    }

    UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(email, password);
    return authenticationManager.authenticate(authToken);
  }

  @Override
  protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain,
      Authentication authResult) throws IOException, ServletException {

    String email = ((org.springframework.security.core.userdetails.User) authResult.getPrincipal()).getUsername();

    Collection<? extends GrantedAuthority> roles = authResult.getAuthorities();

    boolean isAdmin = roles.stream().anyMatch(r -> r.getAuthority().equals("ROLE_ADMIN"));

    List<String> authorities = roles.stream()
        .map(GrantedAuthority::getAuthority) // Obtiene el nombre de la autoridad
        .collect(Collectors.toList());

    String token = Jwts.builder()
        .claim("authorities", authorities)
        .claim("isAdmin", isAdmin)
        .claim("email", email)
        .signWith(TokenJwtConfig.SECRET_KEY)
        .issuedAt(new Date())
        .expiration(new Date(System.currentTimeMillis() + 36000000)).compact();

    response.addHeader(TokenJwtConfig.HEADER_AUTHORIZATION, TokenJwtConfig.PREFIX_TOKEN + token);

    Map<String, Object> body = new HashMap<>();

    body.put("token", token);
    body.put("message", String.format("Hola %s, has iniciado sesion con exito!", email));
    body.put("email", email);

    response.getWriter().write(new ObjectMapper().writeValueAsString(body));
    response.setStatus(200);
    response.setContentType("application/json");

  }

  @Override
  protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response,
      AuthenticationException failed) throws IOException, ServletException {

    Map<String, Object> body = new HashMap<>();
    body.put("message", "Error en la autenticacion email o password incorrecto!");
    body.put("error", failed.getMessage());

    response.getWriter().write(new ObjectMapper().writeValueAsString(body));
    response.setStatus(401);
    response.setContentType("application/json");

  }

}
