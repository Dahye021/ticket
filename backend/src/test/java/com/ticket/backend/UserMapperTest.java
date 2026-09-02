package com.ticket.backend;

import com.ticket.backend.domain.Users;
import com.ticket.backend.mapper.UserMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class UserMapperTest {

    @Autowired
    private UserMapper userMapper;

    @Test
    void findByEmailTest() {
        Users users = userMapper.findByEmail("test@test.com");

        System.out.println("userId = " + users.getUserId());
        System.out.println("userId = " + users.getUserId());
        System.out.println("userId = " + users.getUserId());
        System.out.println("userId = " + users.getUserId());
    }
}
