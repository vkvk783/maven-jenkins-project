package com.weather.monitoring.repository;

import com.weather.monitoring.model.DailySummary;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.Optional;
import java.util.List;

@Repository
public interface DailySummaryRepository extends JpaRepository<DailySummary, Long> {

      Optional<DailySummary> findByCityAndDate(String city, LocalDate date);
      List<DailySummary> findAllByCity(String city);
      List<DailySummary> findByDate(LocalDate date);

      // Optional: For fetching all summaries
      List<DailySummary> findAll();

}
