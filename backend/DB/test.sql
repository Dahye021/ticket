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

-- 토큰 테스트
-- revoked = true 토큰
UPDATE refresh_tokens
SET revoked = TRUE
WHERE refresh_token_id = 1;

-- revoked 원복
UPDATE refresh_tokens
SET revoked = FALSE
WHERE refresh_token_id = 1;

-- 토큰 만료 테스트
UPDATE refresh_tokens
SET expires_at = '2020-01-01 00:00:00'
WHERE refresh_token_id = 1;

-- 로그아웃 후 만료 토큰 확인
SELECT *
FROM refresh_tokens
WHERE revoked = TRUE;