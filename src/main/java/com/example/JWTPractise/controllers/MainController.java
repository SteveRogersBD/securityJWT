package com.example.JWTPractise.controllers;

import com.example.JWTPractise.CustomUserDetails;
import com.example.JWTPractise.models.LoginRequest;
import com.example.JWTPractise.models.RegisterRequest;
import com.example.JWTPractise.models.User;
import com.example.JWTPractise.services.JWTService;
import com.example.JWTPractise.services.UserService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/public")
public class MainController {

    @Autowired
    UserService userService;
    @Autowired
    JWTService jwtService;

    @GetMapping("/csrf")
    public Map<String, String> csrf(HttpServletRequest request) {
        CsrfToken csrfToken = (CsrfToken) request.getAttribute("_csrf");
        if (csrfToken != null) {
            Map<String, String> response = new HashMap<>();
            response.put("token", csrfToken.getToken());
            response.put("headerName", csrfToken.getHeaderName());
            response.put("parameterName", csrfToken.getParameterName());
            return response;
        }
        return Map.of("message", "CSRF token not found");
    }

    @PostMapping("register")
    public ResponseEntity<User> register(@RequestBody RegisterRequest req) {
        User user = new User();
        user.setUsername(req.getUsername());
        user.setPassword(req.getPassword());
        user.setRole("USER");
        user.setEmail(req.getEmail());
        user.setCreatedAt(LocalDateTime.now());
        User newUser = userService.saveNewUser(user);
        return new ResponseEntity<>(newUser, HttpStatus.OK);
    }
    @PostMapping("/signin")
    public String signInUser(@RequestBody LoginRequest req) {
        userService.verify(req);
        return jwtService.createJWT(req.getUsername());
    }


//    @PostMapping("/signin")
//    public ResponseEntity<String> signIn(@RequestBody LoginRequest req){
//        Authentication authentication = authenticationManager.authenticate(
//                new UsernamePasswordAuthenticationToken(req.getUsername(),
//                        req.getPassword()));
//        SecurityContextHolder.getContext().setAuthentication(authentication);
//        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
//        return new ResponseEntity<>("Welcome "+userDetails.getUsername(), HttpStatus.OK);
//    }
}
