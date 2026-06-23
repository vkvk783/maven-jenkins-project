package com.weather.monitoring.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.weather.monitoring.model.WeatherData;
import com.weather.monitoring.repository.WeatherDataRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.logging.Logger;

@Service
public class WeatherDataServiceImpl implements WeatherDataService {

    Logger logger = Logger.getLogger(getClass().getName());

    private final WeatherDataRepository weatherDataRepository;
    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;

    // Configuration properties
    private double tempThreshold;
    private String mainConditionAlert;
    private int consecutiveUpdatesForAlert;

    // to count alert consecutive updates
    private int consecutiveExceedCount = 0;

    @Autowired
    DailySummaryService dailySummaryService;

    @Autowired
    private EmailService emailService; // Injecting EmailService

    @Value("${openweathermap.api.url}")
    private String apiUrl;

    @Value("${openweathermap.api.key}")
    private String apiKey;

    // Constructor
    public WeatherDataServiceImpl(WeatherDataRepository weatherDataRepository,
                                  RestTemplate restTemplate,
                                  ObjectMapper objectMapper,
                                  @Value("${tempThreshold}") double tempThreshold,
                                  @Value("${weather.alert.mainCondition}") String mainConditionAlert,
                                  @Value("${weather.alert.consecutive.updates}") int consecutiveUpdatesForAlert) {
        this.weatherDataRepository = weatherDataRepository;
        this.restTemplate = restTemplate;
        this.objectMapper = objectMapper;
        this.tempThreshold = tempThreshold;
        this.mainConditionAlert = mainConditionAlert;
        this.consecutiveUpdatesForAlert = consecutiveUpdatesForAlert;
    }


    @Override
    @Scheduled(fixedRate = 300000)  // 300000 ms = 5 minutes
    public void fetchAndSaveWeatherData() {
        // for metro cities in India
        String[] cities = {"Delhi", "Mumbai", "Chennai", "Bangalore", "Kolkata", "Hyderabad"};

        for (String city : cities) {
            String url = String.format("%s?q=%s&appid=%s", apiUrl, city, apiKey);

            // Fetch Weather Data
            String response = restTemplate.getForObject(url, String.class);
            try {
                JsonNode jsonResponse = objectMapper.readTree(response);

                // Extract weather data from JSON
                String mainCondition = jsonResponse.get("weather").get(0).get("main").asText();
                double tempInKelvin = jsonResponse.get("main").get("temp").asDouble();
                double temperature = convertKelvinToCelsius(tempInKelvin); // Use conversion method

                // Extract additional parameters: humidity and wind speed
                double humidity = jsonResponse.get("main").get("humidity").asDouble();
                double windSpeed = jsonResponse.get("wind").get("speed").asDouble();

                // Extract the Unix timestamp and convert it to LocalDate
                long timestamp = jsonResponse.get("dt").asLong();
                LocalDate date = Instant.ofEpochSecond(timestamp)
                        .atZone(ZoneId.systemDefault()) // Use system's default timezone
                        .toLocalDate(); // Extract the LocalDate

                // Save weather data
                WeatherData weatherData = new WeatherData();
                weatherData.setCity(city);
                weatherData.setMainCondition(mainCondition);
                weatherData.setTemperature(temperature);
                weatherData.setFeelsLike(convertKelvinToCelsius(jsonResponse.get("main").get("feels_like").asDouble())); // Convert feels like
                weatherData.setHumidity(humidity); // Set humidity
                weatherData.setWindSpeed(windSpeed); // Set wind speed
                weatherData.setUpdateTime(LocalDateTime.now());
                weatherData.setDate(date); // Set the date

                weatherDataRepository.save(weatherData);

                // Create daily summary after saving weather data
                // Update the daily summary
                dailySummaryService.updateDailySummary(weatherData);

                // Check for threshold breach
                checkWeatherAlerts(temperature, mainCondition);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void checkWeatherAlerts(double temperature, String mainCondition) {
        checkForThresholdBreach(temperature, mainCondition);
    }

    protected void checkForThresholdBreach(double temperature, String mainCondition) {
        // Check temperature threshold breach
        if (temperature >= tempThreshold) {
            consecutiveExceedCount++;
            if (consecutiveExceedCount >= consecutiveUpdatesForAlert) {
                triggerAlert("Temperature exceeded " + tempThreshold + "°C for " + consecutiveUpdatesForAlert + " consecutive updates!");
                consecutiveExceedCount = 0; // Reset counter after alert
            }
        } else {
            consecutiveExceedCount = 0; // Reset if condition not met
        }

        // Check weather condition threshold breach
        if (mainCondition.equalsIgnoreCase(mainConditionAlert)) {
            triggerAlert("Weather condition alert: " + mainConditionAlert);
        }
    }

   private void triggerAlert(String alertMessage) {

        //Lets implement logging
        logger.info(alertMessage);

        // Send email notification
        emailService.sendAlertEmail("rohanchintalwar27@gmail.com", "Weather Alert", alertMessage);
    }

    // Temperature conversion methods
    public double convertKelvinToCelsius(double kelvin) {
        double celsius = kelvin - 273.15;
        // Convert to BigDecimal and round to 2 decimal places
        BigDecimal roundedCelsius = BigDecimal.valueOf(celsius).setScale(2, RoundingMode.HALF_UP);
        return roundedCelsius.doubleValue();
    }

    @Override
    public List<WeatherData> findAllByCityAndDate(String city, LocalDate date) {
        return weatherDataRepository.findAllByCityAndDate(city,date);
    }


}
