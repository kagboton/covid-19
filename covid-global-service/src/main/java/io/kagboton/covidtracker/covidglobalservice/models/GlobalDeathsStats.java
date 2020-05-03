package io.kagboton.covidtracker.covidglobalservice.models;

import java.util.List;

public class GlobalDeathsStats {

    private List<CountryDeathsStats> globalDeathsStats;

    public GlobalDeathsStats() {
    }

    public List<CountryDeathsStats> getGlobalDeathsStats() {
        return globalDeathsStats;
    }

    public void setGlobalDeathsStats(List<CountryDeathsStats> globalDeathsStats) {
        this.globalDeathsStats = globalDeathsStats;
    }
}
