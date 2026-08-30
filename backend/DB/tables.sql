use ticket_db;

-- users 테이블
create table users (
                       user_id bigint primary key auto_increment,
                       email varchar(255) not null unique key,
                       password varchar(255) not null,
                       name varchar(50) not null,
                       phone varchar(20) not null,
                       role varchar(20) not null

                           CHECK (role IN ('USER', 'ADMIN'))
);

-- tickets 테이블
create table tickets (
                         ticket_id bigint primary key auto_increment,
                         ticket_name varchar(255) not null,
                         venue varchar(255) not null,
                         sale_start_at datetime not null,
                         sale_end_at datetime not null,
                         valid_start_at datetime not null,
                         valid_end_at datetime not null,
                         total_quantity int not null,
                         remaining_quantity int not null,
                         purchase_limit int,
                         price bigint not null,
                         discount_rate int
);

-- orders 테이블
create table orders (
                        order_id bigint primary key auto_increment,
                        user_id bigint not null,
                        ticket_id bigint not null,
                        quantity int not null,
                        price bigint not null,
                        discount_rate int,
                        discount_amount bigint not null,
                        final_amount bigint not null,
                        order_status varchar(20) not null,
                        ordered_at datetime not null,
                        canceled_at datetime,

                        foreign key (user_id)
                            references users(user_id),

                        foreign key (ticket_id)
                            references tickets(ticket_id)
);

-- refresh_tokens 테이블
create table refresh_tokens (
                                refresh_token_id bigint	primary key auto_increment,
                                user_id bigint not null,
                                refresh_token text not null,
                                expires_at datetime not null,
                                revoked boolean not null default false,
                                created_at datetime not null,

                                foreign key (user_id)
                                    references users(user_id)
);