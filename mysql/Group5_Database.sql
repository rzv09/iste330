DROP DATABASE IF EXISTS CollegeConnection;

CREATE DATABASE CollegeConnection;
USE CollegeConnection;

--SET FOREIGN_KEY_CHECKS = 0;

CREATE TABLE Faculty(
   facultyID    int PRIMARY KEY AUTO_INCREMENT NOT NULL,
   firstName    varchar(45) NOT NULL,
   lastName     varchar(45) NOT NULL,
   email        varchar(45) NOT NULL,
   password     varchar(50) NOT NULL,
   phone        varchar(16) NOT NULL,
   address      varchar(45) NOT NULL,
   details      mediumtext
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE Abstract(
    abstractID      int PRIMARY KEY AUTO_INCREMENT NOT NULL,
    title           varchar(45) NOT NULL,
    description     mediumtext
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE Faculty_Abstract
(
    facultyID int,
    abstractID int,
    PRIMARY KEY (facultyID, abstractID),
    FOREIGN KEY (facultyID) REFERENCES Faculty(facultyID),
    FOREIGN KEY (abstractID) REFERENCES Abstract(abstractID)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE Faculty_Topic(
    keyword_ID      int PRIMARY KEY AUTO_INCREMENT NOT NULL,
    keyword         varchar(45) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE Faculty_Keyword
(
    facultyID int,
    keywordID int,
    PRIMARY KEY (facultyID, keywordID),
    FOREIGN KEY (facultyID) REFERENCES Faculty(facultyID),
    FOREIGN KEY (keywordID) REFERENCES Faculty_Topic(keyword_ID)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE Student(
    studentID    int PRIMARY KEY AUTO_INCREMENT NOT NULL,
    firstName    varchar(45) NOT NULL,
    lastName     varchar(45) NOT NULL,
    email        varchar(45) NOT NULL,
    password     varchar(50) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE Student_Topic(
    keyword_ID      int PRIMARY KEY AUTO_INCREMENT NOT NULL,
    keyword         varchar(45) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE Student_Keyword
(
    studentID int,
    keywordID int,
    PRIMARY KEY (studentID, keywordID),
    FOREIGN KEY (studentID) REFERENCES Faculty(facultyID),
    FOREIGN KEY (keywordID) REFERENCES Student(studentID)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE Guest(
    guestID    int PRIMARY KEY AUTO_INCREMENT NOT NULL,
    name         varchar(45) NOT NULL,
    email        varchar(45),
    password     varchar(50) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE Guest_Topic(
    keyword_ID      int PRIMARY KEY AUTO_INCREMENT NOT NULL,
    keyword         varchar(45) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE Guest_Keyword
(
    guestID int,
    keywordID int,
    PRIMARY KEY (guestID, keywordID),
    FOREIGN KEY (guestID) REFERENCES Faculty(facultyID),
    FOREIGN KEY (keywordID) REFERENCES Guest(guestID)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--SET FOREIGN_KEY_CHECKS = 1;