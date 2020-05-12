package io.kagboton.covidtracker.covidconfirmedservice.resources;


import io.kagboton.covidtracker.covidconfirmedservice.models.CountryConfirmedStats;
import io.kagboton.covidtracker.covidconfirmedservice.services.ConfirmedStatsService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Optional;

import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@RunWith(SpringRunner.class)
@SpringBootTest
public class ConfirmedStatsResourcesTests {

    @InjectMocks
    private ConfirmedStatsResources resources;

    private MockMvc mockMvc;

    @Mock
    private ConfirmedStatsService service;

    @Before
    public void setup(){
        initMocks(this);
        this.mockMvc = MockMvcBuilders.standaloneSetup(resources).build();
    }

    @Test
    public void shouldGetCountryConfirmedStatsBySlug() throws Exception {

        CountryConfirmedStats confirmedStats = new CountryConfirmedStats();
        confirmedStats.setSlug("somewhere");

        Optional<CountryConfirmedStats> confirmedStatsOptional = Optional.of(confirmedStats);

        when(service.getCountryConfirmedStatsBySlug(confirmedStats.getSlug())).thenReturn(confirmedStatsOptional);

        mockMvc.perform(get("/covid/confirmed/" + confirmedStats.getSlug()))
                .andExpect(jsonPath("$.slug").value(confirmedStats.getSlug()))
                .andExpect(status().isOk());
    }


    //todo implements other test cases


}
