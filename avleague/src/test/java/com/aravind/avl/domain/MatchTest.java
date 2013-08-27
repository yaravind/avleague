package com.aravind.avl.domain;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class MatchTest
{
	Team teamA;
	Team teamB;
	Match m;

	@Before
	public void setUp()
	{
		teamA = new Team();
		teamA.setName("Team A");

		teamB = new Team();
		teamB.setName("Team B");

		m = new Match();
		m.setTeamA(teamA);
		m.setTeamB(teamB);
	}

	@Ignore
	@Test
	public void gettWinnerDeterminedByScore()
	{
		m.setTeamAScore(21);
		m.setTeamBScore(10);

		assertEquals(teamA, m.getWinner());
		assertEquals(teamB, m.getLoser());
	}

	@Ignore
	@Test
	public void gettWinner()
	{
		m.setWinner(teamA);
		m.setLoser(teamB);
		assertEquals(teamA, m.getWinner());
		assertEquals(teamB, m.getLoser());
	}
}
