package com.aravind.avl.domain;

import static org.junit.Assert.assertNotNull;

import java.util.Date;
import java.util.Set;
import java.util.TreeSet;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({ "/testContext.xml" })
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

		for (League l1 : all)
		{
			System.out.println(l1);
			sortedLeagues.add(l1);
		}
	}
}
