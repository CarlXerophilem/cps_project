CREATE DATABASE flight_booking
    CREATE TABLE `Personal_Information`(
        `Register_id` INT PRIMARY KEY NOT NULL AUTO_INCREMENT,
        `name` VARCHAR(50) NOT NULL,
        `phone_number` INT,
        `passport_number` VARCHAR(50)
    );

    DESCRIBE `Personal_Information`;


