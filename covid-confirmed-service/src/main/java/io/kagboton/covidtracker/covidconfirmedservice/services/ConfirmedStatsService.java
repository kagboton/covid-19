package io.kagboton.covidtracker.covidconfirmedservice.services;

import io.kagboton.covidtracker.covidconfirmedservice.models.Country;
import io.kagboton.covidtracker.covidconfirmedservice.models.CountryConfirmedStats;
import io.kagboton.covidtracker.covidconfirmedservice.repository.ConfirmedStatsRepository;
import io.kagboton.covidtracker.covidconfirmedservice.services.exception.CountryNotFoundException;
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
public class ConfirmedStatsService implements IConfirmedStatsService {


    private static String CONFIRMED_DATA_URL = "https://raw.githubusercontent.com/CSSEGISandData/COVID-19/master/csse_covid_19_data/csse_covid_19_time_series/time_series_covid19_confirmed_global.csv";

    private static String COUNTRIES_URL = "https://api.covid19api.com/countries";

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private ConfirmedStatsRepository repository;


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
     * @return a list of covid-19 confirmed cases per country and states
     * @throws IOException
     * @throws InterruptedException
     */
    private List<CountryConfirmedStats> fetchDataAndConstructTempCountryConfirmedStats() throws IOException, InterruptedException {

        List<CountryConfirmedStats> tempCountryConfirmedStatsList = new ArrayList<>(); //Temporary country confirmed stats list before update

        /*Parsing CVS with Apache Commons CSV :: Confirmed cases data*/
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(CONFIRMED_DATA_URL))
                .build();

        //Synchronise send
        HttpResponse httpResponse = client.send(request, HttpResponse.BodyHandlers.ofString());

        StringReader csvBodyReader = new StringReader((String) httpResponse.body());
        Iterable<CSVRecord> records = CSVFormat.DEFAULT.withFirstRecordAsHeader().parse(csvBodyReader);

        //Construction of the list of "LocationStats" object from CSV Records
        for (CSVRecord record : records) { //For each line of the confirmed case table

            CountryConfirmedStats tempCountryConfirmedStats = new CountryConfirmedStats();

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

            tempCountryConfirmedStats.setCountry(country);
            tempCountryConfirmedStats.setSlug(slug);
            tempCountryConfirmedStats.setConfirmedTotalCases(totalCasesUntilPresentDay);
            tempCountryConfirmedStats.setConfirmedNewCases(newCases);

            tempCountryConfirmedStatsList.add(tempCountryConfirmedStats);

        }

        return tempCountryConfirmedStatsList;

    }

    @Scheduled(cron = "0 0 1 * * *") //Runs the methode below avec 1:00 am
    @PostConstruct
    private void constructCountryConfirmedStatsAndSaveToDataBase() throws IOException, InterruptedException {

        List<CountryConfirmedStats> tempCountryConfirmedStatsList = this.fetchDataAndConstructTempCountryConfirmedStats();
        List<Country> allWordCountries = this.getAllWorldCountries();

        for (Country country : allWordCountries) {

            //Initialisation of the CountryConfirmedStats objects
            CountryConfirmedStats finalCountryConfirmedStats = new CountryConfirmedStats();

            finalCountryConfirmedStats.setCountry(country.getCountry());
            finalCountryConfirmedStats.setSlug(country.getSlug());
            finalCountryConfirmedStats.setConfirmedTotalCases(0);
            finalCountryConfirmedStats.setConfirmedNewCases(0);

            for (CountryConfirmedStats tempCountryConfirmedStats : tempCountryConfirmedStatsList) {
                //Data update
                if (tempCountryConfirmedStats.getSlug().equals(finalCountryConfirmedStats.getSlug())) {
                    int otherLinesTotalCases = finalCountryConfirmedStats.getConfirmedTotalCases(); //get previous global total cases
                    int newLineTotalCases = tempCountryConfirmedStats.getConfirmedTotalCases();
                    finalCountryConfirmedStats.setConfirmedTotalCases(otherLinesTotalCases + newLineTotalCases); //update totals cases

                    int otherLinesNewCases = finalCountryConfirmedStats.getConfirmedNewCases(); //get previous global new cases
                    int newLineNewCases = tempCountryConfirmedStats.getConfirmedNewCases();
                    finalCountryConfirmedStats.setConfirmedNewCases(otherLinesNewCases + newLineNewCases); //update new cases
                }
            }

            //todo save finalCountryConfirmedStats in file

            repository.save(finalCountryConfirmedStats);
        }


    }

    @Override
    public List<CountryConfirmedStats> getAllCountriesConfirmedStats() {
        return repository.findAll();
    }

    @Override
    public Optional<CountryConfirmedStats> getCountryConfirmedStatsBySlug(String slug) {
        Assert.hasLength(slug, "The slug must not be null or empty!");
        Optional<CountryConfirmedStats> existing = repository.findBySlug(slug);
        if (!existing.isPresent()) throw new CountryNotFoundException("Country " + slug + " not found.");
        return existing;
    }

    //todo implements getFallbackAllCountriesConfirmedStats with hystrix. hint :: get data from file

    //todo implements getFallbackCountryConfirmedStatsBySlug with hystrix :: get data from file


}
