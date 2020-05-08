package io.kagbon.covidtracker.covidconfirmedservice.services;

import io.kagbon.covidtracker.covidconfirmedservice.models.Country;
import io.kagbon.covidtracker.covidconfirmedservice.models.CountryConfirmedStats;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

public interface ICovidConfirmedDataService {

    /**
     * Get all the world countries and their slug from external api
     * @return a list of countries
     */
    List<Country> getAllWorldCountries();

    /**
     * Fetch data from external source (CSV file on github), parse them to custom object and aggregate data in temporary object
     * @return a list of covid-19 confirmed cases per country and states
     * @throws IOException
     * @throws InterruptedException
     */
    List<CountryConfirmedStats> fetchDataAndConstructTempCountryConfirmedStats() throws IOException, InterruptedException;

    /**
     * Get all countries confirmed cases stats (only per country)
     * @return all countries covid-19 confirmed cases compact stats
     */
    List<CountryConfirmedStats> getAllCountriesConfirmedCases();

    /**
     * Get a country confirmed cases stats if exists
     * @param slug
     * @return the country's covid-19 confirmed cases stats
     */
    Optional<CountryConfirmedStats> getCountryConfirmedCases(String slug);
}
