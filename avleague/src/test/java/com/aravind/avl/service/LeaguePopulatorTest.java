package com.aravind.avl.service;

import static org.junit.Assert.assertEquals;

import java.util.Collection;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import com.aravind.avl.domain.League;
import com.aravind.avl.domain.LeagueRepository;
import com.aravind.avl.util.AvlTestUtils;

@DirtiesContext (classMode = ClassMode.AFTER_EACH_TEST_METHOD)
@RunWith (SpringJUnit4ClassRunner.class)
@ContextConfiguration ({ "/testContext.xml"})
@Transactional
public class LeaguePopulatorTest
{
	private final LeagueFactory lf = new LeagueFactory();

	@Autowired
	LeagueRepository repo;

	@Autowired
	private LeaguePopulator populator;

	@Test
	public void import2012SeptemberLeague() throws Exception
	{
		Collection<League> leagues = AvlTestUtils.createLeague(populator,
				"Sri Bala Bharathi 2012 September League_2012-09-08_2012-09-15.properties", 0, 9, 10);
		assertEquals(1, leagues.size());
		League league = leagues.iterator().next();
		assertEquals(8, league.getTeams().size());
	}

	@Test
	public void import2012JuneLeague() throws Exception
	{
		Collection<League> leagues = AvlTestUtils.createLeague(populator,
				"Sri Bala Bharathi 2012 June League_2012-06-16_2012-06-17.properties", 0, 11, 12);
		assertEquals(1, leagues.size());
		League league = leagues.iterator().next();
		assertEquals(17, league.getTeams().size());
	}
}
