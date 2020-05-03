package io.kagboton.covidtracker.covidglobalservice.services;

import io.kagboton.covidtracker.covidglobalservice.models.*;
import io.kagboton.covidtracker.covidglobalservice.repository.CountryGlobalStatsRepository;
import io.kagboton.covidtracker.covidglobalservice.services.exception.CountryNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;


import java.util.List;
import java.util.Optional;

@Service
public class GlobalDataService {

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private CountryGlobalStatsRepository repository;

    //Recover existing country global stats
    public List<CountryGlobalStats> getAllCountryGlobalStats(){
        return repository.findAll();
    }

    public Optional<CountryGlobalStats> getCountryGlobalStats(String slug){
        Optional<CountryGlobalStats> existing = repository.findBySlug(slug);
        if (!existing.isPresent()) throw new CountryNotFoundException("Country " + slug + " not found.");
        return existing;
    }

    public void updateCountryGlobalStats(CountryGlobalStats countryGlobalStats){
        List<CountryGlobalStats> existing = repository.findAll();
        for (CountryGlobalStats c : existing) {
            if (c.getSlug().equals(countryGlobalStats.getSlug())){
                c.setConfirmedTotalCases(countryGlobalStats.getConfirmedTotalCases());
                c.setLatestConfirmedNewCases(countryGlobalStats.getLatestConfirmedNewCases());
                c.setTotalRecovered(countryGlobalStats.getTotalRecovered());
                c.setLatestNewRecovered(countryGlobalStats.getLatestNewRecovered());
                c.setTotalDeaths(countryGlobalStats.getTotalDeaths());
                c.setLatestNewDeaths(countryGlobalStats.getLatestNewDeaths());
                repository.save(c);
            }
        }
    }

    @Scheduled(cron = "0 1 0 * * *")
    @EventListener(ApplicationReadyEvent.class)
    public void constructCountryGlobalStatsFromConfirmedDeathsAndRecoveredData(){

        //Get all stats from confirmed, deaths and recovered micro services
        GlobalConfirmedStats globalConfirmedStats = restTemplate.getForObject("http://covid-confirmed-service/covid/confirmed", GlobalConfirmedStats.class);
        GlobalDeathsStats globalDeathsStats = restTemplate.getForObject("http://covid-deaths-service/covid/deaths", GlobalDeathsStats.class);
        GlobalRecoveredStats globalRecoveredStats = restTemplate.getForObject("http://covid-recovered-service/covid/recovered", GlobalRecoveredStats.class);

        int confirmedListSize = globalConfirmedStats.getGlobalConfirmedStats().size();
        int deathsListSize = globalDeathsStats.getGlobalDeathsStats().size();
        int recoveredListSize = globalRecoveredStats.getGlobalRecoveredStats().size();


        //Construct our global stats from confirmed, deaths and recovered data
        for(var i=0; i < confirmedListSize; i++){

            CountryGlobalStats countryGlobalStats = new CountryGlobalStats();
            String confirmedCaseCountry = globalConfirmedStats.getGlobalConfirmedStats().get(i).getCountry();
            String confirmedCaseCountrySlug = globalConfirmedStats.getGlobalConfirmedStats().get(i).getSlug();

            countryGlobalStats.setCountry(confirmedCaseCountry);
            countryGlobalStats.setSlug(globalConfirmedStats.getGlobalConfirmedStats().get(i).getSlug());
            countryGlobalStats.setConfirmedTotalCases(globalConfirmedStats.getGlobalConfirmedStats().get(i).getConfirmedTotalCases());
            countryGlobalStats.setLatestConfirmedNewCases(globalConfirmedStats.getGlobalConfirmedStats().get(i).getConfirmedNewCases());

            for (var j = 0; j < deathsListSize; j++){
                String deathsCountry = globalDeathsStats.getGlobalDeathsStats().get(j).getCountry();
                String deathsCountrySlug = globalDeathsStats.getGlobalDeathsStats().get(j).getSlug();

                if(confirmedCaseCountry.equals(deathsCountry) && confirmedCaseCountrySlug.equals(deathsCountrySlug)){
                    countryGlobalStats.setTotalDeaths(globalDeathsStats.getGlobalDeathsStats().get(i).getTotalDeaths());
                    countryGlobalStats.setLatestNewDeaths(globalDeathsStats.getGlobalDeathsStats().get(i).getNewDeaths());
                }
            }

            for(var k = 0; k < recoveredListSize; k++){
                String recoveredCountry = globalRecoveredStats.getGlobalRecoveredStats().get(k).getCountry();
                String recoveredCountrySlug = globalRecoveredStats.getGlobalRecoveredStats().get(k).getSlug();

                if(confirmedCaseCountry.equals(recoveredCountry) && confirmedCaseCountrySlug.equals(recoveredCountrySlug)){
                    countryGlobalStats.setTotalRecovered(globalRecoveredStats.getGlobalRecoveredStats().get(i).getTotalRecovered());
                    countryGlobalStats.setLatestNewRecovered(globalRecoveredStats.getGlobalRecoveredStats().get(i).getNewRecovered());
                }
            }

            repository.save(countryGlobalStats);

        }
    }

}
