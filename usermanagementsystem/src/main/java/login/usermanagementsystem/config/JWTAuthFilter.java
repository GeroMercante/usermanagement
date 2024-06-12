package login.usermanagementsystem.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.fasterxml.jackson.databind.ObjectMapper;

import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import login.usermanagementsystem.dto.ErrorResponse;
import login.usermanagementsystem.service.JwtUtils;
import login.usermanagementsystem.service.OurUsersDetailsService;

@Component
public class JWTAuthFilter extends OncePerRequestFilter {
  
  @Autowired
  private JwtUtils jwtUtils;

  @Autowired
  private OurUsersDetailsService ourUsersDetailsService;

  @Override
  protected void doFilterInternal(
    HttpServletRequest request,
    HttpServletResponse response,
    FilterChain filterChain
  ) throws ServletException, java.io.IOException {
    final String authHeader = request.getHeader("Authorization");
    final String jwtToken;
    final String userEmail;

    if (authHeader == null || authHeader.isBlank()) {
      filterChain.doFilter(request, response);
      return;
    }

    jwtToken = authHeader.substring(7);
    userEmail = jwtUtils.extractUsername(jwtToken);

    if (userEmail != null && SecurityContextHolder.getContext().getAuthentication() == null) {
      UserDetails userDetails = ourUsersDetailsService.loadUserByUsername(userEmail);

      if (jwtUtils.isTokeValid(jwtToken, userDetails)) {
        SecurityContext securityContext = SecurityContextHolder.createEmptyContext();
        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
        token.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

        securityContext.setAuthentication(token);
        SecurityContextHolder.setContext(securityContext);
      }
    }

    filterChain.doFilter(request, response);
  }
}
