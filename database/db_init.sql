create user if not exists 'user'@'%' identified by 'spring';
create database if not exists `db_example`;
grant all on `db_example`.* to 'user'@'%';
