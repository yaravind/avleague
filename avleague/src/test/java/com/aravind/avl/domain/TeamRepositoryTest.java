package com.aravind.avl.domain;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.neo4j.helpers.collection.Iterables;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.neo4j.template.Neo4jOperations;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

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
	LeagueRepository leagueRepo;

	@Autowired
	Neo4jOperations template;

	League l;

	Team t;

	Player p;

	@Before
	public void setUp() throws ParseException
	{
		p = new Player();
		p.setName("Aravind Yarram");

		t = new Team();
		t.setName("Alpharetta One");

		Pool pl = new Pool();
		pl.setName("Pool A");

		t.setPool(pl);

		l = new League();
		Date startDate = new SimpleDateFormat("dd/MM/yyyy").parse("01/03/2013");
		l.setStartDate(startDate);
		l.setEndDate(startDate);
		l.setName("name");
	}

	@Test
	public void alias()
	{
		Team prev = new Team();
		prev.setName("Previous Name");
		teamRepo.save(prev);

		t.setPreviouslyKnownAs(prev);
		teamRepo.save(t);

		Team result = teamRepo.findOne(t.getNodeId());

		List<String> findPreviouslyKnownAs = teamRepo.findPreviouslyKnownAs(result.getNodeId());

		result.setAliases(findPreviouslyKnownAs);
		assertNotNull("Should return empty List if no aliases are available", result.getAliases());
		assertEquals(1, result.getAliases().size());
		assertEquals(1, Iterables.count(result.getAliases()));

		result = teamRepo.findOne(prev.getNodeId());
		result.setAliases(teamRepo.findPreviouslyKnownAs(result.getNodeId()));
		assertEquals(1, Iterables.count(result.getAliases()));
	}

	@Test
	public void test()
	{
		teamRepo.save(t);
		assertNotNull(t.getNodeId());
		assertNotNull(t.getPool().getNodeId());

		PlayerTeamLeague playedForInLeague = p.playedForInLeague(t, new Date(), null);
		assertNotNull("Should return an instance of PlayedWith class", playedForInLeague);

		playerRepo.save(p);

		assertNotNull(p.getNodeId());
		assertNotNull(playedForInLeague.getNodeId());
		assertNotNull(t.getPlayers());
	}

	@Test
	public void testFindByName()
	{
		teamRepo.save(t);

		Team t2 = new Team();
		t2.setName("Alpharetta");
		teamRepo.save(t2);

		assertNotNull(t.getNodeId());

		Team team = teamRepo.findByName("Alpharetta One");
		assertNotNull(team);
		assertEquals("Alpharetta One", team.getName());

		team = teamRepo.findByName("Alpharetta");
		assertNotNull("Lower-case and regular expression", team);
		assertEquals("Alpharetta", team.getName());
	}

	@Test
	public void findLeaguesContestedIn() throws ParseException
	{
		Team teamA = new Team();
		teamA.setName("Team A");
		teamRepo.save(teamA);

		p = new Player();
		p.setName("Aravind Yarram");

		teamA.addPlayer(p);
		p.playedForInLeague(teamA, l.getStartDate(), l);

		Team teamB = new Team();
		teamB.setName("Team B");
		teamRepo.save(teamB);

		l.addTeam(teamA);
		l.addTeam(teamB);

		leagueRepo.save(l);

		List<String> leaguesContestedIn = teamRepo.findLeaguesContestedIn(teamA.getName());
		assertNotNull(leaguesContestedIn);
		assertEquals(1, leaguesContestedIn.size());
	}

	@Test
	public void findPlayers() throws ParseException
	{
		// Player p played for Team t in League l
		p.playedForInLeague(t, l.getStartDate(), l);
		playerRepo.save(p);
		t.addPlayer(p);
		teamRepo.save(t);
		l.addTeam(t);
		leagueRepo.save(l);

		// new league
		League anotherLeague = new League();
		Date startDate = new SimpleDateFormat("dd/MM/yyyy").parse("01/03/2013");
		anotherLeague.setStartDate(startDate);
		anotherLeague.setEndDate(startDate);
		anotherLeague.setName("another name");

		// new player for the new league
		Player anotherPlayer = new Player();
		anotherPlayer.setName("another Player");
		anotherPlayer.playedForInLeague(t, anotherLeague.getStartDate(), anotherLeague);
		t.addPlayer(anotherPlayer);

		// add existing player for the new league
		Player existingPlayer = playerRepo.findByName("Aravind Yarram");
		existingPlayer.playedForInLeague(t, anotherLeague.getStartDate(), anotherLeague);
		playerRepo.save(existingPlayer);
		t.addPlayer(existingPlayer);
		teamRepo.save(t);
		anotherLeague.addTeam(t);
		leagueRepo.save(anotherLeague);

		List<Player> players = teamRepo.findPlayers(t.getNodeId(), l.getNodeId());
		assertNotNull(players);
		assertEquals("1 players played for [" + t.getName() + "] in league [" + l.getName() + "]", 1, players.size());

		players = teamRepo.findPlayers(t.getNodeId(), anotherLeague.getNodeId());
		System.err.println(players);
		assertNotNull(players);
		assertEquals("2 players played for [" + t.getName() + "] in league [" + anotherLeague.getName() + "]", 2, players.size());
	}
}