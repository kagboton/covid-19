package io.kagboton.covidtracker.covidrecoveredservice.resources;

import io.kagboton.covidtracker.covidrecoveredservice.models.CountryRecoveredStats;
import io.kagboton.covidtracker.covidrecoveredservice.models.GlobalRecoveredStats;
import io.kagboton.covidtracker.covidrecoveredservice.services.RecoveredStatsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequestMapping("/covid/recovered")
public class RecoveredStatsResources {

    @Autowired
    private RecoveredStatsService dataService;

    @GetMapping("/")
    public ResponseEntity<GlobalRecoveredStats> getLatestGlobalRecoveredCasesStats() {
        GlobalRecoveredStats globalRecoveredStats = new GlobalRecoveredStats();
        globalRecoveredStats.setGlobalRecoveredStats(dataService.getAllCountriesRecoveredStats());
        return ResponseEntity.ok().body(globalRecoveredStats);
    }

    @GetMapping("/{slug}")
    public ResponseEntity<CountryRecoveredStats> getOneCountryRecoveredStats(@PathVariable("slug") String slug){
        CountryRecoveredStats countryRecoveredStats = dataService.getCountryRecoveredStatsBySlug(slug).get();
        return ResponseEntity.ok().body(countryRecoveredStats);
    }


}
