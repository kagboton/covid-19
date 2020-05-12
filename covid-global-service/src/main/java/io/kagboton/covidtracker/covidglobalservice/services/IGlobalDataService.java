package io.kagboton.covidtracker.covidglobalservice.services;

import io.kagboton.covidtracker.covidglobalservice.models.CountryGlobalStats;

import java.util.List;
import java.util.Optional;

public interface IGlobalDataService {

    /**
     * Recover existing country global stats
     * @return all covid stats
     */
    List<CountryGlobalStats> getAllCountryGlobalStats();

    /**
     * Get all covid stats for a country by country slug
     * @param slug
     * @return covid stats for a country
     */
    Optional<CountryGlobalStats> getCountryGlobalStats(String slug);
}
