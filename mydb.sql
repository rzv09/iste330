-- ISTE 330.01 | Habermas
-- Group 5 | Project Part 2
-- Raghav, Charki,Maple, Ramanz,

DROP DATABASE IF EXISTS CollegeConnection;

CREATE DATABASE CollegeConnection;
USE CollegeConnection;

DROP TABLE IF EXISTS faculty;
CREATE TABLE faculty
(
    facultyID      INT UNSIGNED NOT NULL AUTO_INCREMENT,
    firstName      VARCHAR(255) NOT NULL,
    lastName       VARCHAR(255) NOT NULL,
    email          VARCHAR(320) NOT NULL,
    buildingNumber INT          NULL,
    officeNumber   INT          NULL,
    PRIMARY KEY (facultyID)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8;

ALTER TABLE faculty
    AUTO_INCREMENT = 100;

DROP TABLE IF EXISTS faculty_topics;
CREATE TABLE faculty_topics
(
    KeywordID INT UNSIGNED NOT NULL AUTO_INCREMENT,
    word      VARCHAR(80)  NOT NULL, -- set to 80 as the longest word in the english dictionary is only 45 characters long.
    PRIMARY KEY (KeywordID)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8;

DROP TABLE IF EXISTS faculty_keyword;
CREATE TABLE faculty_keyword
(
    KeywordID INT UNSIGNED NOT NULL,
    facultyID INT UNSIGNED NOT NULL,
    PRIMARY KEY (KeywordID, facultyID),
    CONSTRAINT faculty_keyword_keyword_FK
        FOREIGN KEY (KeywordID)
            REFERENCES faculty_topics (KeywordID)
            ON DELETE CASCADE
            ON UPDATE CASCADE,
    CONSTRAINT faculty_keyword_faculty_FK
        FOREIGN KEY (facultyID)
            REFERENCES faculty (facultyID)
            ON DELETE CASCADE
            ON UPDATE CASCADE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8;



DROP TABLE IF EXISTS abstract;
CREATE TABLE abstract
(
    abstractID INT UNSIGNED NOT NULL AUTO_INCREMENT,
    abs_text   MEDIUMTEXT   NOT NULL,
    PRIMARY KEY (abstractID)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8;

ALTER TABLE abstract
    AUTO_INCREMENT = 100;

DROP TABLE IF EXISTS faculty_abstract;
CREATE TABLE faculty_abstract
(
    facultyID  INT UNSIGNED NOT NULL,
    abstractID INT UNSIGNED NOT NULL,
    PRIMARY KEY (facultyID, abstractID),
    CONSTRAINT faculty_abstract_faculty_FK
        FOREIGN KEY (facultyID)
            REFERENCES faculty (facultyID)
            ON DELETE CASCADE
            ON UPDATE CASCADE,
    CONSTRAINT faculty_abstract_abstract_FK
        FOREIGN KEY (abstractID)
            REFERENCES abstract (abstractID)
            ON DELETE CASCADE
            ON UPDATE CASCADE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8;

DROP TABLE IF EXISTS student;
CREATE TABLE student
(
    StudentID INT UNSIGNED NOT NULL AUTO_INCREMENT,
    firstName VARCHAR(255) NOT NULL,
    lastName  VARCHAR(255) NOT NULL,
    email     VARCHAR(320) NOT NULL,
    PRIMARY KEY (StudentID)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8;

ALTER TABLE student
    AUTO_INCREMENT = 100;

DROP TABLE IF EXISTS student_topics;
CREATE TABLE student_topics
(
    KeywordID INT UNSIGNED NOT NULL AUTO_INCREMENT,
    word      VARCHAR(80)  NOT NULL, -- set to 80 as the longest word in the english dictionary is only 45 characters long.
    PRIMARY KEY (KeywordID)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8;

DROP TABLE IF EXISTS student_keyword;
CREATE TABLE student_keyword
(
    KeywordID INT UNSIGNED NOT NULL,
    StudentID INT UNSIGNED NOT NULL,
    PRIMARY KEY (KeywordID, StudentID),
    CONSTRAINT student_keyword_keyword_FK
        FOREIGN KEY (KeywordID)
            REFERENCES student_topics (KeywordID)
            ON DELETE CASCADE
            ON UPDATE CASCADE,
    CONSTRAINT student_keyword_student_FK
        FOREIGN KEY (StudentID)
            REFERENCES student (StudentID)
            ON DELETE CASCADE
            ON UPDATE CASCADE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8;

-- Adds a keyword to a student
DROP PROCEDURE IF EXISTS addStudentKeyword;
DELIMITER //
CREATE PROCEDURE addStudentKeyword(
    IN in_studentID INT,
    IN in_word VARCHAR(80)
)
BEGIN
    DECLARE wordID INT;
    -- Store the ID of the keyword

    -- Test if the word already exists in the table of keywords.
    IF in_word NOT IN (SELECT word FROM student_topics) THEN
        -- If the word doesn't already exist, in the table, insert it
        INSERT INTO student_topics (word) VALUES (in_word);
    END IF;

    -- Get the ID of the word in the keywords table
    SELECT student_topics.KeywordID
    INTO wordID
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
    DECLARE wordID INT;
    -- Store the ID of the keyword
    -- Test if the word even exists in the keyword table.
    -- Get the word ID
    SELECT student_topics.KeywordID
    INTO wordID
    FROM student_topics
    WHERE student_topics.word LIKE p_word;

    -- Actually delete the record
    DELETE
    FROM student_keyword
    WHERE StudentID = p_studentID
      AND KeywordID = wordID;
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
        wordID INT;
    -- Store the ID of the keyword
    -- Test if the word even exists in the keyword table.
    -- Get the word ID
    SELECT faculty_topics.KeywordID
    INTO wordID
    FROM faculty_topics
    WHERE faculty_topics.word LIKE p_word;

    -- Actually delete the record
    DELETE
    FROM faculty_keyword
    WHERE facultyID = p_facultyID
      AND KeywordID = wordID;
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
    DECLARE wordID INT;
    -- Store the ID of the keyword

    -- Test if the word already exists in the table of keywords.
    IF in_word NOT IN (SELECT word FROM faculty_topics) THEN
        -- If the word doesn't already exist, in the table, insert it
        INSERT INTO faculty_topics (word) VALUES (in_word);
    END IF;

    -- Get the ID of the word in the keywords table
    SELECT faculty_topics.KeywordID
    INTO wordID
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
                 LEFT JOIN faculty_keyword USING (facultyID)
                 LEFT JOIN faculty_topics ON faculty_keyword.KeywordID = faculty_topics.KeywordID
                 LEFT JOIN faculty_abstract ON faculty.facultyID = faculty_abstract.facultyID
                 LEFT JOIN abstract ON faculty_abstract.abstractID = abstract.abstractID
        WHERE faculty_topics.word LIKE ('%' || keywordOne || '%')
           OR (abstract.abs_text LIKE ('%' || keywordOne || '%'));
        -- If only two keywords are provided, ignore keywordThree
    ELSEIF keywordThree = '' THEN
        SELECT DISTINCT faculty.lastName, faculty.firstName, faculty.email, faculty.buildingNumber, faculty.officeNumber
        FROM faculty
                 LEFT JOIN faculty_keyword USING (facultyID)
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
                 LEFT JOIN faculty_keyword USING (facultyID)
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
                 LEFT JOIN student_keyword USING (StudentID)
                 LEFT JOIN student_topics ON student_keyword.KeywordID = student_topics.KeywordID
        WHERE student_topics.word LIKE ('%' || keywordOne || '%');
        -- If only two keywords are provided, ignore keywordThree
    ELSEIF keywordThree = '' THEN
        SELECT DISTINCT student.lastName, student.firstName, student.email, student.StudentID
        FROM student
                 LEFT JOIN student_keyword USING (StudentID)
                 LEFT JOIN student_topics ON student_keyword.KeywordID = student_topics.KeywordID
        WHERE student_topics.word LIKE ('%' || keywordOne || '%')
           OR student_topics.word LIKE ('%' || keywordTwo || '%');
        -- If three keywords are provided.
    ELSE
        SELECT DISTINCT student.lastName, student.firstName, student.email, student.StudentID
        FROM student
                 LEFT JOIN student_keyword USING (StudentID)
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
<<<<<<< HEAD
VALUES ('Brom', 'Holcombson', 'Ironscale@varden.org', 5, 7871),
       ('Iron', 'Galbatorix', 'Firstforsworn@broddring.net', 5, 0133),
       ('Oromis', 'Thrándurin', 'Emosage@eldunari.net', 5, 7200),
       ('Angela', 'Uluthrek', 'madwomanwizened@varden.org', 5, 0022);
=======
VALUES
('Brom', 'Holcombson','Ironscale@varden.org', 5, 7871),
('Iron', 'Galbatorix','Firstforsworn@broddring.net',5, 0133),
('Oromis', 'Thrándurin', 'Emosage@eldunari.net', 5, 7200),
('Angela', 'Uluthrek', 'madwomanwizened@varden.org', 5, 0022),
('Annalise', 'Cainhurst', 'hercorruptmajesty@vileblood.edu', 4, 4200),
('Martin', 'Logarius', 'grandexecutioner@sanctum.edu', 4, 2560),
('Florence', 'Gehrman', 'firsthunter@workshop.edu', 4, 1280),
('Percival', 'Willem', 'headprovost@byrgenwerth.edu', 4, 2545),
('Randal', 'Carter', 'professorunspeakable@miskatonic.edu', 10, 1920),
('Harley', 'Warren', 'barrowsleeper@arkham.edu', 10, 1000);
>>>>>>> 228350c (The inserts to prebuild the database have been completed.)

-- Holcombson
CALL addFacultyKeyword(100, 'dracology');
CALL addFacultyKeyword(100, 'fencing');
CALL addFacultyKeyword(100, 'mysticism');
CALL addFacultyKeyword(100, 'history');
CALL buildFacultyAbs(100, 'Keep in mind that many people have died for their beliefs; it’s actually quite common. The real courage is in living and suffering for what you believe.');

-- Galbatorix
CALL addFacultyKeyword(101, 'dracology');
CALL addFacultyKeyword(101, 'dark arts');
CALL addFacultyKeyword(101, 'mysticism');
CALL addFacultyKeyword(101, 'chemistry');
CALL buildFacultyAbs(101, 'To whom is not the issue. When the Varden learn that the legend is real, they will be encouraged to challenge me. And I am not interested in being challenged.');

-- Thrándurin
CALL addFacultyKeyword(102, 'dracology');
CALL addFacultyKeyword(102, 'fencing');
CALL addFacultyKeyword(102, 'mysticism');
CALL addFacultyKeyword(102, 'history');
CALL buildFacultyAbs(102, 'History provides us with numerous examples of people who were convinced that they were doing the right thing and committed terrible crimes because of it.');

-- Uluthrek
CALL addFacultyKeyword(103, 'dracology');
CALL addFacultyKeyword(103, 'mysticism');
CALL addFacultyKeyword(103, 'witchcraft');
CALL addFacultyKeyword(103, 'politics');
CALL buildFacultyAbs(103, 'He’s acting as foolish as a kitten . . . but then, everyone’s entitled to a little foolishness once in a while.');

-- Cainhurst
CALL addFacultyKeyword(104, 'dark arts');
CALL addFacultyKeyword(104, 'politics');
CALL addFacultyKeyword(104, 'witchcraft');
CALL addFacultyKeyword(104, 'epidemiology');
CALL buildFacultyAbs(104, 'Very well. Drink deep of Our blood. Feel the spreading corruption burn. Now, thou’rt too a Vileblood. We two, the very last on this earth. We await thy return. For the honor of Cainhurst.');

-- Logarius
CALL addFacultyKeyword(105, 'alchemy');
CALL addFacultyKeyword(105, 'mysticism');
CALL addFacultyKeyword(105, 'witchcraft');
CALL addFacultyKeyword(105, 'epidemiology');
CALL buildFacultyAbs(105, '
In his time, Master Logarius led his executioners into Cainhurst Castle to cleanse it of the Vilebloods.
But all did not go well and Master Logarius became a blessed anchor, guarding us from evil...Tragic, tragic times...that Master Logarius should be abandoned in the accursed domain of the Vilebloods.');

-- Gehrman
CALL addFacultyKeyword(106, 'witchcraft');
CALL addFacultyKeyword(106, 'fencing');
CALL addFacultyKeyword(106, 'epidemiology');
CALL addFacultyKeyword(106, 'psychology');
CALL buildFacultyAbs(106, 'Dear, oh dear. What was it? The Hunt? The Blood? Or the horrible dream? Oh, it does not matter... It always comes down to the Hunter''s helper to clean up after these sort of messes. Tonight, Gehrman joins the hunt...');

-- Willem
CALL addFacultyKeyword(107, 'history');
CALL addFacultyKeyword(107, 'epidemiology');
CALL addFacultyKeyword(107, 'psychology');
CALL addFacultyKeyword(107, 'mysticism');
CALL buildFacultyAbs(107, 'We are born of the blood, made men by the blood, undone by the blood. Our eyes are yet to open...
Fear the old blood.');

-- Carter
CALL addFacultyKeyword(108, 'psychology');
CALL addFacultyKeyword(108, 'witchcraft');
CALL addFacultyKeyword(108, 'astronomy');
CALL addFacultyKeyword(108, 'politics');
CALL buildFacultyAbs(108, 'In the midnight heavens burning
     Thro’ ethereal deeps afar,
Once I watch’d with restless yearning
     An alluring, aureate star;
Ev’ry eye aloft returning,
     Gleaming nigh the Arctic car.

Mystic waves of beauty blended
     With the gorgeous golden rays;
Phantasies of bliss descended
     In a myrrh’d Elysian haze;
And in lyre-born chords extended
     Harmonies of Lydian lays.

There (thought I) lies scenes of pleasure,
     Where the free and blessed dwell,
And each moment bears a treasure
     Freighted with a lotus-spell,
And there floats a liquid measure
     From the lute of Israfel.

There (I told myself) were shining
     Worlds of happiness unknown,
Peace and Innocence entwining
     By the Crowned Virtue’s throne;
Men of light, their thoughts refining
     Purer, fairer, than our own.

Thus I mus’d, when o’er the vision
     Crept a red delirious change;
Hope dissolving to derision,
     Beauty to distortion strange;
Hymnic chords in weird collision,
     Spectral sights in endless range.

Crimson burn’d the star of sadness
     As behind the beams I peer’d;
All was woe that seem’d but gladness
     Ere my gaze with truth was sear’d;
Cacodaemons, mir’d with madness,
     Thro’ the fever’d flick’ring leer’d.

Now I know the fiendish fable
     That the golden glitter bore;
Now I shun the spangled sable
     That I watch’d and lov’d before;
But the horror, set and stable,
     Haunts my soul for evermore.');

-- Warren
CALL addFacultyKeyword(109, 'psychology');
CALL addFacultyKeyword(109, 'dark arts');
CALL addFacultyKeyword(109, 'history');
CALL addFacultyKeyword(109, 'mysticism');
CALL buildFacultyAbs(109, 'Out of what crypt they crawl, I cannot tell,
But every night I see the rubbery things,
Black, horned, and slender, with membranous wings,
They come in legions on the north wind’s swell
With obscene clutch that titillates and stings,
Snatching me off on monstrous voyagings
To grey worlds hidden deep in nightmare’s well.

Over the jagged peaks of Thok they sweep,
Heedless of all the cries I try to make,
And down the nether pits to that foul lake
Where the puffed shoggoths splash in doubtful sleep.
But ho! If only they would make some sound,
Or wear a face where faces should be found!');


INSERT INTO student (firstName, lastName, email)
VALUES
('Eragon', 'Bromsson', 'brisingr@varden.org'),
('Murtagh', 'Morzansson', 'kingkiller20@broddring.net'),
('Saphira', 'Bjartskular', 'brightscale@varden.org'),
('Orik', 'Dûrgrimst Ingeitum', 'volundr@farthendur.com'),
('Eileen', 'Branwen', 'bloodycrow@workshop.edu'),
('Djura', 'Kegger', 'ashenhunter@powderkeg.edu'),
('Alfred', 'Wheeler', 'wheeldisciple@healingchurch.edu'),
('Ebrietas', 'Cosm', 'cosmogal@oeden.org'),
('Victor', 'Frankenstein', 'shellshocker@victoria.net'),
('Mina', 'Harker', 'garlicstake@helsing.net');

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

-- Saphira
CALL addStudentKeyword(102, 'dracology');
CALL addStudentKeyword(102, 'witchcraft');
CALL addStudentKeyword(102, 'mysticism');
CALL addStudentKeyword(102, 'metalurgy');

-- Orik
CALL addStudentKeyword(103, 'metalurgy');
CALL addStudentKeyword(103, 'fencing');
CALL addStudentKeyword(103, 'history');
CALL addStudentKeyword(103, 'politics');

-- Eileen
CALL addStudentKeyword(104, 'fencing');
CALL addStudentKeyword(104, 'epidemiology');
CALL addStudentKeyword(104, 'psychology');
CALL addStudentKeyword(104, 'security');

-- Djura
CALL addStudentKeyword(105, 'fencing');
CALL addStudentKeyword(105, 'chemistry');
CALL addStudentKeyword(105, 'epidemiology');
CALL addStudentKeyword(105, 'metalurgy');

-- Alfred
CALL addStudentKeyword(106, 'fencing');
CALL addStudentKeyword(106, 'mysticism');
CALL addStudentKeyword(106, 'history');
CALL addStudentKeyword(106, 'medecine');

-- Ebrietas
CALL addStudentKeyword(107, 'mysticism');
CALL addStudentKeyword(107, 'epidemiology');
CALL addStudentKeyword(107, 'medecine');
CALL addStudentKeyword(107, 'chemistry');

-- Victor
CALL addStudentKeyword(108, 'chemistry');
CALL addStudentKeyword(108, 'dark arts');
CALL addStudentKeyword(108, 'medecine');
CALL addStudentKeyword(108, 'biology');

-- Mina
CALL addStudentKeyword(109, 'medecine');
CALL addStudentKeyword(109, 'politics');
CALL addStudentKeyword(109, 'psychology');
CALL addStudentKeyword(109, 'dracology');