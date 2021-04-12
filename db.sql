create user if not exists 'user'@'%' identified by 'spring';
grant all on `db_example`.* to 'user'@'%';
create database if not exists db_example;
