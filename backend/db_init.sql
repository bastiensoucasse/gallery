create user if not exists 'spring'@'%' identified by 'root';
/*create database if not exists `backend_db`;*/
grant all on `backend_db`.* to 'spring'@'%';
