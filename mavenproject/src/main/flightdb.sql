TABLE direct_flights (
    id INT PRIMARY KEY AUTO_INCREMENT,
    flight_no VARCHAR(20),
    origin VARCHAR(10),
    destination VARCHAR(10),
    depart_time DATETIME
);
