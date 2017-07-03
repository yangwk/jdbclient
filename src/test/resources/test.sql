create database test;
use test;

CREATE TABLE student (
  id int(11) NOT NULL AUTO_INCREMENT,
  age int(11) DEFAULT NULL,
  name varchar(50) DEFAULT NULL,
  birthDate timestamp NULL,
  PRIMARY KEY (id)
);

insert into student(id,age,name,birthDate) values (1, 21, 'boy', now() );
insert into student(id,age,name,birthDate) values (2, 18, 'girl', '1996-07-03 19:25:06' );
