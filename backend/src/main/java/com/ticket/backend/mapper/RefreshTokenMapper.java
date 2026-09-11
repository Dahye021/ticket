package com.ticket.backend.mapper;

import com.ticket.backend.domain.RefreshTokens;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface RefreshTokenMapper {

    //RefreshToken 객체를 받아서 DB에 저장 (xml 연결)
    void insertRefreshToken(RefreshTokens refreshTokens);

    //토큰 조회
    RefreshTokens findByRefreshToken(String refreshToken);

    //RefreshToken 무효화
    int revokeRefreshToken(String refreshToken);
}
