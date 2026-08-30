package com.ticket.backend.domain;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class users {
    private Long userId;        //사용자 ID
    private String email;       //이메일
    private String password;    //비밀번호
    private String name;        //이름
    private String phone;       //전화번호
    private String role;        //권한
}