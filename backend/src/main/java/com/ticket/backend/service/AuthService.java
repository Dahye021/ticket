package com.ticket.backend.service;

import com.ticket.backend.domain.RefreshTokens;
import com.ticket.backend.domain.Users;
import com.ticket.backend.dto.auth.AccessTokenResponse;
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

    //로그인 처리
    public LoginResponse login(LoginRequest request) {

        //이메일로 사용자 조회
        Users users = userMapper.findByEmail(request.getEmail());

        //임시 테스트(지울것)
        System.out.println("DB에서 조회한 role = " + users.getRole());

        //사용자가 존재하는지 확인
        if (users == null) {
            throw new IllegalArgumentException(
                    "존재하지 않는 사용자 입니다."
            );
        }

        //비밀번호가 일치하는지 확인
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

        //RefreshToken 정보를 DB에 저장
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

    //토큰 재발급 메서드
    public AccessTokenResponse refresh(String refreshToken) {
        RefreshTokens savedToken = refreshTokenMapper.findByRefreshToken(refreshToken);

        //토큰이 존재 하는지 DB 조회
        if (savedToken == null) {
            throw new IllegalArgumentException("유효하지 않은 Refresh Token입니다.");
        }

        //토큰 무효화 확인
        if (savedToken.isRevoked()) {
            throw new IllegalArgumentException("이미 무효화 된 Refresh Token입니다.");
        }

        //토큰 만료 확인
        if (savedToken.getExpiresAt().isBefore(LocalDateTime.now())) {
            throw new IllegalArgumentException("만료 된 Refresh Token입니다.");
        }

        //JWT 토큰 자체가 유효한지 확인
        if (!jwtProvider.validateToken(refreshToken)) {
            throw new IllegalArgumentException("유효하지 않은 Refresh Token입니다.");
        }

        Long userId = jwtProvider.getUserId(refreshToken);
        Users users = userMapper.findById(userId);
        String newAccessToken = jwtProvider.createAccessToken(users);

        return new AccessTokenResponse(newAccessToken);
    }

    //로그아웃 메서드
    public void logout(String refreshToken) {

        //토큰 무효화
        int result = refreshTokenMapper.revokeRefreshToken(refreshToken);

        if (result == 0) {
            throw new IllegalArgumentException("유효하지 않은 Refresh Token입니다.");
        }
    }
}