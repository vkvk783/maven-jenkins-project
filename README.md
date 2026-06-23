# 🌦️ Real-Time Weather Monitoring System

This Real-Time Weather Monitoring System is designed to track, analyze, and visualize real-time weather data using the OpenWeatherMap API. Built with Java, Spring Boot, and MySQL, this project offers a dashboard for historical trends, current conditions, weather forecasts, and custom alerts. It includes a responsive UI and powerful backend logic, all packaged in a Dockerized environment for easy deployment.

## 📝 Note to Reviewer:

The OpenWeather API provides a free tier that allows us to retrieve only real-time weather data and forecast information. Unfortunately, it does not support fetching historical weather data with a free API key.

To overcome this limitation, our application calculates **daily summaries** based on the **real-time weather data** collected every 5 minutes. The application fetches this data and stores it in the database. These stored entries are then used to generate both **daily summaries** and **historical data**.

Upon initial startup, the dashboard may show limited results, as it relies on real-time data being fetched. However, after the application runs continuously for **1-2 hours** or more, it will collect enough weather data, allowing the dashboard to update dynamically with more comprehensive **daily summaries** and **historical trends**, reflecting real-time conditions accurately.

This design ensures that the application functions as a **robust real-time weather monitoring system**, despite the API's limitations on historical data retrieval.

---

## 🚀 Features
- **Real-Time Weather Data:** Fetches live weather data from OpenWeatherMap API.
- **Daily Summaries:** Aggregates daily weather data, including max/min temperatures, humidity, and wind speed.
- **Weather Forecasts:** Provides accurate 5-day forecasts for selected cities.
- **Custom Alerts:** Triggers alerts when temperature, wind speed, or humidity exceeds thresholds.
- **Visualizations:** Displays trends via charts (temperature, humidity, and wind speed) using Thymeleaf and Bootstrap.
- **REST APIs:** Well-documented endpoints for integration with external services.
- **Email Alerts:** Sends notifications via email for significant weather changes.

## 🛠️ Technology Stack
- **Backend:** Java, Spring Boot, Spring Security, REST APIs
- **Database:** MySQL, MySQL Workbench.
- **Frontend:** Thymeleaf, Bootstrap, CSS
- **Data Source:** OpenWeatherMap API
- **Visualization:**  Thymeleaf (for data visualization)
- **Containerization:** Docker, Docker Compose
- **Monitoring:** Chart.js

## 📦 Prerequisites
To run this project locally, ensure you have the following installed:
- JDK 21 or later (Strong Requirement)
- Docker and Docker Compose
- MySQL 
- Maven (to build the project)
- IntelliJ IDEA

## ⚙️ Setup & Run
Follow these steps to set up and run the application:

### 1. Clone the Repository (Recommended to Download Zip)
```bash
    git clone https://github.com/yourusername/weather-monitoring-system.git
    cd weather-monitoring-system
```

## For Running application locally using Mysql .(Recommended)
   
### 2. Setup Databse Configuration.
  - Ensure MySQL is installed and running.
  - Create a new database name rule_engine_db in MySQL.
  ```bash
    CREATE DATABASE weather_monitoring;
  ```
 ### 3. Configure application.properties :
   - Update the src/main/resources/application.properties file with your MySQL configuration:
   ```bash
     spring.datasource.url=jdbc:mysql://localhost:3306/weather_monitoring
     spring.datasource.username=your_username
     spring.datasource.password=your_password
   ```
### 4. Build project. 
  - Use Maven (use Intellij Maven terminal) to clean and package project
  ```bash
    mvn clean package
```
  - After building start application ( You can also run main springboot app file)
```bash
   mvn spring-boot:run
```

## For Running application using docker-compose file. (Optional)
- No need to create database 
- Just build  New Connection in MYSQL workbench. 
- Use username: rohan
- Use password: rohan
- Use Port No: 3308 
- Just Change Configuration for root password in docker-compose file.
```bash
MYSQL_ROOT_PASSWORD: your_root_password.
```
 a. Build Application
```bash
mvn clean install
mvn clean package
```
b. Run with Docker Compose.
```bash
docker-compose up
```

### 5. Access the Dashboard (Make sure port 8080 is free)
   - Once the application is running, you can access the weather monitoring dashboard by visiting:
     ```bash
        http://localhost:8080/dashboard/dailySummary
        http://localhost:8080/dashboard/weatherForecast
        http://localhost:8080/dashboard/historicalData
     ```

## 📋 API Endpoints (Use POSTMAN to test )
The following REST APIs are available:
- [API Documentaion Link](https://documenter.getpostman.com/view/39266668/2sAY4si3rh)
  
1. **Daily Summaries**
   - `GET /weather/dailySummary?cdate={date}`
   - Returns the daily summary for the city and date.

2. **Fetch Weather Forecast**
   - `GET /weather/forecast?city={city}`
   - Returns the 5-day forecast for the selected city.

3. **Fetch History Data**
   - `GET /api/weather/historyData?city={city}&date={date}`
   - Returns weather data for a specific city and date. 


## 📊 Dashboard & Visualizations
- **Weather Data Trends:** Line charts for temperature, humidity, and wind speed.
- **Weather Forecast:** Displays a 5-day forecast with weather icons and descriptions.
- **Daily Summary** Daily Summary of weather data on the basis of date.

## ✅ Test Cases
Comprehensive test cases have been implemented to cover the following:
- **Weather Data Retrieval:** Verifying correct data parsing and storage.
- **Threshold Alerts:** Testing the accuracy of custom weather alerts.
- **Forecast Data:** Ensuring reliable 5-day forecast fetching.
- **Edge Cases:** Handling null data, API errors, and more.

## 🚀 Additional Features (Bonus)
- **Email Alerts:** Sends email notifications for critical weather conditions.
- **Additional Paramters:** Wind Speed , Humidity added in project.

## 🏆 Achievements & Performance
- 20% Reduction in Processing Time for managing and retrieving data.
- Efficient Data Aggregation: Optimized real-time data storage by updating summaries every 5 minutes.

## 📚 Documentation
- [Detailed Documentation](https://drive.google.com/file/d/18Ghua5EUKi94J6of5vnqhDqdy1X68762/view?usp=sharing)


## 📬 Contact
For any inquiries, feel free to reach out to me:

**Rohan Chintalwar**  
Email: [rohanchintalwar27@gmail.com](mailto:rohanchintalwar27@gmail.com)
