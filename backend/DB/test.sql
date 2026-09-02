use ticket_db;

-- 테스트 유저 계정 목데이터
insert into users(
                  email,
                  password,
                  name,
                  phone,
                  role
) values (
        'test@test.com',
        'test123',
        '테스트유저계정',
        '010-0000-0000',
        'USER'
         );

-- 테스트 관리자 계정 목데이터
insert into users(
                  email,
                  password,
                  name,
                  phone,
                  role
) VALUES (
          'admin@test.com',
          'admin123',
          '테스트관리자계정',
          '010-1234-1234',
          'ADMIN'
         );

select * from users;