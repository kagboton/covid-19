package io.kagboton.covidtracker.covidconfirmedservice.models;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.io.Serializable;

@Entity
public class CountryConfirmedStats implements Serializable {

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = 1L;

    @Id
    private String slug;
    private String country;
    private int confirmedTotalCases;
    private int confirmedNewCases;


    public CountryConfirmedStats() {
    }

    public String getSlug() {
        return slug;
    }

    public void setSlug(String slug) {
        this.slug = slug;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
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
