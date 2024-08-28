CREATE DATABASE IF NOT EXISTS jlaner;
USE jlaner;

CREATE TABLE member (
    member_id BIGINT NOT NULL AUTO_INCREMENT,
    email VARCHAR(255),
    login_id VARCHAR(255),
    name VARCHAR(255),
    provider VARCHAR(255),
    provider_id VARCHAR(255),
    role ENUM('ADMIN', 'USER'),
    PRIMARY KEY (member_id)
);

CREATE TABLE post (
    post_id BIGINT NOT NULL AUTO_INCREMENT,
    member_id BIGINT,
    schedule_date DATETIME(6),
    content_data VARCHAR(255),
    PRIMARY KEY (post_id),
    FOREIGN KEY (member_id) REFERENCES member(member_id) ON DELETE CASCADE
);

CREATE TABLE schedule_data (
    schedule_data_id BIGINT NOT NULL AUTO_INCREMENT,
    member_id BIGINT,
    schedule_date DATETIME(6),
    check_box1 TINYINT(1) NOT NULL,
    check_box2 BIT NOT NULL,
    check_box3 BIT NOT NULL,
    check_box4 BIT NOT NULL,
    check_box5 BIT NOT NULL,
    check_box6 BIT NOT NULL,
    check_box7 BIT NOT NULL,
    check_box8 BIT NOT NULL,
    check_box9 BIT NOT NULL,
    check_box10 BIT NOT NULL,
    check_box11 BIT NOT NULL,
    check_box12 BIT NOT NULL,
    schedule_content1 VARCHAR(255),
    schedule_content2 VARCHAR(255),
    schedule_content3 VARCHAR(255),
    schedule_content4 VARCHAR(255),
    schedule_content5 VARCHAR(255),
    schedule_content6 VARCHAR(255),
    schedule_content7 VARCHAR(255),
    schedule_content8 VARCHAR(255),
    schedule_content9 VARCHAR(255),
    schedule_content10 VARCHAR(255),
    schedule_content11 VARCHAR(255),
    schedule_content12 VARCHAR(255),
    PRIMARY KEY (schedule_data_id),
    FOREIGN KEY (member_id) REFERENCES member(member_id) ON DELETE CASCADE
);
