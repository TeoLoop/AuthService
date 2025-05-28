package com.auth.auth_server.controller;

import com.auth.auth_server.dto.AuthRequest;
import com.auth.auth_server.dto.AuthResponse;
import com.auth.auth_server.dto.RegisterRequest;
import com.auth.auth_server.exception.ApiError;
import com.auth.auth_server.exception.EmailAlreadyUsedException;
import com.auth.auth_server.exception.UsernameAlreadyUsedException;
import com.auth.auth_server.model.UserDetailsImpl;
import com.auth.auth_server.service.AuthenticationService;
import com.auth.auth_server.service.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private AuthenticationService authService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtService jwtService;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody AuthRequest loginRequest) {
        try {
            String token = authService.login(loginRequest.getUsername(), loginRequest.getPassword());
            return ResponseEntity.ok(new AuthResponse(token));
        } catch (RuntimeException ex) {
            return ResponseEntity
                    .status(HttpStatus.UNAUTHORIZED)
                    .body(new ApiError("Usuario o contraseña incorrectos"));
        }
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequest request) {
        try {
            String token = authService.register(
                    request.getUsername(),
                    request.getName(),
                    request.getLastName(),
                    request.getEmail(),
                    request.getPassword()
            );
            return ResponseEntity.ok(new AuthResponse(token));
        } catch (EmailAlreadyUsedException | UsernameAlreadyUsedException ex) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(new ApiError(ex.getMessage()));
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiError("Error interno del servidor"));
        }
    }

}
