USE collegeconnection;
INSERT INTO Faculty (firstName, lastName, email, password, phone, address)
                    VALUES ('Darth', 'Vader', 'dv1234@deathstar.com', 'anakin','123456790', '1 Death Star');
INSERT INTO Faculty (firstName, lastName, email, password, phone, address)
    VALUES ('Darthest', 'Vaderest', 'dv1234@deathstar.com', 'anakin','123456790', '1 Death Star');

INSERT INTO Faculty_Topic (keyword) VALUES ('lightsabers');
INSERT INTO Faculty_Keyword (facultyID, keywordID) VALUES (1, 1);
INSERT INTO Abstract (title, description) VALUES ('The ways of the Sith', 'Describing evil start wars magic');
INSERT INTO Faculty_Abstract (facultyID, abstractID) VALUES (2, 1);

INSERT INTO Student (firstName, lastName, email, password) VALUES ('Luke', 'Skywalker', 'luke@rebels.com', 'greenisgood');
INSERT INTO Student_Topic (keyword) VALUES ('lightsabers');
INSERT INTO Student_Topic (keyword) VALUES ('evil');
INSERT INTO Student_Keyword (studentID, keywordID) VALUES (1, 1);
INSERT INTO Student_Keyword (studentID, keywordID) VALUES (1, 2);



