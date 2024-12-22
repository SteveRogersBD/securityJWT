package com.example.JWTPractise.config;

import com.example.JWTPractise.CustomUserDetails;
import com.example.JWTPractise.CustomUserDetailsService;
import com.example.JWTPractise.services.JWTService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JWTFilter extends OncePerRequestFilter {
    @Autowired
    private JWTService jwtService;
    @Autowired
    private ApplicationContext applicationContext;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String authToken = request.getHeader("Authorization");
        String username = null;
        if(authToken != null && authToken.startsWith("Bearer ")) {
            authToken = authToken.substring(7);
            username = jwtService.extractUsername(authToken);
        }
        if(username!=null && SecurityContextHolder.getContext().getAuthentication()==null)
        {
            UserDetails userDetails = applicationContext.getBean(CustomUserDetailsService.class).
                    loadUserByUsername(username);
            if(jwtService.validateToken(authToken,userDetails))
            {
                UsernamePasswordAuthenticationToken token =
                new UsernamePasswordAuthenticationToken(userDetails,
                        null,userDetails.getAuthorities());
                token.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(token);
            }

        }
        filterChain.doFilter(request, response);

    }
}
