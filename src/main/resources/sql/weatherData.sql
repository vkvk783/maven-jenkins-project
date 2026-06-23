# For storing weather data.

CREATE TABLE WeatherData (
    id INT AUTO_INCREMENT PRIMARY KEY,
    city VARCHAR(100),
    main_condition VARCHAR(50),
    temperature DECIMAL(5, 2),
    feels_like DECIMAL(5, 2),
    update_time TIMESTAMP,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

