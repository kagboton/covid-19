package io.kagboton.covidtracker.coviddeathsservice.services;

import io.kagboton.covidtracker.coviddeathsservice.models.Country;
import io.kagboton.covidtracker.coviddeathsservice.models.CountryDeathsStats;
import io.kagboton.covidtracker.coviddeathsservice.repository.DeathsStatsRepository;
import io.kagboton.covidtracker.coviddeathsservice.services.exception.CountryNotFoundException;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.web.client.RestTemplate;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.io.StringReader;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
public class DeathsStatsService implements IDeathsStatsService {

    private static  String DEATHS_DATA_URL = "https://raw.githubusercontent.com/CSSEGISandData/COVID-19/master/csse_covid_19_data/csse_covid_19_time_series/time_series_covid19_deaths_global.csv";

    private static  String COUNTRIES_URL = "https://api.covid19api.com/countries";

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private DeathsStatsRepository repository;

    /**
     * Get all the world countries and their slug from external api
     * @return a list of countries
     */
    private List<Country> getAllWorldCountries() {

        List<Country> allCountries = new ArrayList<>();

        String result = restTemplate.getForObject(COUNTRIES_URL, String.class); //API call

        JSONArray countries = new JSONArray(result);
        for (int i = 0; i < countries.length(); i++) {
            JSONObject jsonCountry = countries.getJSONObject(i); //The JSON data
            Country country = new Country(); //Country object that we will populate from JSON data

            //Get information for JSON object
            String countryName = jsonCountry.getString("Country");
            String countrySlug = jsonCountry.getString("Slug");

            //Populate our country object with information
            country.setCountry(countryName);
            country.setSlug(countrySlug);

            allCountries.add(country); //Add country object to alLCountries list
        }

        Collections.sort(allCountries, Country.countryComparator); //sort countries by name in ascending order

        //todo save countries in file

        return allCountries;

    }


    /**
     * Fetch data from external source (CSV file on github), parse them to custom object and aggregate data in temporary object
     * @return a list of covid-19 deths per country and states
     * @throws IOException
     * @throws InterruptedException
     */
    private List<CountryDeathsStats> fetchDataAndConstructTempCountryDeathsStats() throws IOException, InterruptedException {

        List<CountryDeathsStats> tempCountryDeathsStatsList = new ArrayList<>(); //Temporary country Deaths stats list before update

        /*Parsing CVS with Apache Commons CSV :: Deaths cases data*/
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(DEATHS_DATA_URL))
                .build();

        //Synchronise send
        HttpResponse httpResponse = client.send(request, HttpResponse.BodyHandlers.ofString());

        StringReader csvBodyReader = new StringReader((String) httpResponse.body());
        Iterable<CSVRecord> records = CSVFormat.DEFAULT.withFirstRecordAsHeader().parse(csvBodyReader);

        //Construction of the list of "LocationStats" object from CSV Records
        for (CSVRecord record : records) { //For each line of the Deaths case table

            CountryDeathsStats tempCountryDeathsStats = new CountryDeathsStats();

            String country = record.get("Country/Region");
            String slug = record.get("Country/Region").replace(" ", "-").toLowerCase().replace("(", "").replace(")", "").replace("'", "").replace(",", "");

            //US particular case
            slug = slug.equals("us") ? "united-states" : slug;

            //Cabo Verde particular case
            slug = slug.equals("cabo-verde") ? "cape-verde" : slug;

            //Czechia particular case
            slug = slug.equals("czechia") ? "czech-republic" : slug;

            //Las PDR particular case
            slug = slug.equals("laos") ? "lao-pdr" : slug;

            //Macedonia particular case
            slug = slug.equals("north-macedonia") ? "macedonia" : slug;


            int totalCasesUntilPresentDay = Integer.parseInt(record.get(record.size() - 1));
            int totalCasesUntilDayBeforePresentDay = Integer.parseInt(record.get(record.size() - 2));
            int newCases = totalCasesUntilPresentDay - totalCasesUntilDayBeforePresentDay;

            tempCountryDeathsStats.setCountry(country);
            tempCountryDeathsStats.setSlug(slug);
            tempCountryDeathsStats.setTotalDeaths(totalCasesUntilPresentDay);
            tempCountryDeathsStats.setNewDeaths(newCases);

            tempCountryDeathsStatsList.add(tempCountryDeathsStats);

        }
        return tempCountryDeathsStatsList;
    }

    @Scheduled(cron = "0 1 0 * * *") //Runs the methode below avec 1:00 am
    @PostConstruct
    private void constructCountryDeathsStatsAndSaveToDataBase() throws IOException, InterruptedException {

        List<CountryDeathsStats> tempCountryDeathsStatsList = this.fetchDataAndConstructTempCountryDeathsStats();
        List<Country> allWordCountries = this.getAllWorldCountries();

        for (Country country : allWordCountries) {

            //Initialisation of the CountryDeathsStats objects
            CountryDeathsStats finalCountryDeathsStats = new CountryDeathsStats();

            finalCountryDeathsStats.setCountry(country.getCountry());
            finalCountryDeathsStats.setSlug(country.getSlug());
            finalCountryDeathsStats.setTotalDeaths(0);
            finalCountryDeathsStats.setNewDeaths(0);

            for (CountryDeathsStats tempCountryDeathsStats : tempCountryDeathsStatsList) {
                //Data update
                if (tempCountryDeathsStats.getSlug().equals(finalCountryDeathsStats.getSlug())) {
                    int otherLinesTotalCases = finalCountryDeathsStats.getTotalDeaths(); //get previous global total cases
                    int newLineTotalCases = tempCountryDeathsStats.getTotalDeaths();
                    finalCountryDeathsStats.setTotalDeaths(otherLinesTotalCases + newLineTotalCases); //update totals cases

                    int otherLinesNewCases = finalCountryDeathsStats.getNewDeaths(); //get previous global new cases
                    int newLineNewCases = tempCountryDeathsStats.getNewDeaths();
                    finalCountryDeathsStats.setNewDeaths(otherLinesNewCases + newLineNewCases); //update new cases
                }
            }

            //todo save finalCountryDeathsStats in file

            repository.save(finalCountryDeathsStats);
        }


    }

    @Override
    public List<CountryDeathsStats> getAllCountriesDeathsStats() {
        return repository.findAll();
    }

    @Override
    public Optional<CountryDeathsStats> getCountryDeathsStatsBySlug(String slug) {
        Assert.hasLength(slug, "The slug must not be null or empty!");
        Optional<CountryDeathsStats> existing = repository.findBySlug(slug);
        if (!existing.isPresent()) throw new CountryNotFoundException("Country " + slug + " not found.");
        return existing;
    }


}
