package io.kagboton.covidtracker.covidglobalservice.resources;


import io.kagboton.covidtracker.covidglobalservice.models.CountryGlobalStats;
import io.kagboton.covidtracker.covidglobalservice.models.LatestTotals;
import io.kagboton.covidtracker.covidglobalservice.services.GlobalDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("covid")
public class CovidGlobalResource {


    @Autowired
    private GlobalDataService globalDataService;


    @GetMapping("/all")
    public ResponseEntity<List<CountryGlobalStats>> getAllCovidStats(){
        List<CountryGlobalStats> countryGlobalStatsList = globalDataService.getAllCountryGlobalStats();
        return ResponseEntity.ok().body(countryGlobalStatsList);
    }

    @GetMapping("/latestTotals")
    public ResponseEntity<LatestTotals> getWorldLatestStats(){
        LatestTotals latestTotals = new LatestTotals();
        List<CountryGlobalStats> countryGlobalStatsList = globalDataService.getAllCountryGlobalStats();
        int confirmed = countryGlobalStatsList.stream().mapToInt(c -> c.getConfirmedTotalCases()).sum();
        int recovered = countryGlobalStatsList.stream().mapToInt(r -> r.getTotalRecovered()).sum();
        int deaths = countryGlobalStatsList.stream().mapToInt(d -> d.getTotalDeaths()).sum();
        latestTotals.setConfirmed(confirmed);
        latestTotals.setRecovered(recovered);
        latestTotals.setDeaths(deaths);
        return ResponseEntity.ok().body(latestTotals);
    }

    @GetMapping("/{slug}")
    public ResponseEntity<CountryGlobalStats> getStatsForOneCountry(@PathVariable("slug") String slug){
        CountryGlobalStats countryStats = globalDataService.getCountryGlobalStats(slug).get();
        return ResponseEntity.ok().body(countryStats);
    }
}

