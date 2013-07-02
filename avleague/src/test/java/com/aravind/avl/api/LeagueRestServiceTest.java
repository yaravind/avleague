package com.aravind.avl.api;

import com.aravind.avl.domain.League;
import com.aravind.avl.service.LeaguePopulator;
import com.aravind.avl.util.AvlTestUtils;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Sasidhar
 * Date: 6/22/13
 * Time: 11:01 AM
 *
 * Integration test for LeagueRestService
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({ "/testContext.xml" })
public class LeagueRestServiceTest {

    @Autowired
    private LeagueRestService leagueRestService;

    @Autowired
    private LeaguePopulator populator;

    @Before
    public void setUp() throws Exception {

        AvlTestUtils.createLeague(populator, "Sri Bala Bharathi 2012 September League_2012-09-08_2012-09-15.properties", 0, 9, 10);
    }

    @After
    public void tearDown() throws Exception {

    }

    @Test
    public void getLeagues() {
        List<League> leagues = leagueRestService.getLeagues();
        Assert.assertNotNull(leagues);
        Assert.assertEquals(1, leagues.size());
    }
}
