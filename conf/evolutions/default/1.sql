# --- !Ups
CREATE TABLE userSignup(
firstname VARCHAR(10),
middlename VARCHAR(10),
lastname VARCHAR(10),
username VARCHAR(25) PRIMARY KEY,
password VARCHAR(15),
confirmPassword VARCHAR(15),
mobile INT(10),
gender VARCHAR(10),
age INT(5),
hobbies VARCHAR(50)
);


create table Assignment(
id int auto_increment primary key,
title varchar(30) not null,
description varchar(100) );

# --- !Downs

drop table userSignup;
drop table Assignment;


