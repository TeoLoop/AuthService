package com.auth.auth_server.service;

import com.auth.auth_server.model.Role;
import com.auth.auth_server.model.User;
import com.auth.auth_server.model.UserDetailsImpl;
import com.auth.auth_server.repository.TokenRepository;
import com.auth.auth_server.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AuthenticationServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private JwtService jwtService;

    @InjectMocks
    private AuthenticationService authService;

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private TokenRepository tokenRepository;

    @Test
    void testRegisterSuccess() {
        // Arrange
        String username = "testuser";
        String email = "test@example.com";
        String password = "123456";
        String jwt = "fake-jwt-token";

        when(userRepository.findByUsername(username)).thenReturn(Optional.empty());
        when(userRepository.findByEmail(email)).thenReturn(Optional.empty());

        User savedUser = new User();
        savedUser.setUsername(username);
        savedUser.setEmail(email);
        savedUser.setPassword(password);
        savedUser.setName("Test");
        savedUser.setLastName("User");
        savedUser.setRole(Role.USER);

        when(jwtService.generateToken(any(UserDetailsImpl.class))).thenReturn(jwt);
        when(userRepository.save(any(User.class))).thenReturn(savedUser);

        // Act
        String result = authService.register(username, "Test", "User", email, password);

        // Assert
        assertEquals(jwt, result);
    }

    @Test
    void testRegisterEmailAlreadyExists() {
        when(userRepository.findByEmail("existing@example.com"))
                .thenReturn(Optional.of(new User()));

        RuntimeException exception = assertThrows(RuntimeException.class, () ->
                authService.register("newuser", "Name", "Lastname", "existing@example.com", "123")
        );

        assertEquals("El email ya está en uso", exception.getMessage());
    }


    @Test
    void testLoginSuccess() {
        String username = "testuser";
        String password = "123456";
        String jwt = "token-jwt";

        User user = new User();
        user.setUsername(username);

        UserDetailsImpl userDetails = new UserDetailsImpl(user);

        Authentication auth = mock(Authentication.class);
        when(auth.getPrincipal()).thenReturn(userDetails);

        when(authenticationManager.authenticate(any()))
                .thenReturn(auth);
        when(jwtService.generateToken(userDetails))
                .thenReturn(jwt);

        String result = authService.login(username, password);

        assertEquals(jwt, result);
    }
}