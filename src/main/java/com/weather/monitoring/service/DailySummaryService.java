package com.weather.monitoring.service;

import com.weather.monitoring.model.DailySummary;
import com.weather.monitoring.model.WeatherData;

import java.time.LocalDate;
import java.util.List;

public interface DailySummaryService {
    // Retrieves the daily summaries for a particular city
    List<DailySummary> getDailySummaries(String city);

    // Retrieves the daily summaries for a particular date
    List<DailySummary> getDailySummaryByDate(LocalDate date);

    // Updates the daily summary based on the latest weather data
    void updateDailySummary(WeatherData weatherData);

}
