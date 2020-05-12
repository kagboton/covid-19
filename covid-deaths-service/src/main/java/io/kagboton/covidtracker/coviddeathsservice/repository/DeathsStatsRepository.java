package io.kagboton.covidtracker.coviddeathsservice.repository;

import io.kagboton.covidtracker.coviddeathsservice.models.CountryDeathsStats;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface DeathsStatsRepository extends JpaRepository<CountryDeathsStats, String> {

    Optional<CountryDeathsStats> findBySlug(String slug);
}
