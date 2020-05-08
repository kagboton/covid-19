package io.kagbon.covidtracker.covidconfirmedservice.resources;

import io.kagbon.covidtracker.covidconfirmedservice.models.CountryConfirmedStats;
import io.kagbon.covidtracker.covidconfirmedservice.models.GlobalConfirmedStats;
import io.kagbon.covidtracker.covidconfirmedservice.services.CovidConfirmedDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequestMapping("/covid/confirmed")
public class CovidConfirmedDataResources {

    @Autowired
    private CovidConfirmedDataService dataService;

    @GetMapping()
    public ResponseEntity<GlobalConfirmedStats> getLatestGlobalConfirmedCasesStats() throws IOException, InterruptedException {
        GlobalConfirmedStats globalConfirmedStats = new GlobalConfirmedStats();
        globalConfirmedStats.setGlobalConfirmedStats(dataService.getAllCountriesConfirmedCases());
        return ResponseEntity.ok().body(globalConfirmedStats);
    }

    @GetMapping("/{slug}")
    public ResponseEntity<CountryConfirmedStats> getOneCountryConfirmedStats(@PathVariable("slug") String slug){
        CountryConfirmedStats countryConfirmedStatsStats = dataService.getCountryConfirmedCases(slug).get();
        return ResponseEntity.ok().body(countryConfirmedStatsStats);
    }



}
