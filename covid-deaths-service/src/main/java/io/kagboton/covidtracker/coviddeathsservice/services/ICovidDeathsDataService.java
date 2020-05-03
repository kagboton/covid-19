package io.kagboton.covidtracker.coviddeathsservice.services;

import io.kagboton.covidtracker.coviddeathsservice.models.Country;
import io.kagboton.covidtracker.coviddeathsservice.models.CountryDeathsStats;

import java.io.IOException;
import java.util.List;

public interface ICovidDeathsDataService {

    /**
     * Get all the world countries and their slug from external api
     * @return a list of countries
     */
    List<Country> getAllWorldCountries();

    /**
     * Fetch data from external source (CSV file on github), parse them to custom object and aggregate data in final object
     * @return a list of countries with their covid-19 deaths
     * @throws IOException
     * @throws InterruptedException
     */
    List<CountryDeathsStats> fetchDataAndConstructFinalCountryDeathsStats() throws IOException, InterruptedException;
}
