package io.kagboton.covidtracker.covidglobalservice.models;

public class CountryConfirmedStats {

    private String country;
    private String slug;
    private int confirmedTotalCases;
    private int confirmedNewCases;


    public CountryConfirmedStats() {
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

    public int getConfirmedNewCases() {
        return confirmedNewCases;
    }

    public void setConfirmedNewCases(int confirmedNewCases) {
        this.confirmedNewCases = confirmedNewCases;
    }
}
