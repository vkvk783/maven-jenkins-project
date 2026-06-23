package com.weather.monitoring.repository;

import com.weather.monitoring.model.Forecast;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


public interface ForecastRepository extends JpaRepository<Forecast, Long> {

    @Modifying
    @Transactional
    @Query(value = "ALTER TABLE forecast AUTO_INCREMENT = 1", nativeQuery = true)
    void resetAutoIncrement();

    List<Forecast> getAllByCity(String city);

}
