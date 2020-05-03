package io.kagboton.covidtracker.covidglobalservice.models;

import java.util.List;

public class GlobalRecoveredStats {

    private List<CountryRecoveredStats> globalRecoveredStats;

    public GlobalRecoveredStats() {
    }

    public List<CountryRecoveredStats> getGlobalRecoveredStats() {
        return globalRecoveredStats;
    }

    public void setGlobalRecoveredStats(List<CountryRecoveredStats> globalRecoveredStats) {
        this.globalRecoveredStats = globalRecoveredStats;
    }
}
