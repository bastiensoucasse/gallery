create user if not exists 'user'@'%' identified by 'spring';
grant all on `db_example`.* to 'user'@'%';

drop database if exists db_example;
create database db_example;
