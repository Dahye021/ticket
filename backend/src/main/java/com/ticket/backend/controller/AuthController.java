package com.ticket.backend.controller;

import com.ticket.backend.dto.auth.AccessTokenResponse;
import com.ticket.backend.dto.auth.LoginRequest;
import com.ticket.backend.dto.auth.LoginResponse;
import com.ticket.backend.dto.auth.RefreshRequest;
import com.ticket.backend.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(
            @RequestBody LoginRequest request
            ) {
        LoginResponse response = authService.login(request);

        return ResponseEntity.ok(response);
    }

    @PostMapping("/refresh")
    public ResponseEntity<AccessTokenResponse> refresh(
            @RequestBody RefreshRequest request
    ) {
        AccessTokenResponse response =
                authService.refresh(request.getRefreshToken());

        return ResponseEntity.ok(response);
    }
}
