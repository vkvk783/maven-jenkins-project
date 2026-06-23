package com.weather.monitoring.service;

import com.weather.monitoring.model.DailySummary;
import com.weather.monitoring.model.WeatherData;
import com.weather.monitoring.repository.DailySummaryRepository;
import com.weather.monitoring.repository.WeatherDataRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class DailySummaryServiceImpl implements DailySummaryService {

    private final DailySummaryRepository dailySummaryRepository;
    private final WeatherDataRepository weatherDataRepository;

    // Variables to keep track of total temperature, count, max, and min for the current day
    private double totalTemp;
    private double totalHumidity;
    private int count;
    private double dailyMaxTemp; // variable for daily max temperature
    private double dailyMinTemp; // variable for daily min temperature
    private double dailyMaxWindSpeed; // variable for daily max wind speed
    private double dailyMinWindSpeed; // variable for daily min wind speed
    private LocalDate currentDate; // Track the current date for comparison

    public DailySummaryServiceImpl(DailySummaryRepository dailySummaryRepository,
                                   WeatherDataRepository weatherDataRepository) {
        this.dailySummaryRepository = dailySummaryRepository;
        this.weatherDataRepository = weatherDataRepository;
        // Initialize totalTemp, count, dailyMaxTemp, dailyMinTemp, and currentDate
        this.totalTemp = 0.0;
        this.totalHumidity = 0.0;
        this.count = 0;
        this.dailyMaxTemp = Double.NEGATIVE_INFINITY; // Start with the lowest possible value
        this.dailyMinTemp = Double.POSITIVE_INFINITY; // Start with the highest possible value
        this.dailyMaxWindSpeed = Double.NEGATIVE_INFINITY;
        this.dailyMinWindSpeed = Double.POSITIVE_INFINITY;
        this.currentDate = LocalDate.now(); // Set initial current date
    }

    @Override
    public void updateDailySummary(WeatherData newWeatherData) {
        String city = newWeatherData.getCity();
        LocalDate date = newWeatherData.getDate();

        // Find existing daily summary
        Optional<DailySummary> optionalDailySummary = dailySummaryRepository.findByCityAndDate(city, date);

        DailySummary dailySummary;
        if (optionalDailySummary.isPresent()) {
            dailySummary = optionalDailySummary.get();

            // Retrieve all weather data for the city and date
            List<WeatherData> weatherDataList = weatherDataRepository.findAllByCityAndDate(city, date);

            // Calculate max/min values and averages
            double maxTemp = weatherDataList.stream().mapToDouble(WeatherData::getTemperature).max().orElse(newWeatherData.getTemperature());
            double minTemp = weatherDataList.stream().mapToDouble(WeatherData::getTemperature).min().orElse(newWeatherData.getTemperature());
            double avgTemp = weatherDataList.stream().mapToDouble(WeatherData::getTemperature).average().orElse(newWeatherData.getTemperature());

            double maxWindSpeed = weatherDataList.stream().mapToDouble(WeatherData::getWindSpeed).max().orElse(newWeatherData.getWindSpeed());
            double minWindSpeed = weatherDataList.stream().mapToDouble(WeatherData::getWindSpeed).min().orElse(newWeatherData.getWindSpeed());
            double avgHumidity = weatherDataList.stream().mapToDouble(WeatherData::getHumidity).average().orElse(newWeatherData.getHumidity());

            // Update daily summary
            dailySummary.setMaxTemp(maxTemp);
            dailySummary.setMinTemp(minTemp);
            dailySummary.setAverageTemp(avgTemp);
            dailySummary.setMaxWindSpeed(maxWindSpeed);
            dailySummary.setMinWindSpeed(minWindSpeed);
            dailySummary.setAverageHumidity(avgHumidity);

            // Set the dominant condition
            String dominantCondition = calculateDominantCondition(weatherDataList);
            dailySummary.setDominantCondition(dominantCondition);

        } else {
            // If no summary exists, create a new one
            dailySummary = new DailySummary();
            dailySummary.setCity(city);
            dailySummary.setDate(date);
            dailySummary.setMaxTemp(newWeatherData.getTemperature());
            dailySummary.setMinTemp(newWeatherData.getTemperature());
            dailySummary.setAverageTemp(newWeatherData.getTemperature());
            dailySummary.setMaxWindSpeed(newWeatherData.getWindSpeed());
            dailySummary.setMinWindSpeed(newWeatherData.getWindSpeed());
            dailySummary.setAverageHumidity(newWeatherData.getHumidity());

            // Set the dominant condition
            dailySummary.setDominantCondition(newWeatherData.getMainCondition());
        }

        // Save the updated summary
        dailySummaryRepository.save(dailySummary);
    }

    @Override
    public List<DailySummary> getDailySummaries(String city) {
        return dailySummaryRepository.findAllByCity(city);
    }

    @Override
    public List<DailySummary> getDailySummaryByDate(LocalDate date) {
        return dailySummaryRepository.findByDate(date); // Fetch summaries for the specified date
    }

    private String calculateDominantCondition(List<WeatherData> weatherDataList) {
        Map<String, Long> conditionCounts = weatherDataList.stream()
                .collect(Collectors.groupingBy(WeatherData::getMainCondition, Collectors.counting()));

        return conditionCounts.entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey)
                .orElse(null); // Return null if no condition found
    }


}