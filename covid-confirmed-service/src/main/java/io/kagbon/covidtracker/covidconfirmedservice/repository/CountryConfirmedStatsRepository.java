package io.kagbon.covidtracker.covidconfirmedservice.repository;

import io.kagbon.covidtracker.covidconfirmedservice.models.CountryConfirmedStats;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CountryConfirmedStatsRepository extends JpaRepository<CountryConfirmedStats, String> {

    Optional<CountryConfirmedStats> findBySlug(String slug);
}
