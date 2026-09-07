package com.ticket.backend.service;

import com.ticket.backend.domain.Users;
import com.ticket.backend.dto.auth.LoginRequest;
import com.ticket.backend.mapper.UserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserMapper userMapper;

    //로그인
    public Users login(LoginRequest request) {
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
        return users;
    }
}