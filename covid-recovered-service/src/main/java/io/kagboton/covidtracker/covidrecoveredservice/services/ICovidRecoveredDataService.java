package io.kagboton.covidtracker.covidrecoveredservice.services;

import io.kagboton.covidtracker.covidrecoveredservice.models.Country;
import io.kagboton.covidtracker.covidrecoveredservice.models.CountryRecoveredStats;

import java.io.IOException;
import java.util.List;

public interface ICovidRecoveredDataService {
    /**
     * Get all the world countries and their slug from external api
     * @return a list of countries
     */
    List<Country> getAllWorldCountries();

    /**
     * Fetch data from external source (CSV file on github), parse them to custom object and aggregate data in final object
     * @return a list of countries with their covid-19 recovered
     * @throws IOException
     * @throws InterruptedException
     */
    List<CountryRecoveredStats> fetchDataAndConstructFinalCountryRecoveredStats() throws IOException, InterruptedException;
}