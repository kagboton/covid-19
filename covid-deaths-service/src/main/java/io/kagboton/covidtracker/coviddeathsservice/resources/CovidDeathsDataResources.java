package io.kagboton.covidtracker.coviddeathsservice.resources;


import io.kagboton.covidtracker.coviddeathsservice.models.GlobalDeathsStats;
import io.kagboton.covidtracker.coviddeathsservice.services.CovidDeathsDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequestMapping("/covid")
public class CovidDeathsDataResources {

    @Autowired
    private CovidDeathsDataService dataService;

    @GetMapping("/deaths")
    public GlobalDeathsStats getLatestGlobalStats() throws IOException, InterruptedException {
        GlobalDeathsStats globalDeathsStats = new GlobalDeathsStats();
        globalDeathsStats.setGlobalDeathsStats(dataService.fetchDataAndConstructFinalCountryDeathsStats());
        return globalDeathsStats;
    }

}
