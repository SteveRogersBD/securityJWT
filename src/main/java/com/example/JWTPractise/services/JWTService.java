package com.example.JWTPractise.services;

import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Service;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import java.security.NoSuchAlgorithmException;
import java.util.Date;
import java.util.Base64;

@Service
public class JWTService {

    private String secretKey;

    public JWTService() {
        try{
            KeyGenerator keyGenerator = KeyGenerator.getInstance("HmacSHA256");
            SecretKey sk = keyGenerator.generateKey();
            secretKey = Base64.getEncoder().encodeToString(sk.getEncoded());
        }catch (NoSuchAlgorithmException e){
            e.printStackTrace();
        }
    }
    public String getJWTFromRequest(HttpServletRequest request) {
        String header = request.getHeader("Authorization");
        if(header!=null && header.startsWith("Bearer"))
        {
            header = header.substring(7);
            return header;
        }
        return null;
    }

    public String createJWTFromUsername(String username) {
        return Jwts.builder()
                .subject(username)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis()+60*60*30))
                .signWith(key())
                .compact();
    }

    private SecretKey key() {
        return Keys.hmacShaKeyFor(Decoders.BASE64.decode(secretKey));
    }

    public String getUsernameFromJWT(String token)
    {
        return Jwts.parser().verifyWith(key())
                .build().parseSignedClaims(token)
                .getPayload().getSubject();

    }

    public boolean validateToken(String token)
    {
        try{
            Jwts.parser().verifyWith(key()).build().
                    parseSignedClaims(token);
            return true;
        }catch (MalformedJwtException | ExpiredJwtException | UnsupportedJwtException |
                IllegalArgumentException e){
            e.printStackTrace();
        }
        return false;
    }

}
