package com.aravind.avl.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.io.File;
import java.net.URI;
import java.util.Collection;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import com.aravind.avl.domain.League;
import com.google.common.io.Resources;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({ "/testContext.xml" })
@Transactional
public class LeaguePopulatorTest
{
	private final LeagueFactory lf = new LeagueFactory();

	@Autowired
	LeaguePopulator populator;

	@Test
	public void import2012SeptemberLeague() throws Exception
	{
		URI uri = Resources.getResource("Sri Bala Bharathi 2012 September League_2012-09-08_2012-09-15.properties").toURI();

		LeagueFactory.ImportProfile profile = new LeagueFactory.ImportProfile();
		profile.teamNameRowNum = 0;
		profile.emailColumnNum = 9;
		profile.phoneColumnNum = 10;

		League league = lf.createLeague(new File(uri), profile);
		assertNotNull(league);
		assertEquals(8, league.getTeams().size());

		Collection<League> leagues = populator.populateDatabase("Sri Bala Bharathi 2012 September League_2012-09-08_2012-09-15.properties",
				profile);
		assertEquals(1, leagues.size());
	}

	@Test
	public void import2012JuneLeague() throws Exception
	{
		URI uri = Resources.getResource("Sri Bala Bharathi 2012 June League_2012-06-16_2012-06-17.properties").toURI();

		LeagueFactory.ImportProfile profile = new LeagueFactory.ImportProfile();
		profile.teamNameRowNum = 0;
		profile.emailColumnNum = 11;
		profile.phoneColumnNum = 12;

		League league = lf.createLeague(new File(uri), profile);
		assertNotNull(league);
		assertEquals(17, league.getTeams().size());

		Collection<League> leagues = populator.populateDatabase("Sri Bala Bharathi 2012 June League_2012-06-16_2012-06-17.properties",
				profile);
		assertEquals(1, leagues.size());
	}
}
