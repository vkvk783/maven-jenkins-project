package com.weather.monitoring.service;

import com.weather.monitoring.model.WeatherData;
import java.time.LocalDate;
import java.util.List;

public interface WeatherDataService {

    void fetchAndSaveWeatherData();

    List<WeatherData> findAllByCityAndDate(String city, LocalDate date);

}
