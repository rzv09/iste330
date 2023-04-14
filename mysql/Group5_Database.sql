DROP DATABASE IF EXISTS CollegeConnection;

CREATE DATABASE CollegeConnection;
USE CollegeConnection;

CREATE TABLE Faculty(
   facultyID    int PRIMARY KEY AUTO_INCREMENT,
   firstName    varchar(45) NOT NULL,
   lastName     varchar(45) NOT NULL,
   email        varchar(45) NOT NULL,
   password     varchar(50) NOT NULL,
   phone        varchar(16) NOT NULL,
   address      varchar(45) NOT NULL,
   details      mediumtext
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE Abstract(
    abstractID      int PRIMARY KEY AUTO_INCREMENT,
    title           varchar(45) NOT NULL,
    description     mediumtext
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE Faculty_Abstract
(
    facultyID int,
    abstractID int,
    PRIMARY KEY (facultyID, abstractID),
    FOREIGN KEY (facultyID) REFERENCES Faculty(facultyID) ON DELETE CASCADE ON UPDATE CASCADE,
    FOREIGN KEY (abstractID) REFERENCES Abstract(abstractID) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE Faculty_Topic(
    keyword_ID      int PRIMARY KEY AUTO_INCREMENT,
    keyword         varchar(45) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE Faculty_Keyword
(
    facultyID int,
    keywordID int,
    PRIMARY KEY (facultyID, keywordID),
    FOREIGN KEY (facultyID) REFERENCES Faculty(facultyID) ON DELETE CASCADE ON UPDATE CASCADE,
    FOREIGN KEY (keywordID) REFERENCES Faculty_Topic(keyword_ID) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE Student(
    studentID    int PRIMARY KEY AUTO_INCREMENT,
    firstName    varchar(45) NOT NULL,
    lastName     varchar(45) NOT NULL,
    email        varchar(45) NOT NULL,
    password     varchar(50) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE Student_Topic(
    keyword_ID      int PRIMARY KEY AUTO_INCREMENT,
    keyword         varchar(45) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE Student_Keyword
(
    studentID int,
    keywordID int,
    PRIMARY KEY (studentID, keywordID),
    FOREIGN KEY (studentID) REFERENCES Faculty(facultyID) ON DELETE CASCADE ON UPDATE CASCADE,
    FOREIGN KEY (keywordID) REFERENCES Student(studentID) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE Guest(
    guestID    int PRIMARY KEY AUTO_INCREMENT,
    name         varchar(45) NOT NULL,
    email        varchar(45),
    password     varchar(50) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE Guest_Topic(
    keyword_ID      int PRIMARY KEY AUTO_INCREMENT,
    keyword         varchar(45) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE Guest_Keyword
(
    guestID int,
    keywordID int,
    PRIMARY KEY (guestID, keywordID),
    FOREIGN KEY (guestID) REFERENCES Faculty(facultyID) ON DELETE CASCADE ON UPDATE CASCADE,
    FOREIGN KEY (keywordID) REFERENCES Guest(guestID) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;