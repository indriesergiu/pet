CREATE database IF NOT EXISTS xml_filter;

USE xml_filter;

CREATE TABLE file (
         id INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
         name VARCHAR(50) NOT NULL
       );

CREATE TABLE line (
         id INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
         file_id INT NOT NULL,
         nr INT NOT NULL,
         FOREIGN KEY (file_id) REFERENCES file(id) ON DELETE CASCADE,
         CHECK (nr >= 0)
       );

CREATE TABLE element (
         id INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
         file_id INT NOT NULL,
         line_id INT NOT NULL,
         nr INT NOT NULL,
         type ENUM('START_DOCUMENT', 'START_ELEMENT', 'DATA', 'COMMENT', 'END_ELEMENT', 'END_DOCUMENT') NOT NULL,
         prefix VARCHAR(100),
         localname VARCHAR(100),
         data VARCHAR(1000),
         encoding VARCHAR(100),
         version VARCHAR(100),
         FOREIGN KEY (file_id) REFERENCES file(id) ON DELETE CASCADE,
         FOREIGN KEY (line_id) REFERENCES line(id) ON DELETE CASCADE,
         CHECK (nr >= 0)
       );

CREATE TABLE attribute (
         id INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
         element_id INT NOT NULL,
         name VARCHAR(100),
         value VARCHAR(100),
         FOREIGN KEY (element_id) REFERENCES element(id) ON DELETE CASCADE
       );