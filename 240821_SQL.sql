#240821 
#https://tychejin.tistory.com/394
#Database 생성
create database board;

#계정 생성 
create user 'nana'@'%' identified by '1234' ;

#권한확인 
show grants for 'nana'@'%' ;

#권한설정 
grant all privileges on board.* to 'nana'@'%';


#캐시 지우고 새로운 설정을 적용 
flush privileges;

#계정삭제 
drop user 'nana'@'%';


#테이블 생성sys_config
CREATE TABLE USER_INFO (
		SEQ_NO	BIGINT NOT NULL PRIMARY KEY auto_increment,
		USER_NAME VARCHAR(20) NOT NULL ,
        PASSWORD VARCHAR(15) NOT NULL, 
        REG_ID VARCHAR(20),
        REG_DATE DATETIME,
		REG_IP VARCHAR(15)
);

CREATE TABLE BOARD_LIST(
		SEQ_NO	BIGINT NOT NULL PRIMARY KEY auto_increment,
        TITLE	VARCHAR(255),
		CONTENTS TEXT, #TEXT를 사용하면 길이 제한이 사라짐.  https://medium.com/daangn/varchar-vs-text-230a718a22a1
		USER_NAME VARCHAR(20) NOT NULL ,
        PASSWORD VARCHAR(15) NOT NULL, 
        USE_YN CHAR(1) NOT NULL,
        REG_ID VARCHAR(20),
		REG_IP VARCHAR(15),
        REG_DATE DATETIME,
        EDIT_ID VARCHAR(20),
		EDIT_IP VARCHAR(15),
        EDIT_DATE DATETIME
        
);

INSERT INTO USER_INFO 
VALUES (1,'MASTER', '1111', 'nana', now(), '1.1.1.1');
	
SELECT * FROM USER_INFO;