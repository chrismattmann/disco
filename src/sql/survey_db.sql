-- Copyright (c) 2007 University of Southern California.
-- All rights reserved.                                            
--                                                               
-- Redistribution and use in source and binary forms are permitted
-- provided that the above copyright notice and this paragraph are
-- duplicated in all such forms and that any documentation, 
-- advertising materials, and other materials related to such 
-- distribution and use acknowledge that the software was 
-- developed by the Software Architecture Research Group at the 
-- University of Southern Calfornia.  The name of the University may 
-- not be used to endorse or promote products derived from this software 
-- without specific prior written permission.
--
-- Any questions, comments, or or corrections should be mailed to the author 
-- of this code, Chris Mattmann, at: mattmann@usc.edu
--
--
-- THIS SOFTWARE IS PROVIDED "AS IS" AND WITHOUT ANY EXPRESS OR 
-- IMPLIED WARRANTIES, INCLUDING, WITHOUT LIMITATION, THE IMPLIED 
-- WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR 
-- PURPOSE.
--
--

CREATE TABLE students (
	student_id INT NOT NULL AUTO_INCREMENT,
	student_username VARCHAR(255) DEFAULT '' NOT NULL,
	is_den TINYINT DEFAULT 0 NOT NULL,
	PRIMARY KEY (student_id)
) ;

CREATE TABLE survey_p1 (
	p1_rowkey INT NOT NULL AUTO_INCREMENT,
	survey_id INT DEFAULT 0 NOT NULL,
	student_id INT DEFAULT 0 NOT NULL,
	question_num INT DEFAULT 0 NOT NULL,
	answer CHAR(1) DEFAULT '' NOT NULL,
	PRIMARY KEY (p1_rowkey)
) ;

CREATE TABLE survey_p2 (
   p2_rowkey INT NOT NULL AUTO_INCREMENT,
   survey_id INT DEFAULT 0 NOT NULL,
   student_id INT NOT NULL,
   scenario_num INT DEFAULT 1 NOT NULL,
   connector_name VARCHAR(255) NOT NULL,
   is_appropriate BOOL DEFAULT 0 NOT NULL,
   PRIMARY KEY (p2_rowkey)
) ;

CREATE TABLE surveys (
  survey_id INT NOT NULL AUTO_INCREMENT,
  student_id INT NOT NULL,
  PRIMARY KEY (survey_id)
);
