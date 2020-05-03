package io.kagboton.covidtracker.covidglobalservice.models;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.io.Serializable;

@Entity
public class CountryGlobalStats implements Serializable {

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = 1L;

    @Id
    String slug;

    String country;

    int confirmedTotalCases;
    int latestConfirmedNewCases;

    int totalRecovered;
    int latestNewRecovered;

    int totalDeaths;
    int latestNewDeaths;

    public CountryGlobalStats() {
    }


    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getSlug() {
        return slug;
    }

    public void setSlug(String slug) {
        this.slug = slug;
    }

    public int getConfirmedTotalCases() {
        return confirmedTotalCases;
    }

    public void setConfirmedTotalCases(int confirmedTotalCases) {
        this.confirmedTotalCases = confirmedTotalCases;
    }

    public int getLatestConfirmedNewCases() {
        return latestConfirmedNewCases;
    }

    public void setLatestConfirmedNewCases(int latestConfirmedNewCases) {
        this.latestConfirmedNewCases = latestConfirmedNewCases;
    }

    public int getTotalRecovered() {
        return totalRecovered;
    }

    public void setTotalRecovered(int totalRecovered) {
        this.totalRecovered = totalRecovered;
    }

    public int getLatestNewRecovered() {
        return latestNewRecovered;
    }

    public void setLatestNewRecovered(int latestNewRecovered) {
        this.latestNewRecovered = latestNewRecovered;
    }

    public int getTotalDeaths() {
        return totalDeaths;
    }

    public void setTotalDeaths(int totalDeaths) {
        this.totalDeaths = totalDeaths;
    }

    public int getLatestNewDeaths() {
        return latestNewDeaths;
    }

    public void setLatestNewDeaths(int latestNewDeaths) {
        this.latestNewDeaths = latestNewDeaths;
    }
}
