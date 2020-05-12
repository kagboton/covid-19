package io.kagboton.covidtracker.covidconfirmedservice.services;

import io.kagboton.covidtracker.covidconfirmedservice.models.CountryConfirmedStats;
import java.util.List;
import java.util.Optional;

public interface IConfirmedStatsService {

    /**
     * Get all countries confirmed cases stats (only per country)
     * @return all countries covid-19 confirmed cases compact stats
     */
    List<CountryConfirmedStats> getAllCountriesConfirmedStats();

    /**
     * Get a country confirmed cases stats if exists
     * @param slug
     * @return the country's covid-19 confirmed cases stats
     */
    Optional<CountryConfirmedStats> getCountryConfirmedStatsBySlug(String slug);
}
