package io.kagboton.covidtracker.coviddeathsservice.resources;


import io.kagboton.covidtracker.coviddeathsservice.models.CountryDeathsStats;
import io.kagboton.covidtracker.coviddeathsservice.models.GlobalDeathsStats;
import io.kagboton.covidtracker.coviddeathsservice.services.DeathsStatsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequestMapping("/covid/deaths")
public class DeathsStatsResources {

    @Autowired
    private DeathsStatsService dataService;

    @GetMapping("/")
    public ResponseEntity<GlobalDeathsStats> getLatestGlobalDeathsCasesStats() {
        GlobalDeathsStats globalDeathsStats = new GlobalDeathsStats();
        globalDeathsStats.setGlobalDeathsStats(dataService.getAllCountriesDeathsStats());
        return ResponseEntity.ok().body(globalDeathsStats);
    }

    @GetMapping("/{slug}")
    public ResponseEntity<CountryDeathsStats> getOneCountryDeathsStats(@PathVariable("slug") String slug){
        CountryDeathsStats countryDeathsStats = dataService.getCountryDeathsStatsBySlug(slug).get();
        return ResponseEntity.ok().body(countryDeathsStats);
    }

}
