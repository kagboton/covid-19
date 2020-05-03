package io.kagboton.covidtracker.covidglobalservice.models;

import java.util.List;

public class GlobalConfirmedStats {

    private List<CountryConfirmedStats> globalConfirmedStats;

    public GlobalConfirmedStats() {
    }

    public List<CountryConfirmedStats> getGlobalConfirmedStats() {
        return globalConfirmedStats;
    }

    public void setGlobalConfirmedStats(List<CountryConfirmedStats> globalConfirmedStats) {
        this.globalConfirmedStats = globalConfirmedStats;
    }
}
