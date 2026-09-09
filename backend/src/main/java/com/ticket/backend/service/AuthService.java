package com.ticket.backend.service;

import com.ticket.backend.domain.RefreshTokens;
import com.ticket.backend.domain.Users;
import com.ticket.backend.dto.auth.LoginRequest;
import com.ticket.backend.dto.auth.LoginResponse;
import com.ticket.backend.mapper.RefreshTokenMapper;
import com.ticket.backend.mapper.UserMapper;
import com.ticket.backend.security.JwtProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserMapper userMapper;
    private final JwtProvider jwtProvider;
    private final RefreshTokenMapper refreshTokenMapper;

    //로그인
    public LoginResponse login(LoginRequest request) {
        Users users = userMapper.findByEmail(request.getEmail());

        //이메일 확인
        if (users == null) {
            throw new IllegalArgumentException(
                    "존재하지 않는 사용자 입니다."
            );
        }

        //비밀번호 확인
        if (!users.getPassword().equals(request.getPassword())) {
            throw new IllegalArgumentException(
                    "이메일 또는 비밀번호가 올바르지 않습니다."
            );
        }

        //토큰 발급
        String accessToken =
                jwtProvider.createAccessToken(users);

        String refreshToken =
                jwtProvider.createRefreshToken(users);

        RefreshTokens tokens = new RefreshTokens();

        tokens.setUserId(users.getUserId());
        tokens.setRefreshToken(refreshToken);
        tokens.setCreatedAt(LocalDateTime.now());
        tokens.setExpiresAt(LocalDateTime.now().plusDays(7));
        tokens.setRevoked(false);

        refreshTokenMapper.insertRefreshToken(tokens);

        return new LoginResponse(
                accessToken,
                refreshToken
        );
    }
}