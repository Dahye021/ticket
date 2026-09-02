package com.ticket.backend.mapper;

import com.ticket.backend.domain.Users;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserMapper {

    //이메일을 받아서 사용자 조회 후 유저 객체 반환
    Users findByEmail(String email);


}