package com.auth.auth_server.service;

import com.auth.auth_server.exception.EmailAlreadyUsedException;
import com.auth.auth_server.exception.UsernameAlreadyUsedException;
import com.auth.auth_server.model.*;
import com.auth.auth_server.repository.TokenRepository;
import com.auth.auth_server.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class AuthenticationService {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TokenRepository tokenRepository;

    public void revokeAllUserTokens(User user) {
        List<Token> validTokens = tokenRepository.findAllByUser(user).stream()
                .filter(t -> !t.isExpired() && !t.isRevoked())
                .collect(Collectors.toList());

        for (Token t : validTokens) {
            t.setExpired(true);
            t.setRevoked(true);
        }

        tokenRepository.saveAll(validTokens);
    }


    public String login(String username, String password) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(username, password)
            );

            UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

            String jwt = jwtService.generateToken(userDetails);

            revokeAllUserTokens(userDetails.getUser());

            Token token = new Token();
            token.setToken(jwt);
            token.setUser(userDetails.getUser());
            token.setExpired(false);
            token.setRevoked(false);
            tokenRepository.save(token);

            return jwt;

        } catch (AuthenticationException ex) {
            throw new RuntimeException("Credenciales inválidas", ex);
        }
    }


    public String register(String username, String name, String lastName, String email, String password) {
        if (userRepository.findByEmail(email).isPresent()) {
            throw new EmailAlreadyUsedException("El email ya está en uso");
        }

        if (userRepository.findByUsername(username).isPresent()) {
            throw new UsernameAlreadyUsedException("El nombre de usuario ya está en uso");
        }

        User user = new User();
        user.setUsername(username);
        user.setEmail(email);
        user.setName(name);
        user.setLastName(lastName);
        user.setPassword(new BCryptPasswordEncoder().encode(password));
        user.setEnabled(true);
        user.setRole(Role.USER);
        user.setProvider(AuthProvider.LOCAL);

        userRepository.save(user);

        return jwtService.generateToken(new UserDetailsImpl(user));
    }
}
