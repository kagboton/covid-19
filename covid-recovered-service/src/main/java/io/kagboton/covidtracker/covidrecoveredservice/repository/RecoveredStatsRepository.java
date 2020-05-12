package io.kagboton.covidtracker.covidrecoveredservice.repository;

import io.kagboton.covidtracker.covidrecoveredservice.models.CountryRecoveredStats;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RecoveredStatsRepository extends JpaRepository<CountryRecoveredStats, String> {

    Optional<CountryRecoveredStats> findBySlug(String slug);
}
