package com.EComP.backend.Controller;

import java.io.IOException;
import java.util.Optional;

import org.json.JSONObject;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.EComP.backend.DTO.AuthenticationRequest;
import com.EComP.backend.Entity.User;
import com.EComP.backend.Repo.UserRepo;
import com.EComP.backend.Services.UserDetailsServiceImpl;
import com.EComP.backend.Utils.JwtUtil;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class AuthController {
    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtTokenUtil;
    private final UserDetailsServiceImpl userDetailsService;
    private final UserRepo userrepo;

    @PostMapping("/authenticate")
    public void createAuthToken(@RequestBody AuthenticationRequest authenticationRequest, HttpServletResponse response) throws IOException{
        try {
            authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(authenticationRequest.getUsername(), authenticationRequest.getPassword()));
        }
        catch (BadCredentialsException e){
            throw new BadCredentialsException("Incorrect username or password");
        }
        final UserDetails userDetails = userDetailsService.loadUserByUsername(authenticationRequest.getUsername());
        Optional<User> optionalUser = userrepo.findFirstByEmail(authenticationRequest.getUsername());
        final String jwt = jwtTokenUtil.generateToken(userDetails.getUsername());
        
        if (optionalUser.isPresent()) {
            response.getWriter().write(new JSONObject()
            .put("userId", optionalUser.get().getId().toString())
            .put("role", optionalUser.get().getRole().toString()).toString()
            );
            response.addHeader("Authorization", "Bearer " + jwt);
        }
    }
}
