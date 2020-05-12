package io.kagboton.covidtracker.coviddeathsservice.models;

import java.util.Comparator;

public class Country {

    String country;
    String slug;

    public Country() {
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

    public static Comparator<Country> countryComparator = new Comparator<Country>() {
        @Override
        public int compare(Country c1, Country c2) {
            String c1Name = c1.getCountry().toLowerCase();
            String c2Name = c2.getCountry().toLowerCase();
            return c1Name.compareTo(c2Name);
        }
    };

    @Override
    public String toString() {
        return "Country{" +
                "country='" + country + '\'' +
                ", slug='" + slug + '\'' +
                '}';
    }
}
