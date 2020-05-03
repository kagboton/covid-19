package io.kagboton.covidtracker.covidrecoveredservice.resources;

import io.kagboton.covidtracker.covidrecoveredservice.models.GlobalRecoveredStats;
import io.kagboton.covidtracker.covidrecoveredservice.services.CovidRecoveredDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequestMapping("/covid")
public class CovidRecoveredDataResources {

    @Autowired
    private CovidRecoveredDataService dataService;

    @GetMapping("/recovered")
    public GlobalRecoveredStats getLatestGlobalStats() throws IOException, InterruptedException {
        GlobalRecoveredStats globalRecoveredStats = new GlobalRecoveredStats();
        globalRecoveredStats.setGlobalRecoveredStats(dataService.fetchDataAndConstructFinalCountryRecoveredStats());
        return globalRecoveredStats;
    }

}
