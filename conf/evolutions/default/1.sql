# --- !Ups
CREATE TABLE userProfile (
 id  int(11)  NULL AUTO_INCREMENT,
firstname  varchar(10),
middlename  varchar(10),
lastname    varchar(10),
username    varchar(25),
password    varchar(15),
mobile      int(10),
gender      varchar(10) ,
age         int(5),
hobbies  varchar(50),
isEnable Boolean Default true,
isAdmin    Boolean Default false,
primary key(id,username)
);


create table assignments(
id int auto_increment primary key,
title varchar(30) not null,
description varchar(100) );


# --- !Downs
drop table userProfile;
drop table assignments;


