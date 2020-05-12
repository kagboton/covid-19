package io.kagboton.covidtracker.coviddeathsservice.services;

import io.kagboton.covidtracker.coviddeathsservice.models.Country;
import io.kagboton.covidtracker.coviddeathsservice.models.CountryDeathsStats;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

public interface IDeathsStatsService {

    /**
     * Get all countries deaths stats (only per country)
     * @return all countries covid-19 deaths compact stats
     */
    List<CountryDeathsStats> getAllCountriesDeathsStats();

    /**
     * Get a country deaths stats if exists
     * @param slug
     * @return the country's covid-19 deaths cases stats
     */
    Optional<CountryDeathsStats> getCountryDeathsStatsBySlug(String slug);
}
