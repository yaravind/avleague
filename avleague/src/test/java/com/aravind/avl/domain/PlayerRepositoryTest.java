package com.aravind.avl.domain;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.neo4j.helpers.collection.IteratorUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.neo4j.conversion.EndResult;
import org.springframework.data.neo4j.conversion.Handler;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

@RunWith (SpringJUnit4ClassRunner.class)
@ContextConfiguration ({ "/testContext.xml"})
@Transactional
public class PlayerRepositoryTest
{
	@Autowired
	PlayerRepository repo;

	Player p;

	@Before
	public void setUp()
	{
		p = new Player();
		p.setName("Aravind Yarram");
	}

	@Test
	public void firstNameFullTextSearch()
	{
		Player save = repo.save(p);
		assertNotNull(save);
		assertNotNull(p.getNodeId());

		EndResult<Player> players = repo.findAllByQuery("firstName", "A*");
		assertNotNull("Starts with first letter match", players.single());

		// TODO not supported
		// players = repo.findAllByQuery("firstName", "aravind");
		// assertNotNull("Lower-case mathch", players.single());
	}

	@Test
	public void firstNameExactMatchOnly()
	{
		Player save = repo.save(p);
		assertNotNull(save);
		assertNotNull(p.getNodeId());

		// either use domain specific method
		Player result = repo.findByPropertyValue("firstName", p.getFirstName());
		assertNotNull("findByPropertyValue should be used for EXACT matches", result);

		// or generic method
		result = repo.findPlayerByFirstName(p.getFirstName());
		assertNotNull("Exact mathch", result);
	}

	@Test
	public void lastNameFullTextSearch()
	{
		Player save = repo.save(p);
		assertNotNull(save);
		assertNotNull(p.getNodeId());

		EndResult<Player> players = repo.findAllByQuery("lastName", "Y*");
		assertNotNull("Starts with first letter match", players.single());

		// TODO not supported
		// players = repo.findAllByQuery("lastName", "yarram");
		// assertNotNull("Lower-case mathch", players.single());
	}

	@Test
	public void lastNameExactMatchOnly()
	{
		Player save = repo.save(p);
		assertNotNull(save);
		assertNotNull(p.getNodeId());

		// either use domain specific method
		Player result = repo.findPlayerByLastName(p.getLastName());
		assertNotNull("Exact mathch", result);

		// or generic method
		result = repo.findByPropertyValue("lastName", p.getLastName());
		assertNotNull("findByPropertyValue should be used for EXACT matches", result);
	}

	@Test
	public void nameFullTextSearch()
	{
		Player save = repo.save(p);
		assertNotNull(save);
		assertNotNull(p.getNodeId());

		Player p1 = new Player();
		p1.setName("Aravind Y");
		save = repo.save(p1);

		EndResult<Player> players = repo.findAllByQuery("name", "Aravind Y".substring(0, 4));
		players.handle(new Handler<Player>()
		{
			@Override
			public void handle(Player value)
			{
				assertNotNull(value.getNodeId());
			}
		});

		Iterable<Player> findByName = repo.findByNameLike("Aravind*");
		assertNotNull(findByName);
		assertEquals(2, IteratorUtil.count(findByName));
	}

	@Test
	public void nameExactMatch()
	{
		Player save = repo.save(p);
		assertNotNull(save);
		assertNotNull(p.getNodeId());

		Player p1 = new Player();
		p1.setName("Aravind Y");
		save = repo.save(p1);

		Player result = repo.findByName("Aravind Yarram");
		assertNotNull("findByPropertyValue should be used for EXACT matches", result);

		result = repo.findByName("Aravind Y");
		assertNotNull("findByPropertyValue should be used for EXACT matches", result);
	}
}
