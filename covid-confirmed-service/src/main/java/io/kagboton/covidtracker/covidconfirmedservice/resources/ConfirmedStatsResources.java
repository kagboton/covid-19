package io.kagboton.covidtracker.covidconfirmedservice.resources;

import io.kagboton.covidtracker.covidconfirmedservice.models.CountryConfirmedStats;
import io.kagboton.covidtracker.covidconfirmedservice.models.GlobalConfirmedStats;
import io.kagboton.covidtracker.covidconfirmedservice.services.ConfirmedStatsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequestMapping("/covid/confirmed")
public class ConfirmedStatsResources {

    @Autowired
    private ConfirmedStatsService dataService;

    @GetMapping("/")
    public ResponseEntity<GlobalConfirmedStats> getLatestGlobalConfirmedCasesStats() {
        GlobalConfirmedStats globalConfirmedStats = new GlobalConfirmedStats();
        globalConfirmedStats.setGlobalConfirmedStats(dataService.getAllCountriesConfirmedStats());
        return ResponseEntity.ok().body(globalConfirmedStats);
    }

    @GetMapping("/{slug}")
    public ResponseEntity<CountryConfirmedStats> getOneCountryConfirmedStats(@PathVariable("slug") String slug){
        CountryConfirmedStats countryConfirmedStatsStats = dataService.getCountryConfirmedStatsBySlug(slug).get();
        return ResponseEntity.ok().body(countryConfirmedStatsStats);
    }



}
