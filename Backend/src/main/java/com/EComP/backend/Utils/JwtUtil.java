

package com.EComP.backend.Utils;
import java.security.Key;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;


@Component
public class JwtUtil {// JWT Util class

    // Define the secret keyprivate
    private final byte[] key = "0a1b2c3d4e5f6g7h8i9j0k1234567890".getBytes();

    // Generate a token using username
    public String generateToken(String username){
        Map<String, Object> claims = new HashMap<>();
        return createToken(claims, username);
    }

    // Create token method
    private String createToken(Map<String, Object> claims, String username) {
        return Jwts.builder()// Create the token
                .setClaims(claims)// Set the claims in the token (if any) 
                .setSubject(username) // Set the subject of the token (username) 
                .setIssuedAt(new Date(System.currentTimeMillis())) // Set the issued date of the token 
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 10)) // Token expiration (10 hours)
                .signWith(getSignKey(), SignatureAlgorithm.HS256) // Sign with HS256 and key 
                .compact(); // Build the token
    }
    // Get the key from the secret key string and return the key object
    private Key getSignKey() { // Get the key from the secret key string and return the key object
        byte[] keyBytes = Base64.getDecoder().decode(key);// Decode the key
        return Keys.hmacShaKeyFor(keyBytes);// Return the key object
    }
    // Validate the token
    public String extractUsername(String token){
                return extractClaim(token, Claims::getSubject);
    }
    // Extract the claim from the token
    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }
    // Extract all the claims from the token
    private Claims extractAllClaims(String token) {
        return Jwts.parserBuilder()
        .setSigningKey(getSignKey())
        .build()
        .parseClaimsJws(token)
        .getBody();
    }
    private Boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }
    // Extract the expiration date from the token
    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }
    // Validate the token
    public Boolean validateToken(String token, String username) {
        final String Username = extractUsername(token);
        return (Username.equals(username) && !isTokenExpired(token));
    }
}
