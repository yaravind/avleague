package com.aravind.avl.domain;

import java.util.List;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;

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
}
