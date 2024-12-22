package com.example.JWTPractise.services;

import com.example.JWTPractise.models.LoginRequest;
import com.example.JWTPractise.models.User;
import com.example.JWTPractise.repositories.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    UserRepo userRepo;
    @Autowired
    AuthenticationManager authenticationManager;

    private final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(12);
    public User saveNewUser(User user) {
        user.setPassword(encoder.encode(user.getPassword()));
        return userRepo.save(user);
    }

    public String verify(LoginRequest request)
    {
        Authentication authentication = authenticationManager.
                authenticate(new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword()));
        if(authentication.isAuthenticated())
        {
            return "Success";
        }
        return "Not authenticated";
    }
}
