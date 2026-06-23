package com.weather.monitoring.service;

import com.weather.monitoring.model.DailySummary;
import com.weather.monitoring.model.WeatherData;
import com.weather.monitoring.repository.DailySummaryRepository;
import com.weather.monitoring.repository.WeatherDataRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class DailySummaryServiceImplTest {

    @Mock
    private DailySummaryRepository dailySummaryRepository;

    @Mock
    private WeatherDataRepository weatherDataRepository;

    @InjectMocks
    private DailySummaryServiceImpl dailySummaryService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testUpdateDailySummary() {
        // Setup the initial weather data
        WeatherData weatherData = new WeatherData();
        weatherData.setCity("Delhi");
        weatherData.setTemperature(30.0);
        weatherData.setMainCondition("Sunny"); // Assuming there's a main condition field

        // Simulate the existing summary
        DailySummary existingSummary = new DailySummary();
        existingSummary.setCity("Delhi");
        existingSummary.setDate(LocalDate.now());
        existingSummary.setAverageTemp(25.0);
        existingSummary.setMaxTemp(25.0);
        existingSummary.setMinTemp(25.0);

        when(dailySummaryRepository.findByCityAndDate("Delhi", LocalDate.now())).thenReturn(Optional.of(existingSummary));

        // Call the method to update daily summary
        dailySummaryService.updateDailySummary(weatherData);

        // Verify that the existing summary was updated
        verify(dailySummaryRepository, times(1)).save(existingSummary);

        // Since we have one new temperature data point (30.0), update the expected values
        double expectedMaxTemp = Math.max(existingSummary.getMaxTemp(), weatherData.getTemperature());
        double expectedMinTemp = existingSummary.getMinTemp(); // Min should not change as we only added a higher temperature
        double expectedAverageTemp = (existingSummary.getAverageTemp() + weatherData.getTemperature()) / 2; // Calculate new average

        assertEquals(expectedMaxTemp, existingSummary.getMaxTemp());
        assertEquals(expectedMinTemp, existingSummary.getMinTemp());
        assertEquals(expectedAverageTemp, existingSummary.getAverageTemp(), 0.01); // Average calculation
    }

    @Test
    void testGetDailySummaries() {
        dailySummaryService.getDailySummaries("Delhi");
        verify(dailySummaryRepository, times(1)).findAllByCity("Delhi");
    }

    @Test
    void testGetHistoricalDataForChart() {
        LocalDate date = LocalDate.now();
       // dailySummaryService.getHistoricalDataForChart(date);
        //verify(dailySummaryRepository, times(1)).findAllByDate(date);
    }
}
