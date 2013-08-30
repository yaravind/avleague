package com.aravind.avl.domain;

import java.util.List;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class LeagueTest
{
	League l = new League();
	Level level;
	Level qf;
	Level sf;

	@Before
	public void setUp()
	{
		level = new Level("Playoffs");
		l.setLevel(level);

		qf = new Level("Quarterfinal");
		level.setNextLevel(qf);

		sf = new Level("Semifinal");
		qf.setNextLevel(sf);
	}

	@Test
	public void findLevelByName()
	{
		assertNotNull(l.findLevelByName("Playoffs"));
		assertNotNull(l.findLevelByName("Quarterfinal"));
		assertNotNull(l.findLevelByName("Semifinal"));

		assertEquals(level, l.findLevelByName("Playoffs"));
		assertEquals(qf, l.findLevelByName("Quarterfinal"));
		assertEquals(sf, l.findLevelByName("Semifinal"));
	}

	@Test
	public void getAllLevelsWithSingleLevel()
	{
		League league = new League();
		Level playoffs = new Level("Playoffs");
		league.setLevel(playoffs);

		List<Level> allLevels = l.getAllLevels();
		assertFalse(allLevels.isEmpty());

		assertEquals(playoffs, allLevels.get(0));
	}

	@Test
	public void getAllLevels()
	{
		List<Level> allLevels = l.getAllLevels();
		assertFalse(allLevels.isEmpty());

		assertEquals(level, allLevels.get(0));
		assertEquals(qf, allLevels.get(1));
		assertEquals(sf, allLevels.get(2));
	}

	@Test
	public void individualAwards()
	{
		Award bestSpiker = addIndividualAward("best spiker", 11.35f);
		l.addAward(bestSpiker);

		assertFalse(bestSpiker.isTeamAward());
		assertEquals("First letter upper case", "Best Spiker", bestSpiker.getAwardFor());
		assertEquals(Float.valueOf(11.35f), Float.valueOf(bestSpiker.totalCost()));

		Award bestBooster = addIndividualAward("best booster", 10.35f);
		l.addAward(bestBooster);

		assertEquals("First letter upper case", "Best Booster", bestBooster.getAwardFor());
		assertEquals(Float.valueOf(10.35f), Float.valueOf(bestBooster.totalCost()));

		assertEquals(2, l.getAwards().size());
	}

	@Test
	public void teamAwards()
	{
		Award bestSpiker = addTeamAward("winner", 5.51f, (short) 8);
		l.addAward(bestSpiker);

		assertEquals(Float.valueOf(44.08f), Float.valueOf(bestSpiker.totalCost()));
		assertEquals(1, l.getAwards().size());
		assertTrue(bestSpiker.isTeamAward());
	}

	public Award addTeamAward(String awardFor, Float unitPrice, Short quantity)
	{
		Award a = new Award(awardFor);
		a.setUnitPrice(unitPrice);
		a.setQuantity(quantity);
		return a;
	}

	public Award addIndividualAward(String awardFor, Float unitPrice)
	{
		Award a = new Award(awardFor);
		a.setUnitPrice(unitPrice);
		a.setQuantity((short) 1);
		return a;
	}
}
