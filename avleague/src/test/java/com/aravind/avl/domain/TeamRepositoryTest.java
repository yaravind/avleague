package com.aravind.avl.domain;

import static org.junit.Assert.assertNotNull;

import java.util.Date;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.neo4j.template.Neo4jOperations;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

@DirtiesContext (classMode = ClassMode.AFTER_EACH_TEST_METHOD)
@RunWith (SpringJUnit4ClassRunner.class)
@ContextConfiguration ({ "/testContext.xml"})
@Transactional
public class TeamRepositoryTest
{
	@Autowired
	TeamRepository teamRepo;

	@Autowired
	PlayerRepository playerRepo;

	@Autowired
	Neo4jOperations template;

	Team t;

	Player p;

	@Before
	public void setUp()
	{
		p = new Player();
		p.setName("Aravind Yarram");

		t = new Team();
		t.setName("Alpharetta One");
	}

	@Test
	public void test()
	{
		teamRepo.save(t);
		assertNotNull(t.getNodeId());

		PlayedWith playedWith = p.playedWith(t, new Date(), null);
		assertNotNull("Should return an instance of PlayedWith class", playedWith);

		playerRepo.save(p);
		assertNotNull(p.getNodeId());
		// template.save(playedWith);

		assertNotNull(playedWith.getNodeId());
		assertNotNull(t.getPlayers());
	}

	@Test
	public void testFindByName()
	{
		teamRepo.save(t);
		assertNotNull(t.getNodeId());

		Team team = teamRepo.findByName("Alpharetta One");
		assertNotNull(team);

		// TODO to add case insensitive search
		// team = teamRepo.findByName("alpharetta One");
		// assertNotNull(team);
	}
}
