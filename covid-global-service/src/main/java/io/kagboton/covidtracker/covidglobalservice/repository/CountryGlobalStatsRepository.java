package io.kagboton.covidtracker.covidglobalservice.repository;

import io.kagboton.covidtracker.covidglobalservice.models.CountryGlobalStats;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CountryGlobalStatsRepository extends JpaRepository<CountryGlobalStats, String> {

    /**
     * Find CountryGlobalStats by the country slug
     * @param slug
     * @return the optional
     */
    Optional<CountryGlobalStats> findBySlug(String slug );
}
