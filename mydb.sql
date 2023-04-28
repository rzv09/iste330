-- ISTE 330.01 | Habermas
-- Group 5 | Project Part 2
-- Raghav, Charki,Maple, Ramanz, 

DROP DATABASE IF EXISTS CollegeConnection; 

CREATE DATABASE CollegeConnection;
USE CollegeConnection;

DROP TABLE IF EXISTS faculty;
CREATE TABLE faculty(
	facultyID INT UNSIGNED NOT NULL AUTO_INCREMENT,
	firstName VARCHAR(255) NOT NULL,
	lastName VARCHAR(255) NOT NULL,
	email VARCHAR(320) NOT NULL,
	buildingNumber INT NULL,
	officeNumber INT NULL,
	PRIMARY KEY (facultyID)
) ENGINE = InnoDB DEFAULT CHARSET = utf8;

ALTER TABLE faculty AUTO_INCREMENT = 100;

DROP TABLE IF EXISTS abstract;
CREATE TABLE abstract(
	abstractID INT UNSIGNED NOT NULL AUTO_INCREMENT,
	abs_text MEDIUMTEXT NOT NULL,
	PRIMARY KEY (abstractID)
) ENGINE = InnoDB DEFAULT CHARSET = utf8;

ALTER TABLE abstract AUTO_INCREMENT = 100;

DROP TABLE IF EXISTS faculty_abstract;
CREATE TABLE faculty_abstract(
facultyID INT UNSIGNED NOT NULL,
abstractID INT UNSIGNED NOT NULL,
PRIMARY KEY (facultyID, abstractID),
CONSTRAINT faculty_abstract_faculty_FK 
	FOREIGN KEY (facultyID) 
	REFERENCES faculty(facultyID)
	ON DELETE CASCADE
	ON UPDATE CASCADE,
 CONSTRAINT faculty_abstract_abstract_FK
	FOREIGN KEY (abstractID)    
	REFERENCES abstract(abstractID)
	ON DELETE CASCADE
	ON UPDATE CASCADE
) ENGINE = InnoDB DEFAULT CHARSET = utf8;

DROP TABLE IF EXISTS student;
CREATE TABLE student(
	StudentID INT UNSIGNED NOT NULL AUTO_INCREMENT,
	firstName VARCHAR(255) NOT NULL,
	lastName VARCHAR(255) NOT NULL,
	email VARCHAR(320) NOT NULL,
	PRIMARY KEY (StudentID)
) ENGINE = InnoDB DEFAULT CHARSET = utf8;

ALTER TABLE student AUTO_INCREMENT = 100;

DROP TABLE IF EXISTS keywords;
CREATE TABLE keywords (
	KeywordID INT UNSIGNED NOT NULL AUTO_INCREMENT,
	word VARCHAR(60) NOT NULL, -- set to 60 as the longest word in the english dictionary is only 45 characters long.
	PRIMARY KEY (KeywordID)
) ENGINE = InnoDB DEFAULT CHARSET = utf8;



DROP TABLE IF EXISTS student_keyword;
CREATE TABLE student_keyword (
	KeywordID INT UNSIGNED NOT NULL,
	StudentID INT UNSIGNED NOT NULL,
	PRIMARY KEY (KeywordID, StudentID),
	CONSTRAINT student_keyword_keyword_FK
		FOREIGN KEY (KeywordID)
		REFERENCES keywords(KeywordID)
		ON DELETE CASCADE
		ON UPDATE CASCADE,
	CONSTRAINT student_keyword_student_FK
		FOREIGN KEY (StudentID)
		REFERENCES student(StudentID)
		ON DELETE CASCADE
		ON UPDATE CASCADE
) ENGINE = InnoDB DEFAULT CHARSET = utf8;

DROP TABLE IF EXISTS faculty_keyword;
CREATE TABLE faculty_keyword (
	KeywordID INT UNSIGNED NOT NULL,
	facultyID INT UNSIGNED NOT NULL,
	PRIMARY KEY (KeywordID, facultyID),
	CONSTRAINT faculty_keyword_keyword_FK
		FOREIGN KEY (KeywordID)
		REFERENCES keywords(KeywordID)
		ON DELETE CASCADE
		ON UPDATE CASCADE,
	CONSTRAINT faculty_keyword_faculty_FK
		FOREIGN KEY (KeywordID)
		REFERENCES faculty(facultyID)
		ON DELETE CASCADE
		ON UPDATE CASCADE
) ENGINE = InnoDB DEFAULT CHARSET = utf8;




DROP TABLE IF EXISTS faculty_student;
CREATE TABLE faculty_student(
facultyID INT UNSIGNED NOT NULL,
StudentID INT UNSIGNED NOT NULL,
PRIMARY KEY (facultyID, StudentID),
CONSTRAINT faculty_student_faculty_FK 
	FOREIGN KEY (facultyID) 
	REFERENCES faculty(facultyID)
	ON DELETE CASCADE
	ON UPDATE CASCADE,
 CONSTRAINT faculty_student_student_FK
	FOREIGN KEY (StudentID)    
	REFERENCES student(StudentID)
	ON DELETE CASCADE
	ON UPDATE CASCADE
) ENGINE = InnoDB DEFAULT CHARSET = utf8;

-- Creates an abstract and adds it to the faculty_abstract table.
DROP PROCEDURE IF EXISTS buildFacultyAbs;
DELIMITER >:)
CREATE PROCEDURE buildFacultyAbs(
	IN in_facultyID INT,
    IN in_abs_text MEDIUMTEXT
)
BEGIN
	DECLARE new_abstractID INT;
    
    INSERT INTO abstract (abs_text)
    VALUES (in_abs_text);
    
    SELECT LAST_INSERT_ID()
    INTO new_abstractID;

	INSERT INTO faculty_abstract (facultyID, abstractID)
    VALUES (in_facultyID, new_abstractID);
END >:)
DELIMITER ;

-- For an existing abstract, adds a faculty_abstract item.
DROP PROCEDURE IF EXISTS update_Faculty_Abs;
DELIMITER >:)
CREATE PROCEDURE update_Faculty_Abs(
	IN in_facultyID INT,
    IN in_abstractID INT
)
BEGIN
	INSERT INTO faculty_abstract (facultyID, abstractID)
    VALUES (in_facultyID, in_abstractID);
END >:)
DELIMITER ;

-- The abstract table is searched for up to three keywords, and the matching faculty members are returned.
DROP PROCEDURE IF EXISTS Abstract_Lookup;
DELIMITER >:)
CREATE PROCEDURE Abstract_Lookup(
	IN keywordOne VARCHAR(80),
    IN keywordTwo VARCHAR(80),
    IN keywordThree VARCHAR(80)
)
BEGIN
	IF keywordTwo = "" THEN
		SELECT DISTINCT faculty.lastName, faculty.firstName, faculty.email, faculty.buildingNumber, faculty.officeNumber
		FROM faculty
		JOIN faculty_abstract ON faculty.facultyID = faculty_abstract.facultyID
		JOIN abstract ON faculty_abstract.abstractID = abstract.abstractID
		WHERE (abstract.abs_text LIKE CONCAT("%", keywordOne, "%"));
	ELSEIF keywordThree = "" THEN
		SELECT DISTINCT faculty.lastName, faculty.firstName, faculty.email, faculty.buildingNumber, faculty.officeNumber
		FROM faculty
		JOIN faculty_abstract ON faculty.facultyID = faculty_abstract.facultyID
		JOIN abstract ON faculty_abstract.abstractID = abstract.abstractID
		WHERE (abstract.abs_text LIKE CONCAT("%", keywordOne, "%"))
			OR (abstract.abs_text LIKE CONCAT("%", keywordTwo, "%"));
	ELSE
		SELECT DISTINCT faculty.lastName, faculty.firstName, faculty.email, faculty.buildingNumber, faculty.officeNumber
		FROM faculty
		JOIN faculty_abstract ON faculty.facultyID = faculty_abstract.facultyID
		JOIN abstract ON faculty_abstract.abstractID = abstract.abstractID
		WHERE (abstract.abs_text LIKE CONCAT("%", keywordOne, "%"))
			OR (abstract.abs_text LIKE CONCAT("%", keywordTwo, "%"))
			OR (abstract.abs_text LIKE CONCAT("%", keywordThree, "%"));
	END IF;
END >:)
DELIMITER ;