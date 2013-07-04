package com.aravind.avl.domain;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Set;
import java.util.TreeSet;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import com.aravind.avl.domain.Match.Level;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({ "/testContext.xml" })
@Transactional
@DirtiesContext(classMode = ClassMode.AFTER_EACH_TEST_METHOD)
public class LeagueRepositoryTest
{
	@Autowired
	LeagueRepository repo;

	League l;
	Team teamA;
	Team teamB;
	Player p;

	@Before
	public void setUp() throws ParseException
	{
		SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");

		l = new League();
		Date startDate = df.parse("01/03/2013");
		l.setStartDate(startDate);
		l.setEndDate(startDate);
		l.setName("name");

		teamA = new Team();
		teamA.setName("Team A");

		p = new Player();
		p.setName("Aravind Yarram");

		teamA.addPlayer(p);
		p.playedWith(teamA, l.getStartDate(), l);

		teamB = new Team();
		teamB.setName("Team B");

		l.addTeam(teamA);
		l.addTeam(teamB);
	}

	@Test
	public void saveLeague() throws ParseException
	{
		l = repo.save(l);
		assertNotNull(l);
		assertNotNull(l.getNodeId());

		Iterable<League> all = repo.findAll();
		Set<League> sortedLeagues = new TreeSet<League>();

		for (League l1 : all)
		{
			sortedLeagues.add(l1);
		}
	}

	@Test
	public void saveMatch()
	{
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM");

		Match m = l.conductMatch(teamA, teamB);
		assertEquals("Match name maker test", teamA.getName() + " v " + teamB.getName(), m.getName());

		m.setLevel(Level.PLAYOFFS);
		m.setMvp(p);
		m.setWinner(teamA);
		l = repo.save(l);
		assertNotNull(m);

		assertNotNull("Make sure match is saved when the league is saved", m.getNodeId());
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
		assertEquals("Most recent league should be returned", "in year 2013", currentLeague.getName());
	}
}
