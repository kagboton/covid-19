package io.kagbon.covidtracker.covidconfirmedservice.resources;

import io.kagbon.covidtracker.covidconfirmedservice.models.GlobalConfirmedStats;
import io.kagbon.covidtracker.covidconfirmedservice.services.CovidConfirmedDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequestMapping("/covid")
public class CovidConfirmedDataResources {

    @Autowired
    private CovidConfirmedDataService dataService;

    @GetMapping("/confirmed")
    public GlobalConfirmedStats getLatestGlobalStats() throws IOException, InterruptedException {
        GlobalConfirmedStats globalConfirmedStats = new GlobalConfirmedStats();
        globalConfirmedStats.setGlobalConfirmedStats(dataService.fetchDataAndConstructFinalCountryConfirmedStats());
        return globalConfirmedStats;
    }

}
