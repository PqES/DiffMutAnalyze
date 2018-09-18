drop user if exists 'equivalency_analyse'@'localhost';
drop database if exists equivalency_analyse;
create user 'equivalency_analyse'@'localhost' IDENTIFIED BY 'eqan1928';
create database equivalency_analyse;
GRANT ALL PRIVILEGES ON equivalency_analyse. * TO 'equivalency_analyse'@'localhost';