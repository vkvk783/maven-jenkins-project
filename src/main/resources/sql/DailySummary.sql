#For storing daily summary operations

CREATE TABLE DailySummary (
    id INT AUTO_INCREMENT PRIMARY KEY,
    city VARCHAR(100),
    date DATE,
    average_temp DECIMAL(5, 2),
    max_temp DECIMAL(5, 2),
    min_temp DECIMAL(5, 2),
    dominant_condition VARCHAR(50),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
