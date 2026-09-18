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

-- 테스트 티켓 목데이터
INSERT INTO tickets (
    ticket_name,
    venue,
    sale_start_at,
    sale_end_at,
    valid_start_at,
    valid_end_at,
    total_quantity,
    remaining_quantity,
    purchase_limit,
    price,
    discount_rate
) VALUES
      (
          '뮤직 페스티벌 2026',
          '서울 올림픽공원',
          '2026-09-01 09:00:00',
          '2026-09-30 23:59:59',
          '2026-10-03 10:00:00',
          '2026-10-03 22:00:00',
          100,
          100,
          2,
          88000,
          10
      ),
      (
          '가을 재즈 콘서트',
          '부산 시민회관',
          '2026-09-05 10:00:00',
          '2026-09-25 23:59:59',
          '2026-10-10 18:00:00',
          '2026-10-10 21:00:00',
          50,
          50,
          4,
          55000,
          0
      ),
      (
          '뮤지컬 별빛의 노래',
          '대구 아트센터',
          '2026-09-10 09:00:00',
          '2026-10-05 23:59:59',
          '2026-10-15 19:30:00',
          '2026-10-15 22:00:00',
          80,
          80,
          3,
          120000,
          15
      ),
      (
          '푸드 페어 입장권',
          '인천 송도 컨벤시아',
          '2026-09-01 00:00:00',
          '2026-09-20 23:59:59',
          '2026-09-21 10:00:00',
          '2026-09-21 18:00:00',
          200,
          200,
          NULL,
          15000,
          NULL
      ),
      (
          '야구 올스타 이벤트',
          '수원 종합운동장',
          '2026-09-08 12:00:00',
          '2026-09-18 23:59:59',
          '2026-09-20 14:00:00',
          '2026-09-20 18:00:00',
          10,
          10,
          1,
          30000,
          20
      );

-- 동시성 제어 테스트 전용 티켓 목데이터
INSERT INTO tickets (
    ticket_name,
    venue,
    sale_start_at,
    sale_end_at,
    valid_start_at,
    valid_end_at,
    total_quantity,
    remaining_quantity,
    purchase_limit,
    price,
    discount_rate
)
VALUES (
           '동시성 테스트 티켓',
           '테스트 장소',
           NOW() - INTERVAL 1 DAY,
           NOW() + INTERVAL 1 DAY,
           NOW() + INTERVAL 2 DAY,
           NOW() + INTERVAL 3 DAY,
           10,
           10,
           NULL,
           10000,
           0
       );



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



-- 티켓 or 주문 테스트
-- 티켓 주문 후 주문 확인
SELECT *
FROM orders
ORDER BY order_id DESC;

-- 티켓 주문 후 재고 확인
SELECT
    ticket_id,
    ticket_name,
    total_quantity,
    remaining_quantity
FROM tickets
WHERE ticket_id = 1;

-- 티켓 취소 확인
SELECT
    order_id,
    user_id,
    order_status,
    canceled_at
FROM orders
ORDER BY order_id DESC;

-- 티켓 취소 후 재고 확인
SELECT ticket_id, remaining_quantity
FROM tickets
WHERE ticket_id = 1;



-- 동시성 제어 테스트
-- 티켓 번호 확인
SELECT
    ticket_id,
    ticket_name,
    total_quantity,
    remaining_quantity,
    purchase_limit
FROM tickets
WHERE ticket_name = '동시성 테스트 티켓'
ORDER BY ticket_id DESC;