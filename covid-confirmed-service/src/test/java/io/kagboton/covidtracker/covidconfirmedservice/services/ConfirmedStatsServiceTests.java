package io.kagboton.covidtracker.covidconfirmedservice.services;

import io.kagboton.covidtracker.covidconfirmedservice.models.CountryConfirmedStats;
import io.kagboton.covidtracker.covidconfirmedservice.repository.ConfirmedStatsRepository;
import io.kagboton.covidtracker.covidconfirmedservice.services.exception.CountryNotFoundException;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ConfirmedStatsServiceTests {

    @Autowired
    private ConfirmedStatsService confirmedStatsService;

    @MockBean //Creates a Mock of CountryConfirmedStatsRepository to bypass the call to the actual CountryConfirmedStatsRepository
    private ConfirmedStatsRepository confirmedStatsRepository;

    @Before
    public void setup(){
        CountryConfirmedStats nowhere = new CountryConfirmedStats();

        nowhere.setSlug("nowhere");
        nowhere.setCountry("NoWhere");
        nowhere.setConfirmedTotalCases(5);
        nowhere.setConfirmedNewCases(2);

        Optional<CountryConfirmedStats> nowhereOptional = Optional.of(nowhere);

        Mockito.when(confirmedStatsRepository.findBySlug(nowhere.getSlug()))
                .thenReturn(nowhereOptional);
    }

    @Test
    public void shouldGetCountryConfirmedStatsBySlug(){
        String slug = "nowhere";
        CountryConfirmedStats found = confirmedStatsService.getCountryConfirmedStatsBySlug(slug).get();
        assertThat(found.getSlug())
                .isEqualTo(slug);
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldFailWhenSlugIsEmpty(){
        confirmedStatsService.getCountryConfirmedStatsBySlug("");
    }

    @Test(expected = CountryNotFoundException.class)
    public void shouldFailWhenNoCountryConfirmedStatsExistsWithGivenSlug(){
        confirmedStatsService.getCountryConfirmedStatsBySlug("nonexistingslug");
    }

    //todo implements other test cases
}
