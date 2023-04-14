/*
File: match_keywords.sql
Author: Declan J. Naughton
*/

SHOW PROCEDURE STATUS WHERE db LIKE "College%";

DELIMITER $$

CREATE PROCEDURE keyword_to_id(
    IN p_word VARCHAR(45)
    OUT o_id
)
BEGIN


END $$


DELIMITER ;