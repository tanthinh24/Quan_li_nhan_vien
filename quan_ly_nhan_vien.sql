create database employee_management_system;

use employee_management_system;

CREATE TABLE `admin` (
  `id` int NOT NULL,
  `username` varchar(255) NOT NULL,
  `password` varchar(255) NOT NULL,
  PRIMARY KEY (`id`)
);

CREATE TABLE `employee` (
  `id` int NOT NULL AUTO_INCREMENT,
  `name` varchar(150) NOT NULL,
  `date_of_birth` date NOT NULL,
  `gender` varchar(10) NOT NULL,
  `email` varchar(100) NOT NULL,
  `phone` varchar(15) NOT NULL,
  `father_name` varchar(150) NOT NULL,
  `mother_name` varchar(150) NOT NULL,
  `address` text NOT NULL,
  `salary` text NOT NULL,
  `image_path` varchar(200) NOT NULL,
  PRIMARY KEY (`id`)
); 

INSERT INTO `admin` VALUES (1,'admin','123');

drop database employee_management_system;

SELECT * FROM employee_management_system.admin;

SELECT * FROM employee_management_system.employee;


