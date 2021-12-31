-- Create database
CREATE DATABASE IF NOT EXISTS  dblp;
USE dblp;

-- Create table
drop table if exists PaperInfo;
CREATE TABLE PaperInfo (
	paper_id int PRIMARY KEY AUTO_INCREMENT,
	title text,
	author text,
	published_year int,
	journal text,
	paper_url text
)
