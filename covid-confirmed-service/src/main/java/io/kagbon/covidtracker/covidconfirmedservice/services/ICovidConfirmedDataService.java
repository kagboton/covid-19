package io.kagbon.covidtracker.covidconfirmedservice.services;

import io.kagbon.covidtracker.covidconfirmedservice.models.Country;
import io.kagbon.covidtracker.covidconfirmedservice.models.CountryConfirmedStats;

import java.io.IOException;
import java.util.List;

public interface ICovidConfirmedDataService {

    /**
     * Get all the world countries and their slug from external api
     * @return a list of countries
     */
    List<Country> getAllWorldCountries();

    /**
     * Fetch data from external source (CSV file on github), parse them to custom object and aggregate data in final object
     * @return a list of countries with their covid-19 confirmed cases
     * @throws IOException
     * @throws InterruptedException
     */
    List<CountryConfirmedStats> fetchDataAndConstructFinalCountryConfirmedStats() throws IOException, InterruptedException;
}
