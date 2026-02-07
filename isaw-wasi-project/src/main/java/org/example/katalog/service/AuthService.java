package org.example.katalog.service;

import org.example.katalog.dto.AuthResponseDTO;
import org.example.katalog.dto.LoginRequestDTO;
import org.example.katalog.dto.RegisterRequestDTO;
import org.example.katalog.model.User;
import org.example.katalog.repository.UserRepository;
import org.example.katalog.security.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

  @Autowired private UserRepository userRepository;

  @Autowired private PasswordEncoder passwordEncoder;

  @Autowired private JwtUtil jwtUtil;

  @Autowired private AuthenticationManager authenticationManager;

  public AuthResponseDTO register(RegisterRequestDTO request) {
    // Check if username already exists
    if (userRepository.existByUsername(request.getUsername())) {
      throw new RuntimeException("Username already taken");
    }

    // Check if email already exists
    if (userRepository.existsByEmail(request.getEmail())) {
      throw new RuntimeException("Email already registered");
    }

    // Create new user
    User user =
        new User(
            request.getUsername(),
            request.getEmail(),
            passwordEncoder.encode(request.getPassword()));

    userRepository.save(user);

    // Generate JWT token
    String token = jwtUtil.generateToken(user.getUsername());

    return new AuthResponseDTO(token, user.getUsername(), user.getEmail());
  }

  public AuthResponseDTO login(LoginRequestDTO request) {
    try {
      // Authenticate user
      authenticationManager.authenticate(
          new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword()));
    } catch (AuthenticationException e) {
      throw new RuntimeException("Invalid username or password");
    }

    // Fetch user details
    User user =
        userRepository
            .findByUsername(request.getUsername())
            .orElseThrow(() -> new RuntimeException("User not found"));

    // Generate JWT token
    String token = jwtUtil.generateToken(user.getUsername());

    return new AuthResponseDTO(token, user.getUsername(), user.getEmail());
  }
}
