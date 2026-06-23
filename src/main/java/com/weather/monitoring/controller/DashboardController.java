package com.weather.monitoring.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/dashboard")
public class DashboardController {

    @GetMapping("")
    public String dashboard(){
        return "dashboard";
    }

    @GetMapping("/dailySummary")
    public String getDailySummary(){
        return "daily-summary";
    }

    @GetMapping("/weatherForecast")
    public String getWeatherForecast(){
        return "weather-forecast";
    }

    @GetMapping("/historicalData")
    public String getHistoricalData(){
        return "historical-data";
    }


}
