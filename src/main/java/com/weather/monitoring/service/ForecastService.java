package com.weather.monitoring.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.weather.monitoring.model.Forecast;
import com.weather.monitoring.repository.ForecastRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class ForecastService {

    private final ForecastRepository forecastRepository;
    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;

    @Value("${openweathermap.api.url}")
    private String apiUrl;

    @Value("${openweathermap.api.key}")
    private String apiKey;

    @Autowired
    public ForecastService(ForecastRepository forecastRepository,
                           RestTemplate restTemplate,
                           ObjectMapper objectMapper) {
        this.forecastRepository = forecastRepository;
        this.restTemplate = restTemplate;
        this.objectMapper = objectMapper;
    }

    @Scheduled(fixedRate = 300000)  // Every 5 minutes
    public void fetchAndSaveWeatherForecast() {

        // Clear the forecast table for every 5 minutes
        forecastRepository.deleteAll();

        // Reset the auto-increment counter (Assuming you are using MySQL)
        forecastRepository.resetAutoIncrement();

        String[] cities = {"Delhi", "Mumbai", "Chennai", "Bangalore", "Kolkata", "Hyderabad"};

        for (String city : cities) {
            String url = String.format("%s?q=%s&appid=%s", apiUrl, city, apiKey);

            String response = restTemplate.getForObject(url, String.class);
            try {
                JsonNode jsonResponse = objectMapper.readTree(response);

                double tempInKelvin = jsonResponse.get("main").get("temp").asDouble();
                double temperature = convertKelvinToCelsius(tempInKelvin);
                double humidity = jsonResponse.get("main").get("humidity").asDouble();
                double windSpeed = jsonResponse.get("wind").get("speed").asDouble();

                LocalDateTime currentForecastTime = LocalDateTime.now();

                // Create a new entry as we have cleared the table
                Forecast forecast = new Forecast();
                forecast.setCity(city);
                forecast.setTemperature(temperature);
                forecast.setHumidity(humidity);
                forecast.setWindSpeed(windSpeed);
                forecast.setForecastTime(currentForecastTime);
                forecastRepository.save(forecast);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public double convertKelvinToCelsius(double kelvin) {
        double celsius = kelvin - 273.15;
        BigDecimal roundedCelsius = BigDecimal.valueOf(celsius).setScale(2, RoundingMode.HALF_UP);
        return roundedCelsius.doubleValue();
    }


    public List<Forecast> getAllByCity(String city){
        return  forecastRepository.getAllByCity(city);
    }
}
