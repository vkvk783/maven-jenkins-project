package com.weather.monitoring.controller;

import com.weather.monitoring.model.DailySummary;
import com.weather.monitoring.model.Forecast;
import com.weather.monitoring.model.WeatherData;
import com.weather.monitoring.service.DailySummaryService;
import com.weather.monitoring.service.ForecastService;
import com.weather.monitoring.service.WeatherDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/weather")
public class WeatherSummaryController {

    @Autowired
    private DailySummaryService dailySummaryService;

    @Autowired
    private WeatherDataService weatherDataService;

    @Autowired
    private ForecastService forecastService;

    @GetMapping("/dailySummary")
    @ResponseBody
    public ResponseEntity<?> getDailySummary(@RequestParam("date") String date) {
        LocalDate parsedDate = LocalDate.parse(date);  // Parse the selected date
        List<DailySummary> dailySummaries = dailySummaryService.getDailySummaryByDate(parsedDate);

        if (dailySummaries.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body("No data found for the selected date.");
        }
        return ResponseEntity.ok(dailySummaries);
    }

    @GetMapping("/historyData")
    @ResponseBody
    public ResponseEntity<?> getHistoryData(@RequestParam("city") String city, @RequestParam("date") String date) {
        LocalDate parsedDate = LocalDate.parse(date);  // Parse the selected date

        List<WeatherData> historyData = weatherDataService.findAllByCityAndDate(city, parsedDate);

        if(historyData.isEmpty()){
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body("No data found for the selected date.");
        }
        return ResponseEntity.ok(historyData);
    }

    @GetMapping("/forecast")
    @ResponseBody
    public ResponseEntity<?> getDailyForecaste(@RequestParam("city") String city) {
        List<Forecast> forecastData = forecastService.getAllByCity(city);

        if (forecastData.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body("No data found for the selected date.");
        }
        return ResponseEntity.ok(forecastData);
    }

}
