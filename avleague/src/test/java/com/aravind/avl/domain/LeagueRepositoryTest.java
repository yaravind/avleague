package com.aravind.avl.domain;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Set;
import java.util.TreeSet;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

@RunWith (SpringJUnit4ClassRunner.class)
@ContextConfiguration ({ "/testContext.xml"})
@Transactional
public class LeagueRepositoryTest
{
	@Autowired
	LeagueRepository repo;

	@Test
	public void saveLeague()
	{
		League l = new League();
		l.setStartDate(new Date());
		l.setEndDate(new Date());
		l.setName("name");

		Team t = new Team();
		t.setName("team");

		Player p = new Player();
		p.setName("Aravind Yarram");

		t.addPlayer(p);
		p.playedWith(t, l.getStartDate(), l);
		l.addTeam(t);

		l = repo.save(l);
		assertNotNull(l);
		assertNotNull(l.getNodeId());

		Iterable<League> all = repo.findAll();
		Set<League> sortedLeagues = new TreeSet<League>();

		for (League l1: all)
		{
			sortedLeagues.add(l1);
		}
	}

	@Test
	public void findCurrentLeague() throws ParseException
	{
		SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");

		League l1 = new League();
		l1.setStartDate(df.parse("01/03/2013"));
		l1.setEndDate(new Date());
		l1.setName("in year 2013");

		League l2 = new League();
		l2.setStartDate(df.parse("01/03/2001"));
		l2.setEndDate(df.parse("01/10/2001"));
		l2.setName("in year 2001");

		repo.save(l1);
		repo.save(l2);

		League currentLeague = repo.findCurrentLeague();
		assertNotNull(currentLeague);
		assertEquals("in year 2013", currentLeague.getName());
	}
}
