package org.example.katalog.controller;

import jakarta.validation.Valid;
import org.example.katalog.dto.AuthResponseDTO;
import org.example.katalog.dto.LoginRequestDTO;
import org.example.katalog.dto.RegisterRequestDTO;
import org.example.katalog.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthResource {

  @Autowired private AuthService authService;

  @PostMapping("/register")
  public ResponseEntity<?> register(@Valid @RequestBody RegisterRequestDTO request) {
    try {
      AuthResponseDTO response = authService.register(request);
      return ResponseEntity.ok(response);
    } catch (RuntimeException e) {
      return ResponseEntity.badRequest().body(e.getMessage());
    }
  }

  @PostMapping("/login")
  public ResponseEntity<?> login(@Valid @RequestBody LoginRequestDTO request) {
    try {
      AuthResponseDTO response = authService.login(request);
      return ResponseEntity.ok(response);
    } catch (RuntimeException e) {
      return ResponseEntity.badRequest().body(e.getMessage());
    }
  }
}
