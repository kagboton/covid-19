package io.kagboton.covidtracker.covidrecoveredservice.services;

import io.kagboton.covidtracker.covidrecoveredservice.models.Country;
import io.kagboton.covidtracker.covidrecoveredservice.models.CountryRecoveredStats;
import io.kagboton.covidtracker.covidrecoveredservice.models.LocationStats;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.io.StringReader;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
public class CovidRecoveredDataService implements ICovidRecoveredDataService {

    private static  String RECOVERED_DATA_URL = "https://raw.githubusercontent.com/CSSEGISandData/COVID-19/master/csse_covid_19_data/csse_covid_19_time_series/time_series_covid19_recovered_global.csv";


    private static  String COUNTRIES_URL = "https://api.covid19api.com/countries";

    @Autowired
    private RestTemplate restTemplate;

    @Override
    public List<Country> getAllWorldCountries()  {

        List<Country> allCountries = new ArrayList<>();

        String result = restTemplate.getForObject(COUNTRIES_URL, String.class); //API call

        JSONArray countries = new JSONArray(result);
        for(int i = 0; i < countries.length(); i++){
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

        return allCountries;

    }


    @Override
    public List<CountryRecoveredStats> fetchDataAndConstructFinalCountryRecoveredStats() throws IOException, InterruptedException {

        List<CountryRecoveredStats> countryRecoveredStatsList = new ArrayList<>();

        List<LocationStats> locationStatsList = new ArrayList<>();

        List<Country> allWordCountries = getAllWorldCountries();

        /*Parsing CVS with Apache Commons CSV :: Confirmed cases data*/
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(RECOVERED_DATA_URL))
                .build();
        //Synchronise send
        HttpResponse httpResponse  = client.send(request, HttpResponse.BodyHandlers.ofString());

        StringReader csvBodyReader = new StringReader((String) httpResponse.body());
        Iterable<CSVRecord> records = CSVFormat.DEFAULT.withFirstRecordAsHeader().parse(csvBodyReader);


        //Construction of the list of "LocationStats" object from CSV Records
        for (CSVRecord record : records) { //For each line of the confirmed case table
            LocationStats locationStats = new LocationStats();

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


            int totalRecoveredUntilPresentDay = Integer.parseInt(record.get(record.size() - 1));
            int totalRecoveredUntilDayBeforePresentDay = Integer.parseInt(record.get(record.size() - 2));
            int newRecovered = totalRecoveredUntilPresentDay - totalRecoveredUntilDayBeforePresentDay;

            locationStats.setCountry(country);
            locationStats.setSlug(slug);
            locationStats.setTotalRecovered(totalRecoveredUntilPresentDay);
            locationStats.setNewRecovered(newRecovered);

            locationStatsList.add(locationStats);

        }


        for (Country country : allWordCountries) {
            //Initialisation of the CountryRecoveredStats objects
            CountryRecoveredStats countryRecoveredStats = new CountryRecoveredStats();
            countryRecoveredStats.setCountry(country.getCountry());
            countryRecoveredStats.setSlug(country.getSlug());
            countryRecoveredStats.setTotalRecovered(0);
            countryRecoveredStats.setNewRecovered(0);

            for (LocationStats locationStats : locationStatsList){
                //Data update
                if(locationStats.getSlug().equals(countryRecoveredStats.getSlug())){
                    int otherLinesRecovered = countryRecoveredStats.getTotalRecovered();
                    int newLineRecovered = locationStats.getTotalRecovered();
                    countryRecoveredStats.setTotalRecovered(otherLinesRecovered + newLineRecovered);

                    int otherLinesNewRecovered = countryRecoveredStats.getNewRecovered();
                    int newLineNewRecovered = locationStats.getNewRecovered();
                    countryRecoveredStats.setNewRecovered(otherLinesNewRecovered + newLineNewRecovered);
                }
            }

            countryRecoveredStatsList.add(countryRecoveredStats);

        }


        return countryRecoveredStatsList;

    }
}
