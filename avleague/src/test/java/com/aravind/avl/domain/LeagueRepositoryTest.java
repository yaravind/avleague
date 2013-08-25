package com.aravind.avl.domain;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import java.util.TreeSet;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.neo4j.support.Neo4jTemplate;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.collect.Iterables;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import static com.google.common.collect.Iterables.get;
import static com.google.common.collect.Iterables.getOnlyElement;

@RunWith (SpringJUnit4ClassRunner.class)
@ContextConfiguration ({ "/testContext.xml"})
@Transactional
@DirtiesContext (classMode = ClassMode.AFTER_EACH_TEST_METHOD)
public class LeagueRepositoryTest
{
	@Autowired
	LeagueRepository repo;

	@Autowired
	MatchRepository matchRepo;

	@Autowired
	Neo4jTemplate template;

	@Autowired
	PoolRepository poolRepo;

	@Autowired
	TeamRepository teamRepo;

	League l;

	Team teamA;

	Team teamB;

	Player p;

	Venue v;

	Level level;
	Pool pool;

	@Before
	public void setUp() throws ParseException
	{
		SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");

		l = new League();
		Date startDate = df.parse("01/03/2013");
		l.setStartDate(startDate);
		l.setEndDate(startDate);
		l.setName("name");

		// Always "attach" the entities to DB before you add them to Hash based collection
		// http://static.springsource.org/spring-data/data-graph/snapshot-site/reference/html/#d5e807

		teamA = new Team();
		teamA.setName("Team A");
		teamRepo.save(teamA);

		p = new Player();
		p.setName("Aravind Yarram");

		teamA.addPlayer(p);
		p.playedForInLeague(teamA, l.getStartDate(), l);

		teamB = new Team();
		teamB.setName("Team B");
		teamRepo.save(teamB);

		l.addTeam(teamA);
		l.addTeam(teamB);

		level = new Level("Playoffs");

		l.setLevel(level);

		pool = new Pool("A");
		pool.addTeam(teamA);
		pool.addTeam(teamB);
		pool = poolRepo.save(pool);
		level.addPool(pool);

		Level qf = new Level("Quarterfinal");
		level.setNextLevel(qf);

		Level sf = new Level("Semifinal");
		qf.setNextLevel(sf);

		v = new Venue("Ocee Park");
		v.addCourt(new Court("High Court"));
		Set<Venue> venues = new HashSet<Venue>();
		venues.add(v);
		l.setPlayedAt(venues);
	}

	@Test
	public void saveLeague() throws ParseException
	{
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
	public void saveMatch() throws ParseException
	{
		repo.save(l);
		Match m = buildMatch(l);

		assertEquals("Match name maker test", teamA.getName() + " v " + teamB.getName(), m.getName());
		m = matchRepo.save(m);

		assertNotNull(m);
		assertNotNull(m.getPool().getNodeId());
		assertNotNull(m.getPlayedOnCourt().getNodeId());
		assertNotNull(m.getWinner().getNodeId());
		assertNotNull(m.getLoser().getNodeId());
		assertNotNull(m.getMvp().getNodeId());
		assertNotNull("Make sure match is saved when the league is saved", m.getNodeId());
	}

	@Test
	public void findPool() throws ParseException
	{
		Match m = buildMatch(l);
		m = matchRepo.save(m);
		repo.save(l);

		Pool result = repo.findPool(l.getName(), level.getName(), pool.getName());

		assertNotNull(result.getNodeId());

		assertNotNull(result.getFixtures());
		assertNotNull(Iterables.getOnlyElement(result.getFixtures()));

		assertNotNull(result.getTeams());
		assertEquals(2, Iterables.size(result.getTeams()));

		assertEquals(pool.getName(), result.getName());
	}

	@Test
	public void findPoolWithNoMatches() throws ParseException
	{
		repo.save(l);

		Pool result = repo.findPool(l.getName(), level.getName(), pool.getName());

		assertNotNull(result.getNodeId());
		assertEquals(0, Iterables.size(result.getFixtures()));

		assertNotNull(result.getTeams());
		assertEquals(2, Iterables.size(result.getTeams()));
	}

	@Test
	public void findMatches() throws ParseException
	{
		Match m = buildMatch(l);
		m = matchRepo.save(m);
		repo.save(l);

		assertNotNull(m.getNodeId());
		assertNotNull(m.getPool().getNodeId());
		assertNotNull(m.getPlayedOnCourt().getNodeId());

		Iterable<Match> matches = repo.findMatches(l.getName(), level.getName(), pool.getName());
		Match match = Iterables.getOnlyElement(matches);
		assertNotNull("Only one match was saved so should return only single match.", match);

		assertNotNull(match.getName());
		assertEquals(m.getName(), match.getName());

		assertNotNull(match.getTeamA());
		assertEquals(m.getTeamA(), match.getTeamA());

		assertNotNull(match.getTeamB());
		assertEquals(m.getTeamB(), match.getTeamB());

		assertNotNull(match.getTime());
		assertEquals(m.getTime(), match.getTime());

		assertNotNull(match.getPool());
		assertEquals(m.getPool(), match.getPool());

		assertNotNull(match.getPlayedOnCourt());
		assertEquals(m.getPlayedOnCourt(), match.getPlayedOnCourt());
	}

	private Match buildMatch(League l) throws ParseException
	{
		SimpleDateFormat df = new SimpleDateFormat("MM-dd-yyyy kk.mm");
		Match m = l.conductMatch(teamA, teamB, get(v.getCourts(), 0), level, getOnlyElement(level.getPools()));

		m.setTime(df.parse("07-27-2013 13.30"));
		m.setMvp(p);
		m.setWinner(teamA);
		m.setLoser(teamB);
		return m;
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
		assertEquals("Most recent league should be returned. The first letter of each word in the name should also be capitalized",
				"In Year 2013", currentLeague.getName());
	}

	@Test
	public void testLevels()
	{
		repo.save(l);
		assertNotNull(l.getLevel().getNodeId());
		assertNotNull(getOnlyElement(l.getLevel().getPools()).getNodeId());

		Iterable<Level> levels = repo.findAllLevels(l.getNodeId());
		assertNotNull(levels);
		assertEquals("Order is important", "Playoffs", get(levels, 0).getName());
		assertEquals("Order is important", "Quarterfinal", get(levels, 1).getName());
		assertEquals("Order is important", "Semifinal", get(levels, 2).getName());
	}
}
