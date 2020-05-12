package io.kagboton.covidtracker.covidconfirmedservice.repository;

import io.kagboton.covidtracker.covidconfirmedservice.models.CountryConfirmedStats;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ConfirmedStatsRepository extends JpaRepository<CountryConfirmedStats, String> {

    Optional<CountryConfirmedStats> findBySlug(String slug);
}
