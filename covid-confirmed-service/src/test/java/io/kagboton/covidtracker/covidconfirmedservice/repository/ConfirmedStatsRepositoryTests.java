package io.kagboton.covidtracker.covidconfirmedservice.repository;

import io.kagboton.covidtracker.covidconfirmedservice.models.CountryConfirmedStats;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit4.SpringRunner;

import static org.assertj.core.api.Assertions.assertThat;


@RunWith(SpringRunner.class)
@DataJpaTest
public class ConfirmedStatsRepositoryTests {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private ConfirmedStatsRepository repository;

    @Test
    public void shouldGetCountryConfirmedStatsBySlug(){
        //given
        CountryConfirmedStats nowhere = new CountryConfirmedStats();
        nowhere.setSlug("nowhere");
        nowhere.setCountry("NoWhere");
        nowhere.setConfirmedTotalCases(5);
        nowhere.setConfirmedNewCases(2);

        entityManager.persist(nowhere);
        entityManager.flush();

        //when
        CountryConfirmedStats found = repository.findBySlug(nowhere.getSlug()).get();

        //then
        assertThat(found.getSlug())
                .isEqualTo(nowhere.getSlug());

        assertThat(found.getCountry())
                .isEqualTo(nowhere.getCountry());

        assertThat(found.getConfirmedTotalCases())
                .isEqualTo(nowhere.getConfirmedTotalCases());

        assertThat(found.getConfirmedNewCases())
                .isEqualTo(nowhere.getConfirmedNewCases());
    }

    //todo implements other test cases


}
