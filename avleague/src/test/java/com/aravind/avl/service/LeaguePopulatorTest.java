package com.aravind.avl.service;

import com.aravind.avl.domain.League;
import com.aravind.avl.util.AvlTestUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;

import static org.junit.Assert.assertEquals;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({ "/testContext.xml" })
@Transactional
public class LeaguePopulatorTest
{
	private final LeagueFactory lf = new LeagueFactory();

	@Autowired
	private LeaguePopulator populator;

	@Test
	public void import2012SeptemberLeague() throws Exception
	{
        Collection<League> leagues = AvlTestUtils.createLeague(populator, "Sri Bala Bharathi 2012 September League_2012-09-08_2012-09-15.properties", 0, 9, 10);
		assertEquals(1, leagues.size());
        assertEquals(8, leagues.iterator().next().getTeams().size());
	}

    @Test
	public void import2012JuneLeague() throws Exception
	{
        Collection<League> leagues = AvlTestUtils.createLeague(populator, "Sri Bala Bharathi 2012 June League_2012-06-16_2012-06-17.properties", 0, 11, 12);
		assertEquals(1, leagues.size());
        assertEquals(17, leagues.iterator().next().getTeams().size());
	}
}
