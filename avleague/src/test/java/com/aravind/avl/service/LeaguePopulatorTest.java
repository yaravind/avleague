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

@RunWith (SpringJUnit4ClassRunner.class)
@ContextConfiguration ({ "/testContext.xml"})
@Transactional
public class LeaguePopulatorTest
{
	private final LeagueFactory lf = new LeagueFactory();

	@Autowired
	LeaguePopulator populator;

	@Test
	public void testPopulateDatabase() throws Exception
	{
		URI uri = Resources.getResource("Sri Bala Bharathi 2012 September League_2012-09-08_2012-09-15.properties").toURI();

		League league = lf.createLeague(new File(uri));
		assertNotNull(league);
		assertEquals(8, league.getTeams().size());

		Collection<League> leagues = populator.populateDatabase();
		assertEquals(1, leagues.size());
	}
}
