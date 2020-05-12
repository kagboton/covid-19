package io.kagboton.covidtracker.covidrecoveredservice.services;


import io.kagboton.covidtracker.covidrecoveredservice.models.CountryRecoveredStats;

import java.util.List;
import java.util.Optional;

public interface IRecoveredStatsService {

    /**
     * Get all countries Recovered stats (only per country)
     * @return all countries covid-19 Recovered compact stats
     */
    List<CountryRecoveredStats> getAllCountriesRecoveredStats();

    /**
     * Get a country Recovered stats if exists
     * @param slug
     * @return the country's covid-19 Recovered cases stats
     */
    Optional<CountryRecoveredStats> getCountryRecoveredStatsBySlug(String slug);
}

