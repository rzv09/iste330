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

DROP TABLE IF EXISTS faculty_topics;
CREATE TABLE faculty_topics (
	KeywordID INT UNSIGNED NOT NULL AUTO_INCREMENT,
	word VARCHAR(80) NOT NULL, -- set to 80 as the longest word in the english dictionary is only 45 characters long.
	PRIMARY KEY (KeywordID)
) ENGINE = InnoDB DEFAULT CHARSET = utf8;

DROP TABLE IF EXISTS faculty_keyword;
CREATE TABLE faculty_keyword (
	KeywordID INT UNSIGNED NOT NULL,
	facultyID INT UNSIGNED NOT NULL,
	PRIMARY KEY (KeywordID, facultyID),
	CONSTRAINT faculty_keyword_keyword_FK
		FOREIGN KEY (KeywordID)
		REFERENCES faculty_topics(KeywordID)
		ON DELETE CASCADE
		ON UPDATE CASCADE,
	CONSTRAINT faculty_keyword_faculty_FK
		FOREIGN KEY (facultyID)
		REFERENCES faculty(facultyID)
		ON DELETE CASCADE
		ON UPDATE CASCADE
) ENGINE = InnoDB DEFAULT CHARSET = utf8;



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

DROP TABLE IF EXISTS student_topics;
CREATE TABLE student_topics (
	KeywordID INT UNSIGNED NOT NULL AUTO_INCREMENT,
	word VARCHAR(80) NOT NULL, -- set to 80 as the longest word in the english dictionary is only 45 characters long.
	PRIMARY KEY (KeywordID)
) ENGINE = InnoDB DEFAULT CHARSET = utf8;

DROP TABLE IF EXISTS student_keyword;
CREATE TABLE student_keyword (
	KeywordID INT UNSIGNED NOT NULL,
	StudentID INT UNSIGNED NOT NULL,
	PRIMARY KEY (KeywordID, StudentID),
	CONSTRAINT student_keyword_keyword_FK
		FOREIGN KEY (KeywordID)
		REFERENCES student_topics(KeywordID)
		ON DELETE CASCADE
		ON UPDATE CASCADE,
	CONSTRAINT student_keyword_student_FK
		FOREIGN KEY (StudentID)
		REFERENCES student(StudentID)
		ON DELETE CASCADE
		ON UPDATE CASCADE
) ENGINE = InnoDB DEFAULT CHARSET = utf8;

-- Adds a keyword to a student
DROP PROCEDURE IF EXISTS addStudentKeyword;
DELIMITER //
CREATE PROCEDURE addStudentKeyword(
	IN in_studentID INT,
	IN in_word VARCHAR(80)
)
BEGIN
	DECLARE wordID INT; -- Store the ID of the keyword

	-- Test if the word already exists in the table of keywords.
	IF in_word NOT IN (SELECT word FROM student_topics) THEN
		-- If the word doesn't already exist, in the table, insert it
		INSERT INTO student_topics (word) VALUES (in_word);
	END IF;

	-- Get the ID of the word in the keywords table
	SELECT student_topics.KeywordID INTO wordID
	FROM student_topics
	WHERE student_topics.word LIKE in_word;

	-- Insert the record
	INSERT INTO student_keyword(StudentID, KeywordID)
	VALUES (in_studentID, wordID);
END//
DELIMITER ;

-- Removes a keyword from a student
DROP PROCEDURE IF EXISTS removeStudentKeyword;
DELIMITER //
CREATE PROCEDURE removeStudentKeyword(
	p_studentID INT,
	p_word VARCHAR(80)
)
BEGIN
	DECLARE wordID INT; -- Store the ID of the keyword
	-- Test if the word even exists in the keyword table.
		-- Get the word ID
		SELECT student_topics.KeywordID INTO wordID
		FROM student_topics
		WHERE student_topics.word LIKE p_word;

		-- Actually delete the record
		DELETE FROM student_keyword
		WHERE StudentID = p_studentID AND
			KeywordID = wordID;
END//
DELIMITER ;

-- Removes a keyword from a faculty member
DROP PROCEDURE IF EXISTS removeFacultyKeyword;
DELIMITER //
CREATE PROCEDURE removeFacultyKeyword(
	p_facultyID INT,
	p_word VARCHAR(80)
)
BEGIN
	DECLARE
	wordID INT; -- Store the ID of the keyword
	-- Test if the word even exists in the keyword table.
		-- Get the word ID
		SELECT faculty_topics.KeywordID INTO wordID
		FROM faculty_topics
		WHERE faculty_topics.word LIKE p_word;

		-- Actually delete the record
		DELETE FROM faculty_keyword
		WHERE facultyID = p_facultyID AND
			KeywordID = wordID;
END//
DELIMITER ;



-- Adds a keyword to a faculty member
DROP PROCEDURE IF EXISTS addFacultyKeyword;
DELIMITER //
CREATE PROCEDURE addFacultyKeyword(
	IN in_facultyID INT,
	IN in_word VARCHAR(80)
)
BEGIN
	DECLARE wordID INT; -- Store the ID of the keyword

	-- Test if the word already exists in the table of keywords.
	IF in_word NOT IN (SELECT word FROM faculty_topics) THEN
		-- If the word doesn't already exist, in the table, insert it
		INSERT INTO faculty_topics (word) VALUES (in_word);
	END IF;

	-- Get the ID of the word in the keywords table
	SELECT faculty_topics.KeywordID INTO wordID
	FROM faculty_topics
	WHERE faculty_topics.word LIKE in_word;

	-- Insert the record
	INSERT INTO faculty_keyword(facultyID, KeywordID)
	VALUES (in_facultyID, wordID);
END//
DELIMITER ;

-- Creates an abstract and adds it to the faculty_abstract table.
DROP PROCEDURE IF EXISTS buildFacultyAbs;
DELIMITER //
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
END//
DELIMITER ;

-- For an existing abstract, adds a faculty_abstract item.
DROP PROCEDURE IF EXISTS update_Faculty_Abs;
DELIMITER //
CREATE PROCEDURE update_Faculty_Abs(
	IN in_facultyID INT,
    IN in_abstractID INT
)
BEGIN
	INSERT INTO faculty_abstract (facultyID, abstractID)
    VALUES (in_facultyID, in_abstractID);
END//
DELIMITER ;

-- The faculty table is searched for any faculty members who have corresponding keywords.
DROP PROCEDURE IF EXISTS Faculty_Keyword_Lookup;
DELIMITER //
CREATE PROCEDURE Faculty_Keyword_Lookup(
	IN keywordOne VARCHAR(80),
    IN keywordTwo VARCHAR(80),
    IN keywordThree VARCHAR(80)
)
BEGIN
	-- If only one keyword is provided, search using that one keyword.
	-- Ignore keywordTwo and keywordThree
	IF keywordTwo = '' THEN
		SELECT DISTINCT faculty.lastName, faculty.firstName, faculty.email, faculty.buildingNumber, faculty.officeNumber
		FROM faculty
		LEFT JOIN faculty_keyword USING(facultyID)
		LEFT JOIN faculty_topics ON faculty_keyword.KeywordID = faculty_topics.KeywordID
        LEFT JOIN faculty_abstract ON faculty.facultyID = faculty_abstract.facultyID
        LEFT JOIN abstract ON faculty_abstract.abstractID = abstract.abstractID
		WHERE faculty_topics.word LIKE ('%' || keywordOne || '%')
		    OR (abstract.abs_text LIKE ('%' || keywordOne ||'%'));
	-- If only two keywords are provided, ignore keywordThree
	ELSEIF keywordThree = '' THEN
		SELECT DISTINCT faculty.lastName, faculty.firstName, faculty.email, faculty.buildingNumber, faculty.officeNumber
		FROM faculty
        LEFT JOIN faculty_keyword USING(facultyID)
        LEFT JOIN faculty_topics ON faculty_keyword.KeywordID = faculty_topics.KeywordID
        LEFT JOIN faculty_abstract ON faculty.facultyID = faculty_abstract.facultyID
        LEFT JOIN abstract ON faculty_abstract.abstractID = abstract.abstractID
		WHERE faculty_topics.word LIKE ('%' || keywordOne || '%')
			OR faculty_topics.word LIKE ('%' || keywordTwo || '%')
            OR abstract.abs_text LIKE ('%' || keywordOne || '%')
            OR abstract.abs_text LIKE ('%' || keywordTwo || '%');
	-- If three keywords are provided.
	ELSE
        SELECT DISTINCT faculty.lastName, faculty.firstName, faculty.email, faculty.buildingNumber, faculty.officeNumber
        FROM faculty
        LEFT JOIN faculty_keyword USING(facultyID)
        LEFT JOIN faculty_topics ON faculty_keyword.KeywordID = faculty_topics.KeywordID
        LEFT JOIN faculty_abstract ON faculty.facultyID = faculty_abstract.facultyID
        LEFT JOIN abstract ON faculty_abstract.abstractID = abstract.abstractID
        WHERE faculty_topics.word LIKE ('%' || keywordOne || '%')
           OR faculty_topics.word LIKE ('%' || keywordTwo || '%')
           OR faculty_topics.word LIKE ('%' || keywordThree || '%')
           OR (abstract.abs_text LIKE ('%' || keywordOne || '%'))
           OR (abstract.abs_text LIKE ('%' || keywordTwo || '%'))
           OR (abstract.abs_text LIKE ('%' || keywordThree || '%'));
	END IF;
END //
DELIMITER ;


-- The student table is searched for any students who have corresponding keywords.
DROP PROCEDURE IF EXISTS Student_Keyword_Lookup;
DELIMITER //
CREATE PROCEDURE Student_Keyword_Lookup(
	IN keywordOne VARCHAR(80),
    IN keywordTwo VARCHAR(80),
    IN keywordThree VARCHAR(80)
)
BEGIN
	-- If only one keyword is provided, search using that one keyword.
	-- Ignore keywordTwo and keywordThree
	IF keywordTwo = '' THEN
		SELECT DISTINCT student.lastName, student.firstName, student.email, student.StudentID
		FROM student
		LEFT JOIN student_keyword USING(StudentID)
		LEFT JOIN student_topics ON student_keyword.KeywordID = student_topics.KeywordID
		WHERE student_topics.word LIKE ('%' || keywordOne || '%');
	-- If only two keywords are provided, ignore keywordThree
	ELSEIF keywordThree = '' THEN
		SELECT DISTINCT student.lastName, student.firstName, student.email, student.StudentID
		FROM student
		LEFT JOIN student_keyword USING(StudentID)
		LEFT JOIN student_topics ON student_keyword.KeywordID = student_topics.KeywordID
		WHERE student_topics.word LIKE ('%' || keywordOne || '%')
			OR student_topics.word LIKE ('%' || keywordTwo || '%');
	-- If three keywords are provided.
	ELSE
		SELECT DISTINCT student.lastName, student.firstName, student.email, student.StudentID
		FROM student
		LEFT JOIN student_keyword USING(StudentID)
		LEFT JOIN student_topics ON student_keyword.KeywordID = student_topics.KeywordID
		WHERE student_topics.word LIKE ('%' || keywordOne || '%')
			OR student_topics.word LIKE ('%' || keywordTwo || '%')
			OR student_topics.word LIKE ('%' || keywordThree || '%');
	END IF;
END //
DELIMITER ;

-- populate database
-- faculty
INSERT INTO faculty (firstName, lastName, email, buildingNumber, officeNumber)
VALUES
('Brom', 'Holcombson','Ironscale@varden.org', 5, 7871),
('Iron', 'Galbatorix','Firstforsworn@broddring.net',5, 0133),
('Oromis', 'Thrándurin', 'Emosage@eldunari.net', 5, 7200),
('Angela', 'Uluthrek', 'madwomanwizened@varden.org', 5, 0022);

-- Holcombson
CALL addFacultyKeyword(100, 'dracology');
CALL addFacultyKeyword(100, 'fencing');
CALL addFacultyKeyword(100, 'mysticism');
CALL addFacultyKeyword(100, 'history');

-- Galbatorix
CALL addFacultyKeyword(101, 'dracology');
CALL addFacultyKeyword(101, 'dark arts');
CALL addFacultyKeyword(101, 'mysticism');
CALL addFacultyKeyword(101, 'alchemy');

-- Thrándurin
CALL addFacultyKeyword(102, 'dracology');
CALL addFacultyKeyword(102, 'fencing');
CALL addFacultyKeyword(102, 'mysticism');
CALL addFacultyKeyword(102, 'history');

-- Uluthrek
CALL addFacultyKeyword(103, 'dracology');
CALL addFacultyKeyword(103, 'mysticism');
CALL addFacultyKeyword(103, 'witchcraft');
CALL addFacultyKeyword(103, 'politics');


INSERT INTO student (firstName, lastName, email)
VALUES
('Eragon', 'Bromsson', 'brisingr@varden.org'),
('Murtagh', 'Morzansson', 'kingkiller20@broddring.net');

-- Eragon
CALL addStudentKeyword(100, 'dracology');
CALL addStudentKeyword(100, 'fencing');
CALL addStudentKeyword(100, 'politics');
CALL addStudentKeyword(100, 'mysticism');

-- Murtagh
CALL addStudentKeyword(101, 'dracology');
CALL addStudentKeyword(101, 'fencing');
CALL addStudentKeyword(101, 'dark arts');
CALL addStudentKeyword(101, 'history');