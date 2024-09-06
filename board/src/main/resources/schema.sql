-- 내장 DB 사용시 + 현 구조
CREATE TABLE users (-- 유저
user_id int auto_increment,
id varchar(18),
pwd varchar(15) not null,
token varchar(5000) not null,
user_name varchar(100),
reg_dt timestamp default now(),
chg_dt timestamp,
reg_id varchar(18) default 'system',
chg_id varchar(18),
primary key(user_id)
);
CREATE TABLE boards (-- 게시물
board_id int auto_increment,
title varchar(300) not null,
content varchar(5000),
user_id int,
board_pwd varchar(15) not null,
del_yn char(1) default 'N',
reg_dt timestamp default now(),
chg_dt timestamp,
-- reg_id int,
-- chg_id varchar(18),
primary key(board_id),
foreign key(user_id) references users (user_id)
);